-- =========================================================
-- DATALOAD - BANCO DO SKADI
-- =========================================================

SET datestyle = 'ISO, DMY';
SET timezone TO 'America/Sao_Paulo';

-- Limpa dados existentes 
TRUNCATE TABLE 
NotificacaoAlerta, Alerta, LeituraTemperatura, lote_frigorifico, Lote, 
Categoria, Termometro, Frigorifico, admin_cd, Usuario, Endereco, CD
RESTART IDENTITY CASCADE;

-- CD 

INSERT INTO CD (id_cd, nome, cnpj) VALUES
(1,  'CD São Paulo - Unidade 1',         '00000000000001'),
(2,  'CD Rio de Janeiro - Unidade 2',    '00000000000002'),
(3,  'CD Belo Horizonte - Unidade 3',    '00000000000003'),
(4,  'CD Curitiba - Unidade 4',          '00000000000004'),
(5,  'CD Porto Alegre - Unidade 5',      '00000000000005'),
(6,  'CD Salvador - Unidade 6',          '00000000000006'),
(7,  'CD Recife - Unidade 7',            '00000000000007'),
(8,  'CD Fortaleza - Unidade 8',         '00000000000008'),
(9,  'CD Brasília - Unidade 9',          '00000000000009'),
(10, 'CD Manaus - Unidade 10',           '00000000000010'),
(11, 'CD São Paulo - Unidade 11',        '00000000000011'),
(12, 'CD Rio de Janeiro - Unidade 12',   '00000000000012'),
(13, 'CD Belo Horizonte - Unidade 13',   '00000000000013'),
(14, 'CD Curitiba - Unidade 14',         '00000000000014'),
(15, 'CD Porto Alegre - Unidade 15',     '00000000000015'),
(16, 'CD Salvador - Unidade 16',         '00000000000016'),
(17, 'CD Recife - Unidade 17',           '00000000000017'),
(18, 'CD Fortaleza - Unidade 18',        '00000000000018'),
(19, 'CD Brasília - Unidade 19',         '00000000000019'),
(20, 'CD Manaus - Unidade 20',           '00000000000020'),
(21, 'CD São Paulo - Unidade 21',        '00000000000021'),
(22, 'CD Rio de Janeiro - Unidade 22',   '00000000000022'),
(23, 'CD Belo Horizonte - Unidade 23',   '00000000000023'),
(24, 'CD Curitiba - Unidade 24',         '00000000000024'),
(25, 'CD Porto Alegre - Unidade 25',     '00000000000025'),
(26, 'CD Salvador - Unidade 26',         '00000000000026'),
(27, 'CD Recife - Unidade 27',           '00000000000027'),
(28, 'CD Fortaleza - Unidade 28',        '00000000000028'),
(29, 'CD Brasília - Unidade 29',         '00000000000029'),
(30, 'CD Manaus - Unidade 30',           '00000000000030'),
(31, 'CD São Paulo - Unidade 31',        '00000000000031'),
(32, 'CD Rio de Janeiro - Unidade 32',   '00000000000032'),
(33, 'CD Belo Horizonte - Unidade 33',   '00000000000033'),
(34, 'CD Curitiba - Unidade 34',         '00000000000034'),
(35, 'CD Porto Alegre - Unidade 35',     '00000000000035'),
(36, 'CD Salvador - Unidade 36',         '00000000000036'),
(37, 'CD Recife - Unidade 37',           '00000000000037'),
(38, 'CD Fortaleza - Unidade 38',        '00000000000038'),
(39, 'CD Brasília - Unidade 39',         '00000000000039'),
(40, 'CD Manaus - Unidade 40',           '00000000000040'),
(41, 'CD São Paulo - Unidade 41',        '00000000000041'),
(42, 'CD Rio de Janeiro - Unidade 42',   '00000000000042'),
(43, 'CD Belo Horizonte - Unidade 43',   '00000000000043'),
(44, 'CD Curitiba - Unidade 44',         '00000000000044'),
(45, 'CD Porto Alegre - Unidade 45',     '00000000000045'),
(46, 'CD Salvador - Unidade 46',         '00000000000046'),
(47, 'CD Recife - Unidade 47',           '00000000000047'),
(48, 'CD Fortaleza - Unidade 48',        '00000000000048'),
(49, 'CD Brasília - Unidade 49',         '00000000000049'),
(50, 'CD Manaus - Unidade 50',           '00000000000050');

-- ENDERECO 

INSERT INTO Endereco (id_endereco, id_cd, rua, numero, bairro, cidade, estado, cep) VALUES
(1,  1,  'Rua das Flores',     101, 'Zona Industrial',     'São Paulo',       'SP', '10000137'),
(2,  2,  'Rua Industrial',     102, 'Distrito Industrial', 'Rio de Janeiro',  'RJ', '10000274'),
(3,  3,  'Rua Comercial',      103, 'Setor Logístico',     'Belo Horizonte',  'MG', '10000411'),
(4,  4,  'Rua dos Andradas',   104, 'Vila Operária',       'Curitiba',        'PR', '10000548'),
(5,  5,  'Rua Voluntários',    105, 'Centro',              'Porto Alegre',    'RS', '10000685'),
(6,  6,  'Rua Brasil',         106, 'Zona Industrial',     'Salvador',        'BA', '10000822'),
(7,  7,  'Rua Central',        107, 'Distrito Industrial', 'Recife',          'PE', '10000959'),
(8,  8,  'Rua Norte',          108, 'Setor Logístico',     'Fortaleza',       'CE', '10001096'),
(9,  9,  'Rua Sul',            109, 'Vila Operária',       'Brasília',        'DF', '10001233'),
(10, 10, 'Rua das Nações',     110, 'Centro',              'Manaus',          'AM', '10001370'),
(11, 11, 'Rua das Flores',     111, 'Zona Industrial',     'São Paulo',       'SP', '10001507'),
(12, 12, 'Rua Industrial',     112, 'Distrito Industrial', 'Rio de Janeiro',  'RJ', '10001644'),
(13, 13, 'Rua Comercial',      113, 'Setor Logístico',     'Belo Horizonte',  'MG', '10001781'),
(14, 14, 'Rua dos Andradas',   114, 'Vila Operária',       'Curitiba',        'PR', '10001918'),
(15, 15, 'Rua Voluntários',    115, 'Centro',              'Porto Alegre',    'RS', '10002055'),
(16, 16, 'Rua Brasil',         116, 'Zona Industrial',     'Salvador',        'BA', '10002192'),
(17, 17, 'Rua Central',        117, 'Distrito Industrial', 'Recife',          'PE', '10002329'),
(18, 18, 'Rua Norte',          118, 'Setor Logístico',     'Fortaleza',       'CE', '10002466'),
(19, 19, 'Rua Sul',            119, 'Vila Operária',       'Brasília',        'DF', '10002603'),
(20, 20, 'Rua das Nações',     120, 'Centro',              'Manaus',          'AM', '10002740'),
(21, 21, 'Rua das Flores',     121, 'Zona Industrial',     'São Paulo',       'SP', '10002877'),
(22, 22, 'Rua Industrial',     122, 'Distrito Industrial', 'Rio de Janeiro',  'RJ', '10003014'),
(23, 23, 'Rua Comercial',      123, 'Setor Logístico',     'Belo Horizonte',  'MG', '10003151'),
(24, 24, 'Rua dos Andradas',   124, 'Vila Operária',       'Curitiba',        'PR', '10003288'),
(25, 25, 'Rua Voluntários',    125, 'Centro',              'Porto Alegre',    'RS', '10003425'),
(26, 26, 'Rua Brasil',         126, 'Zona Industrial',     'Salvador',        'BA', '10003562'),
(27, 27, 'Rua Central',        127, 'Distrito Industrial', 'Recife',          'PE', '10003699'),
(28, 28, 'Rua Norte',          128, 'Setor Logístico',     'Fortaleza',       'CE', '10003836'),
(29, 29, 'Rua Sul',            129, 'Vila Operária',       'Brasília',        'DF', '10003973'),
(30, 30, 'Rua das Nações',     130, 'Centro',              'Manaus',          'AM', '10004110'),
(31, 31, 'Rua das Flores',     131, 'Zona Industrial',     'São Paulo',       'SP', '10004247'),
(32, 32, 'Rua Industrial',     132, 'Distrito Industrial', 'Rio de Janeiro',  'RJ', '10004384'),
(33, 33, 'Rua Comercial',      133, 'Setor Logístico',     'Belo Horizonte',  'MG', '10004521'),
(34, 34, 'Rua dos Andradas',   134, 'Vila Operária',       'Curitiba',        'PR', '10004658'),
(35, 35, 'Rua Voluntários',    135, 'Centro',              'Porto Alegre',    'RS', '10004795'),
(36, 36, 'Rua Brasil',         136, 'Zona Industrial',     'Salvador',        'BA', '10004932'),
(37, 37, 'Rua Central',        137, 'Distrito Industrial', 'Recife',          'PE', '10005069'),
(38, 38, 'Rua Norte',          138, 'Setor Logístico',     'Fortaleza',       'CE', '10005206'),
(39, 39, 'Rua Sul',            139, 'Vila Operária',       'Brasília',        'DF', '10005343'),
(40, 40, 'Rua das Nações',     140, 'Centro',              'Manaus',          'AM', '10005480'),
(41, 41, 'Rua das Flores',     141, 'Zona Industrial',     'São Paulo',       'SP', '10005617'),
(42, 42, 'Rua Industrial',     142, 'Distrito Industrial', 'Rio de Janeiro',  'RJ', '10005754'),
(43, 43, 'Rua Comercial',      143, 'Setor Logístico',     'Belo Horizonte',  'MG', '10005891'),
(44, 44, 'Rua dos Andradas',   144, 'Vila Operária',       'Curitiba',        'PR', '10006028'),
(45, 45, 'Rua Voluntários',    145, 'Centro',              'Porto Alegre',    'RS', '10006165'),
(46, 46, 'Rua Brasil',         146, 'Zona Industrial',     'Salvador',        'BA', '10006302'),
(47, 47, 'Rua Central',        147, 'Distrito Industrial', 'Recife',          'PE', '10006439'),
(48, 48, 'Rua Norte',          148, 'Setor Logístico',     'Fortaleza',       'CE', '10006576'),
(49, 49, 'Rua Sul',            149, 'Vila Operária',       'Brasília',        'DF', '10006713'),
(50, 50, 'Rua das Nações',     150, 'Centro',              'Manaus',          'AM', '10006850');

