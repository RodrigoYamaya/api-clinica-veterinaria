package com.ibmr.sdm.api_clinica.model.dto;

import java.time.LocalDateTime;

public record VinculoMicrochipResponseDto(
        Long id,
        Long animalId,
        Long veterinarioId,
        String codigoMicrochip,
        LocalDateTime dataVinculo,
        LocalDateTime dataFim,
        String motivoEncerramento
) {
}
