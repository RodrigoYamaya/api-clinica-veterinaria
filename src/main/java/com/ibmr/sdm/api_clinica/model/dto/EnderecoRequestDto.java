package com.ibmr.sdm.api_clinica.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDto(

        @NotBlank(message = "O CEP é de preenchimento obrigatório")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "O CEP deve estar no formato 12345-678 ou 12345678")
        String cep,

        @NotBlank(message = "O número é de preenchimento obrigatório")
        String numero,

        String complemento,

        String logradouro,
        String bairro,
        String cidade,
        String uf


) {
}
