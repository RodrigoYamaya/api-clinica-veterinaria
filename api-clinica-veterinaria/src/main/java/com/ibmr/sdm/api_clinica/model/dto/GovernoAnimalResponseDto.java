package com.ibmr.sdm.api_clinica.model.dto;

public record GovernoAnimalResponseDto(
        boolean sucesso,
        boolean duplicada,
        String protocolo,
        String status,
        String mensagem,
        String recebidoEm
) {
}
