//package com.ibmr.sdm.api_clinica.client;
//
//
//import com.ibmr.sdm.api_clinica.model.dto.GovernoRequestDto;
//import com.ibmr.sdm.api_clinica.model.dto.GovernoResponseDto;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//// A URL virá do application.properties: governo.api.url=http://localhost:8081
//@FeignClient(name = "governoClient", url = "${governo.api.url}")
//public interface GovernoClient {
//
//    @PostMapping("/api/governo/animais/microchip")
//    GovernoResponseDto enviarDadosGoverno(@RequestBody GovernoRequestDto request);
//}
