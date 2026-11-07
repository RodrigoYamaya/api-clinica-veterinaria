package com.ibmr.sdm.api_clinica.repository;

import com.ibmr.sdm.api_clinica.model.entities.VinculoMicrochip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VinculoMicrochipRepository extends JpaRepository<VinculoMicrochip, Long> {

    Optional<VinculoMicrochip> findByAnimalIdAndDataFimIsNull(Long animalId);

    List<VinculoMicrochip> findAllByAnimalIdOrderByDataVinculoDesc(Long animalId);

    boolean existsByCodigoMicrochipAndDataFimIsNull(String codigoMicrochip);
}