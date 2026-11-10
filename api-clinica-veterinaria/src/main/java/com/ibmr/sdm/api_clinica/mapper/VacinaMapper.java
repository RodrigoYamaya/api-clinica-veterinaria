package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.VacinaRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.VacinaResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Vacina;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VacinaMapper {

    @Mapping(source = "animalId", target = "animal.id")
    @Mapping(source = "veterinarioId", target = "veterinario.id")
    Vacina toEntity(VacinaRequestDto dto);

    @Mapping(source = "animal.id", target = "animalId")
    @Mapping(source = "veterinario.id", target = "veterinarioId")
    VacinaResponseDto toDto(Vacina entity);
}
