package com.ibmr.sdm.api_clinica.model.dto;

public record LoginResponseDto(
        Long id,
        String nomeCompleto,
        String email
) {
}
