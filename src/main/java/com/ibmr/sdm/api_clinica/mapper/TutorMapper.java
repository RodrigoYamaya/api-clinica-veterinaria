package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.TutorRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.TutorResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Tutor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class})
public interface TutorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "endereco.id", source = "enderecoId")
    Tutor toEntity(TutorRequestDto dto);

    TutorResponseDto toDto(Tutor entity);


}
