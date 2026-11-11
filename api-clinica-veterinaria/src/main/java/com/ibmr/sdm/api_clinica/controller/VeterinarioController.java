package com.ibmr.sdm.api_clinica.controller;


import com.ibmr.sdm.api_clinica.model.dto.LoginRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.LoginResponseDto;
import com.ibmr.sdm.api_clinica.model.dto.VeterinarioRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.VeterinarioResponseDto;
import com.ibmr.sdm.api_clinica.service.VeterinarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarios")
@CrossOrigin(originPatterns = "*")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    @PostMapping
    public ResponseEntity<VeterinarioResponseDto> cadastrarVeterinario(@RequestBody @Valid VeterinarioRequestDto veterinarioRequestDto) {
        VeterinarioResponseDto veterinario = veterinarioService.cadastrar(veterinarioRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(veterinario);
    }

    @GetMapping
    public ResponseEntity<List<VeterinarioResponseDto>> listarVeterinarios() {
        List<VeterinarioResponseDto> veterinarios = veterinarioService.listarVeterinario();
        return ResponseEntity.status(HttpStatus.OK).body(veterinarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioResponseDto> buscarPorId(@PathVariable(value = "id") Long id) {
        VeterinarioResponseDto veterinario = veterinarioService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(veterinario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id) {
        veterinarioService.deletarVeterinario(id);
        return ResponseEntity.status(HttpStatus.OK).body("Veterinário com o ID " + id + " deletado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioResponseDto> updateVeterinario(@PathVariable(value = "id") long id, @RequestBody @Valid VeterinarioRequestDto veterinarioDto) {
        VeterinarioResponseDto veterinarioUpdate = veterinarioService.update(veterinarioDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(veterinarioUpdate);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto login = veterinarioService.realizarLogin(loginRequestDto);
        return ResponseEntity.ok(login);
    }

}
