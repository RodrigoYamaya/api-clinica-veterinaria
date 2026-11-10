package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.SituacaoVacina;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record VacinaRequestDto(
        @NotNull(message = "O ID do animal é obrigatório") Long animalId,
        @NotNull(message = "O ID do veterinário é obrigatório") Long veterinarioId,
        @NotBlank(message = "O nome da vacina é obrigatório") String nomeVacina,
        @NotBlank(message = "O tipo da vacina é obrigatório") String tipoVacina,
        @NotNull(message = "Informe se é antirrábica") Boolean antirrabica,
        @NotBlank(message = "O fabricante é obrigatório") String fabricante,
        @NotBlank(message = "O número do lote é obrigatório") String numeroLote,
        LocalDate dataAplicacao,
        String dose,
        String viaAplicacao,
        LocalDate dataProximaDose,
        @NotNull(message = "A validade do lote é obrigatória") LocalDate validadeLote,
        String observacoes,
        SituacaoVacina situacao
) {
}
