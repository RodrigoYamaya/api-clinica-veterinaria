package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.GovernoRequestDto;
import com.ibmr.sdm.api_clinica.model.entities.Animal;
import com.ibmr.sdm.api_clinica.model.entities.Vacina;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GovernoMapper {

    @Mapping(target = "nomeAnimal", source = "animal.nome")
    @Mapping(target = "especie", source = "animal.especie.nome")
    @Mapping(target = "cpfTutor", source = "animal.tutor.cpf")
    @Mapping(target = "nomeTutor", source = "animal.tutor.nomeCompleto")
    @Mapping(target = "raca", expression = "java(animal.getRaca() != null ? animal.getRaca().getNome() : \"SRD\")")

    @Mapping(target = "dataVacinaAntirrabica", source = "vacina.dataAplicacao")
    @Mapping(target = "nomeVacina", source = "vacina.nomeVacina")
    @Mapping(target = "fabricante", source = "vacina.fabricante")
    @Mapping(target = "numeroLote", source = "vacina.numeroLote")

    @Mapping(target = "cpfVeterinario", source = "vacina.veterinario.cpf")
    @Mapping(target = "crmvNumero", source = "vacina.veterinario.crmvNumero")
    @Mapping(target = "crmvUf", source = "vacina.veterinario.crmvUf")
    @Mapping(target = "clinica", constant = "VetCare")
    GovernoRequestDto toRequestDto(Animal animal, Vacina vacina, String codigoMicrochip);
}