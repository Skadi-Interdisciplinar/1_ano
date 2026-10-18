/* ============================================================
   SKADI · BASE DOS CRUDs
   Motor compartilhado: menu lateral, tabela, pesquisa, paginação,
   janela de cadastro/edição, validação, confirmação e avisos.

   Cada tela só precisa chamar:  Crud.iniciar({ ...configuração })
   (veja funcionarios.js e frigorificos.js como exemplo).

   REPOSITÓRIO
   Os dados passam por um "repositório" com 5 métodos (podem
   devolver valores ou Promises):
       listar()  obter(id)  criar(valores)  atualizar(id, valores)  remover(id)
   Aqui vem um repositório que grava no localStorage do navegador
   (Crud.repositorioLocal). Para ligar no back-end, use
   Crud.repositorioApi({ url: '...' }) no lugar dele.
   ============================================================ */

(function () {
    'use strict';

    const ICONES = window.CrudIcones || window.CrudIcons || {};

    /* ---------- utilidades ---------- */

    const $ = (seletor, raiz = document) => raiz.querySelector(seletor);
    const $$ = (seletor, raiz = document) => Array.from(raiz.querySelectorAll(seletor));

    const escapar = valor => String(valor ?? '').replace(/[&<>"']/g, c => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    }[c]));

    const normalizar = valor => String(valor ?? '')
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .toLowerCase()
        .trim();

    const somenteDigitos = valor => String(valor ?? '').replace(/\D/g, '');

    /* ---------- ícones desenhados (não vieram do SVG exportado) ---------- */

    const EXTRA = {
        adicionar:
            '<svg viewBox="0 0 46 49" aria-hidden="true" focusable="false">' +
            '<ellipse cx="23" cy="24.25" rx="22.83" ry="24.25" fill="#fff" style="filter:drop-shadow(0 0 0.1875rem rgba(0,0,0,.28))"/>' +
            '<circle cx="23" cy="24.25" r="8.2" fill="none" stroke="currentColor" stroke-width="1.2"/>' +
            '<path d="M23 19.7v9.1M18.45 24.25h9.1" stroke="currentColor" stroke-width="1.2" stroke-linecap="round"/></svg>',
        maisCirculo:
            '<svg viewBox="0 0 24 24" aria-hidden="true" focusable="false">' +
            '<circle cx="12" cy="12" r="12" fill="#fff"/>' +
            '<path d="M12 6.4v11.2M6.4 12h11.2" stroke="#0F2E4F" stroke-width="2.8" stroke-linecap="round"/></svg>',
        fechar:
            '<svg viewBox="0 0 26 26" aria-hidden="true" focusable="false">' +
            '<path d="M4.5 4.5l17 17M21.5 4.5l-17 17" stroke="currentColor" stroke-width="2.6" stroke-linecap="round"/></svg>',
        salvar:
            '<svg viewBox="0 0 24 24" fill="currentColor" aria-hidden="true" focusable="false">' +
            '<path d="M17 3H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V7l-4-4zm-5 16a3 3 0 1 1 0-6 3 3 0 0 1 0 6zm3-10H5V5h10v4z"/></svg>',
        seta:
            '<svg viewBox="0 0 18 10" aria-hidden="true" focusable="false">' +
            '<path d="M1.5 1.5L9 8.5l7.5-7" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>',
        olho:
            '<svg viewBox="0 0 24 24" fill="currentColor" aria-hidden="true" focusable="false">' +
            '<path d="M12 5C7 5 2.7 8.1 1 12.5 2.7 16.9 7 20 12 20s9.3-3.1 11-7.5C21.3 8.1 17 5 12 5zm0 12.5a5 5 0 1 1 0-10 5 5 0 0 1 0 10zm0-8a3 3 0 1 0 0 6 3 3 0 0 0 0-6z"/></svg>',
        olhoRiscado:
            '<svg viewBox="0 0 24 24" fill="currentColor" aria-hidden="true" focusable="false">' +
            '<path d="M12 7a5 5 0 0 1 5 5c0 .65-.13 1.26-.36 1.83l2.92 2.92A11.8 11.8 0 0 0 23 12.5C21.3 8.1 17 5 12 5c-1.4 0-2.74.25-3.98.7l2.16 2.16C10.74 7.13 11.35 7 12 7zM2 4.27l2.28 2.28.46.46A11.8 11.8 0 0 0 1 12.5C2.7 16.9 7 20 12 20c1.55 0 3.03-.3 4.38-.84l.42.42L19.73 22 21 20.73 3.27 3 2 4.27zM7.53 9.8l1.55 1.55A2.8 2.8 0 0 0 9 12a3 3 0 0 0 3 3c.22 0 .44-.03.65-.08l1.55 1.55A5 5 0 0 1 7 12c0-.74.18-1.43.53-2.2zm4.31-.78 3.15 3.15.02-.17a3 3 0 0 0-3-3l-.17.02z"/></svg>'
    };

    /* ============================================================
       VALIDAÇÕES REUTILIZÁVEIS
       ============================================================ */

    const Validar = {
        email: valor => /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(valor),

        cpf(valor) {
            const d = somenteDigitos(valor);
            if (d.length !== 11 || /^(\d)\1{10}$/.test(d)) return false;
            const digito = tamanho => {
                let soma = 0;
                for (let i = 0; i < tamanho; i++) soma += Number(d[i]) * (tamanho + 1 - i);
                const resto = (soma * 10) % 11;
                return resto === 10 ? 0 : resto;
            };
            return digito(9) === Number(d[9]) && digito(10) === Number(d[10]);
        }
    };

    /* Números e datas: o formulário usa texto brasileiro (-18,5 e 26/08/2026);
       os dados guardam número (-18.5) e data ISO (2026-08-26), como no banco. */
    const Formato = {
        numeroParaTexto: n => (n === null || n === undefined || n === '' ? '' : String(n).replace('.', ',')),

        textoParaNumero(texto) {
            const t = String(texto ?? '').trim();
            if (/^-?\d{1,3}(\.\d{3})+(,\d+)?$/.test(t)) return Number(t.replace(/\./g, '').replace(',', '.'));   // 1.160,5
            if (!/^-?\d+([.,]\d+)?$/.test(t)) return null;
            return Number(t.replace(',', '.'));
        },

        isoParaData(iso) {
            const m = /^(\d{4})-(\d{2})-(\d{2})/.exec(iso || '');
            return m ? m[3] + '/' + m[2] + '/' + m[1] : '';
        },

        dataParaIso(texto) {
            const m = /^(\d{2})\/(\d{2})\/(\d{4})$/.exec(String(texto ?? '').trim());
            if (!m) return null;
            const dia = Number(m[1]), mes = Number(m[2]), ano = Number(m[3]);
            const data = new Date(ano, mes - 1, dia);
            const valida = data.getFullYear() === ano && data.getMonth() === mes - 1 && data.getDate() === dia;
            return valida ? m[3] + '-' + m[2] + '-' + m[1] : null;
        },

        // 1160 -> "1.160" | 1160,5 -> "1.160,5"
        numeroBr: (n, casas = 2) => Number(n).toLocaleString('pt-BR', { maximumFractionDigits: casas })
    };

    const Mascara = {
        data(valor) {
            const d = somenteDigitos(valor).slice(0, 8);
            return d
                .replace(/^(\d{2})(\d)/, '$1/$2')
                .replace(/^(\d{2})\/(\d{2})(\d)/, '$1/$2/$3');
        },

        cpf(valor) {
            const d = somenteDigitos(valor).slice(0, 11);
            return d
                .replace(/^(\d{3})(\d)/, '$1.$2')
                .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
                .replace(/\.(\d{3})(\d)/, '.$1-$2');
        }
    };

    /* ============================================================
       REPOSITÓRIO NO NAVEGADOR (localStorage)
       ============================================================ */

    function repositorioLocal(chave, semente) {
        let cache = null;

        const gravar = () => {
            try { localStorage.setItem(chave, JSON.stringify(cache)); } catch (erro) { /* sem armazenamento */ }
        };

        const ler = () => {
            if (cache) return cache;
            try {
                const bruto = localStorage.getItem(chave);
                if (bruto) {
                    cache = JSON.parse(bruto);
                    return cache;
                }
            } catch (erro) { /* dados corrompidos: recomeça da semente */ }
            cache = (typeof semente === 'function' ? semente() : []).map((item, i) => ({ id: i + 1, ...item }));
            gravar();
            return cache;
        };

        return {
            listar: () => ler().map(item => ({ ...item })),
            obter: id => {
                const item = ler().find(r => r.id === id);
                return item ? { ...item } : null;
            },
            criar(valores) {
                const linhas = ler();
                const id = linhas.reduce((maior, r) => Math.max(maior, r.id), 0) + 1;
                const novo = { id, ...valores };
                linhas.push(novo);
                gravar();
                return { ...novo };
            },
            atualizar(id, valores) {
                const linhas = ler();
                const indice = linhas.findIndex(r => r.id === id);
                if (indice < 0) throw new Error('Registro não encontrado.');
                linhas[indice] = { ...linhas[indice], ...valores, id };
                gravar();
                return { ...linhas[indice] };
            },
            remover(id) {
                const linhas = ler();
                const indice = linhas.findIndex(r => r.id === id);
                if (indice >= 0) linhas.splice(indice, 1);
                gravar();
            }
        };
    }

    /* ============================================================
       REPOSITÓRIO DE API (fetch + JSON)
       Use quando o back-end expuser as rotas em JSON:
           GET    {url}        -> lista
           GET    {url}/{id}   -> um registro
           POST   {url}        -> cria
           PUT    {url}/{id}   -> atualiza
           DELETE {url}/{id}   -> remove
       mapearEnvio / mapearResposta convertem entre os nomes do
       formulário e os nomes do banco (ex.: id_cd, nivel_acesso).
       ============================================================ */

    function repositorioApi({ url, mapearEnvio = x => x, mapearResposta = x => x, cabecalhos = {} }) {
        const chamar = async (caminho, opcoes = {}) => {
            const resposta = await fetch(url + caminho, {
                credentials: 'same-origin',
                headers: { 'Accept': 'application/json', 'Content-Type': 'application/json', ...cabecalhos },
                ...opcoes
            });
            if (!resposta.ok) throw new Error('HTTP ' + resposta.status);
            return resposta.status === 204 ? null : resposta.json();
        };

        return {
            listar: async () => (await chamar('')).map(mapearResposta),
            obter: async id => mapearResposta(await chamar('/' + id)),
            criar: async valores => mapearResposta(await chamar('', { method: 'POST', body: JSON.stringify(mapearEnvio(valores)) })),
            atualizar: async (id, valores) => mapearResposta(await chamar('/' + id, { method: 'PUT', body: JSON.stringify(mapearEnvio(valores)) })),
            remover: async id => { await chamar('/' + id, { method: 'DELETE' }); }
        };
    }

    /* ============================================================
       AVISO (toast)
       ============================================================ */

    let avisoEl = null;
    let avisoTempo = null;

    function aviso(mensagem, tipo) {
        if (!avisoEl) {
            avisoEl = document.createElement('div');
            avisoEl.className = 'aviso';
            avisoEl.setAttribute('role', 'status');
            avisoEl.setAttribute('aria-live', 'polite');
            document.body.appendChild(avisoEl);
        }
        avisoEl.textContent = mensagem;
        avisoEl.classList.toggle('aviso--erro', tipo === 'erro');
        // força reflow para reiniciar a transição
        void avisoEl.offsetWidth;
        avisoEl.classList.add('visivel');
        clearTimeout(avisoTempo);
        avisoTempo = setTimeout(() => avisoEl.classList.remove('visivel'), 3200);
    }

    /* ============================================================
       JANELA (modal) genérica
       ============================================================ */

    const FOCAVEIS = 'a[href], button:not([disabled]), input:not([disabled]):not([type="hidden"]), [tabindex]:not([tabindex="-1"])';
    const pilhaJanelas = [];

    function criarJanela({ titulo, icone, pequena = false, variante = '', corpo, rodape, aoFechar }) {
        const fundo = document.createElement('div');
        fundo.className = 'janela__fundo';

        const idTitulo = 'janela-titulo-' + Math.random().toString(36).slice(2, 8);

        fundo.innerHTML =
            '<div class="janela' + (pequena ? ' janela--pequena' : '') + (variante ? ' janela--' + variante : '') + '" role="dialog" aria-modal="true" aria-labelledby="' + idTitulo + '">' +
                '<header class="janela__topo">' +
                    (icone ? '<span class="janela__icone">' + icone + '</span>' : '') +
                    '<h2 class="janela__titulo" id="' + idTitulo + '">' + escapar(titulo) + '</h2>' +
                    '<button type="button" class="janela__fechar" aria-label="Fechar">' + EXTRA.fechar + '</button>' +
                '</header>' +
                '<div class="janela__corpo"></div>' +
                '<footer class="janela__rodape"></footer>' +
            '</div>';

        const caixa = $('.janela', fundo);
        const areaCorpo = $('.janela__corpo', fundo);
        const areaRodape = $('.janela__rodape', fundo);

        if (typeof corpo === 'string') areaCorpo.innerHTML = corpo; else areaCorpo.appendChild(corpo);
        if (typeof rodape === 'string') areaRodape.innerHTML = rodape; else areaRodape.appendChild(rodape);

        const anterior = document.activeElement;
        document.body.appendChild(fundo);
        document.body.classList.add('travado');
        requestAnimationFrame(() => fundo.classList.add('aberta'));

        const api = {
            fundo, caixa, corpo: areaCorpo, rodape: areaRodape,
            fechar(motivo) {
                const indice = pilhaJanelas.indexOf(api);
                if (indice < 0) return;
                pilhaJanelas.splice(indice, 1);
                fundo.classList.remove('aberta');
                setTimeout(() => fundo.remove(), 220);
                if (!pilhaJanelas.length) document.body.classList.remove('travado');
                if (anterior && document.contains(anterior)) anterior.focus();
                if (aoFechar) aoFechar(motivo);
            }
        };

        pilhaJanelas.push(api);

        $('.janela__fechar', fundo).addEventListener('click', () => api.fechar('fechar'));

        // clicar fora da caixa fecha
        fundo.addEventListener('mousedown', evento => {
            if (evento.target === fundo) api.fechar('fora');
        });

        // mantém o foco dentro da janela
        caixa.addEventListener('keydown', evento => {
            if (evento.key !== 'Tab') return;
            const itens = $$(FOCAVEIS, caixa).filter(el => el.offsetParent !== null);
            if (!itens.length) return;
            const primeiro = itens[0];
            const ultimo = itens[itens.length - 1];
            if (evento.shiftKey && document.activeElement === primeiro) {
                evento.preventDefault();
                ultimo.focus();
            } else if (!evento.shiftKey && document.activeElement === ultimo) {
                evento.preventDefault();
                primeiro.focus();
            }
        });

        return api;
    }

    /* confirmação (Promise<boolean>) */

    function confirmar({ titulo, texto, rotuloConfirmar = 'Confirmar', perigo = false }) {
        return new Promise(resolve => {
            let resolvido = false;
            const terminar = valor => {
                if (resolvido) return;
                resolvido = true;
                resolve(valor);
            };

            const janela = criarJanela({
                titulo,
                pequena: true,
                corpo: '<p class="janela__texto">' + texto + '</p>',
                rodape:
                    '<span class="janela__espaco"></span>' +
                    '<button type="button" class="botao botao--claro" data-nao>Cancelar</button>' +
                    '<button type="button" class="botao ' + (perigo ? 'botao--perigo' : 'botao--principal') + '" data-sim>' + escapar(rotuloConfirmar) + '</button>',
                aoFechar: () => terminar(false)
            });

            janela.fundo.style.zIndex = '120';

            $('[data-nao]', janela.rodape).addEventListener('click', () => janela.fechar('nao'));
            $('[data-sim]', janela.rodape).addEventListener('click', () => {
                terminar(true);
                janela.fechar('sim');
            });

            $('[data-nao]', janela.rodape).focus();
        });
    }

    /* ============================================================
       MENU LATERAL ("guia")
       ============================================================ */

    function iniciarGuia() {
        const botao = $('[data-guia-botao]');
        const guia = $('[data-guia]');
        const fundo = $('[data-guia-fundo]');
        if (!botao || !guia) return null;

        const definir = aberta => {
            guia.classList.toggle('aberta', aberta);
            if (fundo) fundo.classList.toggle('aberta', aberta);
            botao.setAttribute('aria-expanded', String(aberta));
            botao.setAttribute('aria-label', aberta ? 'Fechar menu' : 'Abrir menu');
        };

        botao.addEventListener('click', () => definir(!guia.classList.contains('aberta')));
        if (fundo) fundo.addEventListener('click', () => definir(false));

        return { fechar: () => definir(false), aberta: () => guia.classList.contains('aberta') };
    }

    /* ============================================================
       LISTA SUSPENSA (campo do tipo "lista")
       ============================================================ */

    function iniciarLista(raiz, aoMudar) {
        const botao = $('.lista__botao', raiz);
        const valorEl = $('.lista__valor', raiz);
        const opcoesEl = $('.lista__opcoes', raiz);
        const entrada = $('input[type="hidden"]', raiz);
        const opcoes = $$('.lista__opcao', raiz);
        const textoPadrao = botao.dataset.placeholder;
        let ativa = -1;

        const marcarAtiva = indice => {
            ativa = indice;
            opcoes.forEach((op, i) => op.classList.toggle('ativa', i === indice));
            if (opcoes[indice]) {
                botao.setAttribute('aria-activedescendant', opcoes[indice].id);
                opcoes[indice].scrollIntoView({ block: 'nearest' });
            }
        };

        // A lista usa posição "fixed" para não ser cortada pela rolagem da janela.
        // Abre para baixo; se faltar espaço, abre para cima.
        const posicionar = () => {
            const rootPx = parseFloat(getComputedStyle(document.documentElement).fontSize) || 16;
            const ret = botao.getBoundingClientRect();
            const abaixo = window.innerHeight - ret.bottom - 12;
            const acima = ret.top - 12;
            const abrirAcima = abaixo < 180 && acima > abaixo;

            opcoesEl.style.position = 'fixed';
            opcoesEl.style.left = (ret.left / rootPx) + 'rem';
            opcoesEl.style.right = 'auto';
            opcoesEl.style.width = (ret.width / rootPx) + 'rem';
            opcoesEl.style.maxHeight = (Math.max(110, Math.min(240, abrirAcima ? acima : abaixo)) / rootPx) + 'rem';

            if (abrirAcima) {
                opcoesEl.style.top = 'auto';
                opcoesEl.style.bottom = ((window.innerHeight - ret.top + 4) / rootPx) + 'rem';
            } else {
                opcoesEl.style.bottom = 'auto';
                opcoesEl.style.top = ((ret.bottom + 4) / rootPx) + 'rem';
            }
            raiz.classList.toggle('lista--acima', abrirAcima);
        };

        const fecharAoRolar = () => fechar();

        const abrir = () => {
            posicionar();
            window.addEventListener('resize', fecharAoRolar);
            const area = raiz.closest('.janela__corpo');
            if (area) area.addEventListener('scroll', fecharAoRolar);

            raiz.classList.add('aberta');
            botao.setAttribute('aria-expanded', 'true');
            const selecionada = opcoes.findIndex(op => op.dataset.valor === entrada.value);
            marcarAtiva(selecionada >= 0 ? selecionada : 0);
        };

        const fechar = () => {
            window.removeEventListener('resize', fecharAoRolar);
            const area = raiz.closest('.janela__corpo');
            if (area) area.removeEventListener('scroll', fecharAoRolar);
            raiz.classList.remove('aberta');
            botao.setAttribute('aria-expanded', 'false');
            botao.removeAttribute('aria-activedescendant');
            opcoes.forEach(op => op.classList.remove('ativa'));
        };

        const definir = valor => {
            const alvo = opcoes.find(op => op.dataset.valor === valor);
            entrada.value = alvo ? valor : '';
            valorEl.textContent = alvo ? alvo.textContent : textoPadrao;
            botao.classList.toggle('lista__botao--vazio', !alvo);
            opcoes.forEach(op => op.setAttribute('aria-selected', String(op === alvo)));
            if (aoMudar) aoMudar(entrada.value);
        };

        botao.addEventListener('click', () => (raiz.classList.contains('aberta') ? fechar() : abrir()));

        botao.addEventListener('keydown', evento => {
            const aberta = raiz.classList.contains('aberta');
            if (evento.key === 'ArrowDown' || evento.key === 'ArrowUp') {
                evento.preventDefault();
                if (!aberta) return abrir();
                const passo = evento.key === 'ArrowDown' ? 1 : -1;
                marcarAtiva((ativa + passo + opcoes.length) % opcoes.length);
            } else if ((evento.key === 'Enter' || evento.key === ' ') && aberta) {
                evento.preventDefault();
                if (opcoes[ativa]) definir(opcoes[ativa].dataset.valor);
                fechar();
            } else if (evento.key === 'Escape' && aberta) {
                evento.preventDefault();
                evento.stopPropagation();
                fechar();
            } else if (evento.key === 'Tab' && aberta) {
                fechar();
            }
        });

        opcoes.forEach((opcao, indice) => {
            opcao.addEventListener('mouseenter', () => marcarAtiva(indice));
            opcao.addEventListener('mousedown', evento => evento.preventDefault());
            opcao.addEventListener('click', () => {
                definir(opcao.dataset.valor);
                fechar();
                botao.focus();
            });
        });

        document.addEventListener('mousedown', evento => {
            if (raiz.classList.contains('aberta') && !raiz.contains(evento.target)) fechar();
        });

        return { definir, fechar, foco: () => botao.focus() };
    }

    /* ============================================================
       CRUD
       ============================================================ */

    function iniciar(config) {
        const entidade = config.entidade;
        const campos = config.campos;
        const colunas = config.colunas;
        const tamanhoPagina = config.tamanhoPagina || 20;
        const repositorio = config.repositorio;

        // "opcoes" pode ser uma lista fixa ou uma função que devolve a lista na hora de abrir o formulário
        const opcoesDe = campo => (typeof campo.opcoes === 'function' ? campo.opcoes() : (campo.opcoes || []));

        const raiz = $('[data-crud]');
        const campoPesquisa = $('[data-crud-pesquisa]', raiz);
        const botaoAdicionar = $('[data-crud-adicionar]', raiz);
        const cabeca = $('[data-crud-cabeca]', raiz);
        const corpo = $('[data-crud-corpo]', raiz);
        const intervalo = $('[data-crud-intervalo]', raiz);
        const botaoAnterior = $('[data-crud-anterior]', raiz);
        const botaoProxima = $('[data-crud-proxima]', raiz);
        const tabela = $('[data-crud-tabela]', raiz);

        const estado = {
            registros: [],
            consulta: '',
            pagina: 1,
            destacarId: null
        };

        const guia = iniciarGuia();

        // preenche os ícones marcados com data-icone="nome"
        const todosIcones = Object.assign({}, EXTRA, ICONES);
        $$('[data-icone]').forEach(el => { el.innerHTML = todosIcones[el.dataset.icone] || ''; });

        /* ---------- tabela ---------- */

        const colunasCss = colunas.map(c => (c.largura || 1) + 'fr').join(' ') + ' ' + (config.larguraAcoes || 72) + 'fr';
        tabela.style.setProperty('--colunas', colunasCss);

        cabeca.innerHTML = colunas
            .map(c => '<div role="columnheader">' + escapar(c.rotulo) + '</div>')
            .join('') + '<div role="columnheader"><span class="so-leitor">Ações</span></div>';

        const textoCelula = (coluna, registro) =>
            coluna.exibir ? coluna.exibir(registro) : (registro[coluna.chave] ?? '');

        const textoPesquisavel = registro => {
            if (config.pesquisaEm) return normalizar(config.pesquisaEm(registro));
            return normalizar(colunas.map(c => textoCelula(c, registro)).join(' '));
        };

        const ordenar = lista => {
            const chave = config.ordenarPor || colunas[0].chave;
            return lista.sort((a, b) => String(a[chave] ?? '').localeCompare(String(b[chave] ?? ''), 'pt-BR', { sensitivity: 'base', numeric: true }));
        };

        const filtrados = () => {
            const termo = normalizar(estado.consulta);
            const todos = ordenar(estado.registros.slice());
            if (!termo) return todos;
            return todos.filter(r => textoPesquisavel(r).includes(termo));
        };

        function renderizar() {
            const lista = filtrados();
            const total = lista.length;
            const totalPaginas = Math.max(1, Math.ceil(total / tamanhoPagina));
            estado.pagina = Math.min(Math.max(1, estado.pagina), totalPaginas);

            const inicio = (estado.pagina - 1) * tamanhoPagina;
            const fim = Math.min(inicio + tamanhoPagina, total);
            const visiveis = lista.slice(inicio, fim);

            if (!visiveis.length) {
                corpo.innerHTML = '<p class="tabela__vazio">' +
                    (estado.consulta
                        ? 'Nenhum resultado para “' + escapar(estado.consulta) + '”.'
                        : (entidade.artigo === 'a' ? 'Nenhuma ' : 'Nenhum ') + escapar(entidade.singular) + ' cadastrad' + (entidade.artigo === 'a' ? 'a' : 'o') + ' ainda.') +
                    '</p>';
            } else {
                corpo.innerHTML = visiveis.map(registro =>
                    '<div class="tabela__linha" role="row" data-id="' + registro.id + '">' +
                        colunas.map(c =>
                            '<div class="tabela__celula" role="cell">' + escapar(textoCelula(c, registro)) + '</div>'
                        ).join('') +
                        '<div class="tabela__celula tabela__acoes" role="cell">' +
                            '<button type="button" class="icone-acao" data-acao="editar" aria-label="Editar ' + escapar(entidade.singular) + ' ' + escapar(registro[config.rotuloLinha || colunas[0].chave]) + '">' + (ICONES.pencil || '') + '</button>' +
                            '<button type="button" class="icone-acao icone-acao--perigo" data-acao="remover" aria-label="Deletar ' + escapar(entidade.singular) + ' ' + escapar(registro[config.rotuloLinha || colunas[0].chave]) + '">' + (ICONES.trash || '') + '</button>' +
                        '</div>' +
                    '</div>'
                ).join('');
            }

            intervalo.textContent = (total ? inicio + 1 : 0) + ' - ' + fim + ' de ' + total;
            botaoAnterior.disabled = estado.pagina <= 1;
            botaoProxima.disabled = estado.pagina >= totalPaginas;

            if (estado.destacarId != null) {
                const linha = $('[data-id="' + estado.destacarId + '"]', corpo);
                if (linha) {
                    linha.classList.add('destaque');
                    linha.scrollIntoView({ block: 'nearest' });
                }
                estado.destacarId = null;
            }
        }

        async function recarregar() {
            // carrega dados de outros cadastros usados na tabela (ex.: nome do frigorífico)
            if (config.aoCarregar) await config.aoCarregar();
            estado.registros = await repositorio.listar();
            renderizar();
        }

        /* ---------- formulário ---------- */

        function htmlCampo(campo, valor, modo) {
            const textoAjuda = modo === 'editar' && campo.ajudaEdicao ? campo.ajudaEdicao : campo.ajuda;
            const id = 'campo-' + campo.nome;
            const classe = 'campo' + (campo.tamanho === 'metade' ? '' : ' campo--cheio');
            const ajuda = textoAjuda ? '<p class="campo__ajuda" id="' + id + '-ajuda">' + escapar(textoAjuda) + '</p>' : '';
            let controle;

            if (campo.tipo === 'lista') {
                const placeholder = campo.placeholder || 'Escolha uma opção';
                const opcoes = opcoesDe(campo);
                const atual = opcoes.find(o => String(o.valor) === String(valor ?? ''));
                controle =
                    '<div class="lista" data-lista="' + campo.nome + '">' +
                        '<input type="hidden" name="' + campo.nome + '" value="' + escapar(atual ? atual.valor : '') + '">' +
                        '<button type="button" class="lista__botao' + (atual ? '' : ' lista__botao--vazio') + '" id="' + id + '" role="combobox" aria-haspopup="listbox" aria-expanded="false" aria-controls="' + id + '-opcoes" data-placeholder="' + escapar(placeholder) + '">' +
                            '<span class="lista__valor">' + escapar(atual ? atual.rotulo : placeholder) + '</span>' +
                            '<span class="lista__seta">' + EXTRA.seta + '</span>' +
                        '</button>' +
                        '<ul class="lista__opcoes" id="' + id + '-opcoes" role="listbox" aria-labelledby="' + id + '-rotulo">' +
                            opcoes.map((o, i) =>
                                '<li class="lista__opcao" role="option" id="' + id + '-op' + i + '" data-valor="' + escapar(o.valor) + '" aria-selected="' + Boolean(atual && String(atual.valor) === String(o.valor)) + '">' + escapar(o.rotulo) + '</li>'
                            ).join('') +
                        '</ul>' +
                    '</div>';
            } else if (campo.tipo === 'senha') {
                controle =
                    '<input class="campo__entrada campo__entrada--senha" id="' + id + '" name="' + campo.nome + '" type="password" autocomplete="new-password" placeholder="' + escapar(campo.placeholder || 'Digite aqui...') + '"' +
                    (textoAjuda ? ' aria-describedby="' + id + '-ajuda"' : '') + '>' +
                    '<button type="button" class="campo__olho" data-olho aria-label="Mostrar senha" aria-pressed="false">' + EXTRA.olho + '</button>';
            } else {
                const tipoHtml = campo.tipo === 'email' ? 'email' : 'text';
                const modo = (campo.tipo === 'cpf' || campo.tipo === 'data') ? ' inputmode="numeric"'
                    : (campo.tipo === 'numero' ? ' inputmode="decimal"'
                    : (campo.tipo === 'email' ? ' inputmode="email"' : ''));
                controle =
                    '<input class="campo__entrada" id="' + id + '" name="' + campo.nome + '" type="' + tipoHtml + '"' + modo +
                    ' autocomplete="' + escapar(campo.autocompletar || 'off') + '"' +
                    ' placeholder="' + escapar(campo.placeholder || 'Digite aqui...') + '"' +
                    (campo.maximo ? ' maxlength="' + campo.maximo + '"' : '') +
                    ' value="' + escapar(valor ?? '') + '"' +
                    (textoAjuda ? ' aria-describedby="' + id + '-ajuda"' : '') + '>';
            }

            return '<div class="' + classe + '" data-campo="' + campo.nome + '">' +
                '<label class="campo__rotulo" id="' + id + '-rotulo" for="' + id + '">' + escapar(campo.rotulo) + '</label>' +
                '<div class="campo__controle">' + controle + '</div>' +
                ajuda +
                '<p class="campo__erro" role="alert" hidden></p>' +
            '</div>';
        }

        // quantas casas decimais o texto tem ("1.160,5" -> 1; "1.160" -> 0; "12.5" -> 1)
        function casasDecimais(texto) {
            const t = String(texto).trim();
            if (t.includes(',')) return t.split(',')[1].length;
            if (/^-?\d{1,3}(\.\d{3})+$/.test(t)) return 0;
            return (t.split('.')[1] || '').length;
        }

        // valor guardado -> texto do formulário
        function valorParaFormulario(campo, valor) {
            if (campo.tipo === 'numero') return Formato.numeroParaTexto(valor);
            if (campo.tipo === 'data') return Formato.isoParaData(valor);
            return valor;
        }

        // texto do formulário -> valor guardado (número, data ISO...)
        function tipar(valores) {
            const tipados = { ...valores };
            campos.forEach(campo => {
                const valor = valores[campo.nome];
                if (campo.tipo === 'numero') tipados[campo.nome] = valor === '' ? null : Formato.textoParaNumero(valor);
                if (campo.tipo === 'data') tipados[campo.nome] = valor === '' ? null : Formato.dataParaIso(valor);
            });
            return tipados;
        }

        function lerValores(formulario) {
            const valores = {};
            campos.forEach(campo => {
                const el = formulario.elements[campo.nome];
                valores[campo.nome] = el ? el.value.trim() : '';
            });
            return valores;
        }

        function mostrarErro(formulario, nome, mensagem) {
            const bloco = $('[data-campo="' + nome + '"]', formulario);
            if (!bloco) return;
            const erro = $('.campo__erro', bloco);
            bloco.classList.toggle('campo--invalido', Boolean(mensagem));
            erro.hidden = !mensagem;
            erro.textContent = mensagem || '';
            const controle = $('input:not([type="hidden"]), .lista__botao', bloco);
            if (controle) {
                if (mensagem) controle.setAttribute('aria-invalid', 'true'); else controle.removeAttribute('aria-invalid');
            }
        }

        function validar(valores, modo, atual) {
            const erros = {};

            campos.forEach(campo => {
                const valor = valores[campo.nome];
                const opcional = modo === 'editar' && campo.opcionalNaEdicao;

                if (!valor) {
                    if (campo.obrigatorio && !opcional) erros[campo.nome] = campo.mensagemObrigatorio || 'Campo obrigatório.';
                    return;
                }

                if (campo.tipo === 'email' && !Validar.email(valor)) {
                    erros[campo.nome] = 'Digite um e-mail válido.';
                } else if (campo.tipo === 'cpf' && !Validar.cpf(valor)) {
                    erros[campo.nome] = 'CPF inválido.';
                } else if (campo.tipo === 'numero') {
                    const numero = Formato.textoParaNumero(valor);
                    const casas = campo.casas ?? 2;
                    if (numero === null) {
                        erros[campo.nome] = 'Digite um número válido.';
                    } else if (casasDecimais(valor) > casas) {
                        erros[campo.nome] = 'Use no máximo ' + casas + ' casas decimais.';
                    } else if (campo.valorMinimo !== undefined && numero < campo.valorMinimo) {
                        erros[campo.nome] = campo.mensagemMinimo || 'O valor mínimo é ' + Formato.numeroParaTexto(campo.valorMinimo) + '.';
                    } else if (campo.valorMaximo !== undefined && numero > campo.valorMaximo) {
                        erros[campo.nome] = campo.mensagemMaximo || 'O valor máximo é ' + Formato.numeroParaTexto(campo.valorMaximo) + '.';
                    }
                } else if (campo.tipo === 'data' && !Formato.dataParaIso(valor)) {
                    erros[campo.nome] = 'Digite uma data válida (dd/mm/aaaa).';
                } else if (campo.minimo && valor.length < campo.minimo) {
                    erros[campo.nome] = 'Use pelo menos ' + campo.minimo + ' caracteres.';
                } else if (campo.tipo === 'lista' && !opcoesDe(campo).some(o => String(o.valor) === valor)) {
                    erros[campo.nome] = 'Escolha uma das opções.';
                } else if (campo.unico) {
                    const repetido = estado.registros.some(r =>
                        (!atual || r.id !== atual.id) &&
                        normalizar(r[campo.nome]) === normalizar(valor)
                    );
                    if (repetido) erros[campo.nome] = campo.mensagemUnico || 'Já existe um cadastro com este valor.';
                }
            });

            // regras entre campos (ex.: validade depois da fabricação); recebe os valores já tipados
            if (config.validarExtra) {
                const extras = config.validarExtra(tipar(valores), estado.registros, atual) || {};
                Object.keys(extras).forEach(nome => { if (!erros[nome]) erros[nome] = extras[nome]; });
            }
            return erros;
        }

        function abrirFormulario(modo, registro) {
            const editando = modo === 'editar';
            const valoresIniciais = editando ? registro : (config.valoresPadrao || {});

            const formulario = document.createElement('form');
            formulario.className = 'formulario';
            formulario.noValidate = true;
            formulario.innerHTML =
                '<div class="formulario__grade">' +
                    campos.map(campo => htmlCampo(campo, valorParaFormulario(campo, valoresIniciais[campo.nome]), modo)).join('') +
                '</div>';

            const idFormulario = 'formulario-' + Math.random().toString(36).slice(2, 8);
            formulario.id = idFormulario;

            const rodape =
                (editando ? '<button type="button" class="botao botao--perigo" data-deletar>' + escapar(entidade.rotuloDeletar) + '</button>' : '') +
                '<span class="janela__espaco"></span>' +
                '<button type="button" class="botao botao--claro" data-cancelar>Cancelar</button>' +
                '<button type="submit" form="' + idFormulario + '" class="botao botao--principal" data-salvar>' +
                    EXTRA.salvar + '<span>' + escapar(editando ? entidade.rotuloSalvarEdicao : entidade.rotuloSalvar) + '</span>' +
                '</button>';

            const janela = criarJanela({
                titulo: editando ? entidade.rotuloEditar : entidade.rotuloNovo,
                icone: EXTRA.maisCirculo,
                variante: config.janela,
                corpo: formulario,
                rodape
            });

            // listas suspensas
            const listas = {};
            $$('[data-lista]', formulario).forEach(el => {
                listas[el.dataset.lista] = iniciarLista(el, () => mostrarErro(formulario, el.dataset.lista, ''));
            });

            // máscaras e limpeza do erro enquanto digita
            campos.forEach(campo => {
                const el = formulario.elements[campo.nome];
                if (!el || campo.tipo === 'lista') return;
                el.addEventListener('input', () => {
                    if (campo.tipo === 'cpf') el.value = Mascara.cpf(el.value);
                    if (campo.tipo === 'data') el.value = Mascara.data(el.value);
                    mostrarErro(formulario, campo.nome, '');
                });
            });

            // mostrar/ocultar senha
            $$('[data-olho]', formulario).forEach(botao => {
                botao.addEventListener('click', () => {
                    const entrada = $('input', botao.parentElement);
                    const visivel = entrada.type === 'password';
                    entrada.type = visivel ? 'text' : 'password';
                    botao.innerHTML = visivel ? EXTRA.olhoRiscado : EXTRA.olho;
                    botao.setAttribute('aria-pressed', String(visivel));
                    botao.setAttribute('aria-label', visivel ? 'Ocultar senha' : 'Mostrar senha');
                });
            });

            $('[data-cancelar]', janela.rodape).addEventListener('click', () => janela.fechar('cancelar'));

            const botaoDeletar = $('[data-deletar]', janela.rodape);
            if (botaoDeletar) {
                botaoDeletar.addEventListener('click', async () => {
                    if (await pedirExclusao(registro)) janela.fechar('deletado');
                });
            }

            formulario.addEventListener('submit', async evento => {
                evento.preventDefault();

                const valores = lerValores(formulario);
                const erros = validar(valores, modo, registro);

                campos.forEach(campo => mostrarErro(formulario, campo.nome, erros[campo.nome]));

                const primeiroErro = campos.find(campo => erros[campo.nome]);
                if (primeiroErro) {
                    const alvo = $('[data-campo="' + primeiroErro.nome + '"] input:not([type="hidden"]), [data-campo="' + primeiroErro.nome + '"] .lista__botao', formulario);
                    if (alvo) alvo.focus();
                    return;
                }

                const botaoSalvar = $('[data-salvar]', janela.rodape);
                botaoSalvar.disabled = true;

                try {
                    const tipados = tipar(valores);
                    const dados = config.aoSalvar ? config.aoSalvar(tipados, { modo, registro }) : tipados;
                    const salvo = editando
                        ? await repositorio.atualizar(registro.id, dados)
                        : await repositorio.criar(dados);

                    estado.consulta = '';
                    campoPesquisa.value = '';
                    estado.destacarId = salvo.id;

                    // vai para a página onde o registro ficou
                    estado.registros = await repositorio.listar();
                    const indice = filtrados().findIndex(r => r.id === salvo.id);
                    estado.pagina = indice >= 0 ? Math.floor(indice / tamanhoPagina) + 1 : 1;
                    renderizar();

                    janela.fechar('salvo');
                    aviso(editando ? 'Alterações salvas.' : capitalizar(entidade.singular) + ' cadastrado' + (entidade.artigo === 'a' ? 'a' : '') + ' com sucesso.');
                } catch (erro) {
                    botaoSalvar.disabled = false;
                    aviso('Não foi possível salvar. Tente novamente.', 'erro');
                }
            });

            const primeiro = $('input:not([type="hidden"])', formulario);
            if (primeiro) setTimeout(() => primeiro.focus(), 60);
        }

        /* ---------- exclusão ---------- */

        async function pedirExclusao(registro) {
            // ex.: categoria que ainda tem lotes não pode ser deletada (regra do banco)
            if (config.validarExclusao) {
                const impedimento = config.validarExclusao(registro, estado.registros);
                if (impedimento) {
                    aviso(impedimento, 'erro');
                    return false;
                }
            }

            const nome = escapar(registro[config.rotuloLinha || colunas[0].chave]);
            const confirmado = await confirmar({
                titulo: entidade.rotuloDeletar + '?',
                texto: 'Você vai deletar <strong>' + nome + '</strong>. Essa ação não pode ser desfeita.',
                rotuloConfirmar: 'Deletar',
                perigo: true
            });
            if (!confirmado) return false;

            try {
                await repositorio.remover(registro.id);
                await recarregar();
                aviso(capitalizar(entidade.singular) + ' deletad' + (entidade.artigo === 'a' ? 'a' : 'o') + '.');
                return true;
            } catch (erro) {
                aviso('Não foi possível deletar. Tente novamente.', 'erro');
                return false;
            }
        }

        const capitalizar = texto => texto.charAt(0).toUpperCase() + texto.slice(1);

        /* ---------- eventos ---------- */

        let tempoPesquisa = null;
        campoPesquisa.addEventListener('input', () => {
            clearTimeout(tempoPesquisa);
            tempoPesquisa = setTimeout(() => {
                estado.consulta = campoPesquisa.value;
                estado.pagina = 1;
                renderizar();
            }, 120);
        });

        campoPesquisa.addEventListener('keydown', evento => {
            if (evento.key === 'Escape' && campoPesquisa.value) {
                campoPesquisa.value = '';
                estado.consulta = '';
                estado.pagina = 1;
                renderizar();
            }
        });

        botaoAdicionar.addEventListener('click', () => abrirFormulario('criar'));

        botaoAnterior.addEventListener('click', () => { estado.pagina -= 1; renderizar(); corpo.scrollTop = 0; });
        botaoProxima.addEventListener('click', () => { estado.pagina += 1; renderizar(); corpo.scrollTop = 0; });

        corpo.addEventListener('click', async evento => {
            const botao = evento.target.closest('[data-acao]');
            if (!botao) return;
            const id = Number(botao.closest('[data-id]').dataset.id);
            const registro = await repositorio.obter(id);
            if (!registro) return;
            if (botao.dataset.acao === 'editar') abrirFormulario('editar', registro);
            if (botao.dataset.acao === 'remover') pedirExclusao(registro);
        });

        // Esc fecha primeiro a camada mais de cima
        document.addEventListener('keydown', evento => {
            if (evento.key !== 'Escape') return;
            if (pilhaJanelas.length) {
                pilhaJanelas[pilhaJanelas.length - 1].fechar('esc');
            } else if (guia && guia.aberta()) {
                guia.fechar();
            }
        });

        recarregar();
    }

    window.Crud = { iniciar, repositorioLocal, repositorioApi, Validar, Mascara, Formato, aviso, confirmar };
})();
