package com.ibmr.sdm.api_clinica.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank String email,
        @NotBlank String senha

) {
}
