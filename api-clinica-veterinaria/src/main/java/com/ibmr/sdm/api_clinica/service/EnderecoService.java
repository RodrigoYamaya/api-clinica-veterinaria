package com.ibmr.sdm.api_clinica.service;

import com.ibmr.sdm.api_clinica.Exceptions.RecursoNaoEncontradoException;
import com.ibmr.sdm.api_clinica.Exceptions.RegraDeNegocioException;
import com.ibmr.sdm.api_clinica.client.ViaCepClient;
import com.ibmr.sdm.api_clinica.mapper.EnderecoMapper;
import com.ibmr.sdm.api_clinica.model.dto.EnderecoRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.EnderecoResponseDto;
import com.ibmr.sdm.api_clinica.model.dto.EnderecoViaCepDto;
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
    private final ViaCepClient viaCepClient;

    public EnderecoService(EnderecoRepository enderecoRepository, EnderecoMapper enderecoMapper, ViaCepClient viaCepClient) {
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
        this.viaCepClient = viaCepClient;
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
    public EnderecoResponseDto cadastrar(EnderecoRequestDto enderecoDto) {
        Endereco endereco = new Endereco();

        processarPreenchimentoEndereco(endereco, enderecoDto);

        Endereco enderecoSave = enderecoRepository.save(endereco);
        return enderecoMapper.toDto(enderecoSave);
    }

    @Transactional
    public EnderecoResponseDto update(EnderecoRequestDto enderecoDto, long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("endereco com ID " + id + " não encontrado"));

        processarPreenchimentoEndereco(endereco, enderecoDto);

        Endereco enderecoUpdate = enderecoRepository.save(endereco);
        return enderecoMapper.toDto(enderecoUpdate);
    }
    @Transactional
    public void deletarEndereco(long id) {
        if(!enderecoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("endereco com ID " + id + " não encontrado");
        }
        enderecoRepository.deleteById(id);
    }

    private void processarPreenchimentoEndereco(Endereco endereco, EnderecoRequestDto dto) {
        String cepLimpo = dto.cep().replaceAll("\\D", "");

        EnderecoViaCepDto viaCepDto = viaCepClient.buscarEnderecoPorCep(cepLimpo);

        if (Boolean.TRUE.equals(viaCepDto.erro()) || viaCepDto.logradouro() == null) {

            if (isEnderecoManualValido(dto)) {
                endereco.setCep(cepLimpo);
                endereco.setLogradouro(dto.logradouro());
                endereco.setBairro(dto.bairro());
                endereco.setCidade(dto.cidade());
                endereco.setUf(dto.uf());
            } else {
                throw new RegraDeNegocioException("Serviço de CEP indisponível ou CEP inválido. Por favor, preencha os campos de endereço (logradouro, bairro, cidade, uf) manualmente.");
            }
        } else {
            endereco.setCep(viaCepDto.cep());
            endereco.setLogradouro(viaCepDto.logradouro());
            endereco.setBairro(viaCepDto.bairro());
            endereco.setCidade(viaCepDto.localidade());
            endereco.setUf(viaCepDto.uf());
            endereco.setCodigoIbge(viaCepDto.ibge());
        }

        endereco.setNumero(dto.numero());

        if (dto.complemento() != null && !dto.complemento().isBlank()) {
            endereco.setComplemento(dto.complemento());
        } else if (viaCepDto != null && viaCepDto.complemento() != null) {
            endereco.setComplemento(viaCepDto.complemento());
        }
    }

    private boolean isEnderecoManualValido(EnderecoRequestDto dto) {
        return dto.logradouro() != null && !dto.logradouro().isBlank() &&
                dto.bairro() != null && !dto.bairro().isBlank() &&
                dto.cidade() != null && !dto.cidade().isBlank() &&
                dto.uf() != null && !dto.uf().isBlank();
    }

}
