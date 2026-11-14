package com.ibmr.sdm.api_clinica.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record GovernoAnimalRequestDto(
        // --- Iremos utilizar JsonProperty resolver conflito campo e valor que estao diferentes que poderia chegar null causando erro; resolver utilizaremos JsonProperty sem a necessidad emudar o nome da variavel la no python;   ---
        @JsonProperty("microchip") String codigoMicrochip,
        @JsonProperty("nome") String nomeAnimal,
        String especie,
        String raca,
        @JsonProperty("dataNascimento") LocalDate dataNascimentoAnimal,
        String sexo,
        String cor,
        @JsonProperty("tutorNome") String nomeTutor,
        String cpfTutor,
        String origem,
        String cpfVeterinario,
        String crmvNumero,
        String crmvUf,
        String clinica
) {
}
