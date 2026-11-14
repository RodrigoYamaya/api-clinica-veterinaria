package com.ibmr.sdm.api_clinica.client;

import com.ibmr.sdm.api_clinica.model.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "governoClient", url = "${governo.api.url}")
public interface GovernoClient {

    @PostMapping("/api/governo/animais")
    GovernoAnimalResponseDto cadastrarAnimal(@RequestBody GovernoAnimalRequestDto request);

    @PutMapping("/api/governo/animais/{microchip}")
    GovernoAnimalResponseDto atualizarAnimal(
            @PathVariable("microchip") String microchip,
            @RequestBody GovernoAnimalRequestDto request
    );

    @PostMapping("/api/governo/vacinacoes-antirrabicas")
    GovernoAnimalResponseDto enviarVacinaAntirrabica(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody GovernoVacinaRequestDto request
    );
}