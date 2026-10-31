package com.ibmr.sdm.api_clinica.repository;

import com.ibmr.sdm.api_clinica.model.entities.Raca;
import com.ibmr.sdm.api_clinica.model.entities.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RacaRepository extends JpaRepository<Raca, Long> {
}
