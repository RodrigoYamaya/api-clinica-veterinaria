package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.client.GovernoClient;
import com.ibmr.sdm.api_clinica.mapper.GovernoMapper;
import com.ibmr.sdm.api_clinica.mapper.IntegracaoGovernoMapper;
import com.ibmr.sdm.api_clinica.model.dto.GovernoAnimalRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.GovernoAnimalResponseDto;
import com.ibmr.sdm.api_clinica.model.dto.GovernoVacinaRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.IntegracaoGovernoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.IntegracaoGovernoResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Animal;
import com.ibmr.sdm.api_clinica.model.entities.IntegracaoGoverno;
import com.ibmr.sdm.api_clinica.model.entities.Vacina;
import com.ibmr.sdm.api_clinica.model.entities.VinculoMicrochip;
import com.ibmr.sdm.api_clinica.model.enums.SituacaoIntegracao;
import com.ibmr.sdm.api_clinica.model.enums.TipoOperacaoIntegracao;
import com.ibmr.sdm.api_clinica.repository.AnimalRepository;
import com.ibmr.sdm.api_clinica.repository.IntegracaoGovernoRepository;
import com.ibmr.sdm.api_clinica.repository.VacinaRepository;
import com.ibmr.sdm.api_clinica.repository.VinculoMicrochipRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IntegracaoGovernoService {

    private final IntegracaoGovernoRepository integracaoRepository;
    private final IntegracaoGovernoMapper integracaoMapper;
    private final AnimalRepository animalRepository;
    private final VacinaRepository vacinaRepository;
    private final VinculoMicrochipRepository microchipRepository;
    private final GovernoMapper governoMapper;
    private final GovernoClient governoClient;

    public IntegracaoGovernoService(
            IntegracaoGovernoRepository integracaoRepository,
            IntegracaoGovernoMapper integracaoMapper,
            AnimalRepository animalRepository,
            VacinaRepository vacinaRepository,
            VinculoMicrochipRepository microchipRepository,
            GovernoMapper governoMapper,
            GovernoClient governoClient) {
        this.integracaoRepository = integracaoRepository;
        this.integracaoMapper = integracaoMapper;
        this.animalRepository = animalRepository;
        this.vacinaRepository = vacinaRepository;
        this.microchipRepository = microchipRepository;
        this.governoMapper = governoMapper;
        this.governoClient = governoClient;
    }

    @Transactional
    public IntegracaoGovernoResponseDto solicitarIntegracao(IntegracaoGovernoRequestDto dto) {

        if (dto.tipoOperacao() == TipoOperacaoIntegracao.VACINA_ANTIRRABICA && dto.vacinaId() == null) {
            throw new IllegalArgumentException("Para a operação VACINA_ANTIRRABICA, o ID da vacina é obrigatório.");
        }

        integracaoRepository.findByChaveIdempotencia(dto.chaveIdempotencia()).ifPresent(existing -> {
            if (existing.getSituacao() == SituacaoIntegracao.CONFIRMADA) {
                throw new IllegalArgumentException("Já existe uma transação concluída com sucesso para esta chave de idempotência.");
            }
        });

        Animal animal = animalRepository.findById(dto.animalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Animal com ID " + dto.animalId() + " não encontrado"));

        Vacina vacina = null;
        if (dto.vacinaId() != null) {
            vacina = vacinaRepository.findById(dto.vacinaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Vacina com ID " + dto.vacinaId() + " não encontrada"));

            if (dto.tipoOperacao() == TipoOperacaoIntegracao.VACINA_ANTIRRABICA && !vacina.getAntirrabica()) {
                throw new IllegalArgumentException("Operação negada: A vacina informada não está classificada como antirrábica.");
            }
        }

        String codigoMicrochip = microchipRepository.findByAnimalIdAndDataFimIsNull(animal.getId())
                .map(VinculoMicrochip::getCodigoMicrochip).orElse(null);

        if (dto.tipoOperacao() == TipoOperacaoIntegracao.VACINA_ANTIRRABICA && codigoMicrochip == null) {
            throw new IllegalArgumentException("Operação negada: O animal não possui um microchip RFID vinculado.");
        }

        IntegracaoGoverno integracao = integracaoRepository.findByChaveIdempotencia(dto.chaveIdempotencia())
                .orElseGet(() -> integracaoMapper.toEntity(dto));

        integracao.setSituacao(SituacaoIntegracao.ENVIADO);
        integracao.setTentativas(integracao.getTentativas() + 1);
        integracao.setDataSolicitacao(LocalDateTime.now());
        integracao = integracaoRepository.saveAndFlush(integracao);

        try {
            GovernoAnimalResponseDto response;

            if (dto.tipoOperacao() == TipoOperacaoIntegracao.VACINA_ANTIRRABICA) {
                GovernoVacinaRequestDto pacoteVacina = governoMapper.toVacinaRequestDto(animal, vacina, codigoMicrochip);
                response = governoClient.enviarVacinaAntirrabica(dto.chaveIdempotencia(), pacoteVacina);
            } else {
                GovernoAnimalRequestDto pacoteAnimal = governoMapper.toAnimalRequestDto(animal, codigoMicrochip);
                response = governoClient.atualizarAnimal(codigoMicrochip, pacoteAnimal);
            }

            integracao.setProtocoloExterno(response.protocolo());
            integracao.setSituacao(SituacaoIntegracao.CONFIRMADA);
            integracao.setDataResposta(LocalDateTime.now());
            integracao.setMensagemErro(null);

        } catch (FeignException e) {
            integracao.setSituacao(SituacaoIntegracao.FALHA);
            integracao.setMensagemErro("Falha na comunicação: HTTP " + e.status() + " - Serviço indisponível");
            integracao.setDataResposta(LocalDateTime.now());
        } catch (Exception e) {
            integracao.setSituacao(SituacaoIntegracao.FALHA);
            integracao.setMensagemErro("Erro interno ao comunicar com API Governamental: " + e.getMessage());
            integracao.setDataResposta(LocalDateTime.now());
        }

        IntegracaoGoverno integracaoSalva = integracaoRepository.save(integracao);
        return integracaoMapper.toDto(integracaoSalva);
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void reprocessarIntegracoesPendentes() {
        List<IntegracaoGoverno> falhas = integracaoRepository.findBySituacao(SituacaoIntegracao.FALHA);

        for (IntegracaoGoverno integracao : falhas) {
            try {
                integracao.setTentativas(integracao.getTentativas() + 1);

                String codigoMicrochip = microchipRepository.findByAnimalIdAndDataFimIsNull(integracao.getAnimal().getId())
                        .map(VinculoMicrochip::getCodigoMicrochip).orElse(null);

                GovernoAnimalResponseDto response;

                if (integracao.getTipoOperacao() == TipoOperacaoIntegracao.VACINA_ANTIRRABICA) {
                    GovernoVacinaRequestDto pacoteVacina = governoMapper.toVacinaRequestDto(
                            integracao.getAnimal(),
                            integracao.getVacina(),
                            codigoMicrochip
                    );
                    response = governoClient.enviarVacinaAntirrabica(integracao.getChaveIdempotencia(), pacoteVacina);
                } else {
                    GovernoAnimalRequestDto pacoteAnimal = governoMapper.toAnimalRequestDto(
                            integracao.getAnimal(),
                            codigoMicrochip
                    );
                    response = governoClient.atualizarAnimal(codigoMicrochip, pacoteAnimal);
                }

                integracao.setProtocoloExterno(response.protocolo());
                integracao.setSituacao(SituacaoIntegracao.CONFIRMADA);
                integracao.setMensagemErro(null);
                integracao.setDataResposta(LocalDateTime.now());
            } catch (Exception e) {
                integracao.setMensagemErro("Nova falha no reenvio automático: " + e.getMessage());
            }
            integracaoRepository.save(integracao);
        }
    }

    public List<IntegracaoGovernoResponseDto> listarTodas() {
        return integracaoRepository.findAll().stream()
                .map(integracaoMapper::toDto)
                .toList();
    }

    public IntegracaoGovernoResponseDto buscarPorId(Long id) {
        IntegracaoGoverno integracao = integracaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Integração com ID " + id + " não encontrada"));
        return integracaoMapper.toDto(integracao);
    }
}