/* ============================================================
   SKADI · ENDEREÇOS DAS UNIDADES (versão JSP)
   Na tela de unidades (cds.js) o endereço já é cadastrado junto com a CD.
   Esta lista existe porque o servlet /endereco abre lista-endereco.jsp:
   mostra os endereços e permite corrigir algum diretamente.
   ============================================================ */

const Sessao = CrudServidor.Sessao;

let cds = [];
const nomeDaCd = id => (cds.find(cd => cd.id === id) || {}).nome || '';

Crud.iniciar({
    entidade: {
        singular: 'endereço',
        artigo: 'o',
        rotuloNovo: 'Cadastrar endereço',
        rotuloEditar: 'Editar endereço',
        rotuloSalvar: 'Salvar endereço',
        rotuloSalvarEdicao: 'Salvar alterações',
        rotuloDeletar: 'Deletar endereço'
    },

    repositorio: CrudServidor.repositorio({
        servlet: 'endereco',
        lista: 'enderecos',
        converter: item => ({
            id: Number(item.id),
            idCd: CrudServidor.numero(item.idCd),
            nomeCd: item.nomeCd,
            cep: Crud.Mascara.cep(item.cep),
            estado: item.estado,
            cidade: item.cidade,
            bairro: item.bairro,
            rua: item.rua,
            numero: CrudServidor.numero(item.numero),
            complemento: item.complemento
        }),
        parametros: dados => ({
            idCd: dados.idCd,
            cep: String(dados.cep).replace(/\D/g, ''),
            estado: dados.estado,
            cidade: dados.cidade,
            bairro: dados.bairro,
            rua: dados.rua,
            numero: dados.numero,
            complemento: dados.complemento
        }),
        mesmoRegistro: (registro, dados) =>
            registro.rua === dados.rua && registro.numero === dados.numero &&
            registro.cidade === dados.cidade && registro.cep === Crud.Mascara.cep(dados.cep)
    }),

    aoCarregar: async () => {
        const lista = await CrudServidor.buscar('endereco?acao=novo', 'cds');
        cds = lista.map(cd => ({ id: Number(cd.id), nome: cd.nome }));
    },

    filtro: registro => CrudServidor.daUnidade(registro.idCd),

    tamanhoPagina: 20,
    ordenarPor: 'cidade',
    rotuloLinha: 'rua',
    gradeColunas: 12,

    colunas: [
        { chave: 'cd',     rotulo: 'Unidade', largura: 200, exibir: r => r.nomeCd || nomeDaCd(r.idCd) || '—' },
        { chave: 'rua',    rotulo: 'Rua',     largura: 260, exibir: r => r.rua + (r.numero ? ', ' + r.numero : '') },
        { chave: 'bairro', rotulo: 'Bairro',  largura: 170 },
        { chave: 'cidade', rotulo: 'Cidade',  largura: 170, exibir: r => r.cidade + ' - ' + r.estado },
        { chave: 'cep',    rotulo: 'CEP',     largura: 120 }
    ],

    campos: [
        { nome: 'idCd', rotulo: 'Unidade (CD)', tipo: 'lista', obrigatorio: true,
          placeholder: 'Escolha a unidade', mensagemObrigatorio: 'Escolha a unidade.',
          opcoes: () => cds.map(cd => ({ valor: String(cd.id), rotulo: cd.nome })),
          mostrarSe: () => Sessao.ehSuperAdmin },
        { nome: 'cep', rotulo: 'CEP', tipo: 'cep', tamanho: 'metade', obrigatorio: true },
        { nome: 'estado', rotulo: 'Estado (sigla)', tipo: 'uf', tamanho: 'metade', obrigatorio: true },
        { nome: 'cidade', rotulo: 'Cidade', tipo: 'texto', obrigatorio: true, maximo: 60 },
        { nome: 'bairro', rotulo: 'Bairro', tipo: 'texto', obrigatorio: true, maximo: 60 },
        { nome: 'rua', rotulo: 'Rua', tipo: 'texto', colunas: 9, obrigatorio: true, maximo: 80 },
        { nome: 'numero', rotulo: 'Número', tipo: 'numero', casas: 0, valorMinimo: 1, mensagemMinimo: 'Informe o número do endereço.', colunas: 3, obrigatorio: true, maximo: 8 },
        { nome: 'complemento', rotulo: 'Complemento', tipo: 'texto', maximo: 60 }
    ],

    valoresPadrao: () => {
        const unidade = CrudServidor.unidadeAtual();
        return { idCd: unidade && unidade.id ? String(unidade.id) : '' };
    }
});

CrudServidor.prepararPagina();
