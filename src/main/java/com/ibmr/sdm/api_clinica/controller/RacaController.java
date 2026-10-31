package com.ibmr.sdm.api_clinica.controller;

import com.ibmr.sdm.api_clinica.model.dto.RacaRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.RacaResponseDto;
import com.ibmr.sdm.api_clinica.service.RacaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/racas")
@CrossOrigin(origins = "*")
public class RacaController {

    @Autowired
    private RacaService racaService;

    @PostMapping
    public ResponseEntity<RacaResponseDto> cadastrarRaca(@RequestBody @Valid RacaRequestDto racaRequestDto) {
        RacaResponseDto raca = racaService.cadastrar(racaRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(raca);
    }

    @GetMapping
    public ResponseEntity<List<RacaResponseDto>> listarRacas() {
        List<RacaResponseDto> racas = racaService.listarRaca();
        return ResponseEntity.status(HttpStatus.OK).body(racas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RacaResponseDto> buscarPorId(@PathVariable(value = "id") Long id) {
        RacaResponseDto raca = racaService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(raca);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id) {
        racaService.deletarRaca(id);
        return ResponseEntity.status(HttpStatus.OK).body("Raça com o ID " + id + " deletada com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<RacaResponseDto> updateRaca(@PathVariable(value = "id") long id, @RequestBody @Valid RacaRequestDto racaDto) {
        RacaResponseDto racaUpdate = racaService.update(racaDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(racaUpdate);
    }
}
