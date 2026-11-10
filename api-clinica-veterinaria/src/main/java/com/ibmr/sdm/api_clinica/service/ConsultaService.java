package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.ConsultaMapper;
import com.ibmr.sdm.api_clinica.model.dto.ConsultaRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.ConsultaResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Consulta;
import com.ibmr.sdm.api_clinica.repository.AnimalRepository;
import com.ibmr.sdm.api_clinica.repository.ConsultaRepository;
import com.ibmr.sdm.api_clinica.repository.VeterinarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;
    private final AnimalRepository animalRepository;
    private final VeterinarioRepository veterinarioRepository;

    public ConsultaService(ConsultaRepository consultaRepository, ConsultaMapper consultaMapper,
                           AnimalRepository animalRepository, VeterinarioRepository veterinarioRepository) {
        this.consultaRepository = consultaRepository;
        this.consultaMapper = consultaMapper;
        this.animalRepository = animalRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Transactional
    public ConsultaResponseDto cadastrar(ConsultaRequestDto dto) {
        validarRelacionamentos(dto.animalId(), dto.veterinarioId());

        Consulta consulta = consultaMapper.toEntity(dto);
        Consulta consultaSalva = consultaRepository.save(consulta);
        return consultaMapper.toDto(consultaSalva);
    }

    public List<ConsultaResponseDto> listarTodas() {
        return consultaRepository.findAll().stream()
                .map(consultaMapper::toDto)
                .toList();
    }

    public List<ConsultaResponseDto> listarPorAnimalId(Long animalId) {
        return consultaRepository.findByAnimalId(animalId).stream()
                .map(consultaMapper::toDto)
                .toList();
    }

    public ConsultaResponseDto buscarPorId(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta com ID " + id + " não encontrada"));
        return consultaMapper.toDto(consulta);
    }

    @Transactional
    public ConsultaResponseDto atualizar(Long id, ConsultaRequestDto dto) {
        Consulta consultaExistente = consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta com ID " + id + " não encontrada"));

        validarRelacionamentos(dto.animalId(), dto.veterinarioId());

        Consulta consultaAtualizada = consultaMapper.toEntity(dto);
        consultaAtualizada.setId(consultaExistente.getId());

        consultaAtualizada = consultaRepository.save(consultaAtualizada);
        return consultaMapper.toDto(consultaAtualizada);
    }

    @Transactional
    public void excluir(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Consulta com ID " + id + " não encontrada");
        }
        consultaRepository.deleteById(id);
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
