package com.ibmr.sdm.api_clinica.repository;

import com.ibmr.sdm.api_clinica.model.entities.IntegracaoGoverno;
import com.ibmr.sdm.api_clinica.model.enums.SituacaoIntegracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface IntegracaoGovernoRepository extends JpaRepository<IntegracaoGoverno,Long> {
    boolean existsByChaveIdempotencia(String chaveIdempotencia);

    List<IntegracaoGoverno> findBySituacao(SituacaoIntegracao situacaoIntegracao);

    Optional<IntegracaoGoverno> findByChaveIdempotencia(String chaveIdempotencia);
}
