package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.Sexo;
import com.ibmr.sdm.api_clinica.model.enums.Situacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AnimalResponseDto(
        Long id,
        String nome,
        LocalDate dataNascimento,
        Integer idadeEstimadaMeses,
        Sexo sexo,
        String cor,
        BigDecimal pesoAtual,
        Long tutorId,
        String tutorNome,
        Long especieId,
        String especieNome,
        Long racaId,
        String racaNome,
        Situacao situacao
) {
}
