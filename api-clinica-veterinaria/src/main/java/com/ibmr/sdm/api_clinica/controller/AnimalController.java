package com.ibmr.sdm.api_clinica.controller;

import com.ibmr.sdm.api_clinica.model.dto.AnimalRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.AnimalResponseDto;
import com.ibmr.sdm.api_clinica.model.dto.ConsultaResponseDto;
import com.ibmr.sdm.api_clinica.model.dto.VacinaResponseDto;
import com.ibmr.sdm.api_clinica.service.AnimalService;
import com.ibmr.sdm.api_clinica.service.ConsultaService;
import com.ibmr.sdm.api_clinica.service.VacinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    @Autowired
    private AnimalService animalService;
    @Autowired
    private ConsultaService consultaService;
    @Autowired
    private VacinaService vacinaService;

    @PostMapping
    public ResponseEntity<AnimalResponseDto> cadastrarAnimal(@RequestBody @Valid AnimalRequestDto animalRequestDto) {
        AnimalResponseDto animal = animalService.cadastrar(animalRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(animal);
    }

    @GetMapping
    public ResponseEntity<List<AnimalResponseDto>> listarAnimais() {
        List<AnimalResponseDto> animais = animalService.listarAnimal();
        return ResponseEntity.status(HttpStatus.OK).body(animais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDto> buscarPorId(@PathVariable(value = "id") Long id) {
        AnimalResponseDto animal = animalService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(animal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id) {
        animalService.deletarAnimal(id);
        return ResponseEntity.status(HttpStatus.OK).body("Animal com o ID " + id + " deletado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponseDto> updateAnimal(@PathVariable(value = "id") long id, @RequestBody @Valid AnimalRequestDto animalDto) {
        AnimalResponseDto animalUpdate = animalService.update(animalDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(animalUpdate);
    }

    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<ConsultaResponseDto>> listarConsultaPorAnimal(@PathVariable(value = "id") Long id) {
        List<ConsultaResponseDto> consultas = consultaService.listarPorAnimalId(id);
        return ResponseEntity.status(HttpStatus.OK).body(consultas);
    }

    @GetMapping("/{id}/vacinas")
    public  ResponseEntity<List<VacinaResponseDto>> listarVacinasPorAnimal(@PathVariable(value = "id") Long id) {
        List<VacinaResponseDto> vacinas = vacinaService.listarPorVacinaId(id);
        return ResponseEntity.status(HttpStatus.OK).body(vacinas);
    }
}
