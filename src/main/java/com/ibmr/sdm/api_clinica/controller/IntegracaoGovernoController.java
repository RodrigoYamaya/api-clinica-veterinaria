package com.ibmr.sdm.api_clinica.controller;

import com.ibmr.sdm.api_clinica.model.dto.IntegracaoGovernoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.IntegracaoGovernoResponseDto;
import com.ibmr.sdm.api_clinica.service.IntegracaoGovernoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/integracoes-governo")
public class IntegracaoGovernoController {

    @Autowired
    private  IntegracaoGovernoService service;

    public IntegracaoGovernoController(IntegracaoGovernoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<IntegracaoGovernoResponseDto> solicitar(@RequestBody @Valid IntegracaoGovernoRequestDto dto) {
        IntegracaoGovernoResponseDto response = service.solicitarIntegracao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IntegracaoGovernoResponseDto>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IntegracaoGovernoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
