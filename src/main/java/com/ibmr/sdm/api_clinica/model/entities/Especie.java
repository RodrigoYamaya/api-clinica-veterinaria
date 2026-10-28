package com.ibmr.sdm.api_clinica.model.entities;

import com.ibmr.sdm.api_clinica.model.enums.Situacao;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60, unique = true)
    private String nome;

    @Column(length = 200)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Situacao situacao = Situacao.ATIVO;

    public  Especie() {

    }

    public Especie(Long id, String descricao, String nome, Situacao situacao) {
        this.id = id;
        this.descricao = descricao;
        this.nome = nome;
        this.situacao = situacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Especie especie)) return false;
        return Objects.equals(id, especie.id) && Objects.equals(nome, especie.nome) && Objects.equals(descricao, especie.descricao) && situacao == especie.situacao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descricao, situacao);
    }
}
