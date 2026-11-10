package com.ibmr.sdm.api_clinica.client;

import com.ibmr.sdm.api_clinica.model.dto.EnderecoViaCepDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(
        name = "viacep",
        url = "https://viacep.com.br/ws",
        fallback = ViaCepClientFallback.class
)
public interface ViaCepClient {
    @GetMapping("/{cep}/json/")
    EnderecoViaCepDto buscarEnderecoPorCep(@PathVariable("cep") String cep);
}
