package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.IntegracaoGovernoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.IntegracaoGovernoResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.IntegracaoGoverno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IntegracaoGovernoMapper {

    @Mapping(source = "animalId", target = "animal.id")
    @Mapping(source = "vacinaId", target = "vacina.id")
    IntegracaoGoverno toEntity(IntegracaoGovernoRequestDto dto);

    @Mapping(source = "animal.id", target = "animalId")
    @Mapping(source = "vacina.id", target = "vacinaId")
    IntegracaoGovernoResponseDto toDto(IntegracaoGoverno entity);
}
