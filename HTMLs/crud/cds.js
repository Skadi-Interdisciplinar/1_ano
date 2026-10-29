/* ============================================================
   SKADI · CDs (unidades) do adm cliente
   O formulário (cadastrar/editar), a validação e a exclusão vêm do crud.js.
   Aqui ficam a configuração e o desenho dos cartões.

   Fluxo do protótipo (Figma · "crud adm cliente cds"):
     - clicar no cartão            -> abre a unidade (funcionarios.html)
     - botão +                     -> janela "Cadastrar nova unidade"
     - lápis                       -> modo edição ("Qual unidade você gostaria de editar?")
         clicar no cartão          -> janela "Editar unidade"
         "Cancelar edição"         -> volta para a lista
     - sem nenhuma unidade         -> "Ops! Parece que você não tem nenhuma unidade cadastrada ainda."

   Banco: CD (nome, cnpj) + Endereco (cep, estado, cidade, bairro, rua, numero INT, complemento), um endereço por unidade.
   Calculados pelo back-end (só leitura): status de operação e contagem de frigoríficos/funcionários.
   ============================================================ */

// Vem do login quando o back-end estiver pronto
const USUARIO = 'Tainá Silva';

const PAGINA_DA_UNIDADE = 'funcionarios.html';

const raiz = document.querySelector('[data-crud]');
const listaEl = raiz.querySelector('[data-cds-lista]');
const vazioEl = raiz.querySelector('[data-cds-vazio]');
const perguntaEl = raiz.querySelector('[data-cds-pergunta]');
const botaoEditar = raiz.querySelector('[data-cds-editar]');
const botaoCancelar = raiz.querySelector('[data-cds-cancelar]');

raiz.querySelector('[data-cds-usuario]').textContent = USUARIO;

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

const localizacao = u => u.cidade + ' - ' + u.estado;
const fracao = (ativos, total) => (ativos ?? 0) + '/' + (total ?? 0);

function cartao(unidade) {
    const acao = (editando ? 'Editar unidade ' : 'Acessar unidade ') + unidade.nome;
    return '<li>' +
        '<button type="button" class="unidade" data-id="' + unidade.id + '" aria-label="' + escapar(acao) + '">' +
            '<span class="unidade__nome">' + escapar(unidade.nome) + '</span>' +
            '<span class="unidade__info">' +
                '<span>CNPJ: ' + escapar(unidade.cnpj) + '</span>' +
                '<span>Localização: ' + escapar(localizacao(unidade)) + '</span>' +
                '<span>Status de Operação: ' + escapar(unidade.statusOperacao || 'NORMAL') + '</span>' +
                '<span>Frigoríficos ativos: ' + fracao(unidade.frigorificosAtivos, unidade.frigorificosTotal) + '</span>' +
                '<span>Funcionários ativos: ' + fracao(unidade.funcionariosAtivos, unidade.funcionariosTotal) + '</span>' +
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

    if (!lista.length) {
        listaEl.innerHTML = '';
        vazioEl.textContent = estado.registros.length
            ? 'Nenhuma unidade encontrada para “' + estado.consulta + '”.'
            : 'Ops! Parece que você não tem nenhuma unidade cadastrada ainda.';
        vazioEl.hidden = false;
        return;
    }

    vazioEl.hidden = true;
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
        Crud.Unidade.definir(unidade);
        window.location.href = PAGINA_DA_UNIDADE;
    }
});

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

    // Troque por Crud.repositorioApi({ url: '...' }) quando o back-end responder em JSON
    repositorio: DadosExemplo.repositorio('cds'),

    ordenarPor: 'nome',
    rotuloLinha: 'nome',

    // a pesquisa aceita o CNPJ com ou sem pontuação
    pesquisaEm: r => [r.nome, r.cnpj, r.cnpj.replace(/[^0-9A-Za-z]/g, ''), r.cidade, r.estado].join(' '),

    renderizarLista: desenharLista,

    // janela de 820 px (a padrão), com grade de 12 colunas para a linha "Rua + Número"
    gradeColunas: 12,

    // campos na ordem do Figma
    campos: [
        { nome: 'nome', rotulo: 'Nome da unidade', tipo: 'texto', obrigatorio: true, maximo: 80,
          placeholder: 'Digite um nome autodescritivo',
          unico: true, mensagemUnico: 'Já existe uma unidade com este nome.' },
        { nome: 'cnpj', rotulo: 'CNPJ', tipo: 'cnpj', obrigatorio: true,
          unico: true, mensagemUnico: 'Já existe uma unidade com este CNPJ.' },
        { nome: 'cep', rotulo: 'CEP', tipo: 'cep', tamanho: 'metade', obrigatorio: true },
        { nome: 'estado', rotulo: 'Estado (sigla)', tipo: 'uf', tamanho: 'metade', obrigatorio: true },
        { nome: 'cidade', rotulo: 'Cidade', tipo: 'texto', obrigatorio: true, maximo: 60 },
        { nome: 'bairro', rotulo: 'Bairro', tipo: 'texto', obrigatorio: true, maximo: 60 },
        { nome: 'rua', rotulo: 'Rua', tipo: 'texto', colunas: 9, obrigatorio: true, maximo: 80 },
        { nome: 'numero', rotulo: 'Número', tipo: 'numero', casas: 0, valorMinimo: 1, mensagemMinimo: 'Informe o número do endereço.', colunas: 3, obrigatorio: true, maximo: 8 },
        { nome: 'complemento', rotulo: 'Complemento', tipo: 'texto', maximo: 60 }
    ],

    // Status de operação e contagens não são digitados: continuam como estão
    // (no banco vêm dos frigoríficos, dos termômetros e dos funcionários da unidade).
    aoSalvar(valores, { registro }) {
        return {
            ...valores,
            statusOperacao: registro ? registro.statusOperacao : 'NORMAL',
            frigorificosAtivos: registro ? registro.frigorificosAtivos : 0,
            frigorificosTotal: registro ? registro.frigorificosTotal : 0,
            funcionariosAtivos: registro ? registro.funcionariosAtivos : 0,
            funcionariosTotal: registro ? registro.funcionariosTotal : 0
        };
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
