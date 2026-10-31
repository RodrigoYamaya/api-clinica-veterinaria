package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.TutorMapper;
import com.ibmr.sdm.api_clinica.model.dto.TutorRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.TutorResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Endereco;
import com.ibmr.sdm.api_clinica.model.entities.Tutor;
import com.ibmr.sdm.api_clinica.repository.EnderecoRepository;
import com.ibmr.sdm.api_clinica.repository.TutorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;
    private final TutorMapper tutorMapper;
    private final EnderecoRepository enderecoRepository;

    public TutorService(TutorRepository tutorRepository, TutorMapper tutorMapper, EnderecoRepository enderecoRepository) {
        this.tutorRepository = tutorRepository;
        this.tutorMapper = tutorMapper;
        this.enderecoRepository = enderecoRepository;
    }

    public List<TutorResponseDto> listarTutor() {
        return tutorRepository.findAll()
                .stream()
                .map(tutorMapper::toDto)
                .collect(Collectors.toList());
    }

    public TutorResponseDto buscarPorId(long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor com ID " + id + " não encontrado"));
        return tutorMapper.toDto(tutor);
    }

    @Transactional
    public TutorResponseDto cadastrar(TutorRequestDto tutorDto) {
        if (tutorRepository.existsByCpf(tutorDto.cpf())) {
            throw new IllegalArgumentException("Já existe um tutor cadastrado com o CPF: " + tutorDto.cpf());
        }

        if (!enderecoRepository.existsById(tutorDto.enderecoId())) {
            throw new RecursoNaoEncontradoException("Endereço com ID " + tutorDto.enderecoId() + " não encontrado");
        }

        Tutor tutor = tutorMapper.toEntity(tutorDto);
        Tutor tutorSave = tutorRepository.save(tutor);
        return tutorMapper.toDto(tutorSave);
    }

    @Transactional
    public void deletarTutor(long id) {
        if (!tutorRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Tutor com ID " + id + " não encontrado");
        }
        tutorRepository.deleteById(id);
    }

    @Transactional
    public TutorResponseDto update(TutorRequestDto tutorDto, long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor com ID " + id + " não encontrado"));

        if (!tutor.getCpf().equals(tutorDto.cpf()) && tutorRepository.existsByCpf(tutorDto.cpf())) {
            throw new IllegalArgumentException("O CPF informado já está em uso por outro tutor.");
        }

        if (!tutor.getEndereco().getId().equals(tutorDto.enderecoId())) {
            Endereco endereco = enderecoRepository.findById(tutorDto.enderecoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Endereço com ID " + tutorDto.enderecoId() + " não encontrado"));
            tutor.setEndereco(endereco);
        }

        tutor.setNomeCompleto(tutorDto.nomeCompleto());
        tutor.setCpf(tutorDto.cpf());
        tutor.setDataNascimento(tutorDto.dataNascimento());
        tutor.setEmail(tutorDto.email());
        tutor.setTelefone(tutorDto.telefone());
        if (tutorDto.situacao() != null) {
            tutor.setSituacao(tutorDto.situacao());
        }

        Tutor tutorUpdate = tutorRepository.save(tutor);
        return tutorMapper.toDto(tutorUpdate);
    }
}
