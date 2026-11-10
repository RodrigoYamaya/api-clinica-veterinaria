package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.RacaRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.RacaResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Raca;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RacaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "especie.id", source = "especieId")
    Raca toEntity(RacaRequestDto dto);

    @Mapping(target = "especieId", source = "especie.id")
    @Mapping(target = "especieNome", source = "especie.nome")
    RacaResponseDto toDto(Raca entity);
}
