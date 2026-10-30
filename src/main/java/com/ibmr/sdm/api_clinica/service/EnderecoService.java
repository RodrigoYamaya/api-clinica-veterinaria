package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.mapper.EnderecoMapper;
import com.ibmr.sdm.api_clinica.model.dto.EnderecoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.EnderecoResponseDto;
import com.ibmr.sdm.api_clinica.model.dto.EspecieResponseDto;
import com.ibmr.sdm.api_clinica.model.entities.Endereco;
import com.ibmr.sdm.api_clinica.model.entities.Especie;
import com.ibmr.sdm.api_clinica.repository.EnderecoRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnderecoService {


    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;

    public EnderecoService(EnderecoRepository enderecoRepository, EnderecoMapper enderecoMapper) {
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
    }

    public EnderecoResponseDto buscarPorId(long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Endereco com ID " + id + " não encontrada"));
        return enderecoMapper.toDto(endereco);
    }

    public List<EnderecoResponseDto> listarEndereco() {
        return enderecoRepository.findAll()
                .stream()
                .map(enderecoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public  EnderecoResponseDto cadastrar(EnderecoRequestDto enderecoDto)  {
        Endereco endereco = enderecoMapper.toEntity(enderecoDto);
        Endereco enderecoSave = enderecoRepository.save(endereco);
        return  enderecoMapper.toDto(enderecoSave);

    }


    @Transactional
    public void deletarEndereco(long id) {
        if(!enderecoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("endereco com ID" + id + "não encontrado");
        }
        enderecoRepository.deleteById(id);
    }


    @Transactional
    public EnderecoResponseDto update(EnderecoRequestDto enderecoDto, long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("endereco com ID" + id + "não encontrado"));

        endereco.setCep(enderecoDto.cep());
        endereco.setNumero(enderecoDto.numero());
        endereco.setComplemento(enderecoDto.complemento());
        endereco.setBairro(enderecoDto.bairro());
        endereco.setCidade(enderecoDto.cidade());
        endereco.setUf(enderecoDto.uf());
        Endereco enderecoUpdate = enderecoRepository.save(endereco);
        return  enderecoMapper.toDto(enderecoUpdate);
    }


}
