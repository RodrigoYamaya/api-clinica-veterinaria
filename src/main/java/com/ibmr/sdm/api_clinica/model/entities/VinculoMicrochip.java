package com.ibmr.sdm.api_clinica.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinculoMicrochip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @Column(name = "codigo_microchip", nullable = false, unique = true)
    private String codigoMicrochip;

    @Column(name = "data_vinculo", nullable = false, updatable = false)
    private LocalDateTime dataVinculo;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @Column(name = "motivo_encerramento")
    private String motivoEncerramento;
}

