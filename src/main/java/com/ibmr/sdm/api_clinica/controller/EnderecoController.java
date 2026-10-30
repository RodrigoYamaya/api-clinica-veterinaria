package com.ibmr.sdm.api_clinica.controller;

import com.ibmr.sdm.api_clinica.model.dto.EnderecoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.EnderecoResponseDto;
import com.ibmr.sdm.api_clinica.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@CrossOrigin(origins = "*")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<EnderecoResponseDto> cadastrarEndereco(@RequestBody @Valid EnderecoRequestDto enderecoRequestDto) {
        EnderecoResponseDto endereco = enderecoService.cadastrar(enderecoRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
    }

    @GetMapping
    public ResponseEntity<List<EnderecoResponseDto>> listarEnderecos() {
        List<EnderecoResponseDto>  endereco = enderecoService.listarEndereco();
        return ResponseEntity.status(HttpStatus.OK).body(endereco);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDto> buscarPorId(@PathVariable(value = "id") Long id) {
        EnderecoResponseDto endereco = enderecoService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(endereco);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id) {
        enderecoService.deletarEndereco(id);
        return ResponseEntity.status(HttpStatus.OK).body("endereco com o ID " + id + " deletado com sucesso.");
    }


    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDto> updateEndereco(@PathVariable(value = "id") long id, @RequestBody @Valid EnderecoRequestDto enderecoDto) {
        EnderecoResponseDto enderecoUpdate = enderecoService.update(enderecoDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(enderecoUpdate);


    }


}
