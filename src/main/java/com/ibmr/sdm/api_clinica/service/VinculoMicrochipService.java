package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.VinculoMicrochipMapper;
import com.ibmr.sdm.api_clinica.model.dto.VinculoMicrochipRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.VinculoMicrochipResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Animal;
import com.ibmr.sdm.api_clinica.model.entities.Veterinario;
import com.ibmr.sdm.api_clinica.model.entities.VinculoMicrochip;
import com.ibmr.sdm.api_clinica.repository.AnimalRepository;
import com.ibmr.sdm.api_clinica.repository.VeterinarioRepository;
import com.ibmr.sdm.api_clinica.repository.VinculoMicrochipRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VinculoMicrochipService {

    private final VinculoMicrochipRepository microchipRepository;
    private final VinculoMicrochipMapper microchipMapper;
    private final AnimalRepository animalRepository;
    private final VeterinarioRepository veterinarioRepository;

    public VinculoMicrochipService(VinculoMicrochipRepository microchipRepository,
                                   VinculoMicrochipMapper microchipMapper,
                                   AnimalRepository animalRepository,
                                   VeterinarioRepository veterinarioRepository) {
        this.microchipRepository = microchipRepository;
        this.microchipMapper = microchipMapper;
        this.animalRepository = animalRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Transactional
    public VinculoMicrochipResponseDto vincularMicrochip(Long animalId, VinculoMicrochipRequestDto dto) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Animal com ID " + animalId + " não encontrado"));

        Veterinario veterinario = veterinarioRepository.findById(dto.veterinarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veterinário com ID " + dto.veterinarioId() + " não encontrado"));

        // Regra de escopo defnido pelo professor: Encerra o chip ativo atual (se existir)
        microchipRepository.findByAnimalIdAndDataFimIsNull(animalId).ifPresent(chipAntigo -> {
            chipAntigo.setDataFim(LocalDateTime.now());
            chipAntigo.setMotivoEncerramento("Substituição por novo microchip");
            microchipRepository.save(chipAntigo);
        });

        // Cria o novo vínculo de chip, escopo defnido
        VinculoMicrochip novoChip = microchipMapper.toEntity(dto);
        novoChip.setAnimal(animal);
        novoChip.setVeterinario(veterinario);
        novoChip.setDataVinculo(LocalDateTime.now());

        return microchipMapper.toDto(microchipRepository.save(novoChip));
    }

    public List<VinculoMicrochipResponseDto> listarHistoricoDoAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new RecursoNaoEncontradoException("Animal com ID " + animalId + " não encontrado");
        }
        return microchipRepository.findAllByAnimalIdOrderByDataVinculoDesc(animalId).stream()
                .map(microchipMapper::toDto)
                .toList();
    }
}
