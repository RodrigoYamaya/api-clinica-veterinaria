package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.AnimalRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.AnimalResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Animal;
import com.ibmr.sdm.api_clinica.model.entities.Especie;
import com.ibmr.sdm.api_clinica.model.entities.Raca;
import com.ibmr.sdm.api_clinica.model.entities.Tutor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimalMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "situacao", constant = "ATIVO")
    @Mapping(target = "nome", source = "dto.nome")
    @Mapping(target = "dataNascimento", source = "dto.dataNascimento")
    @Mapping(target = "tutor", source = "tutor")
    @Mapping(target = "especie", source = "especie")
    @Mapping(target = "raca", source = "raca")
    Animal toEntity(AnimalRequestDto dto, Tutor tutor, Especie especie, Raca raca);

    @Mapping(target = "tutorId", source = "tutor.id")
    @Mapping(target = "tutorNome", source = "tutor.nomeCompleto")
    @Mapping(target = "especieId", source = "especie.id")
    @Mapping(target = "especieNome", source = "especie.nome")
    @Mapping(target = "racaId", source = "raca.id")
    @Mapping(target = "racaNome", source = "raca.nome")
    AnimalResponseDto toDto(Animal animal);
}
