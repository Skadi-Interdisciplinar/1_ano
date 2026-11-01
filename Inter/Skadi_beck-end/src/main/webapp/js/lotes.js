/* ============================================================
   SKADI · CRUD de LOTES (versão JSP)
   Só configuração: a tela está em crud.js e a ligação com o
   servlet /lote em crudServidor.js.

   Tabela do banco:
     Lote (id_lote, codigo_lote UNIQUE, id_categoria, data_fabricacao,
           data_validade, status, id_cd, data_atualizacao)
     status aceito: 'ATIVO' | 'BLOQUEADO' | 'EXPEDIDO' | 'VENDIDO'
     CHECK: data_validade >= data_fabricacao
   O lote novo do admin fica na CD dele (o back-end usa a sessão).
   ============================================================ */

const STATUS_LOTE = [
    { valor: 'ATIVO',     rotulo: 'Ativo' },
    { valor: 'BLOQUEADO', rotulo: 'Bloqueado' },
    { valor: 'EXPEDIDO',  rotulo: 'Expedido' },
    { valor: 'VENDIDO',   rotulo: 'Vendido' }
];

const rotuloStatusLote = valor => (STATUS_LOTE.find(s => s.valor === valor) || {}).rotulo || valor;

/* ---------- dados relacionados ---------- */

let categorias = [];

const nomeDaCategoria = id => (categorias.find(c => c.id === id) || {}).nome || '';

/* ---------- configuração do CRUD ---------- */

Crud.iniciar({
    entidade: {
        singular: 'lote',
        artigo: 'o',
        rotuloNovo: 'Cadastrar lote',
        rotuloEditar: 'Editar lote',
        rotuloSalvar: 'Salvar lote',
        rotuloSalvarEdicao: 'Salvar alterações',
        rotuloDeletar: 'Deletar lote'
    },

    repositorio: CrudServidor.repositorio({
        servlet: 'lote',
        lista: 'lotes',
        converter: item => ({
            id: Number(item.id),
            codigo: item.codigo,
            categoriaId: Number(item.idCategoria),
            dataFabricacao: item.dataFabricacao,
            dataValidade: item.dataValidade,
            status: item.status,
            idCd: CrudServidor.numero(item.idCd)
        }),
        parametros: dados => ({
            codigoLote: dados.codigo,
            idCategoria: dados.categoriaId,
            dataFabricacao: dados.dataFabricacao,
            dataValidade: dados.dataValidade,
            status: dados.status
        }),
        mesmoRegistro: (registro, dados) =>
            registro.codigo === dados.codigo && registro.categoriaId === Number(dados.categoriaId) &&
            registro.dataFabricacao === dados.dataFabricacao && registro.dataValidade === dados.dataValidade &&
            registro.status === dados.status
    }),

    aoCarregar: async () => {
        const itens = await CrudServidor.buscar('lote?acao=novo', 'categorias');
        categorias = itens.map(c => ({ id: Number(c.id), nome: c.nome }));
    },

    // super_admin: só os lotes da unidade escolhida (lotes sem unidade aparecem em todas)
    filtro: registro => CrudServidor.daUnidade(registro.idCd),

    janela: 'grande',
    tamanhoPagina: 20,
    ordenarPor: 'codigo',
    rotuloLinha: 'codigo',

    // mesmas colunas e larguras proporcionais do Figma
    colunas: [
        { chave: 'codigo',         rotulo: 'Código do lote',     largura: 322.8 },
        { chave: 'dataFabricacao', rotulo: 'Data de fabricação', largura: 248,   exibir: r => Crud.Formato.isoParaData(r.dataFabricacao) },
        { chave: 'dataValidade',   rotulo: 'Data de validade',   largura: 275,   exibir: r => Crud.Formato.isoParaData(r.dataValidade) },
        { chave: 'status',         rotulo: 'Status',             largura: 312,   exibir: r => rotuloStatusLote(r.status) }
    ],

    // a pesquisa também encontra pelo nome da categoria
    pesquisaEm: r => [
        r.codigo,
        nomeDaCategoria(r.categoriaId),
        Crud.Formato.isoParaData(r.dataFabricacao),
        Crud.Formato.isoParaData(r.dataValidade),
        rotuloStatusLote(r.status)
    ].join(' '),

    // campos na ordem do Figma
    campos: [
        { nome: 'codigo', rotulo: 'Código do lote', tipo: 'texto', obrigatorio: true, maximo: 50,
          unico: true, mensagemUnico: 'Já existe um lote com este código.' },
        { nome: 'categoriaId', rotulo: 'Categoria', tipo: 'lista', tamanho: 'metade', obrigatorio: true,
          placeholder: 'Escolha a categoria', mensagemObrigatorio: 'Escolha a categoria.',
          opcoes: () => categorias
              .slice()
              .sort((a, b) => a.nome.localeCompare(b.nome, 'pt-BR', { numeric: true }))
              .map(c => ({ valor: String(c.id), rotulo: c.nome })) },
        { nome: 'status', rotulo: 'Status', tipo: 'lista', tamanho: 'metade', obrigatorio: true,
          placeholder: 'Escolha o status', mensagemObrigatorio: 'Escolha o status.',
          opcoes: STATUS_LOTE },
        { nome: 'dataFabricacao', rotulo: 'Data de fabricação', tipo: 'data', tamanho: 'metade', obrigatorio: true,
          placeholder: 'dd/mm/aaaa', maximo: 10 },
        { nome: 'dataValidade', rotulo: 'Data de validade', tipo: 'data', tamanho: 'metade', obrigatorio: true,
          placeholder: 'dd/mm/aaaa', maximo: 10 }
    ],

    // regra do banco: data_validade >= data_fabricacao
    validarExtra(valores) {
        const { dataFabricacao, dataValidade } = valores;
        if (dataFabricacao && dataValidade && dataValidade < dataFabricacao) {
            return { dataValidade: 'A validade não pode ser antes da fabricação.' };
        }
        return {};
    },

    aoSalvar(valores) {
        return { ...valores, categoriaId: Number(valores.categoriaId) };
    }
});

CrudServidor.prepararPagina();
