package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.VeterinarioRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.VeterinarioResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Veterinario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VeterinarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "situacao", constant = "ATIVO")
    @Mapping(target = "senhaHash", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "ultimoAcesso", ignore = true)
    Veterinario toEntity(VeterinarioRequestDto dto);

    VeterinarioResponseDto toDto(Veterinario veterinario);
}

