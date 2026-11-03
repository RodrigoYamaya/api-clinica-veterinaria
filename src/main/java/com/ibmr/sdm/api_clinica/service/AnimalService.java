package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.AnimalMapper;
import com.ibmr.sdm.api_clinica.model.dto.AnimalRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.AnimalResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Animal;
import com.ibmr.sdm.api_clinica.model.entities.Especie;
import com.ibmr.sdm.api_clinica.model.entities.Raca;
import com.ibmr.sdm.api_clinica.model.entities.Tutor;
import com.ibmr.sdm.api_clinica.repository.AnimalRepository;
import com.ibmr.sdm.api_clinica.repository.EspecieRepository;
import com.ibmr.sdm.api_clinica.repository.RacaRepository;
import com.ibmr.sdm.api_clinica.repository.TutorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final TutorRepository tutorRepository;
    private final EspecieRepository especieRepository;
    private final RacaRepository racaRepository;

    public AnimalService(AnimalRepository animalRepository, AnimalMapper animalMapper, TutorRepository tutorRepository, EspecieRepository especieRepository, RacaRepository racaRepository) {
        this.animalRepository = animalRepository;
        this.animalMapper = animalMapper;
        this.tutorRepository = tutorRepository;
        this.especieRepository = especieRepository;
        this.racaRepository = racaRepository;
    }

    public List<AnimalResponseDto> listarAnimal() {
        return animalRepository.findAll()
                .stream()
                .map(animalMapper::toDto)
                .collect(Collectors.toList());
    }

    public AnimalResponseDto buscarPorId(long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Animal com ID " + id + " não encontrado"));
        return animalMapper.toDto(animal);
    }

    @Transactional
    public AnimalResponseDto cadastrar(AnimalRequestDto animalDto) {
        if (animalDto.dataNascimento() == null && animalDto.idadeEstimadaMeses() == null) {
            throw new IllegalArgumentException("É obrigatório informar a data de nascimento ou a idade estimada.");
        }

        Tutor tutor = tutorRepository.findById(animalDto.tutorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor com ID " + animalDto.tutorId() + " não encontrado"));

        Especie especie = especieRepository.findById(animalDto.especieId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Espécie com ID " + animalDto.especieId() + " não encontrada"));

        Raca raca = null;
        if (animalDto.racaId() != null) {
            raca = racaRepository.findById(animalDto.racaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Raça com ID " + animalDto.racaId() + " não encontrada"));
        }

        Animal animal = animalMapper.toEntity(animalDto, tutor, especie, raca);
        Animal animalSalvo = animalRepository.save(animal);
        return animalMapper.toDto(animalSalvo);
    }

    @Transactional
    public void deletarAnimal(long id) {
        if (!animalRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Animal com ID " + id + " não encontrado");
        }
        animalRepository.deleteById(id);
    }

    @Transactional
    public AnimalResponseDto update(AnimalRequestDto animalDto, long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Animal com ID " + id + " não encontrado"));

        if (animalDto.dataNascimento() == null && animalDto.idadeEstimadaMeses() == null) {
            throw new IllegalArgumentException("É obrigatório informar a data de nascimento ou a idade estimada.");
        }

        if (!animal.getTutor().getId().equals(animalDto.tutorId())) {
            Tutor tutor = tutorRepository.findById(animalDto.tutorId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor com ID " + animalDto.tutorId() + " não encontrado"));
            animal.setTutor(tutor);
        }

        if (!animal.getEspecie().getId().equals(animalDto.especieId())) {
            Especie especie = especieRepository.findById(animalDto.especieId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Espécie com ID " + animalDto.especieId() + " não encontrada"));
            animal.setEspecie(especie);
        }

        if (animalDto.racaId() != null) {
            if (animal.getRaca() == null || !animal.getRaca().getId().equals(animalDto.racaId())) {
                Raca raca = racaRepository.findById(animalDto.racaId())
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Raça com ID " + animalDto.racaId() + " não encontrada"));
                animal.setRaca(raca);
            }
        } else {
            animal.setRaca(null);
        }

        animal.setNome(animalDto.nome());
        animal.setDataNascimento(animalDto.dataNascimento());
        animal.setIdadeEstimadaMeses(animalDto.idadeEstimadaMeses());
        animal.setSexo(animalDto.sexo());
        animal.setCor(animalDto.cor());
        animal.setPesoAtual(animalDto.pesoAtual());

        Animal animalUpdate = animalRepository.save(animal);
        return animalMapper.toDto(animalUpdate);
    }


}
