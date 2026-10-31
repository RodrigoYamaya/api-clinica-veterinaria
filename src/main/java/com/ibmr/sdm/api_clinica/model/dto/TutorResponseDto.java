package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.Situacao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TutorResponseDto(
        Long id,
        String nomeCompleto,
        String cpf,
        LocalDate dataNascimento,
        String email,
        String telefone,
        EnderecoResponseDto endereco,
        LocalDateTime dataCadastro,
        Situacao situacao
) {
}
