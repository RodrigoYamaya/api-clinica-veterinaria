package com.ibmr.sdm.api_clinica.repository;

import com.ibmr.sdm.api_clinica.model.entities.IntegracaoGoverno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntegracaoGovernoRepository extends JpaRepository<IntegracaoGoverno,Long> {
    boolean existsByChaveIdempotencia(String chaveIdempotencia);
}
