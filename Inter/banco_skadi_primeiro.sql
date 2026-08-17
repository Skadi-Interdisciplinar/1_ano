SET datestyle = 'ISO, DMY';

-- TABELA CD
create table CD (
    id_cd serial primary key,
    nome varchar(100) not null,
    cnpj char(14) not null unique,
    endereco varchar(255) not null,
    check (cnpj ~ '^[0-9]{14}$')
);

-- TABELA USUARIO
create table Usuario (
    id_usuario serial primary key,
    nome varchar(100) not null,
    cpf char(11) unique,
    email varchar(120) not null unique,
    cargo varchar(80),
    nivel_acesso varchar(30) not null default 'operador',
    id_cd int not null,
    foreign key (id_cd)
        references CD(id_cd),
    check (cpf ~ '^[0-9]{11}$'),
    check (email like '%@%.%'),
    check (nivel_acesso in ('admin', 'gestor', 'operador'))
);

-- TABELA REFRIGERADOR
create table Refrigerador (
    id_refrigerador serial primary key,
    nome varchar(80) not null,
    localizacao varchar(80),
    temperatura_min decimal(5,2) not null,
    temperatura_max decimal(5,2) not null,
    id_cd int not null,
    check (temperatura_min < temperatura_max),
    foreign key (id_cd)
        references CD(id_cd)
);

-- TABELA PRODUTO
create table Produto (
    id_produto serial primary key,
    nome varchar(100) not null,
    categoria varchar(80),
    temperatura_ideal decimal(5,2),
    validade date not null,
	id_refrigerador serial not null,
	foreign key (id_refrigerador)
		references Refrigerador(id_refrigerador),
    check (validade > current_date)
);

-- TABELA TERMOMETRO
create table Termometro (
    id_termometro serial primary key,
    modelo varchar(60),
    status varchar(20) not null default 'ativo',
    id_refrigerador int not null,
    foreign key (id_refrigerador)
        references Refrigerador(id_refrigerador),
    check (status in ('ativo', 'inativo', 'manutencao', 'defeito'))
);

-- TABELA LEITURA TEMPERATURA
create table LeituraTemperatura (
    id_leitura serial primary key,
    temperatura decimal(5,2) not null,
    data date not null default current_date,
    id_termometro int not null,
    foreign key (id_termometro)
        references Termometro(id_termometro),
    check (temperatura between -50 and 50)
);

-- TABELA ALERTA
create table Alerta (
    id_alerta serial primary key,
    status varchar(30) not null default 'pendente',
    tempo_sobrevivencia int,
    data_hora timestamptz not null default current_timestamp,
    tipo varchar(50),
    nivel_gravidade varchar(20) not null default 'baixo',
    canal varchar(30),
    notificacao varchar(100),
    data_envio date,
    id_leitura int not null,
    id_usuario int,
    foreign key (id_leitura)
        references LeituraTemperatura(id_leitura),
    foreign key (id_usuario)
        references Usuario(id_usuario),
    check (status in ('pendente', 'em_andamento', 'resolvido', 'ignorado')),
    check (nivel_gravidade in ('baixo', 'medio', 'alto', 'critico')),
    check (tempo_sobrevivencia is null or tempo_sobrevivencia >= 0)
);