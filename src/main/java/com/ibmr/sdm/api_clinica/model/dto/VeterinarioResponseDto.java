package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.Situacao;

import java.time.LocalDateTime;

public record VeterinarioResponseDto(
        Long id,
        String nomeCompleto,
        String cpf,
        String crmvNumero,
        String crmvUf,
        String email,
        String telefone,
        Situacao situacao,
        LocalDateTime dataCadastro
) {
}
