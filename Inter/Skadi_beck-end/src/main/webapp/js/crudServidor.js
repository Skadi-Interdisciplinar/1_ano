/* ============================================================
   SKADI · LIGAÇÃO DOS CRUDs COM OS SERVLETS (versão JSP)
   O crud.js continua cuidando da tela (tabela, janela, validação).
   Este arquivo troca o repositório de exemplo pelos servlets:
     - os JSP escrevem os registros em <div data-registros="..."><i data-...></i></div>
     - listar   -> GET  servlet?acao=listar&formato=dados
     - salvar   -> POST servlet (acao=salvar)
     - excluir  -> GET  servlet?acao=excluir&id=...
   Quem confere a permissão é sempre o back-end (classe Acesso).
   ============================================================ */
(function () {
    'use strict';

    const CTX = window.SKADI_CONTEXTO || '';
    const dadosCorpo = document.body.dataset;

    /* ---------- usuário logado (vem do JSP) ---------- */
    const Sessao = {
        nivel: dadosCorpo.nivel || '',
        idCd: dadosCorpo.idCd ? Number(dadosCorpo.idCd) : null,
        nome: dadosCorpo.usuario || '',
        get ehSuperAdmin() { return this.nivel === 'super_admin'; },
        get ehAdmin() { return this.nivel === 'admin'; }
    };

    /* ---------- erros com mensagem para o usuário ---------- */
    function erroAmigavel(mensagem) {
        const erro = new Error(mensagem);
        erro.amigavel = true;
        return erro;
    }

    // Códigos "?erro=" que os servlets mandam na URL
    const MENSAGENS_ERRO = {
        validacao: 'O servidor recusou os dados. Confira os campos e tente novamente.',
        senha_fraca: 'A senha precisa ter 8 caracteres ou mais, com uma letra maiúscula e um número.',
        datas: 'A data de validade não pode ser antes da fabricação.',
        temperatura: 'A temperatura máxima precisa ser maior que a mínima.',
        formato: 'Algum número ou data está em um formato inválido.'
    };

    // A página de acesso negado traz no body a conta que está logada agora.
    // Se não for a conta desta tela, outra conta entrou neste navegador (o login vale para todas as abas).
    async function outraContaEntrou(resposta) {
        try {
            const pagina = new DOMParser().parseFromString(await resposta.text(), 'text/html');
            const atual = pagina.body ? pagina.body.dataset : {};
            if (atual.usuario === undefined) return false;
            return atual.usuario !== Sessao.nome || atual.nivel !== Sessao.nivel;
        } catch (erro) {
            return false;
        }
    }

    // Confere para onde o servidor mandou a requisição depois do redirect
    async function verificarResposta(resposta) {
        const destino = new URL(resposta.url || window.location.href, window.location.href);
        const pagina = destino.pathname.split('/').pop();

        if (pagina === 'login.jsp' || pagina === 'login-cliente.jsp') {
            window.location.href = CTX + '/login.jsp?erro=sessao';
            throw erroAmigavel('Sua sessão expirou. Entre novamente.');
        }
        if (pagina === 'acesso-negado.jsp' || resposta.status === 403) {
            if (await outraContaEntrou(resposta)) {
                throw erroAmigavel('Outra conta entrou neste navegador. Recarregue a página para continuar com a conta que está logada agora.');
            }
            throw erroAmigavel('Você não tem permissão para esta ação.');
        }
        if (resposta.status === 500) {
            throw erroAmigavel('Erro no servidor: não foi possível concluir a ação agora. Tente de novo em instantes.');
        }
        if (!resposta.ok) {
            throw erroAmigavel('O servidor não conseguiu concluir a ação (erro ' + resposta.status + ').');
        }

        const codigo = destino.searchParams.get('erro');
        if (codigo) throw erroAmigavel(MENSAGENS_ERRO[codigo] || 'Não foi possível salvar. Confira os dados.');
    }

    /* ---------- leitura dos registros escritos pelo JSP ---------- */
    function lerItens(raiz, nome) {
        return Array.from(raiz.querySelectorAll('[data-registros="' + nome + '"] > i'))
            .map(item => Object.assign({}, item.dataset));
    }

    async function buscarPagina(caminho) {
        const separador = caminho.includes('?') ? '&' : '?';
        const resposta = await fetch(CTX + '/' + caminho + separador + 'formato=dados', {
            credentials: 'same-origin',
            cache: 'no-store'
        });
        await verificarResposta(resposta);
        const html = await resposta.text();
        return new DOMParser().parseFromString(html, 'text/html');
    }

    // Busca uma ou mais listas (ex.: buscar('usuario?acao=novo', 'cds', 'gestores'))
    async function buscar(caminho, ...nomes) {
        const pagina = await buscarPagina(caminho);
        const resultado = {};
        nomes.forEach(nome => { resultado[nome] = lerItens(pagina, nome); });
        return nomes.length === 1 ? resultado[nomes[0]] : resultado;
    }

    async function enviar(servlet, campos) {
        const corpo = new URLSearchParams();
        Object.entries(campos).forEach(([chave, valor]) => {
            if (valor !== undefined && valor !== null) corpo.append(chave, String(valor));
        });
        const resposta = await fetch(CTX + '/' + servlet, {
            method: 'POST',
            credentials: 'same-origin',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
            body: corpo
        });
        await verificarResposta(resposta);
        return resposta;
    }

    async function excluir(servlet, id) {
        const resposta = await fetch(CTX + '/' + servlet + '?acao=excluir&id=' + encodeURIComponent(id), {
            credentials: 'same-origin',
            cache: 'no-store'
        });
        await verificarResposta(resposta);
    }

    /* ---------- conversões ---------- */
    const numero = valor => (valor === undefined || valor === null || valor === '' ? null : Number(valor));
    const texto = valor => (valor === undefined || valor === null ? '' : String(valor));

    /* ---------- repositório no formato que o crud.js espera ---------- */
    // servlet: "usuario" | lista: nome do data-registros | converter: dataset -> registro
    // parametros: registro do formulário -> campos do POST | mesmoRegistro: acha o salvo na lista nova
    function repositorio({ servlet, lista, converter, parametros, mesmoRegistro }) {
        let cache = null;
        let usarCache = false;
        let primeiraVez = true;

        async function listar() {
            if (!usarCache) {
                let itens;
                if (primeiraVez) {
                    // a primeira lista já veio escrita na página pelo JSP
                    itens = lerItens(document, lista);
                    primeiraVez = false;
                } else {
                    itens = await buscar(servlet + '?acao=listar', lista);
                }
                cache = itens.map(converter);
            }
            usarCache = false;
            return cache.map(registro => Object.assign({}, registro));
        }

        // recarrega do servidor e guarda para o próximo listar() do crud.js
        async function atualizarCache() {
            cache = (await buscar(servlet + '?acao=listar', lista)).map(converter);
            usarCache = true;
            return cache;
        }

        return {
            listar,
            obter: id => {
                const achado = (cache || []).find(r => r.id === id);
                return achado ? Object.assign({}, achado) : null;
            },
            async criar(dados) {
                await enviar(servlet, Object.assign({ acao: 'salvar' }, parametros(dados, null)));
                const novos = await atualizarCache();
                const salvo = novos.find(r => mesmoRegistro(r, dados));
                if (!salvo) throw erroAmigavel('O cadastro não foi salvo. Confira se os dados já não existem.');
                return Object.assign({}, salvo);
            },
            async atualizar(id, dados) {
                await enviar(servlet, Object.assign({ acao: 'salvar', id }, parametros(dados, id)));
                const novos = await atualizarCache();
                const salvo = novos.find(r => r.id === id);
                if (!salvo || !mesmoRegistro(salvo, dados)) throw erroAmigavel('Não foi possível salvar as alterações. Confira os dados.');
                return Object.assign({}, salvo);
            },
            async remover(id) {
                await excluir(servlet, id);
                const novos = await atualizarCache();
                if (novos.some(r => r.id === id)) {
                    throw erroAmigavel('Não foi possível deletar: o cadastro ainda está ligado a outros registros.');
                }
            },
            atualizarCache
        };
    }

    /* ---------- unidade (CD) que está sendo analisada ---------- */
    // admin: sempre a própria CD | super_admin: a unidade escolhida na tela de CDs (ou todas)
    function unidadeAtual() {
        const guardada = window.Crud ? Crud.Unidade.obter() : null;
        if (Sessao.ehAdmin) {
            return { id: Sessao.idCd, nome: guardada && guardada.id === Sessao.idCd ? guardada.nome : '' };
        }
        return guardada && guardada.id ? guardada : null;
    }

    // Filtro das telas: o super_admin vê só a unidade escolhida (registros sem CD aparecem em todas)
    function daUnidade(idCd) {
        const unidade = unidadeAtual();
        if (!Sessao.ehSuperAdmin || !unidade) return true;
        return idCd === null || idCd === undefined || idCd === unidade.id;
    }

    function escreverUnidadeNoTopo(nome) {
        const alvo = document.querySelector('[data-unidade-atual]');
        if (alvo && nome) alvo.textContent = nome;
    }

    // Mostra o nome da unidade no cabeçalho, buscando no servidor quando ainda não se sabe
    async function prepararTopo() {
        const unidade = unidadeAtual();
        if (Sessao.ehSuperAdmin && !unidade) {
            escreverUnidadeNoTopo('todas as unidades');
            return;
        }
        if (unidade && unidade.nome) {
            escreverUnidadeNoTopo(unidade.nome);
            return;
        }
        if (Sessao.ehAdmin && Sessao.idCd) {
            try {
                const cds = await buscar('cd?acao=listar', 'cds');
                const minha = cds.find(cd => Number(cd.id) === Sessao.idCd);
                if (minha) {
                    Crud.Unidade.definir({ id: Sessao.idCd, nome: minha.nome });
                    escreverUnidadeNoTopo(minha.nome);
                }
            } catch (erro) { /* o cabeçalho fica com o texto padrão */ }
        }
    }

    // Chamado no fim de cada tela: nome da unidade no topo e aviso de erro vindo do servidor
    function prepararPagina() {
        prepararTopo();
        const codigo = new URLSearchParams(window.location.search).get('erro');
        if (codigo && window.Crud) {
            Crud.aviso(MENSAGENS_ERRO[codigo] || 'Não foi possível concluir a ação.', 'erro');
            // tira o erro da barra de endereço para não repetir o aviso ao atualizar
            const url = new URL(window.location.href);
            url.searchParams.delete('erro');
            window.history.replaceState(null, '', url);
        }
    }

    window.CrudServidor = {
        Sessao, repositorio, buscar, enviar, excluir, erroAmigavel,
        unidadeAtual, daUnidade, prepararTopo, prepararPagina, numero, texto
    };
})();
