/* ============================================================
   SKADI · CDs (unidades) — versão JSP
   O formulário (cadastrar/editar), a validação e a exclusão vêm do crud.js.
   Aqui ficam a configuração, o desenho dos cartões e a ligação com os servlets.

   Fluxo do protótipo (Figma · "crud adm cliente cds"):
     - clicar no cartão            -> abre a unidade (funcionários)
     - botão +                     -> janela "Cadastrar nova unidade"
     - lápis                       -> modo edição ("Qual unidade você gostaria de editar?")
         clicar no cartão          -> janela "Editar unidade"
         "Cancelar edição"         -> volta para a lista
     - sem nenhuma unidade         -> "Ops! Parece que você não tem nenhuma unidade cadastrada ainda."

   Back-end:
     - CD (nome, cnpj) pelo servlet /cd e Endereco pelo servlet /endereco (um endereço por unidade);
     - admin: só a própria CD; cadastra a primeira no 1º acesso e não exclui;
     - super_admin: todas as unidades.
   ============================================================ */

const Sessao = CrudServidor.Sessao;

const PAGINA_DA_UNIDADE = (window.SKADI_CONTEXTO || '') + '/usuario?acao=listar';

const raiz = document.querySelector('[data-crud]');
const listaEl = raiz.querySelector('[data-cds-lista]');
const vazioEl = raiz.querySelector('[data-cds-vazio]');
const perguntaEl = raiz.querySelector('[data-cds-pergunta]');
const botaoEditar = raiz.querySelector('[data-cds-editar]');
const botaoCancelar = raiz.querySelector('[data-cds-cancelar]');
const primeiroAcesso = document.body.dataset.primeiroAcesso === 'true';

let editando = false;
let crud = null;

/* ---------- modo edição ---------- */

function definirEdicao(ativa) {
    editando = ativa;
    document.body.classList.toggle('cds--editando', ativa);
    botaoEditar.setAttribute('aria-pressed', String(ativa));
    botaoCancelar.hidden = !ativa;
    perguntaEl.textContent = ativa
        ? 'Qual unidade você gostaria de editar?'
        : 'Qual unidade você gostaria de acessar?';
    crud.renderizar();
    // o lápis some no modo edição: o foco vai para o botão que aparece no lugar
    (ativa ? botaoCancelar : botaoEditar).focus();
}

botaoEditar.addEventListener('click', () => definirEdicao(true));
botaoCancelar.addEventListener('click', () => definirEdicao(false));

// Esc sai do modo edição (se não houver janela aberta)
document.addEventListener('keydown', evento => {
    if (evento.key === 'Escape' && editando && !document.querySelector('.janela__fundo')) definirEdicao(false);
});

/* ---------- cartões ---------- */

const escapar = Crud.escapar;

const localizacao = u => (u.cidade && u.estado ? u.cidade + ' - ' + u.estado : 'Endereço não cadastrado');
const fracao = (ativos, total) => (ativos ?? 0) + '/' + (total ?? 0);

function cartao(unidade) {
    const acao = (editando ? 'Editar unidade ' : 'Acessar unidade ') + unidade.nome;
    return '<li>' +
        '<button type="button" class="unidade" data-id="' + unidade.id + '" aria-label="' + escapar(acao) + '">' +
            '<span class="unidade__nome">' + escapar(unidade.nome) + '</span>' +
            '<span class="unidade__info">' +
                '<span>CNPJ: ' + escapar(unidade.cnpj) + '</span>' +
                '<span>Localização: ' + escapar(localizacao(unidade)) + '</span>' +
                '<span>Frigoríficos ativos: ' + fracao(unidade.frigorificosAtivos, unidade.frigorificosTotal) + '</span>' +
                '<span>Funcionários: ' + (unidade.funcionariosTotal ?? 0) + '</span>' +
            '</span>' +
        '</button>' +
    '</li>';
}

function desenharLista({ lista, estado }) {
    // mantém o nome do cabeçalho das outras telas em dia
    const atual = Crud.Unidade.obter();
    if (atual) {
        const existente = estado.registros.find(r => r.id === atual.id);
        if (!existente) Crud.Unidade.limpar();
        else if (existente.nome !== atual.nome) Crud.Unidade.definir(existente);
    }

    // admin com a CD já cadastrada não cadastra outra
    const botaoAdicionar = raiz.querySelector('[data-crud-adicionar]');
    if (botaoAdicionar && Sessao.ehAdmin) botaoAdicionar.hidden = estado.registros.length > 0;

    if (!lista.length) {
        listaEl.innerHTML = '';
        vazioEl.textContent = estado.registros.length
            ? 'Nenhuma unidade encontrada para “' + estado.consulta + '”.'
            : 'Ops! Parece que você não tem nenhuma unidade cadastrada ainda.';
        vazioEl.hidden = false;
        botaoEditar.hidden = !estado.registros.length;
        return;
    }

    vazioEl.hidden = true;
    botaoEditar.hidden = false;
    listaEl.innerHTML = lista.map(cartao).join('');
}

