package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.client.GovernoClient;
import com.ibmr.sdm.api_clinica.mapper.GovernoMapper;
import com.ibmr.sdm.api_clinica.mapper.IntegracaoGovernoMapper;
import com.ibmr.sdm.api_clinica.model.dto.GovernoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.GovernoResponseDto;
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
    private final GovernoClient governoClient; // Agora injetado!

    public IntegracaoGovernoService(
            IntegracaoGovernoRepository integracaoRepository,
            IntegracaoGovernoMapper integracaoMapper,
            AnimalRepository animalRepository,
            VacinaRepository vacinaRepository,
            VinculoMicrochipRepository microchipRepository,
            GovernoMapper governoMapper,
            GovernoClient governoClient) { // Injetado no construtor
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

        GovernoRequestDto pacoteEnvio = governoMapper.toRequestDto(animal, vacina, codigoMicrochip);

        try {
            if (dto.tipoOperacao() == TipoOperacaoIntegracao.VACINA_ANTIRRABICA) {
                IntegracaoGovernoResponseDto response = governoClient.enviarVacinaAntirrabica(dto.chaveIdempotencia(), dto);
                integracao.setProtocoloExterno(response.protocoloExterno());
            } else {
                GovernoResponseDto response = governoClient.atualizarAnimal(codigoMicrochip, pacoteEnvio);
                integracao.setProtocoloExterno(response.protocolo());
            }

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

                GovernoRequestDto pacoteEnvio = governoMapper.toRequestDto(
                        integracao.getAnimal(),
                        integracao.getVacina(),
                        codigoMicrochip
                );

                if (integracao.getTipoOperacao() == TipoOperacaoIntegracao.VACINA_ANTIRRABICA) {
                    IntegracaoGovernoRequestDto dtoReenvio = new IntegracaoGovernoRequestDto(
                            integracao.getAnimal().getId(),
                            integracao.getVacina() != null ? integracao.getVacina().getId() : null,
                            integracao.getTipoOperacao(),
                            integracao.getChaveIdempotencia()
                    );
                    IntegracaoGovernoResponseDto response = governoClient.enviarVacinaAntirrabica(integracao.getChaveIdempotencia(), dtoReenvio);
                    integracao.setProtocoloExterno(response.protocoloExterno());
                } else {
                    GovernoResponseDto response = governoClient.atualizarAnimal(codigoMicrochip, pacoteEnvio);
                    integracao.setProtocoloExterno(response.protocolo());
                }

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