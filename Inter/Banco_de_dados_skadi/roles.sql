-- ROLES DE GRUPO
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'fronts') THEN
        CREATE ROLE fronts NOLOGIN;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'dados_back') THEN
        CREATE ROLE dados_back NOLOGIN;
    END IF;
END $$;

-- FRONT: somente INSERT
GRANT CONNECT ON DATABASE skadi_primeiro TO fronts;
GRANT USAGE ON SCHEMA public TO fronts;
GRANT INSERT ON ALL TABLES IN SCHEMA public TO fronts;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO fronts;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT INSERT ON TABLES TO fronts;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT USAGE ON SEQUENCES TO fronts;

-- DADOS E BACK: acesso total
GRANT ALL PRIVILEGES ON DATABASE skadi_primeiro TO dados_back;
GRANT ALL PRIVILEGES ON SCHEMA public TO dados_back;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO dados_back;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO dados_back;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO dados_back;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL PRIVILEGES ON TABLES TO dados_back;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL PRIVILEGES ON SEQUENCES TO dados_back;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL PRIVILEGES ON FUNCTIONS TO dados_back;

-- CRIACAO DOS USUARIOS
DO $$
DECLARE
    usuario TEXT;
BEGIN
    FOREACH usuario IN ARRAY ARRAY[
        'enzo_nascimento',
        'mariana_proque',
        'isabella_omena',
        'maria_luisa',
        'vinicius_guerra',
        'diego_valadares',
        'beatriz_hinsching',
        'gustavo_munhoz',
        'giovanna_almeida'
    ]
    LOOP
        IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = usuario) THEN
            EXECUTE format(
                'CREATE ROLE %I LOGIN NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION',
                usuario
            );
        END IF;
    END LOOP;
END $$;

-- BACK / DADOS / FULL STACK: acesso total
GRANT dados_back TO enzo_nascimento;
GRANT dados_back TO mariana_proque;
GRANT dados_back TO isabella_omena;
GRANT dados_back TO vinicius_guerra;
GRANT dados_back TO beatriz_hinsching;
GRANT dados_back TO gustavo_munhoz;
GRANT dados_back TO giovanna_almeida;

-- FRONT: somente INSERT
GRANT fronts TO isabella_omena;
GRANT fronts TO maria_luisa;
GRANT fronts TO diego_valadares;