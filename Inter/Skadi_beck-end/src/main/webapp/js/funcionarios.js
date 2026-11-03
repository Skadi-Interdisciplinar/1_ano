/* ============================================================
   SKADI · CRUD de FUNCIONÁRIOS (versão JSP)
   Só configuração: a tela está em crud.js e a ligação com o
   servlet /usuario em crudServidor.js.

   Regras do back-end (UsuarioServlet):
     - admin cadastra gestor e operador da própria CD;
     - só o super_admin cadastra administradores (sem CD: o admin cadastra a dele no 1º acesso);
     - todo funcionário tem username e senha (como no protótipo em HTMLs/crud);
       só administradores entram neste site, os outros níveis usam o app;
     - na edição o nível de acesso não muda e a senha em branco mantém a atual.
   Regra do banco: operador sempre tem gestor; gestor e admin não têm.
   ============================================================ */

const Sessao = CrudServidor.Sessao;

// Valores iguais a Usuario.nivel_acesso do banco
const NIVEL_NA_TABELA = {
    operador: 'Operador',
    gestor: 'Gerente',
    admin: 'Admin',
    super_admin: 'Super Admin'
};

const NIVEIS = [
    { valor: 'operador', rotulo: 'Operador' },
    { valor: 'gestor',   rotulo: 'Gerente' }
].concat(Sessao.ehSuperAdmin ? [{ valor: 'admin', rotulo: 'Administrador' }] : []);

/* ---------- dados das listas do formulário ---------- */

let cds = [];
let gestores = [];

const nomeDaCd = id => (cds.find(cd => cd.id === id) || {}).nome || '';
const nomeDoGestor = id => (gestores.find(g => g.id === id) || {}).nome || '';

// Mesma regra do Validator do back-end
const REGRA_SENHA = /^(?=.*[0-9])(?=.*[A-Z]).{8,}$/;

/* ---------- configuração do CRUD ---------- */

