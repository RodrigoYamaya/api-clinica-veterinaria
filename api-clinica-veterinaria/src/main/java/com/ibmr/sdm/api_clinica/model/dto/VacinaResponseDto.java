package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.SituacaoVacina;

import java.time.LocalDate;

public record VacinaResponseDto(
        Long id,
        Long animalId,
        Long veterinarioId,
        String nomeVacina,
        String tipoVacina,
        Boolean antirrabica,
        String fabricante,
        String numeroLote,
        LocalDate dataAplicacao,
        String dose,
        String viaAplicacao,
        LocalDate dataProximaDose,
        LocalDate validadeLote,
        String observacoes,
        SituacaoVacina situacao
) {
}
