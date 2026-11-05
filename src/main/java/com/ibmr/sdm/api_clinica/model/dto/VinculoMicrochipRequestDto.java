package com.ibmr.sdm.api_clinica.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VinculoMicrochipRequestDto(
        @NotNull(message = "O ID do veterinário é obrigatório")
        Long veterinarioId,

        @NotBlank(message = "O código do microchip é obrigatório")
        String codigoMicrochip
) {
}
