CREATE TABLE endereco (
                          id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                          cep           CHAR(8)      NOT NULL,
                          logradouro    VARCHAR(150) NOT NULL,
                          numero        VARCHAR(10)  NOT NULL,
                          complemento   VARCHAR(80),
                          bairro        VARCHAR(80)  NOT NULL,
                          cidade        VARCHAR(80)  NOT NULL,
                          uf            CHAR(2)      NOT NULL,
                          codigo_ibge   CHAR(7)
) ENGINE=InnoDB;

CREATE TABLE tutor (
                       id               BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nome_completo    VARCHAR(150) NOT NULL,
                       cpf              CHAR(11)     NOT NULL,
                       data_nascimento  DATE         NOT NULL,
                       email            VARCHAR(150) NOT NULL,
                       telefone         VARCHAR(20)  NOT NULL,
                       endereco_id      BIGINT       NOT NULL,
                       data_cadastro    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       situacao         ENUM('ATIVO','INATIVO') NOT NULL DEFAULT 'ATIVO',
                       CONSTRAINT uq_tutor_cpf UNIQUE (cpf),
                       CONSTRAINT fk_tutor_endereco FOREIGN KEY (endereco_id) REFERENCES endereco (id)
) ENGINE=InnoDB;

CREATE TABLE especie (
                         id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome       VARCHAR(60)  NOT NULL,
                         descricao  VARCHAR(200),
                         situacao   ENUM('ATIVO','INATIVO') NOT NULL DEFAULT 'ATIVO',
                         CONSTRAINT uq_especie_nome UNIQUE (nome)
) ENGINE=InnoDB;

CREATE TABLE raca (
                      id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                      nome        VARCHAR(80)  NOT NULL,
                      descricao   VARCHAR(200),
                      especie_id  BIGINT       NOT NULL,
                      situacao    ENUM('ATIVO','INATIVO') NOT NULL DEFAULT 'ATIVO',
                      CONSTRAINT uq_raca_especie_nome UNIQUE (especie_id, nome),
                      CONSTRAINT uq_raca_especie_id   UNIQUE (especie_id, id),
                      CONSTRAINT fk_raca_especie FOREIGN KEY (especie_id) REFERENCES especie (id)
) ENGINE=InnoDB;

CREATE TABLE veterinario (
                             id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nome_completo           VARCHAR(150) NOT NULL,
                             cpf                     CHAR(11)     NOT NULL,
                             crmv_numero             VARCHAR(15)  NOT NULL,
                             crmv_uf                 CHAR(2)      NOT NULL,
                             email                   VARCHAR(150) NOT NULL,
                             telefone                VARCHAR(20),
                             senha_hash              VARCHAR(255) NOT NULL,
                             senha_confirmada        BOOLEAN      NOT NULL DEFAULT FALSE,
                             data_confirmacao_senha  DATETIME,
                             codigo_confirmacao      VARCHAR(64),
                             codigo_expira_em        DATETIME,
                             data_cadastro           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             situacao                ENUM('ATIVO','INATIVO') NOT NULL DEFAULT 'ATIVO',
                             ultimo_acesso           DATETIME,
                             CONSTRAINT uq_vet_cpf   UNIQUE (cpf),
                             CONSTRAINT uq_vet_email UNIQUE (email),
                             CONSTRAINT uq_vet_crmv  UNIQUE (crmv_numero, crmv_uf)
) ENGINE=InnoDB;

CREATE TABLE animal (
                        id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nome                  VARCHAR(80)  NOT NULL,
                        data_nascimento       DATE,
                        idade_estimada_meses  INT,
                        sexo                  ENUM('MACHO','FEMEA','INDEFINIDO') NOT NULL,
                        cor                   VARCHAR(40),
                        peso_atual            DECIMAL(6,2),
                        tutor_id              BIGINT NOT NULL,
                        especie_id            BIGINT NOT NULL,
                        raca_id               BIGINT NULL,
                        situacao              ENUM('ATIVO','INATIVO') NOT NULL DEFAULT 'ATIVO',
                        CONSTRAINT ck_animal_idade CHECK (data_nascimento IS NOT NULL OR idade_estimada_meses IS NOT NULL),
                        CONSTRAINT fk_animal_tutor   FOREIGN KEY (tutor_id)   REFERENCES tutor (id),
                        CONSTRAINT fk_animal_especie FOREIGN KEY (especie_id) REFERENCES especie (id),
                        CONSTRAINT fk_animal_raca    FOREIGN KEY (especie_id, raca_id) REFERENCES raca (especie_id, id)
) ENGINE=InnoDB;

