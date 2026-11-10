package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.TipoOperacaoIntegracao;
import jakarta.validation.constraints.NotNull;

public record IntegracaoGovernoRequestDto(
        @NotNull(message = "O ID do animal é obrigatório para a integração")
        Long animalId,

        Long vacinaId,

        @NotNull(message = "O tipo de operação é obrigatório")
        TipoOperacaoIntegracao tipoOperacao,

        String chaveIdempotencia
) {
}
