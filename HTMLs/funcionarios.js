/* ============================================================
   SKADI · CRUD de FUNCIONÁRIOS
   Só configuração: o comportamento está em crud.js.
   Para criar outro CRUD (frigoríficos, termômetros, ...) copie
   este arquivo e troque entidade, colunas e campos.
   ============================================================ */

const NIVEIS = [
    { valor: 'operador',      rotulo: 'Operador' },
    { valor: 'gerente',       rotulo: 'Gerente' },
    { valor: 'administrador', rotulo: 'Administrador' },
    { valor: 'superadmin',    rotulo: 'Super Admin' }
];

// Na tabela o Figma mostra "Admin" para o nível Administrador
const NIVEL_NA_TABELA = {
    operador: 'Operador',
    gerente: 'Gerente',
    administrador: 'Admin',
    superadmin: 'Super Admin'
};

/* ---------- dados de exemplo (55 registros, como no Figma) ---------- */

function cpfDeExemplo(numero) {
    const base = String(100000000 + numero * 7919 % 899999999).slice(0, 9).split('').map(Number);
    const digito = tamanho => {
        let soma = 0;
        for (let i = 0; i < tamanho; i++) soma += (i < 9 ? base[i] : base[9]) * (tamanho + 1 - i);
        const resto = (soma * 10) % 11;
        return resto === 10 ? 0 : resto;
    };
    base.push(digito(9));
    base.push(digito(10));
    const d = base.join('');
    return d.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})$/, '$1.$2.$3-$4');
}

function gerarExemplos() {
    const equipe = [
        ['Gustavo Munhoz', 'Guh.munhoz'], ['Beatriz Hinsching', 'Bia.hinsching'],
        ['Daniel Pavesi', 'Dani.pavesi'], ['Diego Valadares', 'Diego.valadares'],
        ['Enzo Nascimento', 'Enzo.nascimento'], ['Giovanna Almeida', 'Gio.almeida'],
        ['Isabella Omena', 'Isa.omena'], ['Maria Luisa Passos', 'Malu.passos'],
        ['Mariana Proque', 'Mari.proque'], ['Vinicius Guerra', 'Vini.guerra']
    ];
    const nomes = ['Carlos', 'Fernanda', 'Rafael', 'Juliana', 'Marcelo', 'Patrícia', 'Thiago', 'Camila', 'Lucas', 'Aline', 'Bruno', 'Renata', 'André', 'Larissa', 'Felipe'];
    const sobrenomes = ['Silva', 'Souza', 'Oliveira', 'Santos', 'Pereira', 'Costa', 'Rodrigues', 'Almeida', 'Nunes', 'Ribeiro', 'Carvalho', 'Lima'];
    const cargos = ['Analista de logística', 'Supervisor de frio', 'Técnico de manutenção', 'Coordenador de CD', 'Auxiliar de operações', 'Analista de qualidade'];
    const cds = ['Belterra', 'Anhanguera', 'Dutra', 'Osasco'];
    const gestores = ['Beatriz Hinsching', 'Daniel Pavesi', 'Isabella Omena'];
    const niveis = ['administrador', 'gerente', 'operador', 'operador', 'superadmin'];
    const semAcento = t => t.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();

    const lista = [];
    for (let i = 0; i < 55; i++) {
        let nome, username;
        if (i < equipe.length) {
            [nome, username] = equipe[i];
        } else {
            const primeiro = nomes[(i * 7) % nomes.length];
            const ultimo = sobrenomes[(i * 5) % sobrenomes.length];
            nome = primeiro + ' ' + ultimo;
            username = semAcento(primeiro) + '.' + semAcento(ultimo) + (i > 30 ? i : '');
        }
        lista.push({
            nome,
            username,
            email: semAcento(username) + '@institutojef.org.br',
            cpf: cpfDeExemplo(i + 1),
            cargo: cargos[i % cargos.length],
            acesso: i < 3 ? 'administrador' : niveis[i % niveis.length],
            cd: cds[i % cds.length],
            gestor: gestores[i % gestores.length],
            senhaDefinida: true
        });
    }
    return lista;
}

