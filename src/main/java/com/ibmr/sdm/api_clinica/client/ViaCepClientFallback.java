package com.ibmr.sdm.api_clinica.client;

import com.ibmr.sdm.api_clinica.model.dto.EnderecoViaCepDto;
import org.springframework.stereotype.Component;

@Component
public class ViaCepClientFallback implements ViaCepClient{
    @Override
    public EnderecoViaCepDto buscarEnderecoPorCep(String cep) {
        return new EnderecoViaCepDto(cep, null, null, null, null, null, null, true);
    }
}
