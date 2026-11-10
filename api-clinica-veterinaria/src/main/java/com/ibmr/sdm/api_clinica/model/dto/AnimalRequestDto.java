package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AnimalRequestDto(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        LocalDate dataNascimento,

        Integer idadeEstimadaMeses,

        @NotNull(message = "O sexo é obrigatório")
        Sexo sexo,

        String cor,

        BigDecimal pesoAtual,

        @NotNull(message = "O ID do tutor é obrigatório")
        Long tutorId,

        @NotNull(message = "O ID da espécie é obrigatório")
        Long especieId,

        Long racaId
) {
}
