package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.Situacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EspecieRequestDto(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 60, message = "O nome deve ter no máximo 60 caracteres")
        String nome,

        @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
        String descricao,

        Situacao situacao
) {
}
