package com.ibmr.sdm.api_clinica.controller;

import com.ibmr.sdm.api_clinica.model.dto.VinculoMicrochipRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.VinculoMicrochipResponseDto;
import com.ibmr.sdm.api_clinica.service.VinculoMicrochipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalMicrochipController {

    @Autowired
    private VinculoMicrochipService vinculoMicrochipService;

    @PostMapping("/{id}/microchip")
    public ResponseEntity<VinculoMicrochipResponseDto> vincularMicrochip(
            @PathVariable Long id,
            @RequestBody @Valid VinculoMicrochipRequestDto dto) {

        VinculoMicrochipResponseDto response = vinculoMicrochipService.vincularMicrochip(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/microchip/historico")
    public ResponseEntity<List<VinculoMicrochipResponseDto>> listarHistorico(@PathVariable Long id) {
        return ResponseEntity.ok(vinculoMicrochipService.listarHistoricoDoAnimal(id));
    }

}
