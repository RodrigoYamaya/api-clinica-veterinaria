package com.ibmr.sdm.api_clinica.model.entities;

import com.ibmr.sdm.api_clinica.model.enums.Situacao;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Raca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(length = 200)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Especie especie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Situacao situacao = Situacao.ATIVO;
}
