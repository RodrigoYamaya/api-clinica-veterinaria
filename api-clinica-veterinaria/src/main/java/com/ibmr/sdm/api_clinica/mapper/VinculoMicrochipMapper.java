package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.VinculoMicrochipRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.VinculoMicrochipResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.VinculoMicrochip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VinculoMicrochipMapper {

    @Mapping(source = "veterinarioId", target = "veterinario.id")
    VinculoMicrochip toEntity(VinculoMicrochipRequestDto dto);

    @Mapping(source = "animal.id", target = "animalId")
    @Mapping(source = "veterinario.id", target = "veterinarioId")
    VinculoMicrochipResponseDto toDto(VinculoMicrochip entity);
}
