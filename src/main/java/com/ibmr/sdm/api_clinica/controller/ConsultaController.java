package com.ibmr.sdm.api_clinica.controller;

import com.ibmr.sdm.api_clinica.model.dto.ConsultaRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.ConsultaResponseDto;
import com.ibmr.sdm.api_clinica.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@CrossOrigin(origins = "*")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<ConsultaResponseDto> cadastrar(@RequestBody @Valid ConsultaRequestDto dto) {
        ConsultaResponseDto response = consultaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ConsultaResponseDto>> listarTodas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaResponseDto> atualizar(@PathVariable Long id, @RequestBody @Valid ConsultaRequestDto dto) {
        return ResponseEntity.ok(consultaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        consultaService.excluir(id);
        return ResponseEntity.noContent().build();
    }


}
