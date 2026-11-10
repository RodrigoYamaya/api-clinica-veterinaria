package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.SituacaoConsulta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConsultaRequestDto(
        @NotNull(message = "O ID do animal é obrigatório") Long animalId,
        @NotNull(message = "O ID do veterinário é obrigatório") Long veterinarioId,
        @NotNull(message = "A data e hora são obrigatórias") LocalDateTime dataHora,
        @NotBlank(message = "O motivo é obrigatório") String motivo,
        String anamnese,
        String sintomas,
        String diagnostico,
        String tratamento,
        String prescricao,
        BigDecimal pesoKg,
        BigDecimal temperaturaC,
        String observacoes,
        SituacaoConsulta situacao
) {
}
