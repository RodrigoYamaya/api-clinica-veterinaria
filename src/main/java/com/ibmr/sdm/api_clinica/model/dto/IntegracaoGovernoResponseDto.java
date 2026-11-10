package com.ibmr.sdm.api_clinica.model.dto;

import com.ibmr.sdm.api_clinica.model.enums.SituacaoIntegracao;
import com.ibmr.sdm.api_clinica.model.enums.TipoOperacaoIntegracao;

import java.time.LocalDateTime;

public record IntegracaoGovernoResponseDto(
        Long id,
        Long animalId,
        Long vacinaId,
        TipoOperacaoIntegracao tipoOperacao,
        String protocoloExterno,
        String chaveIdempotencia,
        SituacaoIntegracao situacao,
        LocalDateTime dataSolicitacao,
        LocalDateTime dataResposta,
        Integer tentativas,
        String mensagemErro
) {
}
