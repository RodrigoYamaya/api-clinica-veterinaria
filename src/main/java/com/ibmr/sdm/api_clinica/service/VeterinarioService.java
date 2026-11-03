package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.VeterinarioMapper;
import com.ibmr.sdm.api_clinica.model.dto.VeterinarioRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.VeterinarioResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Veterinario;
import com.ibmr.sdm.api_clinica.repository.VeterinarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final VeterinarioMapper veterinarioMapper;
//    private final PasswordEncoder passwordEncoder;

    public VeterinarioService(VeterinarioRepository veterinarioRepository, VeterinarioMapper veterinarioMapper) {
        this.veterinarioRepository = veterinarioRepository;
        this.veterinarioMapper = veterinarioMapper;
//        this.passwordEncoder = passwordEncoder;
    }

    public List<VeterinarioResponseDto> listarVeterinario() {
        return veterinarioRepository.findAll()
                .stream()
                .map(veterinarioMapper::toDto)
                .collect(Collectors.toList());
    }

    public VeterinarioResponseDto buscarPorId(long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veterinário com ID " + id + " não encontrado"));
        return veterinarioMapper.toDto(veterinario);
    }

    @Transactional
    public VeterinarioResponseDto cadastrar(VeterinarioRequestDto veterinarioDto) {
        if (veterinarioRepository.existsByCpf(veterinarioDto.cpf())) {
            throw new IllegalArgumentException("Já existe um veterinário cadastrado com o CPF: " + veterinarioDto.cpf());
        }

        Veterinario veterinario = veterinarioMapper.toEntity(veterinarioDto);
        veterinario.setSenhaHash(veterinarioDto.senha());
        Veterinario veterinarioSalvo = veterinarioRepository.save(veterinario);
        return veterinarioMapper.toDto(veterinarioSalvo);
    }

    @Transactional
    public void deletarVeterinario(long id) {
        if (!veterinarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Veterinário com ID " + id + " não encontrado");
        }
        veterinarioRepository.deleteById(id);
    }

    @Transactional
    public VeterinarioResponseDto update(VeterinarioRequestDto veterinarioDto, long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veterinário com ID " + id + " não encontrado"));

        if (!veterinario.getCpf().equals(veterinarioDto.cpf()) && veterinarioRepository.existsByCpf(veterinarioDto.cpf())) {
            throw new IllegalArgumentException("O CPF informado já está em uso por outro veterinário.");
        }

        veterinario.setNomeCompleto(veterinarioDto.nomeCompleto());
        veterinario.setCpf(veterinarioDto.cpf());
        veterinario.setCrmvNumero(veterinarioDto.crmvNumero());
        veterinario.setCrmvUf(veterinarioDto.crmvUf());
        veterinario.setEmail(veterinarioDto.email());
        veterinario.setTelefone(veterinarioDto.telefone());

         //Se a requisição enviar uma nova senha, nós a atualizamos. Se não, mantemos a antiga.Mas opcional ainda,devemos focar em outras regras que não a prioridade.
        if (veterinarioDto.senha() != null && !veterinarioDto.senha().isBlank()) {
            veterinario.setSenhaHash(veterinarioDto.senha());
        }

        Veterinario veterinarioUpdate = veterinarioRepository.save(veterinario);
        return veterinarioMapper.toDto(veterinarioUpdate);
    }
}
