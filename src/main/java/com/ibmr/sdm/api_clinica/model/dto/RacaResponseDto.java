package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.Situacao;

public record RacaResponseDto(
        Long id,
        String nome,
        String descricao,
        Long especieId,
        String especieNome,
        Situacao situacao

) {
}
