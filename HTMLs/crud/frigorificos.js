/* ============================================================
   SKADI · CRUD de FRIGORÍFICOS
   Só configuração: o comportamento está em crud.js.

   Tabela do banco:  Frigorifico (id_frigorifico, nome, localizacao, id_cd)
   Colunas calculadas (somente leitura, vêm do back-end):
     - Temperatura atual: última LeituraTemperatura dos termômetros do frigorífico
     - Temperatura ideal: faixa da Categoria dos lotes guardados nele
   ============================================================ */

/* ---------- formatação ---------- */

const formatarNumero = numero => String(numero).replace('.', ',');

const formatarTemperatura = numero =>
    numero === null || numero === undefined ? '—' : formatarNumero(numero) + '°C';

// No Figma a faixa aparece do mais quente para o mais frio: "-18°C a -20°C"
const formatarFaixa = (minima, maxima) =>
    minima === null || minima === undefined || maxima === null || maxima === undefined
        ? '—'
        : formatarTemperatura(maxima) + ' a ' + formatarTemperatura(minima);

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

    // Troque por Crud.repositorioApi({ url: '...' }) quando o back-end responder em JSON
    repositorio: DadosExemplo.repositorio('frigorificos'),

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
        r.localizacao,
        formatarTemperatura(r.temperaturaAtual),
        formatarFaixa(r.temperaturaMinima, r.temperaturaMaxima)
    ].join(' '),

    // campos do formulário, na ordem do Figma (colunas do banco: nome e localizacao, ambos VARCHAR(80))
    campos: [
        { nome: 'nome',        rotulo: 'Nome do frigorífico', tipo: 'texto', obrigatorio: true, maximo: 80,
          placeholder: 'Digite um nome autodescritivo',
          unico: true, mensagemUnico: 'Já existe um frigorífico com este nome.' },
        { nome: 'localizacao', rotulo: 'Localização',         tipo: 'texto', obrigatorio: true, maximo: 80,
          placeholder: 'Ex.: Setor D - Corredor 4' }
    ],

    // Temperatura atual e ideal não são digitadas aqui: continuam como estão
    // (no banco elas vêm das leituras dos termômetros e da categoria dos lotes).
    aoSalvar(valores, { registro }) {
        return {
            ...valores,
            temperaturaAtual: registro ? registro.temperaturaAtual : null,
            temperaturaMinima: registro ? registro.temperaturaMinima : null,
            temperaturaMaxima: registro ? registro.temperaturaMaxima : null
        };
    }
});
