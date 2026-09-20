-- ============================================================
-- MOTIVA VERDE - SPRINT 03
-- Script de carga de dados para testes
-- Oracle Database
-- ============================================================

-- ============================================================
-- LIMPEZA DOS DADOS DE TESTE
-- Permite executar o script novamente sem gerar duplicidades
-- ============================================================

DELETE FROM TB_INTERVENCAO_OPERACIONAL;
DELETE FROM TB_TRECHO_RODOVIA;
DELETE FROM TB_RELATORIO_PRIORIDADE;
DELETE FROM TB_EQUIPE_MANUTENCAO;

COMMIT;

-- ============================================================
-- 1. EQUIPES DE MANUTENÇÃO
-- Dados baseados na Sprint 02
-- ============================================================

INSERT INTO TB_EQUIPE_MANUTENCAO (
    NOME,
    ESPECIALIDADE
) VALUES (
    'Equipe Norte',
    'Roçada mecanizada'
);

INSERT INTO TB_EQUIPE_MANUTENCAO (
    NOME,
    ESPECIALIDADE
) VALUES (
    'Equipe Sul',
    'Pulverização'
);


-- ============================================================
-- 2. TRECHOS DE RODOVIA
-- Dados equivalentes aos utilizados no Main da Sprint 02
-- ============================================================

-- KM 12
INSERT INTO TB_TRECHO_RODOVIA (
    QUILOMETRO,
    ALTURA_VEGETACAO,
    CONDICAO_CRESCIMENTO,
    ID_EQUIPE,
    TIPO_TRECHO,
    CODIGO_SENSOR
) VALUES (
    12,
    18.0,
    'BAIXO_CRESCIMENTO',
    (
        SELECT ID_EQUIPE
        FROM TB_EQUIPE_MANUTENCAO
        WHERE NOME = 'Equipe Norte'
    ),
    'RODOVIA',
    NULL
);


-- KM 25
INSERT INTO TB_TRECHO_RODOVIA (
    QUILOMETRO,
    ALTURA_VEGETACAO,
    CONDICAO_CRESCIMENTO,
    ID_EQUIPE,
    TIPO_TRECHO,
    CODIGO_SENSOR
) VALUES (
    25,
    24.0,
    'CRESCIMENTO_MODERADO',
    (
        SELECT ID_EQUIPE
        FROM TB_EQUIPE_MANUTENCAO
        WHERE NOME = 'Equipe Sul'
    ),
    'RODOVIA',
    NULL
);


-- KM 38 - Trecho monitorado via IoT
INSERT INTO TB_TRECHO_RODOVIA (
    QUILOMETRO,
    ALTURA_VEGETACAO,
    CONDICAO_CRESCIMENTO,
    ID_EQUIPE,
    TIPO_TRECHO,
    CODIGO_SENSOR
) VALUES (
    38,
    22.0,
    'CRESCIMENTO_MODERADO',
    (
        SELECT ID_EQUIPE
        FROM TB_EQUIPE_MANUTENCAO
        WHERE NOME = 'Equipe Norte'
    ),
    'MONITORADO',
    'SENSOR-IOT-038'
);


-- KM 47 - Trecho monitorado via IoT
INSERT INTO TB_TRECHO_RODOVIA (
    QUILOMETRO,
    ALTURA_VEGETACAO,
    CONDICAO_CRESCIMENTO,
    ID_EQUIPE,
    TIPO_TRECHO,
    CODIGO_SENSOR
) VALUES (
    47,
    29.0,
    'ALTO_CRESCIMENTO',
    (
        SELECT ID_EQUIPE
        FROM TB_EQUIPE_MANUTENCAO
        WHERE NOME = 'Equipe Norte'
    ),
    'MONITORADO',
    'SENSOR-IOT-047'
);


-- KM 60
INSERT INTO TB_TRECHO_RODOVIA (
    QUILOMETRO,
    ALTURA_VEGETACAO,
    CONDICAO_CRESCIMENTO,
    ID_EQUIPE,
    TIPO_TRECHO,
    CODIGO_SENSOR
) VALUES (
    60,
    35.0,
    'ALTO_CRESCIMENTO',
    (
        SELECT ID_EQUIPE
        FROM TB_EQUIPE_MANUTENCAO
        WHERE NOME = 'Equipe Sul'
    ),
    'RODOVIA',
    NULL
);


-- ============================================================
-- 3. INTERVENÇÕES OPERACIONAIS
-- Registros históricos para testes
-- ============================================================

INSERT INTO TB_INTERVENCAO_OPERACIONAL (
    ID_TRECHO,
    TIPO_INTERVENCAO,
    ALTURA_ANTES,
    ALTURA_DEPOIS
) VALUES (
    (
        SELECT ID_TRECHO
        FROM TB_TRECHO_RODOVIA
        WHERE QUILOMETRO = 25
    ),
    'PULVERIZACAO',
    24.0,
    19.0
);

INSERT INTO TB_INTERVENCAO_OPERACIONAL (
    ID_TRECHO,
    TIPO_INTERVENCAO,
    ALTURA_ANTES,
    ALTURA_DEPOIS
) VALUES (
    (
        SELECT ID_TRECHO
        FROM TB_TRECHO_RODOVIA
        WHERE QUILOMETRO = 60
    ),
    'ROCADA_MECANIZADA',
    35.0,
    8.0
);


-- ============================================================
-- 4. RELATÓRIO DE PRIORIDADE
-- Registro inicial para teste da persistência
-- ============================================================

INSERT INTO TB_RELATORIO_PRIORIDADE (
    QT_BAIXA,
    QT_MODERADA,
    QT_ALTA,
    QT_URGENTE,
    RESUMO
) VALUES (
    0,
    1,
    1,
    3,
    'Relatório inicial de teste baseado nos trechos da Sprint 02.'
);


COMMIT;