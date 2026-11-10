package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.Situacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RacaRequestDto(
        @NotBlank(message = "O nome da raça é obrigatório")
        @Size(max = 80, message = "O nome da raça deve ter no máximo 80 caracteres")
        String nome,

        @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
        String descricao,

        @NotNull(message = "O ID da espécie é obrigatório")
        Long especieId,

        Situacao situacao
) {
}
