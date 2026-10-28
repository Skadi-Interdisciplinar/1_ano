/* ============================================================
   SKADI · CRUD de TERMÔMETROS
   Só configuração: o comportamento está em crud.js.

   Tabela do banco:
     Termometro (id_termometro, modelo, status, id_frigorifico, data_atualizacao)
     status aceito: 'ativo' | 'inativo' | 'manutencao'
   "Temperatura atual" é calculada (última LeituraTemperatura) e só aparece na lista.
   ============================================================ */

const STATUS_TERMOMETRO = [
    { valor: 'ativo',      rotulo: 'Ativo' },
    { valor: 'inativo',    rotulo: 'Inativo' },
    { valor: 'manutencao', rotulo: 'Manutenção' }
];

const rotuloStatus = valor => (STATUS_TERMOMETRO.find(s => s.valor === valor) || {}).rotulo || valor;

// -19.4 -> "-19,4" (no Figma a temperatura desta tela aparece sem "°C")
const formatarLeitura = numero =>
    numero === null || numero === undefined ? '—' : String(numero).replace('.', ',');

/* ---------- dados relacionados ---------- */

const repositorioFrigorificos = DadosExemplo.repositorio('frigorificos');
let frigorificos = [];

const nomeDoFrigorifico = id => (frigorificos.find(f => f.id === id) || {}).nome || '—';

/* ---------- configuração do CRUD ---------- */

Crud.iniciar({
    entidade: {
        singular: 'termômetro',
        artigo: 'o',
        rotuloNovo: 'Cadastrar novo termômetro',
        rotuloEditar: 'Editar termômetro',
        rotuloSalvar: 'Salvar termômetro',
        rotuloSalvarEdicao: 'Salvar alterações',
        rotuloDeletar: 'Deletar termômetro'
    },

    // Troque por Crud.repositorioApi({ url: '...' }) quando o back-end responder em JSON
    repositorio: DadosExemplo.repositorio('termometros'),

    // carrega os frigoríficos para mostrar o nome na tabela e montar a lista do formulário
    aoCarregar: async () => { frigorificos = await repositorioFrigorificos.listar(); },

    janela: 'grande',
    tamanhoPagina: 20,
    ordenarPor: 'modelo',
    rotuloLinha: 'modelo',

    // mesmas colunas e larguras proporcionais do Figma
    colunas: [
        { chave: 'modelo',           rotulo: 'Modelo',            largura: 242.8 },
        { chave: 'frigorificoId',    rotulo: 'Localização',       largura: 285,   exibir: r => nomeDoFrigorifico(r.frigorificoId) },
        { chave: 'status',           rotulo: 'Status',            largura: 274.5, exibir: r => rotuloStatus(r.status) },
        { chave: 'temperaturaAtual', rotulo: 'Temperatura atual', largura: 355.5, exibir: r => formatarLeitura(r.temperaturaAtual) }
    ],

    campos: [
        { nome: 'modelo', rotulo: 'Modelo', tipo: 'texto', obrigatorio: true, maximo: 60 },

        // O Figma não mostra este campo, mas o banco exige o frigorífico (id_frigorifico NOT NULL)
        { nome: 'frigorificoId', rotulo: 'Frigorífico', tipo: 'lista', tamanho: 'metade', obrigatorio: true,
          placeholder: 'Escolha o frigorífico', mensagemObrigatorio: 'Escolha o frigorífico.',
          opcoes: () => frigorificos
              .slice()
              .sort((a, b) => a.nome.localeCompare(b.nome, 'pt-BR', { numeric: true }))
              .map(f => ({ valor: String(f.id), rotulo: f.nome })) },

        { nome: 'status', rotulo: 'Status', tipo: 'lista', tamanho: 'metade', obrigatorio: true,
          placeholder: 'Escolha o status', mensagemObrigatorio: 'Escolha o status.',
          opcoes: STATUS_TERMOMETRO }
    ],

    // A leitura atual não é digitada: continua como está (vem dos sensores)
    aoSalvar(valores, { registro }) {
        return {
            modelo: valores.modelo,
            frigorificoId: Number(valores.frigorificoId),
            status: valores.status,
            temperaturaAtual: registro ? registro.temperaturaAtual : null
        };
    }
});
