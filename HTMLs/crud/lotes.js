/* ============================================================
   SKADI · CRUD de LOTES
   Só configuração: o comportamento está em crud.js.

   Tabela do banco:
     Lote (id_lote, codigo_lote UNIQUE, id_categoria, data_fabricacao,
           data_validade, status, data_atualizacao)
     status aceito: 'ativo' | 'bloqueado' | 'expedido' | 'vencido'
     CHECK: data_validade >= data_fabricacao
   ============================================================ */

const STATUS_LOTE = [
    { valor: 'ativo',     rotulo: 'Ativo' },
    { valor: 'bloqueado', rotulo: 'Bloqueado' },
    { valor: 'expedido',  rotulo: 'Expedido' },
    { valor: 'vencido',   rotulo: 'Vencido' }
];

const rotuloStatusLote = valor => (STATUS_LOTE.find(s => s.valor === valor) || {}).rotulo || valor;

/* ---------- dados relacionados ---------- */

const repositorioCategorias = DadosExemplo.repositorio('categorias');
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

    // Troque por Crud.repositorioApi({ url: '...' }) quando o back-end responder em JSON
    repositorio: DadosExemplo.repositorio('lotes'),

    aoCarregar: async () => { categorias = await repositorioCategorias.listar(); },

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
