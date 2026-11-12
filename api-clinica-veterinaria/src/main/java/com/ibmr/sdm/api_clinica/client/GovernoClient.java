package com.ibmr.sdm.api_clinica.client;


import com.ibmr.sdm.api_clinica.model.dto.GovernoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.GovernoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// A URL virá do application.properties: governo.api.url=http://localhost:8082
@FeignClient(name = "governoClient", url = "${governo.api.url}")
public interface GovernoClient {

    @PostMapping("/api/governo/animais")
    GovernoResponseDto cadastrarAnimal(@RequestBody GovernoRequestDto request);

    @PutMapping("/api/governo/animais/{microchip}")
    GovernoResponseDto atualizarAnimal(
            @PathVariable("microchip") String microchip,
            @RequestBody GovernoRequestDto request
    );

    @PostMapping("/api/governo/vacinacoes-antirrabicas")
    GovernoResponseDto enviarVacinaAntirrabica(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody GovernoRequestDto request
    );




}
