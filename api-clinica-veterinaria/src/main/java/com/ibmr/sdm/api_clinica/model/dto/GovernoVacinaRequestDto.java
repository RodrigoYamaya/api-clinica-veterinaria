package com.ibmr.sdm.api_clinica.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record GovernoVacinaRequestDto(
        @JsonProperty("codigo_microchip")
        String codigoMicrochip,

        @JsonProperty("nome_animal")
        String nomeAnimal,

        @JsonProperty("data_aplicacao")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataAplicacao,

        @JsonProperty("nome_vacina")
        String nomeVacina,

        @JsonProperty("fabricante")
        String fabricante,

        @JsonProperty("numero_lote")
        String numeroLote,

        @JsonProperty("veterinario_responsavel")
        String veterinarioResponsavel

) {
}