listaEl.addEventListener('click', evento => {
    const botao = evento.target.closest('.unidade');
    if (!botao) return;
    const unidade = crud.estado.registros.find(r => r.id === Number(botao.dataset.id));
    if (!unidade) return;

    if (editando) {
        crud.abrirFormulario('editar', unidade);
    } else {
        // as outras telas mostram (e o super_admin filtra) a unidade escolhida
        Crud.Unidade.definir(unidade);
        window.location.href = PAGINA_DA_UNIDADE;
    }
});

/* ---------- ligação com os servlets /cd e /endereco ---------- */

const somenteDigitos = valor => String(valor ?? '').replace(/\D/g, '');

// CD + endereço + contagens montam um cartão
async function carregarUnidades(cdsItens) {
    const [enderecos, frigorificos, ativos, usuarios] = await Promise.all([
        CrudServidor.buscar('endereco?acao=listar', 'enderecos'),
        CrudServidor.buscar('frigorifico?acao=listar', 'frigorificos'),
        CrudServidor.buscar('frigorifico?acao=listarAtivos', 'frigorificos'),
        CrudServidor.buscar('usuario?acao=listar', 'usuarios')
    ].map(promessa => promessa.catch(() => [])));

    const contar = (lista, idCd) => lista.filter(item => Number(item.idCd) === idCd).length;

    return cdsItens.map(item => {
        const id = Number(item.id);
        const endereco = enderecos.find(e => Number(e.idCd) === id) || {};
        return {
            id,
            nome: item.nome,
            cnpj: Crud.Mascara.cnpj(item.cnpj),
            enderecoId: endereco.id ? Number(endereco.id) : null,
            cep: endereco.cep ? Crud.Mascara.cep(endereco.cep) : '',
            estado: endereco.estado || '',
            cidade: endereco.cidade || '',
            bairro: endereco.bairro || '',
            rua: endereco.rua || '',
            numero: CrudServidor.numero(endereco.numero),
            complemento: endereco.complemento || '',
            frigorificosAtivos: contar(ativos, id),
            frigorificosTotal: contar(frigorificos, id),
            funcionariosTotal: contar(usuarios, id)
        };
    });
}

const repositorioUnidades = (() => {
    let cache = [];
    let primeiraVez = true;

    async function recarregar() {
        let itens;
        if (primeiraVez) {
            itens = Array.from(document.querySelectorAll('[data-registros="cds"] > i')).map(i => Object.assign({}, i.dataset));
            primeiraVez = false;
        } else {
            itens = await CrudServidor.buscar('cd?acao=listar', 'cds');
        }
        cache = await carregarUnidades(itens);
        return cache;
    }

    // endereço da unidade: cria ou atualiza
    async function salvarEndereco(idCd, enderecoId, dados) {
        await CrudServidor.enviar('endereco', {
            acao: 'salvar',
            id: enderecoId || '',
            idCd,
            cep: somenteDigitos(dados.cep),
            estado: dados.estado,
            cidade: dados.cidade,
            bairro: dados.bairro,
            rua: dados.rua,
            numero: dados.numero,
            complemento: dados.complemento
        });
    }

    return {
        listar: async () => (await recarregar()).map(r => Object.assign({}, r)),
        obter: id => {
            const achado = cache.find(r => r.id === id);
            return achado ? Object.assign({}, achado) : null;
        },
        async criar(dados) {
            const cnpj = somenteDigitos(dados.cnpj);
            await CrudServidor.enviar('cd', { acao: 'salvar', nome: dados.nome, cnpj });
            const cds = await CrudServidor.buscar('cd?acao=listar', 'cds');
            const nova = cds.find(cd => cd.cnpj === cnpj && cd.nome === dados.nome);
            if (!nova) throw CrudServidor.erroAmigavel('A unidade não foi salva. Confira se o CNPJ já não está cadastrado.');
            await salvarEndereco(Number(nova.id), null, dados);
            const salva = (await recarregar()).find(r => r.id === Number(nova.id));
            if (!salva.enderecoId) throw CrudServidor.erroAmigavel('A unidade foi salva, mas o endereço não. Edite a unidade para conferir o endereço.');
            return Object.assign({}, salva);
        },
        async atualizar(id, dados) {
            const anterior = cache.find(r => r.id === id) || {};
            await CrudServidor.enviar('cd', { acao: 'salvar', id, nome: dados.nome, cnpj: somenteDigitos(dados.cnpj) });
            await salvarEndereco(id, anterior.enderecoId, dados);
            const salva = (await recarregar()).find(r => r.id === id);
            if (!salva || salva.nome !== dados.nome || somenteDigitos(salva.cnpj) !== somenteDigitos(dados.cnpj)) {
                throw CrudServidor.erroAmigavel('Não foi possível salvar as alterações da unidade.');
            }
            return Object.assign({}, salva);
        },
        async remover(id) {
            await CrudServidor.excluir('cd', id);
            if ((await recarregar()).some(r => r.id === id)) {
                throw CrudServidor.erroAmigavel('Não foi possível deletar a unidade.');
            }
        }
    };
})();

