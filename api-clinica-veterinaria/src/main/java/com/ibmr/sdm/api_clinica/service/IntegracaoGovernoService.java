package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.GovernoMapper;
import com.ibmr.sdm.api_clinica.mapper.IntegracaoGovernoMapper;
import com.ibmr.sdm.api_clinica.model.dto.GovernoRequestDto;
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
import jakarta.transaction.Transactional;
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
    //private final GovernoClient governoClient;

    //vai faltar adicionar construtor GovernoClient
    public IntegracaoGovernoService(IntegracaoGovernoRepository integracaoRepository, IntegracaoGovernoMapper integracaoMapper, AnimalRepository animalRepository, VacinaRepository vacinaRepository, VinculoMicrochipRepository microchipRepository, GovernoMapper governoMapper) {
        this.integracaoRepository = integracaoRepository;
        this.integracaoMapper = integracaoMapper;
        this.animalRepository = animalRepository;
        this.vacinaRepository = vacinaRepository;
        this.microchipRepository = microchipRepository;
        this.governoMapper = governoMapper;
    }

    @Transactional
    public IntegracaoGovernoResponseDto solicitarIntegracao(IntegracaoGovernoRequestDto dto) {

        // validações especies da vacina Validações antirrabica;
        if (dto.tipoOperacao() == TipoOperacaoIntegracao.VACINA_ANTIRRABICA && dto.vacinaId() == null) {
            throw new IllegalArgumentException("Para a operação VACINA_ANTIRRABICA, o ID da vacina é obrigatório.");
        }

        // Ajustaremos a Idempotência: Só barra se já tiver sido CONFIRMADA com sucesso no governo
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

        // Aqui vamos verificar Regra Crítica: Registro antirrábico exige microchip obrigatoriamente
        if (dto.tipoOperacao() == TipoOperacaoIntegracao.VACINA_ANTIRRABICA && codigoMicrochip == null) {
            throw new IllegalArgumentException("Operação negada: O animal não possui um microchip RFID vinculado.");
        }

        // Aqui vamos Registrar ou recuperar o histórico inicial (PENDENTE / ENVIADO)
        IntegracaoGoverno integracao = integracaoRepository.findByChaveIdempotencia(dto.chaveIdempotencia())
                .orElseGet(() -> integracaoMapper.toEntity(dto));

        integracao.setSituacao(SituacaoIntegracao.ENVIADO);
        integracao.setTentativas(integracao.getTentativas() + 1); // Incrementa a tentativa atual de envio
        integracao.setDataSolicitacao(LocalDateTime.now());
        integracao = integracaoRepository.saveAndFlush(integracao);

        // 3. Montar o pacote de envio do Governo
        GovernoRequestDto pacoteEnvio = governoMapper.toRequestDto(animal, vacina, codigoMicrochip);

        // 4. Fluxo de Comunicação Síncrona com tratamento de falhas distribuídas
        try {
            // Chamada Feign real  lembre-se (descomente quando a API do governo estiver pronta) aguardemos luiz feilpe e lucas finalziar api governo
            // GovernoResponseDto response = governoClient.enviarDadosGoverno(pacoteEnvio);

            // Simulação do Sucesso:
            integracao.setSituacao(SituacaoIntegracao.CONFIRMADA);
            // integracao.setProtocoloExterno(response.protocolo());
            integracao.setDataResposta(LocalDateTime.now());
            integracao.setMensagemErro(null);

        } catch (Exception e) {
            // Aqui vamos fazer Tratamento de Indisponibilidade/Timeout
            integracao.setSituacao(SituacaoIntegracao.FALHA);
            integracao.setMensagemErro("Falha na comunicação com a API Governamental: " + e.getMessage());
            integracao.setDataResposta(LocalDateTime.now());
        }

        IntegracaoGoverno integracaoSalva = integracaoRepository.save(integracao);
        return integracaoMapper.toDto(integracaoSalva);
    }

    //AQUI  MÉTODO ADICIONAL OBRIGATÓRIO PARA O TESTE 15 (REENVIO DE PENDENTES) QUE FOI DEFINIDA PELO ESCOPO TRABALHO; regra irritante
    @Transactional
    public void reprocessarIntegracoesPendentes() {
        // Buscaremos todas as integrações que falharam na comunicação anterior;
        List<IntegracaoGoverno> falhas = integracaoRepository.findBySituacao(SituacaoIntegracao.FALHA);

        for (IntegracaoGoverno integracao : falhas) {
            try {
                integracao.setTentativas(integracao.getTentativas() + 1);

                // Aqui vai Recriar o pacote com os dados salvos na entidade;
                String codigoMicrochip = microchipRepository.findByAnimalIdAndDataFimIsNull(integracao.getAnimal().getId())
                        .map(VinculoMicrochip::getCodigoMicrochip).orElse(null);

                GovernoRequestDto pacoteEnvio = governoMapper.toRequestDto(
                        integracao.getAnimal(),
                        integracao.getVacina(),
                        codigoMicrochip
                );

                // Aqui vai Executa nova tentativa síncrona da api governo feita pelo luiz felipe
                // GovernoResponseDto response = governoClient.enviarDadosGoverno(pacoteEnvio);

                integracao.setSituacao(SituacaoIntegracao.CONFIRMADA);
                // integracao.setProtocoloExterno(response.protocolo());
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
