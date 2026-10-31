package com.ibmr.sdm.api_clinica.controller;


import com.ibmr.sdm.api_clinica.model.dto.TutorRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.TutorResponseDto;
import com.ibmr.sdm.api_clinica.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutores")
@CrossOrigin(origins = "*")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @PostMapping
    public ResponseEntity<TutorResponseDto> cadastrarTutor(@RequestBody @Valid TutorRequestDto tutorRequestDto) {
        TutorResponseDto tutor = tutorService.cadastrar(tutorRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tutor);
    }

    @GetMapping
    public ResponseEntity<List<TutorResponseDto>> listarTutores() {
        List<TutorResponseDto> tutores = tutorService.listarTutor();
        return ResponseEntity.status(HttpStatus.OK).body(tutores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorResponseDto> buscarPorId(@PathVariable(value = "id") Long id) {
        TutorResponseDto tutor = tutorService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(tutor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id) {
        tutorService.deletarTutor(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tutor com o ID " + id + " deletado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorResponseDto> updateTutor(@PathVariable(value = "id") long id, @RequestBody @Valid TutorRequestDto tutorDto) {
        TutorResponseDto tutorUpdate = tutorService.update(tutorDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(tutorUpdate);
    }
    
}
