package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.EspecieRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.EspecieResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Especie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EspecieMapper {

    @Mapping(target = "id", ignore = true)
    Especie toEntity(EspecieRequestDto dto);

    EspecieResponseDto toDto(Especie entity);
}
