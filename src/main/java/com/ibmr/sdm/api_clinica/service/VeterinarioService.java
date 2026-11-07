package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.Exceptions.RegraDeNegocioException;
import com.ibmr.sdm.api_clinica.mapper.VeterinarioMapper;
import com.ibmr.sdm.api_clinica.model.dto.LoginRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.LoginResponseDto;
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

        if (veterinarioDto.senha() != null && !veterinarioDto.senha().isBlank()) {
            veterinario.setSenhaHash(veterinarioDto.senha());
        }

        Veterinario veterinarioUpdate = veterinarioRepository.save(veterinario);
        return veterinarioMapper.toDto(veterinarioUpdate);
    }


    @Transactional
    public LoginResponseDto realizarLogin(LoginRequestDto request) {
        Veterinario vet = veterinarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RegraDeNegocioException("E-mail ou senha inválidos."));

        // 2. Aqui iremos Compara a senha (focamos na simplicidade, sem autenticação a principio)
        if (!vet.getSenhaHash().equals(request.senha())) {
            throw new RegraDeNegocioException("E-mail ou senha inválidos."); // Mesma mensagem por segurança
        }

        if ("INATIVO".equals(vet.getSituacao().name())) {
            throw new RegraDeNegocioException("Veterinário inativo no sistema.");
        }
        return veterinarioMapper.toLoginResponseDto(vet);
    }
}
