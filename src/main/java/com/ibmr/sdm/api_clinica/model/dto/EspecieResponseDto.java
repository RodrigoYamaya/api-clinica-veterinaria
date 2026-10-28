package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.Situacao;

public record EspecieResponseDto(
        Long id,
        String nome,
        String descricao,
        Situacao situacao

) {
}