-- USUARIO 

INSERT INTO Usuario (id_usuario, username, cod_gestor, nome, cpf, email, senha, cargo, nivel_acesso, id_cd) VALUES
(1,  'usuario1',  NULL, 'Ana Souza',           '00000000001', 'usuario1@logfresh.com',  '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Diretor de Operações',       'admin', 1),
(2,  'usuario2',  NULL, 'Carlos Lima',         '00000000002', 'usuario2@logfresh.com',  '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Administrador de Sistema', 'admin',       2),
(3,  'usuario3',  NULL, 'Fernanda Ribeiro',    '00000000003', 'usuario3@logfresh.com',  '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Administrador de Sistema', 'admin',       3),
(4,  'usuario4',  NULL, 'João Almeida',        '00000000004', 'usuario4@logfresh.com',  '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Administrador de Sistema', 'admin',       4),
(5,  'usuario5',  NULL, 'Mariana Nunes',       '00000000005', 'usuario5@logfresh.com',  '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Administrador de Sistema', 'admin',       5),
(6,  'usuario6',  NULL, 'Rafael Dias',         '00000000006', 'usuario6@logfresh.com',  '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Administrador de Sistema', 'admin',       6),
(7,  'usuario7',  NULL, 'Beatriz Costa',       '00000000007', 'usuario7@logfresh.com',  '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Administrador de Sistema', 'admin',       7),
(8,  'usuario8',  NULL, 'Eduardo Pereira',     '00000000008', 'usuario8@logfresh.com',  '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Administrador de Sistema', 'admin',       8),
(9,  'usuario9',  NULL, 'Camila Santos',       '00000000009', 'usuario9@logfresh.com',  '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Administrador de Sistema', 'admin',       9),
(10, 'usuario10', NULL, 'Lucas Oliveira',      '00000000010', 'usuario10@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Administrador de Sistema', 'admin',       10),
(11, 'usuario11', NULL, 'Patrícia Barbosa',    '00000000011', 'usuario11@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Gestor de CD',              'gestor',      11),
(12, 'usuario12', NULL, 'Rodrigo Cardoso',     '00000000012', 'usuario12@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Gestor de CD',              'gestor',      12),
(13, 'usuario13', NULL, 'Juliana Teixeira',    '00000000013', 'usuario13@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Gestor de CD',              'gestor',      13),
(14, 'usuario14', NULL, 'Marcos Rocha',        '00000000014', 'usuario14@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Gestor de CD',              'gestor',      14),
(15, 'usuario15', NULL, 'Larissa Martins',     '00000000015', 'usuario15@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Gestor de CD',              'gestor',      15),
(16, 'usuario16', NULL, 'Gustavo Souza',       '00000000016', 'usuario16@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Gestor de CD',              'gestor',      16),
(17, 'usuario17', NULL, 'Vanessa Lima',        '00000000017', 'usuario17@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Gestor de CD',              'gestor',      17),
(18, 'usuario18', NULL, 'Thiago Ribeiro',      '00000000018', 'usuario18@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Gestor de CD',              'gestor',      18),
(19, 'usuario19', NULL, 'Renata Almeida',      '00000000019', 'usuario19@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Gestor de CD',              'gestor',      19),
(20, 'usuario20', NULL, 'Bruno Nunes',         '00000000020', 'usuario20@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Gestor de CD',              'gestor',      20),
(21, 'usuario21', 11, 'Ana Dias',              '00000000021', 'usuario21@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   11),
(22, 'usuario22', 12, 'Carlos Costa',          '00000000022', 'usuario22@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   12),
(23, 'usuario23', 13, 'Fernanda Pereira',      '00000000023', 'usuario23@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   13),
(24, 'usuario24', 14, 'João Santos',           '00000000024', 'usuario24@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   14),
(25, 'usuario25', 15, 'Mariana Oliveira',      '00000000025', 'usuario25@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   15),
(26, 'usuario26', 16, 'Rafael Barbosa',        '00000000026', 'usuario26@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   16),
(27, 'usuario27', 17, 'Beatriz Cardoso',       '00000000027', 'usuario27@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   17),
(28, 'usuario28', 18, 'Eduardo Teixeira',      '00000000028', 'usuario28@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   18),
(29, 'usuario29', 19, 'Camila Rocha',          '00000000029', 'usuario29@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   19),
(30, 'usuario30', 20, 'Lucas Martins',         '00000000030', 'usuario30@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   20),
(31, 'usuario31', 11, 'Patrícia Souza',        '00000000031', 'usuario31@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   11),
(32, 'usuario32', 12, 'Rodrigo Lima',          '00000000032', 'usuario32@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   12),
(33, 'usuario33', 13, 'Juliana Ribeiro',       '00000000033', 'usuario33@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   13),
(34, 'usuario34', 14, 'Marcos Almeida',        '00000000034', 'usuario34@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   14),
(35, 'usuario35', 15, 'Larissa Nunes',         '00000000035', 'usuario35@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   15),
(36, 'usuario36', 16, 'Gustavo Dias',          '00000000036', 'usuario36@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   16),
(37, 'usuario37', 17, 'Vanessa Costa',         '00000000037', 'usuario37@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   17),
(38, 'usuario38', 18, 'Thiago Pereira',        '00000000038', 'usuario38@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   18),
(39, 'usuario39', 19, 'Renata Santos',         '00000000039', 'usuario39@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   19),
(40, 'usuario40', 20, 'Bruno Oliveira',        '00000000040', 'usuario40@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   20),
(41, 'usuario41', 11, 'Ana Barbosa',           '00000000041', 'usuario41@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   11),
(42, 'usuario42', 12, 'Carlos Cardoso',        '00000000042', 'usuario42@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   12),
(43, 'usuario43', 13, 'Fernanda Teixeira',     '00000000043', 'usuario43@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   13),
(44, 'usuario44', 14, 'João Rocha',            '00000000044', 'usuario44@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   14),
(45, 'usuario45', 15, 'Mariana Martins',       '00000000045', 'usuario45@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   15),
(46, 'usuario46', 16, 'Rafael Souza',          '00000000046', 'usuario46@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   16),
(47, 'usuario47', 17, 'Beatriz Lima',          '00000000047', 'usuario47@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   17),
(48, 'usuario48', 18, 'Eduardo Ribeiro',       '00000000048', 'usuario48@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   18),
(49, 'usuario49', 19, 'Camila Almeida',        '00000000049', 'usuario49@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   19),
(50, 'usuario50', 20, 'Lucas Nunes',           '00000000050', 'usuario50@logfresh.com', '$2b$10$KIXq3nPZ0fQe7uYwR1sT8eJ4vXhLmN2oPa6bCdE9fGhIjKlMnOpQr', 'Operador de Monitoramento', 'operador',   20);

-- ADMIN_CD 

INSERT INTO admin_cd (cod_admin, cod_cd) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7), (8, 8), (9, 9), (10, 10),
(1, 11), (2, 12), (3, 13), (4, 14), (5, 15), (6, 16), (7, 17), (8, 18), (9, 19), (10, 20),
(1, 21), (2, 22), (3, 23), (4, 24), (5, 25), (6, 26), (7, 27), (8, 28), (9, 29), (10, 30),
(1, 31), (2, 32), (3, 33), (4, 34), (5, 35), (6, 36), (7, 37), (8, 38), (9, 39), (10, 40),
(1, 41), (2, 42), (3, 43), (4, 44), (5, 45), (6, 46), (7, 47), (8, 48), (9, 49), (10, 50);

-- FRIGORIFICO 

INSERT INTO Frigorifico (id_frigorifico, nome, modelo, localizacao, id_cd, temperatura_max, temperatura_min) VALUES
(1,  'Freezer Industrial 1',  'FR-2000',    'Setor A - Corredor 1', 1,  -18.00, -25.00),
(2,  'Câmara Fria 2',         'CF-Pro 500', 'Setor B - Corredor 2', 2,    4.00,   0.00),
(3,  'Freezer Industrial 3',  'TermoBox 9', 'Setor C - Corredor 3', 3,  -18.00, -25.00),
(4,  'Câmara Fria 4',         'Polar 300',  'Setor D - Corredor 4', 4,    4.00,   0.00),
(5,  'Freezer Industrial 5',  'FR-2000',    'Setor A - Corredor 5', 5,  -18.00, -25.00),
(6,  'Câmara Fria 6',         'CF-Pro 500', 'Setor B - Corredor 6', 6,    4.00,   0.00),
(7,  'Freezer Industrial 7',  'TermoBox 9', 'Setor C - Corredor 1', 7,  -18.00, -25.00),
(8,  'Câmara Fria 8',         'Polar 300',  'Setor D - Corredor 2', 8,    4.00,   0.00),
(9,  'Freezer Industrial 9',  'FR-2000',    'Setor A - Corredor 3', 9,  -18.00, -25.00),
(10, 'Câmara Fria 10',        'CF-Pro 500', 'Setor B - Corredor 4', 10,   4.00,   0.00),
(11, 'Freezer Industrial 11', 'TermoBox 9', 'Setor C - Corredor 5', 11, -18.00, -25.00),
(12, 'Câmara Fria 12',        'Polar 300',  'Setor D - Corredor 6', 12,   4.00,   0.00),
(13, 'Freezer Industrial 13', 'FR-2000',    'Setor A - Corredor 1', 13, -18.00, -25.00),
(14, 'Câmara Fria 14',        'CF-Pro 500', 'Setor B - Corredor 2', 14,   4.00,   0.00),
(15, 'Freezer Industrial 15', 'TermoBox 9', 'Setor C - Corredor 3', 15, -18.00, -25.00),
(16, 'Câmara Fria 16',        'Polar 300',  'Setor D - Corredor 4', 16,   4.00,   0.00),
(17, 'Freezer Industrial 17', 'FR-2000',    'Setor A - Corredor 5', 17, -18.00, -25.00),
(18, 'Câmara Fria 18',        'CF-Pro 500', 'Setor B - Corredor 6', 18,   4.00,   0.00),
(19, 'Freezer Industrial 19', 'TermoBox 9', 'Setor C - Corredor 1', 19, -18.00, -25.00),
(20, 'Câmara Fria 20',        'Polar 300',  'Setor D - Corredor 2', 20,   4.00,   0.00),
(21, 'Freezer Industrial 21', 'FR-2000',    'Setor A - Corredor 3', 21, -18.00, -25.00),
(22, 'Câmara Fria 22',        'CF-Pro 500', 'Setor B - Corredor 4', 22,   4.00,   0.00),
(23, 'Freezer Industrial 23', 'TermoBox 9', 'Setor C - Corredor 5', 23, -18.00, -25.00),
(24, 'Câmara Fria 24',        'Polar 300',  'Setor D - Corredor 6', 24,   4.00,   0.00),
(25, 'Freezer Industrial 25', 'FR-2000',    'Setor A - Corredor 1', 25, -18.00, -25.00),
(26, 'Câmara Fria 26',        'CF-Pro 500', 'Setor B - Corredor 2', 26,   4.00,   0.00),
(27, 'Freezer Industrial 27', 'TermoBox 9', 'Setor C - Corredor 3', 27, -18.00, -25.00),
(28, 'Câmara Fria 28',        'Polar 300',  'Setor D - Corredor 4', 28,   4.00,   0.00),
(29, 'Freezer Industrial 29', 'FR-2000',    'Setor A - Corredor 5', 29, -18.00, -25.00),
(30, 'Câmara Fria 30',        'CF-Pro 500', 'Setor B - Corredor 6', 30,   4.00,   0.00),
(31, 'Freezer Industrial 31', 'TermoBox 9', 'Setor C - Corredor 1', 31, -18.00, -25.00),
(32, 'Câmara Fria 32',        'Polar 300',  'Setor D - Corredor 2', 32,   4.00,   0.00),
(33, 'Freezer Industrial 33', 'FR-2000',    'Setor A - Corredor 3', 33, -18.00, -25.00),
(34, 'Câmara Fria 34',        'CF-Pro 500', 'Setor B - Corredor 4', 34,   4.00,   0.00),
(35, 'Freezer Industrial 35', 'TermoBox 9', 'Setor C - Corredor 5', 35, -18.00, -25.00),
(36, 'Câmara Fria 36',        'Polar 300',  'Setor D - Corredor 6', 36,   4.00,   0.00),
(37, 'Freezer Industrial 37', 'FR-2000',    'Setor A - Corredor 1', 37, -18.00, -25.00),
(38, 'Câmara Fria 38',        'CF-Pro 500', 'Setor B - Corredor 2', 38,   4.00,   0.00),
(39, 'Freezer Industrial 39', 'TermoBox 9', 'Setor C - Corredor 3', 39, -18.00, -25.00),
(40, 'Câmara Fria 40',        'Polar 300',  'Setor D - Corredor 4', 40,   4.00,   0.00),
(41, 'Freezer Industrial 41', 'FR-2000',    'Setor A - Corredor 5', 41, -18.00, -25.00),
(42, 'Câmara Fria 42',        'CF-Pro 500', 'Setor B - Corredor 6', 42,   4.00,   0.00),
(43, 'Freezer Industrial 43', 'TermoBox 9', 'Setor C - Corredor 1', 43, -18.00, -25.00),
(44, 'Câmara Fria 44',        'Polar 300',  'Setor D - Corredor 2', 44,   4.00,   0.00),
(45, 'Freezer Industrial 45', 'FR-2000',    'Setor A - Corredor 3', 45, -18.00, -25.00),
(46, 'Câmara Fria 46',        'CF-Pro 500', 'Setor B - Corredor 4', 46,   4.00,   0.00),
(47, 'Freezer Industrial 47', 'TermoBox 9', 'Setor C - Corredor 5', 47, -18.00, -25.00),
(48, 'Câmara Fria 48',        'Polar 300',  'Setor D - Corredor 6', 48,   4.00,   0.00),
(49, 'Freezer Industrial 49', 'FR-2000',    'Setor A - Corredor 1', 49, -18.00, -25.00),
(50, 'Câmara Fria 50',        'CF-Pro 500', 'Setor B - Corredor 2', 50,   4.00,   0.00);

-- TERMOMETRO 

INSERT INTO Termometro (id_termometro, modelo, status, id_frigorifico) VALUES
(1,  'Sensor DHT-2200',        'ATIVO',      1),
(2,  'Sensor TempLog X3',      'ATIVO',      2),
(3,  'Sensor TermoSense Pro',  'ATIVO',      3),
(4,  'Sensor IoT-Cold',        'ATIVO',      4),
(5,  'Sensor ThermoGuard',     'MANUTENCAO', 5),
(6,  'Sensor DHT-2200',        'ATIVO',      6),
(7,  'Sensor TempLog X3',      'ATIVO',      7),
(8,  'Sensor TermoSense Pro',  'ATIVO',      8),
(9,  'Sensor IoT-Cold',        'ATIVO',      9),
(10, 'Sensor ThermoGuard',     'INATIVO',    10),
(11, 'Sensor DHT-2200',        'ATIVO',      11),
(12, 'Sensor TempLog X3',      'ATIVO',      12),
(13, 'Sensor TermoSense Pro',  'ATIVO',      13),
(14, 'Sensor IoT-Cold',        'ATIVO',      14),
(15, 'Sensor ThermoGuard',     'MANUTENCAO', 15),
(16, 'Sensor DHT-2200',        'ATIVO',      16),
(17, 'Sensor TempLog X3',      'ATIVO',      17),
(18, 'Sensor TermoSense Pro',  'ATIVO',      18),
(19, 'Sensor IoT-Cold',        'ATIVO',      19),
(20, 'Sensor ThermoGuard',     'INATIVO',    20),
(21, 'Sensor DHT-2200',        'ATIVO',      21),
(22, 'Sensor TempLog X3',      'ATIVO',      22),
(23, 'Sensor TermoSense Pro',  'ATIVO',      23),
(24, 'Sensor IoT-Cold',        'ATIVO',      24),
(25, 'Sensor ThermoGuard',     'MANUTENCAO', 25),
(26, 'Sensor DHT-2200',        'ATIVO',      26),
(27, 'Sensor TempLog X3',      'ATIVO',      27),
(28, 'Sensor TermoSense Pro',  'ATIVO',      28),
(29, 'Sensor IoT-Cold',        'ATIVO',      29),
(30, 'Sensor ThermoGuard',     'INATIVO',    30),
(31, 'Sensor DHT-2200',        'ATIVO',      31),
(32, 'Sensor TempLog X3',      'ATIVO',      32),
(33, 'Sensor TermoSense Pro',  'ATIVO',      33),
(34, 'Sensor IoT-Cold',        'ATIVO',      34),
(35, 'Sensor ThermoGuard',     'MANUTENCAO', 35),
(36, 'Sensor DHT-2200',        'ATIVO',      36),
(37, 'Sensor TempLog X3',      'ATIVO',      37),
(38, 'Sensor TermoSense Pro',  'ATIVO',      38),
(39, 'Sensor IoT-Cold',        'ATIVO',      39),
(40, 'Sensor ThermoGuard',     'INATIVO',    40),
(41, 'Sensor DHT-2200',        'ATIVO',      41),
(42, 'Sensor TempLog X3',      'ATIVO',      42),
(43, 'Sensor TermoSense Pro',  'ATIVO',      43),
(44, 'Sensor IoT-Cold',        'ATIVO',      44),
(45, 'Sensor ThermoGuard',     'MANUTENCAO', 45),
(46, 'Sensor DHT-2200',        'ATIVO',      46),
(47, 'Sensor TempLog X3',      'ATIVO',      47),
(48, 'Sensor TermoSense Pro',  'ATIVO',      48),
(49, 'Sensor IoT-Cold',        'ATIVO',      49),
(50, 'Sensor ThermoGuard',     'INATIVO',    50);

-- CATEGORIA 

INSERT INTO Categoria (id_categoria, nome, vida_util_horas, temperatura_max, temperatura_min) VALUES
(1,  'Picanha Bovina Premium Congelado',              INTERVAL '3 hours 12 minutes', -18.00, -25.00),
(2,  'Alcatra Bovina Premium Resfriado',              INTERVAL '0 hours 55 minutes',   4.00,   0.00),
(3,  'Maminha Bovina Premium Congelado',              INTERVAL '2 hours 20 minutes', -18.00, -25.00),
(4,  'Patinho Bovino Premium Resfriado',              INTERVAL '1 hours 0 minutes',    4.00,   0.00),
(5,  'Fraldinha Bovina Premium Congelado',            INTERVAL '3 hours 24 minutes', -18.00, -25.00),
(6,  'Contra Filé Bovino Premium Resfriado',          INTERVAL '0 hours 58 minutes',   4.00,   0.00),
(7,  'Coxão Mole Bovino Premium Congelado',           INTERVAL '3 hours 33 minutes', -18.00, -25.00),
(8,  'Acém Bovino Premium Resfriado',                 INTERVAL '1 hours 5 minutes',    4.00,   0.00),
(9,  'Peito de Frango Premium Congelado',             INTERVAL '2 hours 15 minutes', -18.00, -25.00),
(10, 'Sobrecoxa de Frango Premium Resfriado',         INTERVAL '0 hours 37 minutes',   4.00,   0.00),
(11, 'Picanha Bovina Standard Congelado',             INTERVAL '3 hours 15 minutes', -18.00, -25.00),
(12, 'Alcatra Bovina Standard Resfriado',             INTERVAL '0 hours 54 minutes',   4.00,   0.00),
(13, 'Maminha Bovina Standard Congelado',             INTERVAL '2 hours 24 minutes', -18.00, -25.00),
(14, 'Patinho Bovino Standard Resfriado',             INTERVAL '1 hours 1 minutes',    4.00,   0.00),
(15, 'Fraldinha Bovina Standard Congelado',           INTERVAL '3 hours 27 minutes', -18.00, -25.00),
(16, 'Contra Filé Bovino Standard Resfriado',         INTERVAL '0 hours 56 minutes',   4.00,   0.00),
(17, 'Coxão Mole Bovino Standard Congelado',          INTERVAL '3 hours 30 minutes', -18.00, -25.00),
(18, 'Acém Bovino Standard Resfriado',                INTERVAL '1 hours 4 minutes',    4.00,   0.00),
(19, 'Peito de Frango Standard Congelado',            INTERVAL '2 hours 19 minutes', -18.00, -25.00),
(20, 'Sobrecoxa de Frango Standard Resfriado',        INTERVAL '0 hours 35 minutes',   4.00,   0.00),
(21, 'Picanha Bovina Econômica Congelado',            INTERVAL '3 hours 18 minutes', -18.00, -25.00),
(22, 'Alcatra Bovina Econômica Resfriado',            INTERVAL '0 hours 59 minutes',   4.00,   0.00),
(23, 'Maminha Bovina Econômica Congelado',            INTERVAL '2 hours 26 minutes', -18.00, -25.00),
(24, 'Patinho Bovino Econômica Resfriado',            INTERVAL '1 hours 2 minutes',    4.00,   0.00),
(25, 'Fraldinha Bovina Econômica Congelado',          INTERVAL '3 hours 36 minutes', -18.00, -25.00),
(26, 'Contra Filé Bovino Econômica Resfriado',        INTERVAL '0 hours 57 minutes',   4.00,   0.00),
(27, 'Coxão Mole Bovino Econômica Congelado',         INTERVAL '3 hours 39 minutes', -18.00, -25.00),
(28, 'Acém Bovino Econômica Resfriado',               INTERVAL '1 hours 6 minutes',    4.00,   0.00),
(29, 'Peito de Frango Econômica Congelado',           INTERVAL '2 hours 17 minutes', -18.00, -25.00),
(30, 'Sobrecoxa de Frango Econômica Resfriado',       INTERVAL '0 hours 36 minutes',   4.00,   0.00),
(31, 'Picanha Bovina Especial Congelado',             INTERVAL '3 hours 21 minutes', -18.00, -25.00),
(32, 'Alcatra Bovina Especial Resfriado',             INTERVAL '0 hours 34 minutes',   4.00,   0.00),
(33, 'Maminha Bovina Especial Congelado',             INTERVAL '2 hours 28 minutes', -18.00, -25.00),
(34, 'Patinho Bovino Especial Resfriado',             INTERVAL '1 hours 3 minutes',    4.00,   0.00),
(35, 'Fraldinha Bovina Especial Congelado',           INTERVAL '3 hours 45 minutes', -18.00, -25.00),
(36, 'Contra Filé Bovino Especial Resfriado',         INTERVAL '0 hours 56 minutes',   4.00,   0.00),
(37, 'Coxão Mole Bovino Especial Congelado',          INTERVAL '3 hours 42 minutes', -18.00, -25.00),
(38, 'Acém Bovino Especial Resfriado',                INTERVAL '1 hours 7 minutes',    4.00,   0.00),
(39, 'Peito de Frango Especial Congelado',            INTERVAL '2 hours 20 minutes', -18.00, -25.00),
(40, 'Sobrecoxa de Frango Especial Resfriado',        INTERVAL '0 hours 38 minutes',   4.00,   0.00),
(41, 'Picanha Bovina Básica Congelado',               INTERVAL '3 hours 48 minutes', -18.00, -25.00),
(42, 'Alcatra Bovina Básica Resfriado',               INTERVAL '0 hours 33 minutes',   4.00,   0.00),
(43, 'Maminha Bovina Básica Congelado',               INTERVAL '2 hours 31 minutes', -18.00, -25.00),
(44, 'Patinho Bovino Básica Resfriado',               INTERVAL '1 hours 8 minutes',    4.00,   0.00),
(45, 'Fraldinha Bovina Básica Congelado',             INTERVAL '3 hours 51 minutes', -18.00, -25.00),
(46, 'Contra Filé Bovino Básica Resfriado',           INTERVAL '0 hours 58 minutes',   4.00,   0.00),
(47, 'Coxão Mole Bovino Básica Congelado',            INTERVAL '3 hours 54 minutes', -18.00, -25.00),
(48, 'Acém Bovino Básica Resfriado',                  INTERVAL '1 hours 10 minutes',   4.00,   0.00),
(49, 'Peito de Frango Básica Congelado',              INTERVAL '2 hours 22 minutes', -18.00, -25.00),
(50, 'Sobrecoxa de Frango Básica Resfriado',          INTERVAL '0 hours 37 minutes',   4.00,   0.00);

-- LOTE 

INSERT INTO Lote (id_lote, codigo_lote, id_categoria, data_fabricacao, data_validade, status) VALUES
(1,  'LT-2026-0001', 1,  CURRENT_DATE - 2,  CURRENT_DATE + 178, 'ATIVO'),
(2,  'LT-2026-0002', 2,  CURRENT_DATE - 3,  CURRENT_DATE + 4,   'ATIVO'),
(3,  'LT-2026-0003', 3,  CURRENT_DATE - 4,  CURRENT_DATE + 176, 'VENDIDO'),
(4,  'LT-2026-0004', 4,  CURRENT_DATE - 5,  CURRENT_DATE + 2,   'ATIVO'),
(5,  'LT-2026-0005', 5,  CURRENT_DATE - 6,  CURRENT_DATE + 174, 'EXPEDIDO'),
(6,  'LT-2026-0006', 6,  CURRENT_DATE - 7,  CURRENT_DATE + 0,   'ATIVO'),
(7,  'LT-2026-0007', 7,  CURRENT_DATE - 8,  CURRENT_DATE + 172, 'BLOQUEADO'),
(8,  'LT-2026-0008', 8,  CURRENT_DATE - 9,  CURRENT_DATE - 2,   'ATIVO'),
(9,  'LT-2026-0009', 9,  CURRENT_DATE - 10, CURRENT_DATE + 170, 'ATIVO'),
(10, 'LT-2026-0010', 10, CURRENT_DATE - 11, CURRENT_DATE - 4,   'VENDIDO'),
(11, 'LT-2026-0011', 11, CURRENT_DATE - 12, CURRENT_DATE + 168, 'ATIVO'),
(12, 'LT-2026-0012', 12, CURRENT_DATE - 13, CURRENT_DATE - 6,   'EXPEDIDO'),
(13, 'LT-2026-0013', 13, CURRENT_DATE - 14, CURRENT_DATE + 166, 'ATIVO'),
(14, 'LT-2026-0014', 14, CURRENT_DATE - 15, CURRENT_DATE - 8,   'BLOQUEADO'),
(15, 'LT-2026-0015', 15, CURRENT_DATE - 16, CURRENT_DATE + 164, 'ATIVO'),
(16, 'LT-2026-0016', 16, CURRENT_DATE - 17, CURRENT_DATE - 10,  'ATIVO'),
(17, 'LT-2026-0017', 17, CURRENT_DATE - 18, CURRENT_DATE + 162, 'VENDIDO'),
(18, 'LT-2026-0018', 18, CURRENT_DATE - 19, CURRENT_DATE - 12,  'ATIVO'),
(19, 'LT-2026-0019', 19, CURRENT_DATE - 20, CURRENT_DATE + 160, 'EXPEDIDO'),
(20, 'LT-2026-0020', 20, CURRENT_DATE - 21, CURRENT_DATE - 14,  'ATIVO'),
(21, 'LT-2026-0021', 21, CURRENT_DATE - 22, CURRENT_DATE + 158, 'BLOQUEADO'),
(22, 'LT-2026-0022', 22, CURRENT_DATE - 23, CURRENT_DATE - 16,  'ATIVO'),
(23, 'LT-2026-0023', 23, CURRENT_DATE - 24, CURRENT_DATE + 156, 'ATIVO'),
(24, 'LT-2026-0024', 24, CURRENT_DATE - 25, CURRENT_DATE - 18,  'VENDIDO'),
(25, 'LT-2026-0025', 25, CURRENT_DATE - 26, CURRENT_DATE + 154, 'ATIVO'),
(26, 'LT-2026-0026', 26, CURRENT_DATE - 27, CURRENT_DATE - 20,  'EXPEDIDO'),
(27, 'LT-2026-0027', 27, CURRENT_DATE - 28, CURRENT_DATE + 152, 'ATIVO'),
(28, 'LT-2026-0028', 28, CURRENT_DATE - 29, CURRENT_DATE - 22,  'BLOQUEADO'),
(29, 'LT-2026-0029', 29, CURRENT_DATE - 30, CURRENT_DATE + 150, 'ATIVO'),
(30, 'LT-2026-0030', 30, CURRENT_DATE - 1,  CURRENT_DATE + 6,   'ATIVO'),
(31, 'LT-2026-0031', 31, CURRENT_DATE - 2,  CURRENT_DATE + 178, 'VENDIDO'),
(32, 'LT-2026-0032', 32, CURRENT_DATE - 3,  CURRENT_DATE + 4,   'ATIVO'),
(33, 'LT-2026-0033', 33, CURRENT_DATE - 4,  CURRENT_DATE + 176, 'EXPEDIDO'),
(34, 'LT-2026-0034', 34, CURRENT_DATE - 5,  CURRENT_DATE + 2,   'ATIVO'),
(35, 'LT-2026-0035', 35, CURRENT_DATE - 6,  CURRENT_DATE + 174, 'BLOQUEADO'),
(36, 'LT-2026-0036', 36, CURRENT_DATE - 7,  CURRENT_DATE + 0,   'ATIVO'),
(37, 'LT-2026-0037', 37, CURRENT_DATE - 8,  CURRENT_DATE + 172, 'ATIVO'),
(38, 'LT-2026-0038', 38, CURRENT_DATE - 9,  CURRENT_DATE - 2,   'VENDIDO'),
(39, 'LT-2026-0039', 39, CURRENT_DATE - 10, CURRENT_DATE + 170, 'ATIVO'),
(40, 'LT-2026-0040', 40, CURRENT_DATE - 11, CURRENT_DATE - 4,   'EXPEDIDO'),
(41, 'LT-2026-0041', 41, CURRENT_DATE - 12, CURRENT_DATE + 168, 'ATIVO'),
(42, 'LT-2026-0042', 42, CURRENT_DATE - 13, CURRENT_DATE - 6,   'BLOQUEADO'),
(43, 'LT-2026-0043', 43, CURRENT_DATE - 14, CURRENT_DATE + 166, 'ATIVO'),
(44, 'LT-2026-0044', 44, CURRENT_DATE - 15, CURRENT_DATE - 8,   'ATIVO'),
(45, 'LT-2026-0045', 45, CURRENT_DATE - 16, CURRENT_DATE + 164, 'VENDIDO'),
(46, 'LT-2026-0046', 46, CURRENT_DATE - 17, CURRENT_DATE - 10,  'ATIVO'),
(47, 'LT-2026-0047', 47, CURRENT_DATE - 18, CURRENT_DATE + 162, 'EXPEDIDO'),
(48, 'LT-2026-0048', 48, CURRENT_DATE - 19, CURRENT_DATE - 12,  'ATIVO'),
(49, 'LT-2026-0049', 49, CURRENT_DATE - 20, CURRENT_DATE + 160, 'BLOQUEADO'),
(50, 'LT-2026-0050', 50, CURRENT_DATE - 21, CURRENT_DATE - 14,  'ATIVO');

-- LOTE_FRIGORIFICO 

INSERT INTO lote_frigorifico (id_lote_frigorifico, id_lote, id_frigorifico, data_entrada, data_saida) VALUES
(1,  1,  1,  CURRENT_TIMESTAMP - INTERVAL '2 days',  NULL),
(2,  2,  2,  CURRENT_TIMESTAMP - INTERVAL '3 days',  NULL),
(3,  3,  3,  CURRENT_TIMESTAMP - INTERVAL '4 days',  NULL),
(4,  4,  4,  CURRENT_TIMESTAMP - INTERVAL '5 days',  NULL),
(5,  5,  5,  CURRENT_TIMESTAMP - INTERVAL '6 days',  CURRENT_TIMESTAMP - INTERVAL '4 days'),
(6,  6,  6,  CURRENT_TIMESTAMP - INTERVAL '7 days',  NULL),
(7,  7,  7,  CURRENT_TIMESTAMP - INTERVAL '8 days',  NULL),
(8,  8,  8,  CURRENT_TIMESTAMP - INTERVAL '9 days',  NULL),
(9,  9,  9,  CURRENT_TIMESTAMP - INTERVAL '10 days', NULL),
(10, 10, 10, CURRENT_TIMESTAMP - INTERVAL '6 days',  CURRENT_TIMESTAMP - INTERVAL '4 days'),
(11, 11, 11, CURRENT_TIMESTAMP - INTERVAL '12 days', NULL),
(12, 12, 12, CURRENT_TIMESTAMP - INTERVAL '13 days', NULL),
(13, 13, 13, CURRENT_TIMESTAMP - INTERVAL '14 days', NULL),
(14, 14, 14, CURRENT_TIMESTAMP - INTERVAL '15 days', NULL),
(15, 15, 15, CURRENT_TIMESTAMP - INTERVAL '6 days',  CURRENT_TIMESTAMP - INTERVAL '4 days'),
(16, 16, 16, CURRENT_TIMESTAMP - INTERVAL '17 days', NULL),
(17, 17, 17, CURRENT_TIMESTAMP - INTERVAL '18 days', NULL),
(18, 18, 18, CURRENT_TIMESTAMP - INTERVAL '19 days', NULL),
(19, 19, 19, CURRENT_TIMESTAMP - INTERVAL '20 days', NULL),
(20, 20, 20, CURRENT_TIMESTAMP - INTERVAL '6 days',  CURRENT_TIMESTAMP - INTERVAL '4 days'),
(21, 21, 21, CURRENT_TIMESTAMP - INTERVAL '2 days',  NULL),
(22, 22, 22, CURRENT_TIMESTAMP - INTERVAL '3 days',  NULL),
(23, 23, 23, CURRENT_TIMESTAMP - INTERVAL '4 days',  NULL),
(24, 24, 24, CURRENT_TIMESTAMP - INTERVAL '5 days',  NULL),
(25, 25, 25, CURRENT_TIMESTAMP - INTERVAL '6 days',  CURRENT_TIMESTAMP - INTERVAL '4 days'),
(26, 26, 26, CURRENT_TIMESTAMP - INTERVAL '7 days',  NULL),
(27, 27, 27, CURRENT_TIMESTAMP - INTERVAL '8 days',  NULL),
(28, 28, 28, CURRENT_TIMESTAMP - INTERVAL '9 days',  NULL),
(29, 29, 29, CURRENT_TIMESTAMP - INTERVAL '10 days', NULL),
(30, 30, 30, CURRENT_TIMESTAMP - INTERVAL '6 days',  CURRENT_TIMESTAMP - INTERVAL '4 days'),
(31, 31, 31, CURRENT_TIMESTAMP - INTERVAL '12 days', NULL),
(32, 32, 32, CURRENT_TIMESTAMP - INTERVAL '13 days', NULL),
(33, 33, 33, CURRENT_TIMESTAMP - INTERVAL '14 days', NULL),
(34, 34, 34, CURRENT_TIMESTAMP - INTERVAL '15 days', NULL),
(35, 35, 35, CURRENT_TIMESTAMP - INTERVAL '6 days',  CURRENT_TIMESTAMP - INTERVAL '4 days'),
(36, 36, 36, CURRENT_TIMESTAMP - INTERVAL '17 days', NULL),
(37, 37, 37, CURRENT_TIMESTAMP - INTERVAL '18 days', NULL),
(38, 38, 38, CURRENT_TIMESTAMP - INTERVAL '19 days', NULL),
(39, 39, 39, CURRENT_TIMESTAMP - INTERVAL '20 days', NULL),
(40, 40, 40, CURRENT_TIMESTAMP - INTERVAL '6 days',  CURRENT_TIMESTAMP - INTERVAL '4 days'),
(41, 41, 41, CURRENT_TIMESTAMP - INTERVAL '2 days',  NULL),
(42, 42, 42, CURRENT_TIMESTAMP - INTERVAL '3 days',  NULL),
(43, 43, 43, CURRENT_TIMESTAMP - INTERVAL '4 days',  NULL),
(44, 44, 44, CURRENT_TIMESTAMP - INTERVAL '5 days',  NULL),
(45, 45, 45, CURRENT_TIMESTAMP - INTERVAL '6 days',  CURRENT_TIMESTAMP - INTERVAL '4 days'),
(46, 46, 46, CURRENT_TIMESTAMP - INTERVAL '7 days',  NULL),
(47, 47, 47, CURRENT_TIMESTAMP - INTERVAL '8 days',  NULL),
(48, 48, 48, CURRENT_TIMESTAMP - INTERVAL '9 days',  NULL),
(49, 49, 49, CURRENT_TIMESTAMP - INTERVAL '10 days', NULL),
(50, 50, 50, CURRENT_TIMESTAMP - INTERVAL '6 days',  CURRENT_TIMESTAMP - INTERVAL '4 days');


-- LEITURATEMPERATURA 

INSERT INTO LeituraTemperatura (id_leitura, temperatura, data_hora, id_termometro) VALUES
(1,  -16.30, CURRENT_TIMESTAMP - INTERVAL '97 minutes',   1),
(2,    3.20, CURRENT_TIMESTAMP - INTERVAL '194 minutes',  2),
(3,  -19.80, CURRENT_TIMESTAMP - INTERVAL '291 minutes',  3),
(4,    2.70, CURRENT_TIMESTAMP - INTERVAL '388 minutes',  4),
(5,  -22.10, CURRENT_TIMESTAMP - INTERVAL '485 minutes',  5),
(6,    4.60, CURRENT_TIMESTAMP - INTERVAL '582 minutes',  6),
(7,  -17.40, CURRENT_TIMESTAMP - INTERVAL '679 minutes',  7),
(8,    1.90, CURRENT_TIMESTAMP - INTERVAL '776 minutes',  8),
(9,  -20.50, CURRENT_TIMESTAMP - INTERVAL '873 minutes',  9),
(10,   5.30, CURRENT_TIMESTAMP - INTERVAL '970 minutes',  10),
(11, -15.80, CURRENT_TIMESTAMP - INTERVAL '1067 minutes', 11),
(12,   3.90, CURRENT_TIMESTAMP - INTERVAL '1164 minutes', 12),
(13, -18.60, CURRENT_TIMESTAMP - INTERVAL '1261 minutes', 13),
(14,   0.80, CURRENT_TIMESTAMP - INTERVAL '1358 minutes', 14),
(15, -23.40, CURRENT_TIMESTAMP - INTERVAL '1455 minutes', 15),
(16,   4.20, CURRENT_TIMESTAMP - INTERVAL '1552 minutes', 16),
(17, -21.90, CURRENT_TIMESTAMP - INTERVAL '1649 minutes', 17),
(18,   2.10, CURRENT_TIMESTAMP - INTERVAL '1746 minutes', 18),
(19, -17.00, CURRENT_TIMESTAMP - INTERVAL '1843 minutes', 19),
(20,   3.70, CURRENT_TIMESTAMP - INTERVAL '1940 minutes', 20),
(21, -19.20, CURRENT_TIMESTAMP - INTERVAL '2037 minutes', 21),
(22,   5.10, CURRENT_TIMESTAMP - INTERVAL '2134 minutes', 22),
(23, -16.90, CURRENT_TIMESTAMP - INTERVAL '2231 minutes', 23),
(24,   2.50, CURRENT_TIMESTAMP - INTERVAL '2328 minutes', 24),
(25, -22.60, CURRENT_TIMESTAMP - INTERVAL '2425 minutes', 25),
(26,   4.80, CURRENT_TIMESTAMP - INTERVAL '2522 minutes', 26),
(27, -18.10, CURRENT_TIMESTAMP - INTERVAL '2619 minutes', 27),
(28,   1.40, CURRENT_TIMESTAMP - INTERVAL '2716 minutes', 28),
(29, -20.20, CURRENT_TIMESTAMP - INTERVAL '2813 minutes', 29),
(30,   3.30, CURRENT_TIMESTAMP - INTERVAL '2910 minutes', 30),
(31, -15.40, CURRENT_TIMESTAMP - INTERVAL '3007 minutes', 31),
(32,   5.80, CURRENT_TIMESTAMP - INTERVAL '3104 minutes', 32),
(33, -17.80, CURRENT_TIMESTAMP - INTERVAL '3201 minutes', 33),
(34,   2.90, CURRENT_TIMESTAMP - INTERVAL '3298 minutes', 34),
(35, -21.10, CURRENT_TIMESTAMP - INTERVAL '3395 minutes', 35),
(36,   4.00, CURRENT_TIMESTAMP - INTERVAL '3492 minutes', 36),
(37, -19.50, CURRENT_TIMESTAMP - INTERVAL '3589 minutes', 37),
(38,   1.10, CURRENT_TIMESTAMP - INTERVAL '3686 minutes', 38),
(39, -23.00, CURRENT_TIMESTAMP - INTERVAL '3783 minutes', 39),
(40,   3.50, CURRENT_TIMESTAMP - INTERVAL '3880 minutes', 40),
(41, -16.10, CURRENT_TIMESTAMP - INTERVAL '3977 minutes', 41),
(42,   4.90, CURRENT_TIMESTAMP - INTERVAL '4074 minutes', 42),
(43, -18.90, CURRENT_TIMESTAMP - INTERVAL '4171 minutes', 43),
(44,   2.30, CURRENT_TIMESTAMP - INTERVAL '4268 minutes', 44),
(45, -22.30, CURRENT_TIMESTAMP - INTERVAL '4365 minutes', 45),
(46,   5.50, CURRENT_TIMESTAMP - INTERVAL '4462 minutes', 46),
(47, -17.70, CURRENT_TIMESTAMP - INTERVAL '4559 minutes', 47),
(48,   0.60, CURRENT_TIMESTAMP - INTERVAL '4656 minutes', 48),
(49, -20.80, CURRENT_TIMESTAMP - INTERVAL '4753 minutes', 49),
(50,   3.10, CURRENT_TIMESTAMP - INTERVAL '4850 minutes', 50);

-- ALERTA 

INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (1, 'temperatura_fora_padrao', 'atenção', 'pendente', INTERVAL '2 hours 14 minutes', CURRENT_TIMESTAMP - INTERVAL '275 minutes', NULL, 1, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (2, 'temperatura_fora_padrao', 'atenção', 'em_andamento', INTERVAL '1 hours 34 minutes', CURRENT_TIMESTAMP - INTERVAL '372 minutes', NULL, 2, 2);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (3, 'porta_aberta', 'atenção', 'resolvido', INTERVAL '0 hours 39 minutes', CURRENT_TIMESTAMP - INTERVAL '469 minutes', CURRENT_TIMESTAMP - INTERVAL '405 minutes', 3, 3);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (4, 'temperatura_fora_padrao', 'atenção', 'ignorado', INTERVAL '0 hours 17 minutes', CURRENT_TIMESTAMP - INTERVAL '566 minutes', CURRENT_TIMESTAMP - INTERVAL '489 minutes', 4, 4);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (5, 'termometro_offline', 'atenção', 'pendente', NULL, CURRENT_TIMESTAMP - INTERVAL '663 minutes', NULL, 5, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (6, 'temperatura_fora_padrao', 'crítico', 'em_andamento', INTERVAL '0 hours 17 minutes', CURRENT_TIMESTAMP - INTERVAL '760 minutes', NULL, 6, 6);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (7, 'temperatura_fora_padrao', 'estável', 'resolvido', INTERVAL '2 hours 5 minutes', CURRENT_TIMESTAMP - INTERVAL '857 minutes', CURRENT_TIMESTAMP - INTERVAL '832 minutes', 7, 7);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (8, 'porta_aberta', 'estável', 'ignorado', INTERVAL '3 hours 4 minutes', CURRENT_TIMESTAMP - INTERVAL '954 minutes', CURRENT_TIMESTAMP - INTERVAL '916 minutes', 8, 8);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (9, 'temperatura_fora_padrao', 'atenção', 'pendente', INTERVAL '0 hours 24 minutes', CURRENT_TIMESTAMP - INTERVAL '1051 minutes', NULL, 9, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (10, 'termometro_offline', 'atenção', 'em_andamento', NULL, CURRENT_TIMESTAMP - INTERVAL '1148 minutes', NULL, 10, 10);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (11, 'temperatura_fora_padrao', 'atenção', 'resolvido', INTERVAL '1 hours 45 minutes', CURRENT_TIMESTAMP - INTERVAL '1245 minutes', CURRENT_TIMESTAMP - INTERVAL '1168 minutes', 11, 11);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (12, 'temperatura_fora_padrao', 'crítico', 'ignorado', INTERVAL '0 hours 43 minutes', CURRENT_TIMESTAMP - INTERVAL '1342 minutes', CURRENT_TIMESTAMP - INTERVAL '1252 minutes', 12, 12);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (13, 'porta_aberta', 'estável', 'pendente', INTERVAL '0 hours 56 minutes', CURRENT_TIMESTAMP - INTERVAL '1439 minutes', NULL, 13, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (14, 'temperatura_fora_padrao', 'atenção', 'em_andamento', INTERVAL '0 hours 25 minutes', CURRENT_TIMESTAMP - INTERVAL '1536 minutes', NULL, 14, 14);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (15, 'termometro_offline', 'atenção', 'resolvido', NULL, CURRENT_TIMESTAMP - INTERVAL '1633 minutes', CURRENT_TIMESTAMP - INTERVAL '1595 minutes', 15, 15);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (16, 'temperatura_fora_padrao', 'atenção', 'ignorado', INTERVAL '0 hours 32 minutes', CURRENT_TIMESTAMP - INTERVAL '1730 minutes', CURRENT_TIMESTAMP - INTERVAL '1679 minutes', 16, 16);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (17, 'temperatura_fora_padrao', 'atenção', 'pendente', INTERVAL '1 hours 13 minutes', CURRENT_TIMESTAMP - INTERVAL '1827 minutes', NULL, 17, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (18, 'porta_aberta', 'atenção', 'em_andamento', INTERVAL '2 hours 35 minutes', CURRENT_TIMESTAMP - INTERVAL '1924 minutes', NULL, 18, 18);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (19, 'temperatura_fora_padrao', 'crítico', 'resolvido', INTERVAL '0 hours 11 minutes', CURRENT_TIMESTAMP - INTERVAL '2021 minutes', CURRENT_TIMESTAMP - INTERVAL '1931 minutes', 19, 19);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (20, 'termometro_offline', 'atenção', 'ignorado', NULL, CURRENT_TIMESTAMP - INTERVAL '2118 minutes', CURRENT_TIMESTAMP - INTERVAL '2015 minutes', 20, 20);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (21, 'temperatura_fora_padrao', 'atenção', 'pendente', INTERVAL '2 hours 40 minutes', CURRENT_TIMESTAMP - INTERVAL '2215 minutes', NULL, 21, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (22, 'temperatura_fora_padrao', 'atenção', 'em_andamento', INTERVAL '1 hours 45 minutes', CURRENT_TIMESTAMP - INTERVAL '2312 minutes', NULL, 22, 22);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (23, 'porta_aberta', 'atenção', 'resolvido', INTERVAL '0 hours 49 minutes', CURRENT_TIMESTAMP - INTERVAL '2409 minutes', CURRENT_TIMESTAMP - INTERVAL '2358 minutes', 23, 23);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (24, 'temperatura_fora_padrao', 'atenção', 'ignorado', INTERVAL '0 hours 19 minutes', CURRENT_TIMESTAMP - INTERVAL '2506 minutes', CURRENT_TIMESTAMP - INTERVAL '2442 minutes', 24, 24);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (25, 'termometro_offline', 'atenção', 'pendente', NULL, CURRENT_TIMESTAMP - INTERVAL '2603 minutes', NULL, 25, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (26, 'temperatura_fora_padrao', 'estável', 'em_andamento', INTERVAL '0 hours 49 minutes', CURRENT_TIMESTAMP - INTERVAL '2700 minutes', NULL, 26, 26);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (27, 'temperatura_fora_padrao', 'atenção', 'resolvido', INTERVAL '0 hours 23 minutes', CURRENT_TIMESTAMP - INTERVAL '2797 minutes', CURRENT_TIMESTAMP - INTERVAL '2694 minutes', 27, 27);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (28, 'porta_aberta', 'estável', 'ignorado', INTERVAL '2 hours 56 minutes', CURRENT_TIMESTAMP - INTERVAL '2894 minutes', CURRENT_TIMESTAMP - INTERVAL '2869 minutes', 28, 28);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (29, 'temperatura_fora_padrao', 'atenção', 'pendente', INTERVAL '1 hours 8 minutes', CURRENT_TIMESTAMP - INTERVAL '2991 minutes', NULL, 29, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (30, 'termometro_offline', 'atenção', 'em_andamento', NULL, CURRENT_TIMESTAMP - INTERVAL '3088 minutes', NULL, 30, 30);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (31, 'temperatura_fora_padrao', 'crítico', 'resolvido', INTERVAL '1 hours 0 minutes', CURRENT_TIMESTAMP - INTERVAL '3185 minutes', CURRENT_TIMESTAMP - INTERVAL '3121 minutes', 31, 31);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (32, 'temperatura_fora_padrao', 'crítico', 'ignorado', INTERVAL '0 hours 10 minutes', CURRENT_TIMESTAMP - INTERVAL '3282 minutes', CURRENT_TIMESTAMP - INTERVAL '3205 minutes', 32, 32);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (33, 'porta_aberta', 'estável', 'pendente', INTERVAL '0 hours 53 minutes', CURRENT_TIMESTAMP - INTERVAL '3379 minutes', NULL, 33, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (34, 'temperatura_fora_padrao', 'atenção', 'em_andamento', INTERVAL '1 hours 38 minutes', CURRENT_TIMESTAMP - INTERVAL '3476 minutes', NULL, 34, 34);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (35, 'termometro_offline', 'atenção', 'resolvido', NULL, CURRENT_TIMESTAMP - INTERVAL '3573 minutes', CURRENT_TIMESTAMP - INTERVAL '3548 minutes', 35, 35);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (36, 'temperatura_fora_padrao', 'atenção', 'ignorado', INTERVAL '0 hours 31 minutes', CURRENT_TIMESTAMP - INTERVAL '3670 minutes', CURRENT_TIMESTAMP - INTERVAL '3632 minutes', 36, 36);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (37, 'temperatura_fora_padrao', 'atenção', 'pendente', INTERVAL '0 hours 18 minutes', CURRENT_TIMESTAMP - INTERVAL '3767 minutes', NULL, 37, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (38, 'porta_aberta', 'atenção', 'em_andamento', INTERVAL '2 hours 29 minutes', CURRENT_TIMESTAMP - INTERVAL '3864 minutes', NULL, 38, 38);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (39, 'temperatura_fora_padrao', 'estável', 'resolvido', INTERVAL '2 hours 10 minutes', CURRENT_TIMESTAMP - INTERVAL '3961 minutes', CURRENT_TIMESTAMP - INTERVAL '3884 minutes', 39, 39);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (40, 'termometro_offline', 'atenção', 'ignorado', NULL, CURRENT_TIMESTAMP - INTERVAL '4058 minutes', CURRENT_TIMESTAMP - INTERVAL '3968 minutes', 40, 40);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (41, 'temperatura_fora_padrao', 'atenção', 'pendente', INTERVAL '2 hours 33 minutes', CURRENT_TIMESTAMP - INTERVAL '4155 minutes', NULL, 41, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (42, 'temperatura_fora_padrao', 'atenção', 'em_andamento', INTERVAL '0 hours 18 minutes', CURRENT_TIMESTAMP - INTERVAL '4252 minutes', NULL, 42, 42);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (43, 'porta_aberta', 'atenção', 'resolvido', INTERVAL '0 hours 46 minutes', CURRENT_TIMESTAMP - INTERVAL '4349 minutes', CURRENT_TIMESTAMP - INTERVAL '4311 minutes', 43, 43);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (44, 'temperatura_fora_padrao', 'crítico', 'ignorado', INTERVAL '0 hours 44 minutes', CURRENT_TIMESTAMP - INTERVAL '4446 minutes', CURRENT_TIMESTAMP - INTERVAL '4395 minutes', 44, 44);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (45, 'termometro_offline', 'atenção', 'pendente', NULL, CURRENT_TIMESTAMP - INTERVAL '4543 minutes', NULL, 45, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (46, 'temperatura_fora_padrao', 'atenção', 'em_andamento', INTERVAL '0 hours 48 minutes', CURRENT_TIMESTAMP - INTERVAL '4640 minutes', NULL, 46, 46);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (47, 'temperatura_fora_padrao', 'atenção', 'resolvido', INTERVAL '0 hours 26 minutes', CURRENT_TIMESTAMP - INTERVAL '4737 minutes', CURRENT_TIMESTAMP - INTERVAL '4647 minutes', 47, 47);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (48, 'porta_aberta', 'estável', 'ignorado', INTERVAL '3 hours 28 minutes', CURRENT_TIMESTAMP - INTERVAL '4834 minutes', CURRENT_TIMESTAMP - INTERVAL '4731 minutes', 48, 48);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (49, 'temperatura_fora_padrao', 'atenção', 'pendente', INTERVAL '1 hours 16 minutes', CURRENT_TIMESTAMP - INTERVAL '4931 minutes', NULL, 49, NULL);
INSERT INTO Alerta (id_alerta, tipo, nivel_gravidade, status, vida_util, data_hora, data_hora_resolucao, id_leitura, id_usuario) VALUES (50, 'termometro_offline', 'atenção', 'em_andamento', NULL, CURRENT_TIMESTAMP - INTERVAL '5028 minutes', NULL, 50, 50);
-- NOTIFICACAO ALERTA

INSERT INTO NotificacaoAlerta (id_notificacao, id_alerta, id_usuario, data_hora_envio) VALUES
(1,  1,  21, CURRENT_TIMESTAMP - INTERVAL '95 minutes'),
(2,  2,  12, CURRENT_TIMESTAMP - INTERVAL '192 minutes'),
(3,  3,  23, CURRENT_TIMESTAMP - INTERVAL '289 minutes'),
(4,  4,  14, CURRENT_TIMESTAMP - INTERVAL '386 minutes'),
(5,  5,  25, CURRENT_TIMESTAMP - INTERVAL '483 minutes'),
(6,  6,  16, CURRENT_TIMESTAMP - INTERVAL '580 minutes'),
(7,  7,  27, CURRENT_TIMESTAMP - INTERVAL '677 minutes'),
(8,  8,  18, CURRENT_TIMESTAMP - INTERVAL '774 minutes'),
(9,  9,  29, CURRENT_TIMESTAMP - INTERVAL '871 minutes'),
(10, 10, 20, CURRENT_TIMESTAMP - INTERVAL '968 minutes'),
(11, 11, 31, CURRENT_TIMESTAMP - INTERVAL '1065 minutes'),
(12, 12, 12, CURRENT_TIMESTAMP - INTERVAL '1162 minutes'),
(13, 13, 33, CURRENT_TIMESTAMP - INTERVAL '1259 minutes'),
(14, 14, 14, CURRENT_TIMESTAMP - INTERVAL '1356 minutes'),
(15, 15, 35, CURRENT_TIMESTAMP - INTERVAL '1453 minutes'),
(16, 16, 16, CURRENT_TIMESTAMP - INTERVAL '1550 minutes'),
(17, 17, 37, CURRENT_TIMESTAMP - INTERVAL '1647 minutes'),
(18, 18, 18, CURRENT_TIMESTAMP - INTERVAL '1744 minutes'),
(19, 19, 39, CURRENT_TIMESTAMP - INTERVAL '1841 minutes'),
(20, 20, 20, CURRENT_TIMESTAMP - INTERVAL '1938 minutes'),
(21, 21, 41, CURRENT_TIMESTAMP - INTERVAL '2035 minutes'),
(22, 22, 12, CURRENT_TIMESTAMP - INTERVAL '2132 minutes'),
(23, 23, 43, CURRENT_TIMESTAMP - INTERVAL '2229 minutes'),
(24, 24, 14, CURRENT_TIMESTAMP - INTERVAL '2326 minutes'),
(25, 25, 45, CURRENT_TIMESTAMP - INTERVAL '2423 minutes'),
(26, 26, 16, CURRENT_TIMESTAMP - INTERVAL '2520 minutes'),
(27, 27, 47, CURRENT_TIMESTAMP - INTERVAL '2617 minutes'),
(28, 28, 18, CURRENT_TIMESTAMP - INTERVAL '2714 minutes'),
(29, 29, 49, CURRENT_TIMESTAMP - INTERVAL '2811 minutes'),
(30, 30, 20, CURRENT_TIMESTAMP - INTERVAL '2908 minutes'),
(31, 31, 21, CURRENT_TIMESTAMP - INTERVAL '3005 minutes'),
(32, 32, 12, CURRENT_TIMESTAMP - INTERVAL '3102 minutes'),
(33, 33, 23, CURRENT_TIMESTAMP - INTERVAL '3199 minutes'),
(34, 34, 14, CURRENT_TIMESTAMP - INTERVAL '3296 minutes'),
(35, 35, 25, CURRENT_TIMESTAMP - INTERVAL '3393 minutes'),
(36, 36, 16, CURRENT_TIMESTAMP - INTERVAL '3490 minutes'),
(37, 37, 27, CURRENT_TIMESTAMP - INTERVAL '3587 minutes'),
(38, 38, 18, CURRENT_TIMESTAMP - INTERVAL '3684 minutes'),
(39, 39, 29, CURRENT_TIMESTAMP - INTERVAL '3781 minutes'),
(40, 40, 20, CURRENT_TIMESTAMP - INTERVAL '3878 minutes'),
(41, 41, 31, CURRENT_TIMESTAMP - INTERVAL '3975 minutes'),
(42, 42, 12, CURRENT_TIMESTAMP - INTERVAL '4072 minutes'),
(43, 43, 33, CURRENT_TIMESTAMP - INTERVAL '4169 minutes'),
(44, 44, 14, CURRENT_TIMESTAMP - INTERVAL '4266 minutes'),
(45, 45, 35, CURRENT_TIMESTAMP - INTERVAL '4363 minutes'),
(46, 46, 16, CURRENT_TIMESTAMP - INTERVAL '4460 minutes'),
(47, 47, 37, CURRENT_TIMESTAMP - INTERVAL '4557 minutes'),
(48, 48, 18, CURRENT_TIMESTAMP - INTERVAL '4654 minutes'),
(49, 49, 39, CURRENT_TIMESTAMP - INTERVAL '4751 minutes'),
(50, 50, 20, CURRENT_TIMESTAMP - INTERVAL '4848 minutes');

-- AJUSTE DAS SEQUENCIAS

SELECT setval(pg_get_serial_sequence('cd', 'id_cd'), (SELECT MAX(id_cd) FROM cd));
SELECT setval(pg_get_serial_sequence('endereco', 'id_endereco'), (SELECT MAX(id_endereco) FROM endereco));
SELECT setval(pg_get_serial_sequence('usuario', 'id_usuario'), (SELECT MAX(id_usuario) FROM usuario));
SELECT setval(pg_get_serial_sequence('frigorifico', 'id_frigorifico'), (SELECT MAX(id_frigorifico) FROM frigorifico));
SELECT setval(pg_get_serial_sequence('termometro', 'id_termometro'), (SELECT MAX(id_termometro) FROM termometro));
SELECT setval(pg_get_serial_sequence('categoria', 'id_categoria'), (SELECT MAX(id_categoria) FROM categoria));
SELECT setval(pg_get_serial_sequence('lote', 'id_lote'), (SELECT MAX(id_lote) FROM lote));
SELECT setval(pg_get_serial_sequence('lote_frigorifico', 'id_lote_frigorifico'), (SELECT MAX(id_lote_frigorifico) FROM lote_frigorifico));
SELECT setval(pg_get_serial_sequence('leituratemperatura', 'id_leitura'), (SELECT MAX(id_leitura) FROM leituratemperatura));
SELECT setval(pg_get_serial_sequence('alerta', 'id_alerta'), (SELECT MAX(id_alerta) FROM alerta));
SELECT setval(pg_get_serial_sequence('notificacaoalerta', 'id_notificacao'), (SELECT MAX(id_notificacao) FROM notificacaoalerta));