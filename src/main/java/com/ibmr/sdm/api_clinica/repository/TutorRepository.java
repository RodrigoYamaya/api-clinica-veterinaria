package com.ibmr.sdm.api_clinica.repository;

import com.ibmr.sdm.api_clinica.model.entities.Tutor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    boolean existsByCpf(String cpf);

    Optional<Tutor> findByCpf(String cpf);

}
