package com.ibmr.sdm.api_clinica.model.entities;

import com.ibmr.sdm.api_clinica.model.enums.Sexo;
import com.ibmr.sdm.api_clinica.model.enums.Situacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    private LocalDate dataNascimento;
    private Integer idadeEstimadaMeses;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Sexo sexo;

    private BigDecimal pesoAtual;

    @Column(length = 50)
    private String cor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Tutor tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Especie especie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Raca raca;

    @Enumerated(EnumType.STRING)
    private Situacao situacao = Situacao.ATIVO;


}
