package com.ibmr.sdm.api_clinica.model.dto;

public record EnderecoViaCepDto(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        String ibge,
        Boolean erro
) {
}
