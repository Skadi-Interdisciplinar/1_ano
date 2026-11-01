/* ============================================================
   SKADI · CRUD de FRIGORÍFICOS (versão JSP)
   Só configuração: a tela está em crud.js e a ligação com o
   servlet /frigorifico em crudServidor.js.

   Tabela do banco: Frigorifico (id_frigorifico, nome, modelo, localizacao, id_cd,
                    temperatura_max, temperatura_min)  CHECK: max > min
   O banco exige o modelo e a faixa de temperatura, por isso eles entram no formulário.
   "Temperatura atual" virá das leituras dos sensores (ainda sem tela no back-end).
   ============================================================ */

const Sessao = CrudServidor.Sessao;

/* ---------- formatação ---------- */

const formatarNumero = numero => String(numero).replace('.', ',');

const formatarTemperatura = numero =>
    numero === null || numero === undefined ? '—' : formatarNumero(numero) + '°C';

// No Figma a faixa aparece do mais quente para o mais frio: "-18°C a -20°C"
const formatarFaixa = (minima, maxima) =>
    minima === null || minima === undefined || maxima === null || maxima === undefined
        ? '—'
        : formatarTemperatura(maxima) + ' a ' + formatarTemperatura(minima);

/* ---------- dados relacionados ---------- */

let cds = [];
const nomeDaCd = id => (cds.find(cd => cd.id === id) || {}).nome || '';

/* ---------- configuração do CRUD ---------- */

Crud.iniciar({
    entidade: {
        singular: 'frigorífico',
        artigo: 'o',
        rotuloNovo: 'Adicionar frigorífico',
        rotuloEditar: 'Editar frigorífico',
        rotuloSalvar: 'Salvar frigorífico',
        rotuloSalvarEdicao: 'Salvar alterações',
        rotuloDeletar: 'Deletar frigorífico'
    },

    repositorio: CrudServidor.repositorio({
        servlet: 'frigorifico',
        lista: 'frigorificos',
        converter: item => ({
            id: Number(item.id),
            nome: item.nome,
            modelo: item.modelo,
            localizacao: item.localizacao,
            idCd: CrudServidor.numero(item.idCd),
            temperaturaAtual: null,
            temperaturaMinima: CrudServidor.numero(item.temperaturaMin),
            temperaturaMaxima: CrudServidor.numero(item.temperaturaMax)
        }),
        parametros: dados => ({
            nome: dados.nome,
            modelo: dados.modelo,
            localizacao: dados.localizacao,
            idCd: dados.idCd,
            temperaturaMin: dados.temperaturaMinima,
            temperaturaMax: dados.temperaturaMaxima
        }),
        mesmoRegistro: (registro, dados) =>
            registro.nome === dados.nome && registro.modelo === dados.modelo && registro.localizacao === dados.localizacao &&
            registro.temperaturaMinima === dados.temperaturaMinima && registro.temperaturaMaxima === dados.temperaturaMaxima
    }),

    // a lista de unidades só aparece para o super_admin
    aoCarregar: async () => {
        if (!Sessao.ehSuperAdmin) return;
        const lista = await CrudServidor.buscar('frigorifico?acao=novo', 'cds');
        cds = lista.map(cd => ({ id: Number(cd.id), nome: cd.nome }));
    },

    filtro: registro => CrudServidor.daUnidade(registro.idCd),

    janela: 'grande',          // janela de 910 px do Figma
    tamanhoPagina: 20,
    ordenarPor: 'nome',

    // mesmas colunas e larguras proporcionais do Figma
    colunas: [
        { chave: 'nome',        rotulo: 'Frigorífico',        largura: 242.8 },
        { chave: 'localizacao', rotulo: 'Localização',        largura: 285 },
        { chave: 'temperaturaAtual', rotulo: 'Temperatura atual', largura: 274.5,
          exibir: r => formatarTemperatura(r.temperaturaAtual) },
        { chave: 'temperaturaIdeal', rotulo: 'Temperatura ideal', largura: 355.5,
          exibir: r => formatarFaixa(r.temperaturaMinima, r.temperaturaMaxima) }
    ],

    // a pesquisa usa o que aparece na tabela
    pesquisaEm: r => [
        r.nome,
        r.modelo,
        r.localizacao,
        nomeDaCd(r.idCd),
        formatarFaixa(r.temperaturaMinima, r.temperaturaMaxima)
    ].join(' '),

    // campos do formulário, na ordem do Figma + modelo, faixa de temperatura e unidade (exigidos pelo banco)
    campos: [
        { nome: 'nome',        rotulo: 'Nome do frigorífico', tipo: 'texto', obrigatorio: true, maximo: 80,
          placeholder: 'Digite um nome autodescritivo',
          unico: true, mensagemUnico: 'Já existe um frigorífico com este nome.' },
        { nome: 'modelo',      rotulo: 'Modelo',              tipo: 'texto', obrigatorio: true, maximo: 60,
          placeholder: 'Ex.: FR-2000' },
        { nome: 'localizacao', rotulo: 'Localização',         tipo: 'texto', obrigatorio: true, maximo: 80,
          placeholder: 'Ex.: Setor D - Corredor 4' },
        { nome: 'temperaturaMinima', rotulo: 'Temperatura mínima (°C)', tipo: 'numero', tamanho: 'metade', obrigatorio: true,
          casas: 1, valorMinimo: -50, valorMaximo: 50, placeholder: 'Ex.: -20',
          mensagemMinimo: 'Use valores entre -50 e 50.', mensagemMaximo: 'Use valores entre -50 e 50.' },
        { nome: 'temperaturaMaxima', rotulo: 'Temperatura máxima (°C)', tipo: 'numero', tamanho: 'metade', obrigatorio: true,
          casas: 1, valorMinimo: -50, valorMaximo: 50, placeholder: 'Ex.: -15',
          mensagemMinimo: 'Use valores entre -50 e 50.', mensagemMaximo: 'Use valores entre -50 e 50.' },
        { nome: 'idCd', rotulo: 'Unidade (CD)', tipo: 'lista', obrigatorio: true,
          placeholder: 'Escolha a unidade', mensagemObrigatorio: 'Escolha a unidade.',
          opcoes: () => cds.map(cd => ({ valor: String(cd.id), rotulo: cd.nome })),
          // o admin sempre cadastra na própria CD
          mostrarSe: () => Sessao.ehSuperAdmin }
    ],

    valoresPadrao: () => {
        const unidade = CrudServidor.unidadeAtual();
        return { idCd: unidade && unidade.id ? String(unidade.id) : '' };
    },

    // regra do banco: temperatura_max > temperatura_min
    validarExtra(valores) {
        const { temperaturaMinima: minima, temperaturaMaxima: maxima } = valores;
        if (minima !== null && maxima !== null && maxima <= minima) {
            return { temperaturaMaxima: 'A máxima precisa ser maior que a mínima.' };
        }
        return {};
    }
});

CrudServidor.prepararPagina();
