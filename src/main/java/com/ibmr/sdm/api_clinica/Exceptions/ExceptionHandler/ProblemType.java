package com.ibmr.sdm.api_clinica.Exceptions.ExceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {
    DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Registro já cadastrado"),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema");

    private final String title;
    private final String uri;

    ProblemType(String path, String title) {
        this.uri = "https://api-clinica.local" + path;
        this.title = title;
    }
}
