package com.ibmr.sdm.api_clinica.model.entities;

import com.ibmr.sdm.api_clinica.model.enums.SituacaoIntegracao;
import com.ibmr.sdm.api_clinica.model.enums.SituacaoVacina;
import com.ibmr.sdm.api_clinica.model.enums.TipoOperacaoIntegracao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegracaoGoverno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacina_id")
    private Vacina vacina;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao", nullable = false)
    private TipoOperacaoIntegracao tipoOperacao;

    @Column(name = "protocolo_externo", length = 50)
    private String protocoloExterno;

    @Column(name = "chave_idempotencia", nullable = false, unique = true, length = 100)
    private String chaveIdempotencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, columnDefinition = "VARCHAR(50)")
    private SituacaoIntegracao situacao = SituacaoIntegracao.PENDENTE;

    @Column(name = "data_solicitacao", nullable = false, updatable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    @Column(name = "tentativas", nullable = false)
    private Integer tentativas = 0;

    @Column(name = "mensagem_erro", length = 500)
    private String mensagemErro;
}
