package com.ibmr.sdm.api_clinica.model.entities;

import com.ibmr.sdm.api_clinica.model.enums.SituacaoVacina;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Veterinario veterinario;

    @Column(nullable = false, length = 100)
    private String nomeVacina;

    @Column(nullable = false, length = 60)
    private String tipoVacina;

    @Column(nullable = false)
    @Builder.Default
    private Boolean antirrabica = false;

    @Column(nullable = false, length = 100)
    private String fabricante;

    @Column(nullable = false, length = 40)
    private String numeroLote;


    private LocalDate dataAplicacao;

    @Column(length = 30)
    private String dose;

    @Column(length = 30)
    private String viaAplicacao;


    private LocalDate dataProximaDose;

    @Column(nullable = false)
    private LocalDate validadeLote;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", columnDefinition = "VARCHAR(50)")
    @Builder.Default
    private SituacaoVacina situacao = SituacaoVacina.AGENDADA;
}
