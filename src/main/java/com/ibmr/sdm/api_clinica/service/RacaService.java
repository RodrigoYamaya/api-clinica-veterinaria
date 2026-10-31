package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.RacaMapper;
import com.ibmr.sdm.api_clinica.model.dto.RacaRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.RacaResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Especie;
import com.ibmr.sdm.api_clinica.model.entities.Raca;
import com.ibmr.sdm.api_clinica.repository.EspecieRepository;
import com.ibmr.sdm.api_clinica.repository.RacaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RacaService {

    private final RacaRepository racaRepository;
    private final RacaMapper racaMapper;
    private final EspecieRepository especieRepository;

    public RacaService(RacaRepository racaRepository, RacaMapper racaMapper, EspecieRepository especieRepository) {
        this.racaRepository = racaRepository;
        this.racaMapper = racaMapper;
        this.especieRepository = especieRepository;
    }

    public List<RacaResponseDto> listarRaca() {
        return racaRepository.findAll()
                .stream()
                .map(racaMapper::toDto)
                .collect(Collectors.toList());
    }

    public RacaResponseDto buscarPorId(long id) {
        Raca raca = racaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Raça com ID " + id + " não encontrada"));
        return racaMapper.toDto(raca);
    }

    @Transactional
    public RacaResponseDto cadastrar(RacaRequestDto racaDto) {
        if (!especieRepository.existsById(racaDto.especieId())) {
            throw new RecursoNaoEncontradoException("Espécie com ID " + racaDto.especieId() + " não encontrada");
        }

        Raca raca = racaMapper.toEntity(racaDto);
        Raca racaSave = racaRepository.save(raca);
        return racaMapper.toDto(racaSave);
    }

    @Transactional
    public void deletarRaca(long id) {
        if (!racaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Raça com ID " + id + " não encontrada");
        }
        racaRepository.deleteById(id);
    }

    @Transactional
    public RacaResponseDto update(RacaRequestDto racaDto, long id) {
        Raca raca = racaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Raça com ID " + id + " não encontrada"));

        if (!raca.getEspecie().getId().equals(racaDto.especieId())) {
            Especie especie = especieRepository.findById(racaDto.especieId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Espécie com ID " + racaDto.especieId() + " não encontrada"));
            raca.setEspecie(especie);
        }

        raca.setNome(racaDto.nome());
        raca.setDescricao(racaDto.descricao());
        if (racaDto.situacao() != null) {
            raca.setSituacao(racaDto.situacao());
        }

        Raca racaUpdate = racaRepository.save(raca);
        return racaMapper.toDto(racaUpdate);
    }
}
