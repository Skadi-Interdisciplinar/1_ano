/*
	-------------BANCO DO SKADI----------------
*/
 
SET datestyle = 'ISO, DMY';
ALTER DATABASE skadi_primeiro SET timezone TO 'America/Sao_Paulo';
 
DROP TABLE IF EXISTS NotificacaoAlerta CASCADE;
DROP TABLE IF EXISTS Alerta CASCADE;
DROP TABLE IF EXISTS LeituraTemperatura CASCADE;
DROP TABLE IF EXISTS lote_frigorifico CASCADE;
DROP TABLE IF EXISTS Lote CASCADE;
DROP TABLE IF EXISTS Categoria CASCADE;
DROP TABLE IF EXISTS Termometro CASCADE;
DROP TABLE IF EXISTS Frigorifico CASCADE;
DROP TABLE IF EXISTS Usuario CASCADE;
DROP TABLE IF EXISTS CD CASCADE;
 
 
-- TABELA CD
CREATE TABLE CD (
	id_cd SERIAL,
	nome VARCHAR(100) NOT NULL,
	cnpj CHAR(14) NOT NULL,
	endereco VARCHAR(255) NOT NULL,
 
	CONSTRAINT pk_cd PRIMARY KEY (id_cd),
	CONSTRAINT uq_cd_cnpj UNIQUE (cnpj),
	CONSTRAINT ck_cd_cnpj CHECK (cnpj ~ '^[0-9]{14}$')
);
 
-- TABELA USUARIO
CREATE TABLE Usuario (
	id_usuario SERIAL,
	nome VARCHAR(100) NOT NULL,
	cpf CHAR(11),
	email VARCHAR(120) NOT NULL,
	senha VARCHAR(255) NOT NULL,
	cargo VARCHAR(80),
	nivel_acesso VARCHAR(30) NOT NULL DEFAULT 'operador',
	id_cd INTEGER NOT NULL,
 
	CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
	CONSTRAINT uq_usuario_cpf UNIQUE (cpf),
	CONSTRAINT uq_usuario_email UNIQUE (email),
	CONSTRAINT fk_usuario_cd FOREIGN KEY (id_cd) REFERENCES CD(id_cd) ON DELETE RESTRICT,
	CONSTRAINT ck_usuario_cpf CHECK (cpf ~ '^[0-9]{11}$'),
	CONSTRAINT ck_usuario_email CHECK (email LIKE '%@%.%'),
	CONSTRAINT ck_usuario_nivel_acesso CHECK (nivel_acesso IN ('admin', 'gestor', 'operador', 'super_admin'))
);
 
-- TABELA FRIGORIFICO
CREATE TABLE Frigorifico (
	id_frigorifico SERIAL,
	nome VARCHAR(80) NOT NULL,
	localizacao VARCHAR(80),
	id_cd INTEGER NOT NULL,
 
	CONSTRAINT pk_frigorifico PRIMARY KEY (id_frigorifico),
	CONSTRAINT fk_frigorifico_cd FOREIGN KEY (id_cd) REFERENCES CD(id_cd) ON DELETE RESTRICT
);
 
-- TABELA TERMOMETRO
CREATE TABLE Termometro (
	id_termometro SERIAL,
	modelo VARCHAR(60),
	status VARCHAR(20) NOT NULL DEFAULT 'ativo',
	id_frigorifico INTEGER NOT NULL,
 
	CONSTRAINT pk_termometro PRIMARY KEY (id_termometro),
	CONSTRAINT fk_termometro_frigorifico FOREIGN KEY (id_frigorifico) REFERENCES Frigorifico(id_frigorifico) ON DELETE CASCADE,
	CONSTRAINT ck_termometro_status CHECK (status IN ('ativo', 'inativo', 'manutencao'))
);
 
-- TABELA CATEGORIA
CREATE TABLE Categoria (
	id_categoria SERIAL,
	nome VARCHAR(150) NOT NULL,
	vida_util_horas DECIMAL(7,2) NOT NULL,
 
	CONSTRAINT pk_categoria PRIMARY KEY (id_categoria),
	CONSTRAINT uq_categoria_nome UNIQUE (nome),
	CONSTRAINT ck_categoria_vida_util CHECK (vida_util_horas > 0)
);
 
