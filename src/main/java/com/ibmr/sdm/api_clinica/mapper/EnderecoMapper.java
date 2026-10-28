package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.EnderecoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.EnderecoResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    @Mapping(target = "id", ignore = true)
    Endereco  toEntity(EnderecoRequestDto dto);

    EnderecoResponseDto toDto(Endereco entity);
}
