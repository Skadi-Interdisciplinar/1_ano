/* ============================================================
   SKADI · CRUD de CATEGORIAS
   Só configuração: o comportamento está em crud.js.

   Tabela do banco:
     Categoria (id_categoria, nome UNIQUE, vida_util_horas > 0,
                temperatura_min, temperatura_max, data_atualizacao)
     CHECK: temperatura_max > temperatura_min
   "Total de lotes" é calculado (quantidade de lotes da categoria).
   ============================================================ */

const formatarGrau = numero => String(numero).replace('.', ',') + '°C';

// No Figma a faixa aparece do mais quente para o mais frio: "-18°C a -20°C"
const formatarFaixa = (minima, maxima) => formatarGrau(maxima) + ' a ' + formatarGrau(minima);

/* ---------- dados relacionados ---------- */

const repositorioLotes = DadosExemplo.repositorio('lotes');
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

    // Troque por Crud.repositorioApi({ url: '...' }) quando o back-end responder em JSON
    repositorio: DadosExemplo.repositorio('categorias'),

    aoCarregar: async () => { lotes = await repositorioLotes.listar(); },

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

    // regra do banco: lote tem FK com ON DELETE RESTRICT para a categoria
    validarExclusao(registro) {
        const total = totalDeLotes(registro);
        if (total > 0) {
            return 'Não dá para deletar: ' + total + (total === 1 ? ' lote usa' : ' lotes usam') + ' esta categoria.';
        }
        return '';
    }
});
