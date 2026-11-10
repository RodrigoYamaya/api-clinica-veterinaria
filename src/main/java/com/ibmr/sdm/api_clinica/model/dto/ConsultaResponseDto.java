package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.SituacaoConsulta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConsultaResponseDto(
        Long id,
        Long animalId,
        Long veterinarioId,
        LocalDateTime dataHora,
        String motivo,
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
