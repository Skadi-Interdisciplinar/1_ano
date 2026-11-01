/* ============================================================
   SKADI · CRUD de CATEGORIAS (versão JSP)
   Só configuração: a tela está em crud.js e a ligação com o
   servlet /categoria em crudServidor.js.

   Tabela do banco:
     Categoria (id_categoria, nome UNIQUE, vida_util_horas > 0,
                temperatura_min, temperatura_max, data_atualizacao)
     CHECK: temperatura_max > temperatura_min
   O catálogo é o mesmo para todas as unidades: só o super_admin cadastra,
   edita e exclui; o admin apenas consulta.
   "Total de lotes" conta os lotes que o usuário pode ver.
   ============================================================ */

const Sessao = CrudServidor.Sessao;

const formatarGrau = numero => String(numero).replace('.', ',') + '°C';

// No Figma a faixa aparece do mais quente para o mais frio: "-18°C a -20°C"
const formatarFaixa = (minima, maxima) =>
    minima === null || maxima === null ? '—' : formatarGrau(maxima) + ' a ' + formatarGrau(minima);

// No banco a vida útil é um INTERVAL ("06:30:00", "2 days 04:00:00"); a tela mostra em horas.
function intervaloParaHoras(intervalo) {
    const texto = String(intervalo || '');
    const dias = /(-?\d+)\s+days?/.exec(texto);
    const hora = /(-?\d+):(\d+):(\d+(?:\.\d+)?)/.exec(texto);
    let horas = (dias ? Number(dias[1]) * 24 : 0);
    if (hora) horas += Number(hora[1]) + Number(hora[2]) / 60 + Number(hora[3]) / 3600;
    return !dias && !hora ? null : Math.round(horas * 100) / 100;
}

/* ---------- dados relacionados ---------- */

let lotes = [];
const totalDeLotes = categoria => lotes.filter(l => l.categoriaId === categoria.id).length;

/* ---------- configuração do CRUD ---------- */

Crud.iniciar({
    entidade: {
        singular: 'categoria',
        artigo: 'a',
        rotuloNovo: 'Cadastrar categoria',
        rotuloEditar: 'Editar categoria',
        rotuloSalvar: 'Salvar categoria',
        rotuloSalvarEdicao: 'Salvar alterações',
        rotuloDeletar: 'Deletar categoria'
    },

    repositorio: CrudServidor.repositorio({
        servlet: 'categoria',
        lista: 'categorias',
        converter: item => ({
            id: Number(item.id),
            nome: item.nome,
            vidaUtilHoras: intervaloParaHoras(item.vidaUtilHoras),
            vidaUtilIntervalo: item.vidaUtilHoras,
            temperaturaMinima: CrudServidor.numero(item.temperaturaMin),
            temperaturaMaxima: CrudServidor.numero(item.temperaturaMax)
        }),
        parametros: dados => ({
            nome: dados.nome,
            // sem mudança na vida útil, devolve o intervalo original (evita arredondar)
            vidaUtilHoras: dados.vidaUtilIntervalo || dados.vidaUtilHoras + ' hours',
            temperaturaMin: dados.temperaturaMinima,
            temperaturaMax: dados.temperaturaMaxima
        }),
        mesmoRegistro: (registro, dados) =>
            registro.nome === dados.nome && registro.vidaUtilHoras === dados.vidaUtilHoras &&
            registro.temperaturaMinima === dados.temperaturaMinima && registro.temperaturaMaxima === dados.temperaturaMaxima
    }),

    aoCarregar: async () => {
        const itens = await CrudServidor.buscar('lote?acao=listar', 'lotes');
        lotes = itens.map(l => ({ id: Number(l.id), categoriaId: Number(l.idCategoria) }));
    },

    // admin só consulta o catálogo
    somenteLeitura: !Sessao.ehSuperAdmin,

    janela: 'grande',
    tamanhoPagina: 20,
    ordenarPor: 'nome',

    // mesmas colunas e larguras proporcionais do Figma
    colunas: [
        { chave: 'nome',          rotulo: 'Categoria',          largura: 322.8 },
        { chave: 'vidaUtilHoras', rotulo: 'Vida útil em horas', largura: 248,   exibir: r => Crud.Formato.numeroBr(r.vidaUtilHoras) },
        { chave: 'temperaturaIdeal', rotulo: 'Temperatura ideal', largura: 275, exibir: r => formatarFaixa(r.temperaturaMinima, r.temperaturaMaxima) },
        { chave: 'totalLotes',    rotulo: 'Total de lotes',     largura: 312,   exibir: r => totalDeLotes(r) }
    ],

    pesquisaEm: r => [r.nome, Crud.Formato.numeroBr(r.vidaUtilHoras), formatarFaixa(r.temperaturaMinima, r.temperaturaMaxima)].join(' '),

    // campos na ordem do Figma (2 colunas)
    campos: [
        { nome: 'nome', rotulo: 'Nome da categoria', tipo: 'texto', tamanho: 'metade', obrigatorio: true, maximo: 150,
          placeholder: 'Digite um nome autodescritivo',
          unico: true, mensagemUnico: 'Já existe uma categoria com este nome.' },
        { nome: 'vidaUtilHoras', rotulo: 'Vida útil em horas', tipo: 'numero', tamanho: 'metade', obrigatorio: true,
          casas: 2, valorMinimo: 0.01, valorMaximo: 99999.99,
          mensagemMinimo: 'A vida útil precisa ser maior que zero.' },
        { nome: 'temperaturaMinima', rotulo: 'Temperatura mínima', tipo: 'numero', tamanho: 'metade', obrigatorio: true,
          casas: 2, valorMinimo: -50, valorMaximo: 50, mensagemMinimo: 'Use valores entre -50 e 50.', mensagemMaximo: 'Use valores entre -50 e 50.' },
        { nome: 'temperaturaMaxima', rotulo: 'Temperatura máxima', tipo: 'numero', tamanho: 'metade', obrigatorio: true,
          casas: 2, valorMinimo: -50, valorMaximo: 50, mensagemMinimo: 'Use valores entre -50 e 50.', mensagemMaximo: 'Use valores entre -50 e 50.' }
    ],

    // regra do banco: temperatura_max > temperatura_min
    validarExtra(valores) {
        const { temperaturaMinima: minima, temperaturaMaxima: maxima } = valores;
        if (minima !== null && maxima !== null && maxima <= minima) {
            return { temperaturaMaxima: 'A máxima precisa ser maior que a mínima.' };
        }
        return {};
    },

    aoSalvar(valores, { registro }) {
        const mesmaVidaUtil = registro && registro.vidaUtilHoras === valores.vidaUtilHoras;
        return Object.assign({}, valores, { vidaUtilIntervalo: mesmaVidaUtil ? registro.vidaUtilIntervalo : '' });
    },

    // regra do banco: lote tem FK com ON DELETE RESTRICT para a categoria
    validarExclusao(registro) {
        const total = totalDeLotes(registro);
        if (total > 0) {
            return 'Não dá para deletar: ' + total + (total === 1 ? ' lote usa' : ' lotes usam') + ' esta categoria.';
        }
        return '';
    }
});

CrudServidor.prepararPagina();
