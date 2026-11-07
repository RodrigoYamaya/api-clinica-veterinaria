package com.ibmr.sdm.api_clinica.repository;

import com.ibmr.sdm.api_clinica.model.entities.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long > {
    boolean existsByCpf (String cpf);
    Optional<Veterinario> findByEmail(String email);
}
