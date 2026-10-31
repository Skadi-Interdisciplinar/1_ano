-- Admin pode existir antes de cadastrar a própria CD; super_admin permanece sem CD.
ALTER TABLE usuario ALTER COLUMN id_cd DROP NOT NULL;

-- Mantém super_admin desvinculado de qualquer CD.
UPDATE usuario SET id_cd = NULL WHERE lower(nivel_acesso) = 'super_admin';

-- Gestores e operadores são funcionários sem login.
ALTER TABLE usuario ALTER COLUMN username DROP NOT NULL;
ALTER TABLE usuario ALTER COLUMN senha DROP NOT NULL;

-- Associa os lotes à CD para separá-los por administrador.
ALTER TABLE lote ADD COLUMN IF NOT EXISTS id_cd INTEGER;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'lote_id_cd_fkey'
                   AND conrelid = 'lote'::regclass) THEN
        ALTER TABLE lote ADD CONSTRAINT lote_id_cd_fkey
            FOREIGN KEY (id_cd) REFERENCES cd(id_cd);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_usuario_id_cd_nivel ON usuario (id_cd, nivel_acesso);
CREATE INDEX IF NOT EXISTS idx_lote_id_cd ON lote (id_cd);