const crud = Crud.iniciar({
    entidade: {
        singular: 'funcionário',
        artigo: 'o',
        rotuloNovo: 'Cadastrar novo funcionário',
        rotuloEditar: 'Editar funcionário',
        rotuloSalvar: 'Salvar funcionário',
        rotuloSalvarEdicao: 'Salvar alterações',
        rotuloDeletar: 'Deletar funcionário'
    },

    repositorio: CrudServidor.repositorio({
        servlet: 'usuario',
        lista: 'usuarios',
        converter: item => ({
            id: Number(item.id),
            nome: item.nome,
            username: item.username,
            email: item.email,
            cpf: Crud.Mascara.cpf(item.cpf),
            cargo: item.cargo,
            acesso: item.nivel,
            idCd: CrudServidor.numero(item.idCd),
            nomeCd: item.nomeCd,
            codGestor: CrudServidor.numero(item.codGestor),
            senhaDefinida: item.temSenha === 'true'
        }),
        parametros: dados => ({
            nome: dados.nome,
            email: dados.email,
            username: dados.username,
            cpf: dados.cpf,
            cargo: dados.cargo,
            nivelAcesso: dados.acesso,
            senha: dados.senha,
            idCd: dados.idCd,
            // o campo só aparece (e só é enviado) para operador
            codGestor: dados.codGestor || ''
        }),
        // o CPF é único no banco; na edição confere também o que foi alterado
        mesmoRegistro: (registro, dados) =>
            registro.cpf.replace(/\D/g, '') === String(dados.cpf).replace(/\D/g, '') &&
            registro.nome === dados.nome && registro.email === dados.email
    }),

    aoCarregar: async () => {
        const opcoes = await CrudServidor.buscar('usuario?acao=novo', 'cds', 'gestores');
        cds = opcoes.cds.map(cd => ({ id: Number(cd.id), nome: cd.nome }));
        gestores = opcoes.gestores.map(g => ({ id: Number(g.id), nome: g.nome, idCd: CrudServidor.numero(g.idCd) }));
    },

    // super_admin: só a unidade escolhida na tela de CDs
    filtro: registro => CrudServidor.daUnidade(registro.idCd),

    tamanhoPagina: 20,
    ordenarPor: 'nome',

    // mesmas colunas e larguras proporcionais do Figma
    colunas: [
        { chave: 'nome',     rotulo: 'Nome',     largura: 131.6 },
        { chave: 'username', rotulo: 'Username', largura: 162.6, exibir: r => r.username || '—' },
        { chave: 'email',    rotulo: 'E-mail',   largura: 324.1 },
        { chave: 'senha',    rotulo: 'Senha',    largura: 119.6, exibir: r => (r.senhaDefinida ? '*******' : '—') },
        { chave: 'acesso',   rotulo: 'Acesso',   largura: 117.7, exibir: r => NIVEL_NA_TABELA[r.acesso] || r.acesso },
        { chave: 'cd',       rotulo: 'CD',       largura: 145.6, exibir: r => r.nomeCd || nomeDaCd(r.idCd) || '—' },
        { chave: 'gestor',   rotulo: 'Gestor',   largura: 156.6, exibir: r => nomeDoGestor(r.codGestor) || '—' }
    ],

    // a pesquisa também encontra o nome completo do nível ("Administrador")
    pesquisaEm: r => [r.nome, r.username, r.email, r.cpf, r.cargo, r.nomeCd || nomeDaCd(r.idCd),
        nomeDoGestor(r.codGestor), NIVEL_NA_TABELA[r.acesso], r.acesso === 'admin' ? 'Administrador' : ''].join(' '),

    // campos do formulário, na ordem do Figma (CD e gestor no fim, porque o banco exige)
    campos: [
        { nome: 'nome',     rotulo: 'Nome do funcionário', tipo: 'texto', obrigatorio: true, maximo: 100, autocompletar: 'off' },
        { nome: 'email',    rotulo: 'E-mail',              tipo: 'email', obrigatorio: true, maximo: 120, unico: true, mensagemUnico: 'Já existe um funcionário com este e-mail.' },
        { nome: 'username', rotulo: 'Username',            tipo: 'texto', tamanho: 'metade', obrigatorio: true, maximo: 50, minimo: 3, unico: true, mensagemUnico: 'Este username já está em uso.' },
        { nome: 'cpf',      rotulo: 'CPF',                 tipo: 'cpf',   tamanho: 'metade', obrigatorio: true, maximo: 14, unico: true, mensagemUnico: 'Já existe um funcionário com este CPF.', placeholder: '000.000.000-00' },
        { nome: 'cargo',    rotulo: 'Cargo',               tipo: 'texto', obrigatorio: true, maximo: 80 },
        { nome: 'acesso',   rotulo: 'Nível de acesso',     tipo: 'lista', obrigatorio: true, opcoes: NIVEIS, placeholder: 'Escolha o nível de acesso', mensagemObrigatorio: 'Escolha o nível de acesso.',
          // o back-end não muda o nível de acesso na edição
          mostrarSe: (valores, modo) => modo === 'criar' },
        { nome: 'senha',    rotulo: 'Senha',               tipo: 'senha', obrigatorio: true, minimo: 8, opcionalNaEdicao: true,
          ajuda: 'Mínimo de 8 caracteres, com uma letra maiúscula e um número.',
          ajudaEdicao: 'Deixe em branco para manter a senha atual.' },
        { nome: 'idCd',     rotulo: 'Unidade (CD)',        tipo: 'lista', tamanho: 'metade', obrigatorio: true, placeholder: 'Escolha a unidade', mensagemObrigatorio: 'Escolha a unidade.',
          opcoes: () => cds.map(cd => ({ valor: String(cd.id), rotulo: cd.nome })),
          // admin sempre usa a própria CD; conta de administrador nasce sem CD
          mostrarSe: valores => Sessao.ehSuperAdmin && valores.acesso !== 'admin' },
        { nome: 'codGestor', rotulo: 'Gestor',             tipo: 'lista', tamanho: 'metade', obrigatorio: true,
          placeholder: 'Escolha o gestor', mensagemObrigatorio: 'Escolha o gestor do operador.',
          opcoes: () => gestores
              .filter(g => CrudServidor.daUnidade(g.idCd))
              .map(g => ({ valor: String(g.id), rotulo: Sessao.ehSuperAdmin && nomeDaCd(g.idCd) ? g.nome + ' (' + nomeDaCd(g.idCd) + ')' : g.nome })),
          // o banco exige gestor só para o operador
          mostrarSe: valores => valores.acesso === 'operador' }
    ],

    // novo cadastro já vem com a unidade que está sendo analisada
    valoresPadrao: () => {
        const unidade = CrudServidor.unidadeAtual();
        return { idCd: unidade && unidade.id ? String(unidade.id) : '' };
    },

    validarExtra(valores) {
        if (valores.senha && !REGRA_SENHA.test(valores.senha)) {
            return { senha: 'Use 8 caracteres ou mais, com uma letra maiúscula e um número.' };
        }
        return {};
    },

    // contas da equipe Skadi (super_admin) não são alteradas por esta tela
    aoEditar(registro) {
        if (registro.acesso === 'super_admin') {
            Crud.aviso('Contas da equipe Skadi não podem ser editadas aqui.', 'erro');
            return;
        }
        crud.abrirFormulario('editar', Object.assign({}, registro, {
            idCd: registro.idCd ? String(registro.idCd) : '',
            codGestor: registro.codGestor ? String(registro.codGestor) : ''
        }));
    },

    podeExcluir: registro => registro.acesso !== 'super_admin' && (Sessao.ehSuperAdmin || registro.acesso !== 'admin'),

    // regra do banco: o operador não pode ficar sem gestor
    async validarExclusao(registro) {
        if (registro.acesso !== 'gestor') return '';
        const usuarios = await CrudServidor.buscar('usuario?acao=listar', 'usuarios');
        const total = usuarios.filter(u => CrudServidor.numero(u.codGestor) === registro.id).length;
        return total
            ? 'Não dá para deletar: ' + total + (total === 1 ? ' operador tem' : ' operadores têm') +
              ' esta pessoa como gestor. Troque o gestor deles antes.'
            : '';
    }
});

CrudServidor.prepararPagina();