/* ---------- configuração do CRUD ---------- */

crud = Crud.iniciar({
    entidade: {
        singular: 'unidade',
        artigo: 'a',
        rotuloNovo: 'Cadastrar nova unidade',
        rotuloEditar: 'Editar unidade',
        rotuloSalvar: 'Salvar unidade',
        rotuloSalvarEdicao: 'Salvar alterações',
        rotuloDeletar: 'Deletar unidade'
    },

    repositorio: repositorioUnidades,

    ordenarPor: 'nome',
    rotuloLinha: 'nome',

    // a pesquisa aceita o CNPJ com ou sem pontuação
    pesquisaEm: r => [r.nome, r.cnpj, r.cnpj.replace(/[^0-9A-Za-z]/g, ''), r.cidade, r.estado].join(' '),

    renderizarLista: desenharLista,

    // excluir unidade é só do super_admin
    podeExcluir: () => Sessao.ehSuperAdmin,

    // janela de 820 px (a padrão), com grade de 12 colunas para a linha "Rua + Número"
    gradeColunas: 12,

    // campos na ordem do Figma
    campos: [
        { nome: 'nome', rotulo: 'Nome da unidade', tipo: 'texto', obrigatorio: true, maximo: 100,
          placeholder: 'Digite um nome autodescritivo',
          unico: true, mensagemUnico: 'Já existe uma unidade com este nome.' },
        { nome: 'cnpj', rotulo: 'CNPJ', tipo: 'cnpj', obrigatorio: true, placeholder: '00.000.000/0000-00',
          unico: true, mensagemUnico: 'Já existe uma unidade com este CNPJ.' },
        { nome: 'cep', rotulo: 'CEP', tipo: 'cep', tamanho: 'metade', obrigatorio: true },
        { nome: 'estado', rotulo: 'Estado (sigla)', tipo: 'uf', tamanho: 'metade', obrigatorio: true },
        { nome: 'cidade', rotulo: 'Cidade', tipo: 'texto', obrigatorio: true, maximo: 60 },
        { nome: 'bairro', rotulo: 'Bairro', tipo: 'texto', obrigatorio: true, maximo: 60 },
        { nome: 'rua', rotulo: 'Rua', tipo: 'texto', colunas: 9, obrigatorio: true, maximo: 80 },
        { nome: 'numero', rotulo: 'Número', tipo: 'numero', casas: 0, valorMinimo: 1, mensagemMinimo: 'Informe o número do endereço.', colunas: 3, obrigatorio: true, maximo: 8 },
        { nome: 'complemento', rotulo: 'Complemento', tipo: 'texto', maximo: 60 }
    ],

    // o banco guarda o CNPJ só com os 14 números (o Validator do back-end também)
    validarExtra(valores) {
        if (valores.cnpj && somenteDigitos(valores.cnpj).length !== 14) {
            return { cnpj: 'CNPJ inválido. Use os 14 números.' };
        }
        return {};
    },

    // regra do banco: frigorífico e funcionário têm FK para o CD (ON DELETE RESTRICT)
    validarExclusao(registro) {
        const partes = [];
        const frigorificos = registro.frigorificosTotal || 0;
        const funcionarios = registro.funcionariosTotal || 0;
        if (frigorificos) partes.push(frigorificos + (frigorificos === 1 ? ' frigorífico' : ' frigoríficos'));
        if (funcionarios) partes.push(funcionarios + (funcionarios === 1 ? ' funcionário' : ' funcionários'));
        return partes.length ? 'Não dá para deletar: esta unidade ainda tem ' + partes.join(' e ') + '.' : '';
    }
});

CrudServidor.prepararPagina();

// 1º acesso do admin: a janela de cadastro já abre
if (primeiroAcesso) crud.abrirFormulario('criar');
