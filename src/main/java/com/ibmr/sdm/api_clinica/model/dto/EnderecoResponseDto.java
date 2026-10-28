package com.ibmr.sdm.api_clinica.model.dto;

public record EnderecoResponseDto(
        Long id,
        String cep,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String codigoIbge
) {
}
