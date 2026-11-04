package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.ConsultaRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.ConsultaResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConsultaMapper {
    @Mapping(source = "animalId", target = "animal.id")
    @Mapping(source = "veterinarioId", target = "veterinario.id")
    Consulta toEntity(ConsultaRequestDto dto);

    @Mapping(source = "animal.id", target = "animalId")
    @Mapping(source = "veterinario.id", target = "veterinarioId")
    ConsultaResponseDto toDto(Consulta entity);
}
