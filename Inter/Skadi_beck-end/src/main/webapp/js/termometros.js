/* ============================================================
   SKADI · CRUD de TERMÔMETROS (versão JSP)
   Só configuração: a tela está em crud.js e a ligação com o
   servlet /termometro em crudServidor.js.

   Tabela do banco:
     Termometro (id_termometro, modelo, status, id_frigorifico, data_atualizacao)
     status aceito: 'ATIVO' | 'INATIVO' | 'MANUTENCAO'
   "Temperatura atual" virá das leituras dos sensores (ainda sem tela no back-end).
   ============================================================ */

const STATUS_TERMOMETRO = [
    { valor: 'ATIVO',      rotulo: 'Ativo' },
    { valor: 'INATIVO',    rotulo: 'Inativo' },
    { valor: 'MANUTENCAO', rotulo: 'Manutenção' }
];

const rotuloStatus = valor => (STATUS_TERMOMETRO.find(s => s.valor === valor) || {}).rotulo || valor;

// -19.4 -> "-19,4" (no Figma a temperatura desta tela aparece sem "°C")
const formatarLeitura = numero =>
    numero === null || numero === undefined ? '—' : String(numero).replace('.', ',');

/* ---------- dados relacionados ---------- */

// O servlet já manda os frigoríficos que o usuário pode ver junto com a lista
let frigorificos = [];

function lerFrigorificos(itens) {
    frigorificos = itens.map(f => ({ id: Number(f.id), nome: f.nome, idCd: CrudServidor.numero(f.idCd) }));
}

const frigorificoDe = id => frigorificos.find(f => f.id === id) || {};
const nomeDoFrigorifico = id => frigorificoDe(id).nome || '—';

/* ---------- configuração do CRUD ---------- */

const repositorio = CrudServidor.repositorio({
    servlet: 'termometro',
    lista: 'termometros',
    converter: item => ({
        id: Number(item.id),
        modelo: item.modelo,
        frigorificoId: Number(item.idFrigorifico),
        status: item.status,
        temperaturaAtual: null
    }),
    parametros: dados => ({
        modelo: dados.modelo,
        idFrigorifico: dados.frigorificoId,
        status: dados.status
    }),
    mesmoRegistro: (registro, dados) =>
        registro.modelo === dados.modelo && registro.status === dados.status &&
        registro.frigorificoId === Number(dados.frigorificoId)
});

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

    repositorio,

    // frigoríficos: na 1ª vez vêm da página; depois, da lista recarregada do servidor
    aoCarregar: async () => {
        if (!frigorificos.length) {
            lerFrigorificos(Array.from(document.querySelectorAll('[data-registros="frigorificos"] > i')).map(i => Object.assign({}, i.dataset)));
        } else {
            lerFrigorificos(await CrudServidor.buscar('termometro?acao=novo', 'frigorificos'));
        }
    },

    // super_admin: só os termômetros dos frigoríficos da unidade escolhida
    filtro: registro => CrudServidor.daUnidade(frigorificoDe(registro.frigorificoId).idCd),

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

    pesquisaEm: r => [r.modelo, nomeDoFrigorifico(r.frigorificoId), rotuloStatus(r.status)].join(' '),

    campos: [
        { nome: 'modelo', rotulo: 'Modelo', tipo: 'texto', obrigatorio: true, maximo: 60 },
        // O Figma não mostra este campo, mas o banco exige o frigorífico (id_frigorifico NOT NULL)
        { nome: 'frigorificoId', rotulo: 'Frigorífico', tipo: 'lista', tamanho: 'metade', obrigatorio: true,
          placeholder: 'Escolha o frigorífico', mensagemObrigatorio: 'Escolha o frigorífico.',
          opcoes: () => frigorificos
              .filter(f => CrudServidor.daUnidade(f.idCd))
              .sort((a, b) => a.nome.localeCompare(b.nome, 'pt-BR', { numeric: true }))
              .map(f => ({ valor: String(f.id), rotulo: f.nome })) },
        { nome: 'status', rotulo: 'Status', tipo: 'lista', tamanho: 'metade', obrigatorio: true,
          placeholder: 'Escolha o status', mensagemObrigatorio: 'Escolha o status.',
          opcoes: STATUS_TERMOMETRO }
    ],

    aoSalvar(valores) {
        return {
            modelo: valores.modelo,
            frigorificoId: Number(valores.frigorificoId),
            status: valores.status
        };
    }
});

CrudServidor.prepararPagina();
