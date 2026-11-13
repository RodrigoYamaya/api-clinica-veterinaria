package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.VacinaMapper;
import com.ibmr.sdm.api_clinica.model.dto.IntegracaoGovernoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.VacinaRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.VacinaResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Vacina;
import com.ibmr.sdm.api_clinica.model.enums.TipoOperacaoIntegracao;
import com.ibmr.sdm.api_clinica.repository.AnimalRepository;
import com.ibmr.sdm.api_clinica.repository.VacinaRepository;
import com.ibmr.sdm.api_clinica.repository.VeterinarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VacinaService {

    private final VacinaRepository vacinaRepository;
    private final VacinaMapper vacinaMapper;
    private final AnimalRepository animalRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final IntegracaoGovernoService integracaoService;

    public VacinaService(VacinaRepository vacinaRepository, VacinaMapper vacinaMapper,
                         AnimalRepository animalRepository, VeterinarioRepository veterinarioRepository,
                         IntegracaoGovernoService integracaoService) {
        this.vacinaRepository = vacinaRepository;
        this.vacinaMapper = vacinaMapper;
        this.animalRepository = animalRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.integracaoService = integracaoService;
    }

    @Transactional
    public VacinaResponseDto cadastrar(VacinaRequestDto dto) {
        validarRelacionamentos(dto.animalId(), dto.veterinarioId());

        Vacina vacina = vacinaMapper.toEntity(dto);

        //AQUI A vacina é salva localmente primeiro (cumprindo a premissa obrigatória)
        Vacina vacinaSalva = vacinaRepository.save(vacina);

        // aqui chama a INTEGRAÇÃO: Dispara apenas se for antirrábica
        if (vacinaSalva.getAntirrabica() != null && vacinaSalva.getAntirrabica()) {
            IntegracaoGovernoRequestDto integracaoDto = new IntegracaoGovernoRequestDto(
                    dto.animalId(),
                    vacinaSalva.getId(),
                    TipoOperacaoIntegracao.VACINA_ANTIRRABICA,
                    UUID.randomUUID().toString() // Gera a chave de idempotência única
            );

            // Chama a comunicação de forma automática após o registro local;
            integracaoService.solicitarIntegracao(integracaoDto);
        }

        return vacinaMapper.toDto(vacinaSalva);
    }

    public List<VacinaResponseDto> listarTodas() {
        return vacinaRepository.findAll().stream()
                .map(vacinaMapper::toDto)
                .toList();
    }

    public List<VacinaResponseDto> listarPorVacinaId(Long animalId) {
        return vacinaRepository.findByAnimalId(animalId).stream()
                .map(vacinaMapper::toDto)
                .toList();
    }

    public VacinaResponseDto buscarPorId(Long id) {
        Vacina vacina = vacinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vacina com ID " + id + " não encontrada"));
        return vacinaMapper.toDto(vacina);
    }

    @Transactional
    public VacinaResponseDto atualizar(Long id, VacinaRequestDto dto) {
        Vacina vacinaExistente = vacinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vacina com ID " + id + " não encontrada"));

        validarRelacionamentos(dto.animalId(), dto.veterinarioId());

        Vacina vacinaAtualizada = vacinaMapper.toEntity(dto);
        vacinaAtualizada.setId(vacinaExistente.getId());

        vacinaAtualizada = vacinaRepository.save(vacinaAtualizada);
        return vacinaMapper.toDto(vacinaAtualizada);
    }

    @Transactional
    public void excluir(Long id) {
        if (!vacinaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Vacina com ID " + id + " não encontrada");
        }
        vacinaRepository.deleteById(id);
    }

    private void validarRelacionamentos(Long animalId, Long veterinarioId) {
        if (!animalRepository.existsById(animalId)) {
            throw new RecursoNaoEncontradoException("Animal com ID " + animalId + " não encontrado");
        }
        if (!veterinarioRepository.existsById(veterinarioId)) {
            throw new RecursoNaoEncontradoException("Veterinário com ID " + veterinarioId + " não encontrado");
        }
    }
}