CREATE TABLE vinculo_microchip (
                                   id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   animal_id            BIGINT      NOT NULL,
                                   veterinario_id       BIGINT      NOT NULL,
                                   codigo_microchip     VARCHAR(20) NOT NULL,
                                   data_vinculo         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   data_fim             DATETIME    NULL,
                                   motivo_encerramento  VARCHAR(200),
                                   animal_ativo_id      BIGINT GENERATED ALWAYS AS (IF(data_fim IS NULL, animal_id, NULL)) STORED,
                                   CONSTRAINT uq_microchip_codigo       UNIQUE (codigo_microchip),
                                   CONSTRAINT uq_microchip_animal_ativo UNIQUE (animal_ativo_id),
                                   CONSTRAINT fk_vinculo_animal FOREIGN KEY (animal_id)      REFERENCES animal (id),
                                   CONSTRAINT fk_vinculo_vet    FOREIGN KEY (veterinario_id) REFERENCES veterinario (id)
) ENGINE=InnoDB;

CREATE TABLE consulta (
                          id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                          animal_id       BIGINT   NOT NULL,
                          veterinario_id  BIGINT   NOT NULL,
                          data_hora       DATETIME NOT NULL,
                          motivo          VARCHAR(200) NOT NULL,
                          anamnese        TEXT,
                          sintomas        TEXT,
                          diagnostico     TEXT,
                          tratamento      TEXT,
                          prescricao      TEXT,
                          peso_kg         DECIMAL(6,2),
                          temperatura_c   DECIMAL(4,1),
                          observacoes     TEXT,
                          situacao        ENUM('AGENDADA','REALIZADA','CANCELADA','NAO_COMPARECEU') NOT NULL DEFAULT 'AGENDADA',
                          INDEX ix_consulta_animal (animal_id, data_hora),
                          CONSTRAINT fk_consulta_animal FOREIGN KEY (animal_id)      REFERENCES animal (id),
                          CONSTRAINT fk_consulta_vet    FOREIGN KEY (veterinario_id) REFERENCES veterinario (id)
) ENGINE=InnoDB;

CREATE TABLE vacina (
                        id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
                        animal_id          BIGINT       NOT NULL,
                        veterinario_id     BIGINT       NOT NULL,
                        nome_vacina        VARCHAR(100) NOT NULL,
                        tipo_vacina        VARCHAR(60)  NOT NULL,
                        antirrabica        BOOLEAN      NOT NULL DEFAULT FALSE,
                        fabricante         VARCHAR(100) NOT NULL,
                        numero_lote        VARCHAR(40)  NOT NULL,
                        data_aplicacao     DATE,
                        dose               VARCHAR(30),
                        via_aplicacao      VARCHAR(30),
                        data_proxima_dose  DATE,
                        validade_lote      DATE         NOT NULL,
                        observacoes        TEXT,
                        situacao           ENUM('AGENDADA','APLICADA','CANCELADA') NOT NULL DEFAULT 'AGENDADA',
                        INDEX ix_vacina_animal (animal_id, data_aplicacao),
                        CONSTRAINT fk_vacina_animal FOREIGN KEY (animal_id)      REFERENCES animal (id),
                        CONSTRAINT fk_vacina_vet    FOREIGN KEY (veterinario_id) REFERENCES veterinario (id)
) ENGINE=InnoDB;

CREATE TABLE integracao_governo (
                                    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    animal_id           BIGINT       NOT NULL,
                                    vacina_id           BIGINT       NULL,
                                    tipo_operacao       ENUM('CADASTRO_ANIMAL','ATUALIZACAO_ANIMAL','VACINA_ANTIRRABICA') NOT NULL,
                                    protocolo_externo   VARCHAR(50),
                                    chave_idempotencia  VARCHAR(100) NOT NULL,
                                    situacao            ENUM('PENDENTE','ENVIADA','CONFIRMADA','FALHA') NOT NULL DEFAULT 'PENDENTE',
                                    data_solicitacao    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    data_resposta       DATETIME     NULL,
                                    tentativas          INT          NOT NULL DEFAULT 0,
                                    mensagem_erro       VARCHAR(500),
                                    CONSTRAINT uq_integracao_chave UNIQUE (chave_idempotencia),
                                    INDEX ix_integracao_situacao (situacao),
                                    CONSTRAINT fk_integracao_animal FOREIGN KEY (animal_id) REFERENCES animal (id),
                                    CONSTRAINT fk_integracao_vacina FOREIGN KEY (vacina_id) REFERENCES vacina (id)
) ENGINE=InnoDB;