package com.ibmr.sdm.api_clinica.mapper;

import com.ibmr.sdm.api_clinica.model.dto.GovernoAnimalRequestDto;
import com.ibmr.sdm.api_clinica.model.dto.GovernoVacinaRequestDto;
import com.ibmr.sdm.api_clinica.model.entities.Animal;
import com.ibmr.sdm.api_clinica.model.entities.Vacina;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GovernoMapper {

    @Mapping(target = "codigoMicrochip", source = "codigoMicrochip")
    @Mapping(target = "nomeAnimal", source = "animal.nome")
    @Mapping(target = "especie", source = "animal.especie.nome")
    @Mapping(target = "raca", expression = "java(animal.getRaca() != null ? animal.getRaca().getNome() : \"SRD\")")
    @Mapping(target = "dataNascimentoAnimal", source = "animal.dataNascimento")
    @Mapping(target = "sexo", source = "animal.sexo")
    @Mapping(target = "cor", source = "animal.cor")
    @Mapping(target = "nomeTutor", source = "animal.tutor.nomeCompleto")
    @Mapping(target = "cpfTutor", source = "animal.tutor.cpf")
    @Mapping(target = "origem", ignore = true)
    @Mapping(target = "cpfVeterinario", ignore = true)
    @Mapping(target = "crmvNumero", ignore = true)
    @Mapping(target = "crmvUf", ignore = true)
    @Mapping(target = "clinica", constant = "VetCare")
    GovernoAnimalRequestDto toAnimalRequestDto(Animal animal, String codigoMicrochip);

    @Mapping(target = "codigoMicrochip", source = "codigoMicrochip")
    @Mapping(target = "nomeAnimal", source = "animal.nome")
    @Mapping(target = "dataAplicacao", source = "vacina.dataAplicacao")
    @Mapping(target = "nomeVacina", source = "vacina.nomeVacina")
    @Mapping(target = "fabricante", source = "vacina.fabricante")
    @Mapping(target = "numeroLote", source = "vacina.numeroLote")
    @Mapping(target = "veterinarioResponsavel", expression = "java(vacina.getVeterinario() != null ? vacina.getVeterinario().getNomeCompleto() + \" - CRMV \" + vacina.getVeterinario().getCrmvNumero() + \"/\" + vacina.getVeterinario().getCrmvUf() : null)")
    GovernoVacinaRequestDto toVacinaRequestDto(Animal animal, Vacina vacina, String codigoMicrochip);
}