-- TABELA LOTE
CREATE TABLE Lote (
	id_lote SERIAL,
	codigo_lote VARCHAR(50) NOT NULL,
	id_categoria INTEGER NOT NULL,
	data_fabricacao DATE NOT NULL,
	data_validade DATE NOT NULL,
	status VARCHAR(20) NOT NULL DEFAULT 'ativo',
    temperatura_max DECIMAL(5,2) NOT NULL,
    temperatura_min DECIMAL(5,2) NOT NULL,
 
	CONSTRAINT pk_lote PRIMARY KEY (id_lote),
	CONSTRAINT uq_lote_codigo UNIQUE (codigo_lote),
	CONSTRAINT fk_lote_categoria FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria) ON DELETE RESTRICT,
	CONSTRAINT ck_lote_datas CHECK (data_validade >= data_fabricacao),
	CONSTRAINT ck_lote_status CHECK (status IN ('ativo', 'bloqueado', 'expedido', 'vencido')),
    CONSTRAINT ck_faixa_temperatura CHECK (temperatura_max > temperatura_min)
);
 
-- TABELA LOTE FRIGORIFICO
CREATE TABLE lote_frigorifico (
	id_lote_frigorifico SERIAL,
	id_lote INTEGER NOT NULL,
	id_frigorifico INTEGER NOT NULL,
	data_entrada TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
	data_saida TIMESTAMPTZ,
 
	CONSTRAINT pk_lote_frigorifico PRIMARY KEY (id_lote_frigorifico),
	CONSTRAINT fk_lotefrig_lote FOREIGN KEY (id_lote) REFERENCES Lote(id_lote) ON DELETE CASCADE,
	CONSTRAINT fk_lotefrig_frigorifico FOREIGN KEY (id_frigorifico) REFERENCES Frigorifico(id_frigorifico) ON DELETE RESTRICT,
	CONSTRAINT ck_lotefrig_periodo CHECK (data_saida IS NULL OR data_saida > data_entrada)
);
 
-- TABELA LEITURA TEMPERATURA
CREATE TABLE LeituraTemperatura (
	id_leitura SERIAL,
	temperatura DECIMAL(5,2) NOT NULL,
	data_hora TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
	id_termometro INTEGER NOT NULL,
 
	CONSTRAINT pk_leitura PRIMARY KEY (id_leitura),
	CONSTRAINT fk_leitura_termometro FOREIGN KEY (id_termometro) REFERENCES Termometro(id_termometro) ON DELETE RESTRICT,
	CONSTRAINT ck_leitura_temperatura CHECK (temperatura BETWEEN -50 AND 50)
);
 
-- TABELA ALERTA
CREATE TABLE Alerta (
	id_alerta SERIAL,
	tipo VARCHAR(50) NOT NULL DEFAULT 'temperatura_fora_padrao',
	nivel_gravidade VARCHAR(20) NOT NULL DEFAULT 'baixo',
	status VARCHAR(30) NOT NULL DEFAULT 'pendente',
	tempo_sobrevivencia INTEGER,
	data_hora TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
	data_hora_resolucao TIMESTAMPTZ,
	id_leitura INTEGER NOT NULL,
	id_usuario INTEGER,
 
	CONSTRAINT pk_alerta PRIMARY KEY (id_alerta),
	CONSTRAINT fk_alerta_leitura FOREIGN KEY (id_leitura) REFERENCES LeituraTemperatura(id_leitura) ON DELETE CASCADE,
	CONSTRAINT fk_alerta_usuario FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE SET NULL,
	CONSTRAINT ck_alerta_tipo CHECK (tipo IN ('temperatura_fora_padrao', 'termometro_offline', 'porta_aberta')),
	CONSTRAINT ck_alerta_status CHECK (status IN ('pendente', 'em_andamento', 'resolvido', 'ignorado')),
	CONSTRAINT ck_alerta_gravidade CHECK (nivel_gravidade IN ('baixo', 'medio', 'alto', 'critico')),
	CONSTRAINT ck_alerta_sobrevivencia CHECK (tempo_sobrevivencia IS NULL OR tempo_sobrevivencia >= 0),
	CONSTRAINT ck_alerta_resolucao CHECK (data_hora_resolucao IS NULL OR data_hora_resolucao >= data_hora),
	CONSTRAINT ck_alerta_resolvido CHECK (status <> 'resolvido' OR (id_usuario IS NOT NULL AND data_hora_resolucao IS NOT NULL))
);
 
-- TABELA NOTIFICACAO ALERTA
CREATE TABLE NotificacaoAlerta (
	id_notificacao SERIAL,
	id_alerta INTEGER NOT NULL,
	id_usuario INTEGER NOT NULL,
	canal VARCHAR(30) NOT NULL,
	data_hora_envio TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
 
	CONSTRAINT pk_notificacao PRIMARY KEY (id_notificacao),
	CONSTRAINT fk_notificacao_alerta FOREIGN KEY (id_alerta) REFERENCES Alerta(id_alerta) ON DELETE CASCADE,
	CONSTRAINT fk_notificacao_usuario FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE RESTRICT,
	CONSTRAINT uq_notificacao_envio UNIQUE (id_alerta, id_usuario, canal),
	CONSTRAINT ck_notificacao_canal CHECK (canal IN ('App'))
);