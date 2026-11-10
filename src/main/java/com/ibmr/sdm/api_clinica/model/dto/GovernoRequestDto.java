package com.ibmr.sdm.api_clinica.model.dto;

import java.time.LocalDate;

public record GovernoRequestDto(
        String codigoMicrochip,
        String nomeAnimal,
        String especie,
        String raca,
        String cpfTutor,
        String nomeTutor,
        LocalDate dataVacinaAntirrabica,
        String nomeVacina,
        String fabricante,
        String numeroLote,
        String nomeVeterinario,
        String crmvVeterinario
) {
}
