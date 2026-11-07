package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.VacinaMapper;
import com.ibmr.sdm.api_clinica.model.dto.VacinaRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.VacinaResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Vacina;
import com.ibmr.sdm.api_clinica.repository.AnimalRepository;
import com.ibmr.sdm.api_clinica.repository.VacinaRepository;
import com.ibmr.sdm.api_clinica.repository.VeterinarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacinaService {

    private final VacinaRepository vacinaRepository;
    private final VacinaMapper vacinaMapper;
    private final AnimalRepository animalRepository;
    private final VeterinarioRepository veterinarioRepository;

    public VacinaService(VacinaRepository vacinaRepository, VacinaMapper vacinaMapper,
                         AnimalRepository animalRepository, VeterinarioRepository veterinarioRepository) {
        this.vacinaRepository = vacinaRepository;
        this.vacinaMapper = vacinaMapper;
        this.animalRepository = animalRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Transactional
    public VacinaResponseDto cadastrar(VacinaRequestDto dto) {
        validarRelacionamentos(dto.animalId(), dto.veterinarioId());

        Vacina vacina = vacinaMapper.toEntity(dto);
        Vacina vacinaSalva = vacinaRepository.save(vacina);
        return vacinaMapper.toDto(vacinaSalva);
    }

    public List<VacinaResponseDto> listarTodas() {
        return vacinaRepository.findAll().stream()
                .map(vacinaMapper::toDto)
                .toList();
    }

    public List<VacinaResponseDto> listarPorVacinaId(Long vacinaId) {
        return vacinaRepository.findByAnimalId(vacinaId).stream()
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