/* ---------- configuração do CRUD ---------- */

Crud.iniciar({
    entidade: {
        singular: 'funcionário',
        artigo: 'o',
        rotuloNovo: 'Cadastrar novo funcionário',
        rotuloEditar: 'Editar funcionário',
        rotuloSalvar: 'Salvar funcionário',
        rotuloSalvarEdicao: 'Salvar alterações',
        rotuloDeletar: 'Deletar funcionário'
    },

    // Troque por um repositório que usa fetch() para ligar no back-end
    repositorio: Crud.repositorioLocal('skadi.funcionarios.v1', gerarExemplos),

    tamanhoPagina: 20,
    ordenarPor: 'nome',

    // mesmas colunas e larguras proporcionais do Figma
    colunas: [
        { chave: 'nome',     rotulo: 'Nome',     largura: 131.6 },
        { chave: 'username', rotulo: 'Username', largura: 162.6 },
        { chave: 'email',    rotulo: 'E-mail',   largura: 324.1 },
        { chave: 'senha',    rotulo: 'Senha',    largura: 119.6, exibir: () => '*******' },
        { chave: 'acesso',   rotulo: 'Acesso',   largura: 117.7, exibir: r => NIVEL_NA_TABELA[r.acesso] || r.acesso },
        { chave: 'cd',       rotulo: 'CD',       largura: 145.6 },
        { chave: 'gestor',   rotulo: 'Gestor',   largura: 156.6 }
    ],

    // a pesquisa também encontra o nome completo do nível ("Administrador")
    pesquisaEm: r => [r.nome, r.username, r.email, r.cargo, r.cd, r.gestor, NIVEL_NA_TABELA[r.acesso], (NIVEIS.find(n => n.valor === r.acesso) || {}).rotulo].join(' '),

    // campos do formulário, na ordem do Figma
    campos: [
        { nome: 'nome',     rotulo: 'Nome do funcionário', tipo: 'texto', obrigatorio: true, maximo: 80, autocompletar: 'off' },
        { nome: 'email',    rotulo: 'E-mail',              tipo: 'email', obrigatorio: true, maximo: 120, unico: true, mensagemUnico: 'Já existe um funcionário com este e-mail.' },
        { nome: 'username', rotulo: 'Username',            tipo: 'texto', tamanho: 'metade', obrigatorio: true, maximo: 30, minimo: 3, unico: true, mensagemUnico: 'Este username já está em uso.' },
        { nome: 'cpf',      rotulo: 'CPF',                 tipo: 'cpf',   tamanho: 'metade', obrigatorio: true, maximo: 14, unico: true, mensagemUnico: 'Já existe um funcionário com este CPF.', placeholder: '000.000.000-00' },
        { nome: 'cargo',    rotulo: 'Cargo',               tipo: 'texto', obrigatorio: true, maximo: 60 },
        { nome: 'acesso',   rotulo: 'Nível de acesso',     tipo: 'lista', obrigatorio: true, opcoes: NIVEIS, placeholder: 'Escolha o nível de acesso', mensagemObrigatorio: 'Escolha o nível de acesso.' },
        { nome: 'senha',    rotulo: 'Senha',               tipo: 'senha', obrigatorio: true, minimo: 8, opcionalNaEdicao: true, ajuda: 'Mínimo de 8 caracteres.', ajudaEdicao: 'Deixe em branco para manter a senha atual.' }
    ],

    // CD e gestor não aparecem no formulário do Figma: novos cadastros herdam estes valores
    valoresPadrao: {},
    aoSalvar(valores, { modo, registro }) {
        // A senha NUNCA é guardada no navegador: ela deve ser enviada ao back-end,
        // que guarda apenas o hash. Aqui só marcamos que existe uma senha definida.
        const { senha, ...dados } = valores;
        return {
            ...dados,
            cd: registro ? registro.cd : 'Belterra',
            gestor: registro ? registro.gestor : 'Beatriz Hinsching',
            senhaDefinida: Boolean(senha) || Boolean(registro && registro.senhaDefinida)
        };
    }
});
