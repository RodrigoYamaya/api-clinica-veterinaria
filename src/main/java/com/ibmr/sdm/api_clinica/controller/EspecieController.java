package com.ibmr.sdm.api_clinica.controller;

import com.ibmr.sdm.api_clinica.model.dto.EnderecoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.EnderecoResponseDto;
import com.ibmr.sdm.api_clinica.model.dto.EspecieRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.EspecieResponseDto;
import com.ibmr.sdm.api_clinica.service.EspecieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especies")
@CrossOrigin(origins = "*")
public class EspecieController {

    @Autowired
    private EspecieService especieService;

    @PostMapping
    public ResponseEntity<EspecieResponseDto> cadastrarEspecie(@RequestBody @Valid EspecieRequestDto especieDto) {
        EspecieResponseDto especie = especieService.cadastrar(especieDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(especie);
    }


    @GetMapping
    public ResponseEntity<List<EspecieResponseDto>> listarEspecie() {
        List<EspecieResponseDto> especie = especieService.listarEspecie();
        return ResponseEntity.status(HttpStatus.OK).body(especie);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecieResponseDto> buscarPorId(@PathVariable(value = "id") Long id) {
        EspecieResponseDto especie = especieService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(especie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id) {
        especieService.deletarEspecie(id);
        return ResponseEntity.status(HttpStatus.OK).body("especie com o ID " + id + " deletado com sucesso.");
    }


    @PutMapping("/{id}")
    public ResponseEntity<EspecieResponseDto> updateEspecie(@PathVariable(value = "id") long id, @RequestBody @Valid EspecieRequestDto especieDto) {
        EspecieResponseDto especieUpdate = especieService.update(especieDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(especieUpdate);
    }

}
