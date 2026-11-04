package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.IntegracaoGovernoMapper;
import com.ibmr.sdm.api_clinica.model.dto.IntegracaoGovernoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.IntegracaoGovernoResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.IntegracaoGoverno;
import com.ibmr.sdm.api_clinica.repository.AnimalRepository;
import com.ibmr.sdm.api_clinica.repository.IntegracaoGovernoRepository;
import com.ibmr.sdm.api_clinica.repository.VacinaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntegracaoGovernoService {

    private final IntegracaoGovernoRepository integracaoRepository;
    private final IntegracaoGovernoMapper integracaoMapper;
    private final AnimalRepository animalRepository;
    private final VacinaRepository vacinaRepository;

    public IntegracaoGovernoService(IntegracaoGovernoRepository integracaoRepository,
                                    IntegracaoGovernoMapper integracaoMapper,
                                    AnimalRepository animalRepository,
                                    VacinaRepository vacinaRepository) {
        this.integracaoRepository = integracaoRepository;
        this.integracaoMapper = integracaoMapper;
        this.animalRepository = animalRepository;
        this.vacinaRepository = vacinaRepository;
    }

    @Transactional
    public IntegracaoGovernoResponseDto solicitarIntegracao(IntegracaoGovernoRequestDto dto) {
        // 1. Aqui vai Validação de Idempotência (Mitigação de Erros TMR)
        if (integracaoRepository.existsByChaveIdempotencia(dto.chaveIdempotencia())) {
            throw new IllegalArgumentException("Já existe uma requisição processada com esta chave de idempotência.");
        }

        // 2.Aqui vai Validar Existência do Animal
        if (!animalRepository.existsById(dto.animalId())) {
            throw new RecursoNaoEncontradoException("Animal com ID " + dto.animalId() + " não encontrado");
        }

        // 3. Aqui vai Validar Existência da Vacina (Se o ID foi informado no JSON)
        if (dto.vacinaId() != null && !vacinaRepository.existsById(dto.vacinaId())) {
            throw new RecursoNaoEncontradoException("Vacina com ID " + dto.vacinaId() + " não encontrada");
        }

        IntegracaoGoverno integracao = integracaoMapper.toEntity(dto);
        IntegracaoGoverno integracaoSalva = integracaoRepository.save(integracao);

        // =========================================================================================
        // Neste espaço, vamos definir a chamada HTTP (OpenFeign) para o projeto da API do Governo.
        // E logo após a resposta, faremos um 'integracaoSalva.setSituacao(CONFIRMADA)'
        // Nisso se nos optamos pela comunicação sincrona,falta decidir se vamos utilizar comunicação assincrona(mensageria(rabbit ou kafka)
        // temos que decidir!!
        // =========================================================================================

        return integracaoMapper.toDto(integracaoSalva);
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
