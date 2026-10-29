package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.EspecieMapper;
import com.ibmr.sdm.api_clinica.model.dto.EspecieRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.EspecieResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Especie;
import com.ibmr.sdm.api_clinica.repository.EspecieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspecieService {

    private final EspecieRepository especieRepository;
    private final EspecieMapper especieMapper;

    public EspecieService(EspecieRepository especieRepository, EspecieMapper especieMapper) {
        this.especieRepository = especieRepository;
        this.especieMapper = especieMapper;
    }

    public List<EspecieResponseDto> listarEspecie() {
        return especieRepository.findAll()
                .stream()
                .map(especieMapper::toDto)
                .collect(Collectors.toList());
    }

    public EspecieResponseDto buscarPorId(long id) {
        Especie especie = especieRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Espécie com ID " + id + " não encontrada"));
        return especieMapper.toDto(especie);
    }

    @Transactional
    public EspecieResponseDto cadastrar(EspecieRequestDto especieDto) {
        Especie especie = especieMapper.toEntity(especieDto);
        Especie especieSave = especieRepository.save(especie);
        return especieMapper.toDto(especieSave);
    }

    @Transactional
    public void deletarEspecie(long id) {
        if (!especieRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Espécie com ID " + id + " não encontrada");
        }
        especieRepository.deleteById(id);
    }

    @Transactional
    public EspecieResponseDto update(EspecieRequestDto especieDto, long id) {
        Especie especie = especieRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Espécie com ID " + id + " não encontrada"));

        especie.setNome(especieDto.nome());
        especie.setDescricao(especieDto.descricao());
        if (especieDto.situacao() != null) {
            especie.setSituacao(especieDto.situacao());
        }

        Especie especieUpdate = especieRepository.save(especie);
        return especieMapper.toDto(especieUpdate);
    }
}
