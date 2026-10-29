/* ============================================================
   SKADI · TELAS DE CADASTRO (versão JSP)
   As páginas lista-*.jsp e form-*.jsp já vêm prontas do servidor
   (servlet + JSTL). Este arquivo só acrescenta o comportamento:

     - menu lateral ("guia")
     - pesquisa e paginação da tabela
     - confirmação antes de deletar
     - máscaras e validação dos formulários
     - tela de unidades (escolher / editar)

   Nada aqui depende de dados de exemplo ou do localStorage para
   guardar cadastros: quem grava é o servlet.
   ============================================================ */

(function () {
    'use strict';

    /* ---------- utilidades ---------- */

    const $ = (seletor, raiz = document) => raiz.querySelector(seletor);
    const $$ = (seletor, raiz = document) => Array.from(raiz.querySelectorAll(seletor));

    const normalizar = valor => String(valor ?? '')
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .toLowerCase()
        .trim();

    const somenteDigitos = valor => String(valor ?? '').replace(/\D/g, '');

    /* ============================================================
       NOME DA UNIDADE NO CABEÇALHO
       (a tela de unidades guarda a escolha; o servidor não guarda)
       ============================================================ */

    function mostrarUnidadeAtual() {
        const titulo = $('[data-unidade-atual]');
        if (!titulo) return;

        try {
            const unidade = JSON.parse(localStorage.getItem('skadi.unidadeAtual') || 'null');
            if (unidade && unidade.nome) titulo.textContent = unidade.nome;
        } catch (erro) { /* sem armazenamento: mantém o texto do servidor */ }
    }

    /* ============================================================
       MENU LATERAL ("guia")
       ============================================================ */

    function iniciarGuia() {
        const botao = $('[data-guia-botao]');
        const guia = $('[data-guia]');
        const fundo = $('[data-guia-fundo]');
        if (!botao || !guia) return;

        const definir = aberta => {
            guia.classList.toggle('aberta', aberta);
            if (fundo) fundo.classList.toggle('aberta', aberta);
            botao.setAttribute('aria-expanded', String(aberta));
            botao.setAttribute('aria-label', aberta ? 'Fechar menu' : 'Abrir menu');
        };

        botao.addEventListener('click', () => definir(!guia.classList.contains('aberta')));
        if (fundo) fundo.addEventListener('click', () => definir(false));

        document.addEventListener('keydown', evento => {
            if (evento.key === 'Escape' && guia.classList.contains('aberta') && !$('.janela__fundo')) definir(false);
        });
    }

    /* ============================================================
       JANELA DE CONFIRMAÇÃO
       ============================================================ */

    function confirmar({ titulo, nome, rotuloConfirmar }) {
        return new Promise(resolve => {
            const anterior = document.activeElement;

            const fundo = document.createElement('div');
            fundo.className = 'janela__fundo';
            fundo.innerHTML =
                '<div class="janela janela--pequena" role="alertdialog" aria-modal="true" aria-labelledby="confirmar-titulo">' +
                    '<header class="janela__topo">' +
                        '<h2 class="janela__titulo" id="confirmar-titulo"></h2>' +
                    '</header>' +
                    '<div class="janela__corpo"><p class="janela__texto"></p></div>' +
                    '<footer class="janela__rodape">' +
                        '<span class="janela__espaco"></span>' +
                        '<button type="button" class="botao botao--claro" data-nao>Cancelar</button>' +
                        '<button type="button" class="botao botao--perigo" data-sim></button>' +
                    '</footer>' +
                '</div>';

            // textos entram como texto (nada de HTML vindo do banco)
            $('.janela__titulo', fundo).textContent = titulo;
            const texto = $('.janela__texto', fundo);
            texto.append('Você vai deletar ');
            const destaque = document.createElement('strong');
            destaque.textContent = nome;
            texto.append(destaque, '. Essa ação não pode ser desfeita.');
            $('[data-sim]', fundo).textContent = rotuloConfirmar;

            const fechar = resultado => {
                fundo.classList.remove('aberta');
                document.removeEventListener('keydown', aoTeclar);
                setTimeout(() => fundo.remove(), 200);
                document.body.classList.remove('travado');
                if (anterior && document.contains(anterior)) anterior.focus();
                resolve(resultado);
            };

            const aoTeclar = evento => {
                if (evento.key === 'Escape') fechar(false);
                if (evento.key === 'Tab') {
                    const itens = $$('button', fundo);
                    const primeiro = itens[0];
                    const ultimo = itens[itens.length - 1];
                    if (evento.shiftKey && document.activeElement === primeiro) { evento.preventDefault(); ultimo.focus(); }
                    else if (!evento.shiftKey && document.activeElement === ultimo) { evento.preventDefault(); primeiro.focus(); }
                }
            };

            $('[data-nao]', fundo).addEventListener('click', () => fechar(false));
            $('[data-sim]', fundo).addEventListener('click', () => fechar(true));
            fundo.addEventListener('mousedown', evento => { if (evento.target === fundo) fechar(false); });
            document.addEventListener('keydown', aoTeclar);

            document.body.appendChild(fundo);
            document.body.classList.add('travado');
            requestAnimationFrame(() => fundo.classList.add('aberta'));
            $('[data-nao]', fundo).focus();
        });
    }

    /* links com data-confirmar pedem confirmação antes de seguir (deletar) */
    function iniciarExclusao() {
        document.addEventListener('click', async evento => {
            const link = evento.target.closest('a[data-confirmar]');
            if (!link) return;

            evento.preventDefault();

            const confirmado = await confirmar({
                titulo: link.dataset.confirmar,
                nome: link.dataset.nome || '',
                rotuloConfirmar: 'Deletar'
            });

            if (confirmado) window.location.href = link.href;
        });
    }

    /* ============================================================
       TABELA: pesquisa e paginação (sobre as linhas que o servidor enviou)
       ============================================================ */

    function iniciarTabela() {
        const corpo = $('[data-tabela-corpo]');
        if (!corpo) return;

        const tamanhoPagina = Number(corpo.dataset.tamanhoPagina) || 20;
        const campoPesquisa = $('[data-crud-pesquisa]');
        const intervalo = $('[data-crud-intervalo]');
        const anterior = $('[data-crud-anterior]');
        const proxima = $('[data-crud-proxima]');
        const vazio = $('[data-tabela-vazio]');
        const linhas = $$('.tabela__linha', corpo);

        let pagina = 1;
        let consulta = '';

        const indices = linhas.map(linha => normalizar(linha.dataset.busca || linha.textContent));

        function renderizar() {
            const termo = normalizar(consulta);
            const visiveis = linhas.filter((linha, i) => !termo || indices[i].includes(termo));
            const total = visiveis.length;
            const totalPaginas = Math.max(1, Math.ceil(total / tamanhoPagina));
            pagina = Math.min(Math.max(1, pagina), totalPaginas);

            const inicio = (pagina - 1) * tamanhoPagina;
            const fim = Math.min(inicio + tamanhoPagina, total);

            linhas.forEach(linha => { linha.hidden = true; });
            visiveis.slice(inicio, fim).forEach(linha => { linha.hidden = false; });

            if (vazio) {
                vazio.hidden = total > 0;
                vazio.textContent = linhas.length && consulta
                    ? 'Nenhum resultado para “' + consulta + '”.'
                    : (vazio.dataset.texto || 'Nenhum registro cadastrado ainda.');
            }

            if (intervalo) intervalo.textContent = (total ? inicio + 1 : 0) + ' - ' + fim + ' de ' + total;
            if (anterior) anterior.disabled = pagina <= 1;
            if (proxima) proxima.disabled = pagina >= totalPaginas;
        }

        let tempo = null;
        if (campoPesquisa) {
            campoPesquisa.addEventListener('input', () => {
                clearTimeout(tempo);
                tempo = setTimeout(() => { consulta = campoPesquisa.value; pagina = 1; renderizar(); }, 120);
            });

            campoPesquisa.addEventListener('keydown', evento => {
                if (evento.key === 'Escape' && campoPesquisa.value) {
                    campoPesquisa.value = '';
                    consulta = '';
                    pagina = 1;
                    renderizar();
                }
            });
        }

        if (anterior) anterior.addEventListener('click', () => { pagina -= 1; renderizar(); corpo.scrollTop = 0; });
        if (proxima) proxima.addEventListener('click', () => { pagina += 1; renderizar(); corpo.scrollTop = 0; });

        renderizar();
    }

    /* ============================================================
       FORMULÁRIOS: máscaras e validação
       Os servlets validam de novo; aqui é só para avisar mais cedo.
       ============================================================ */

    const Mascara = {
        cpf(valor) {
            const d = somenteDigitos(valor).slice(0, 11);
            return d
                .replace(/^(\d{3})(\d)/, '$1.$2')
                .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
                .replace(/\.(\d{3})(\d)/, '.$1-$2');
        },

        cnpj(valor) {
            const d = somenteDigitos(valor).slice(0, 14);
            return d
                .replace(/^(\d{2})(\d)/, '$1.$2')
                .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
                .replace(/\.(\d{3})(\d)/, '.$1/$2')
                .replace(/(\d{4})(\d)/, '$1-$2');
        },

        cep(valor) {
            const d = somenteDigitos(valor).slice(0, 8);
            return d.length > 5 ? d.slice(0, 5) + '-' + d.slice(5) : d;
        },

        uf: valor => String(valor ?? '').toUpperCase().replace(/[^A-Z]/g, '').slice(0, 2)
    };

    const Validar = {
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
        },

        cnpj(valor) {
            const d = somenteDigitos(valor);
            if (d.length !== 14 || /^(\d)\1{13}$/.test(d)) return false;
            const calcular = (base, pesos) => {
                const soma = base.split('').reduce((total, n, i) => total + Number(n) * pesos[i], 0);
                const resto = soma % 11;
                return resto < 2 ? 0 : 11 - resto;
            };
            const dv1 = calcular(d.slice(0, 12), [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);
            const dv2 = calcular(d.slice(0, 12) + dv1, [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);
            return dv1 === Number(d[12]) && dv2 === Number(d[13]);
        },

        cep: valor => /^\d{5}-?\d{3}$/.test(String(valor ?? '').trim()),

        uf: valor => ['AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'].includes(String(valor ?? '').toUpperCase()),

        email: valor => /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(String(valor ?? ''))
    };

    /* vida útil: "1160" vira "1160:00:00" (formato de intervalo do PostgreSQL) */
    const normalizarIntervalo = valor => /^\d+$/.test(valor.trim()) ? valor.trim() + ':00:00' : valor.trim();

    function mostrarErro(campo, mensagem) {
        const bloco = campo.closest('.campo');
        if (!bloco) return;

        let erro = $('.campo__erro', bloco);
        if (!erro) {
            erro = document.createElement('p');
            erro.className = 'campo__erro';
            erro.setAttribute('role', 'alert');
            bloco.appendChild(erro);
        }

        bloco.classList.toggle('campo--invalido', Boolean(mensagem));
        erro.hidden = !mensagem;
        erro.textContent = mensagem || '';

        if (mensagem) campo.setAttribute('aria-invalid', 'true'); else campo.removeAttribute('aria-invalid');
    }

    function mensagemDoCampo(campo, formulario) {
        const valor = campo.value.trim();

        if (campo.required && !valor) return 'Campo obrigatório.';
        if (!valor) return '';

        switch (campo.dataset.validar) {
            case 'cpf': if (!Validar.cpf(valor)) return 'CPF inválido.'; break;
            case 'cnpj': if (!Validar.cnpj(valor)) return 'CNPJ inválido.'; break;
            case 'cep': if (!Validar.cep(valor)) return 'CEP inválido.'; break;
            case 'uf': if (!Validar.uf(valor)) return 'Use a sigla de um estado (ex.: SP).'; break;
            case 'email': if (!Validar.email(valor)) return 'Digite um e-mail válido.'; break;
            default: break;
        }

        if (campo.minLength > 0 && valor.length < campo.minLength) return 'Use pelo menos ' + campo.minLength + ' caracteres.';

        if (campo.type === 'number') {
            const numero = Number(valor);
            if (Number.isNaN(numero)) return 'Digite um número válido.';
            if (campo.min !== '' && numero < Number(campo.min)) return 'O valor mínimo é ' + campo.min + '.';
            if (campo.max !== '' && numero > Number(campo.max)) return 'O valor máximo é ' + campo.max + '.';
        }

        // regras entre campos
        if (campo.dataset.maiorQue) {
            const outro = formulario.elements[campo.dataset.maiorQue];
            if (outro && outro.value !== '' && Number(valor) <= Number(outro.value)) return 'A máxima precisa ser maior que a mínima.';
        }

        if (campo.dataset.naoAntesDe) {
            const outro = formulario.elements[campo.dataset.naoAntesDe];
            if (outro && outro.value && valor < outro.value) return 'A validade não pode ser antes da fabricação.';
        }

        if (campo.dataset.intervalo !== undefined && !/^\d+(:\d{2}){1,2}$/.test(normalizarIntervalo(valor))) {
            return 'Use horas (ex.: 1160) ou HH:MM:SS.';
        }

        return '';
    }

    function iniciarFormulario(formulario) {
        const campos = $$('input:not([type="hidden"]), select, textarea', formulario);

        // máscaras
        campos.forEach(campo => {
            const mascara = Mascara[campo.dataset.mascara];

            // valores que vêm do banco só com dígitos (CPF, CNPJ, CEP) entram já formatados
            if (mascara && campo.value) campo.value = mascara(campo.value);

            campo.addEventListener('input', () => {
                if (mascara) campo.value = mascara(campo.value);
                if (campo.closest('.campo--invalido')) mostrarErro(campo, mensagemDoCampo(campo, formulario));
            });

            campo.addEventListener('change', () => {
                if (campo.dataset.intervalo !== undefined) campo.value = normalizarIntervalo(campo.value);
            });

            campo.addEventListener('blur', () => {
                if (campo.value.trim() !== '') mostrarErro(campo, mensagemDoCampo(campo, formulario));
            });
        });

        // mostrar/ocultar senha
        $$('[data-olho]', formulario).forEach(botao => {
            botao.addEventListener('click', () => {
                const entrada = $('input', botao.parentElement);
                const visivel = entrada.type === 'password';
                entrada.type = visivel ? 'text' : 'password';
                const imagem = $('img', botao);
            imagem.src = imagem.src.replace(visivel ? 'olho_ac.svg' : 'olho_fc.svg', visivel ? 'olho_fc.svg' : 'olho_ac.svg');
                botao.setAttribute('aria-pressed', String(visivel));
                botao.setAttribute('aria-label', visivel ? 'Ocultar senha' : 'Mostrar senha');
            });
        });

        // validação ao enviar (o formulário usa novalidate: as mensagens são as do site)
        formulario.addEventListener('submit', evento => {
            let primeiroComErro = null;

            campos.forEach(campo => {
                if (campo.disabled) return;
                if (campo.dataset.intervalo !== undefined) campo.value = normalizarIntervalo(campo.value);

                const mensagem = mensagemDoCampo(campo, formulario);
                mostrarErro(campo, mensagem);
                if (mensagem && !primeiroComErro) primeiroComErro = campo;
            });

            if (primeiroComErro) {
                evento.preventDefault();
                primeiroComErro.focus();
                return;
            }

            const enviar = $('[type="submit"]', formulario);
            if (enviar) enviar.disabled = true;      // evita duplo clique
        });
    }

    /* ============================================================
       TELA DE UNIDADES (lista-cd.jsp)
       ============================================================ */

    function iniciarUnidades() {
        const raiz = $('[data-unidades]');
        if (!raiz) return;

        const pergunta = $('[data-pergunta]', raiz);
        const botaoEditar = $('[data-unidades-editar]', raiz);
        const botaoCancelar = $('[data-unidades-cancelar]', raiz);
        const campoPesquisa = $('[data-crud-pesquisa]', raiz);
        const lista = $('[data-unidades-lista]', raiz);
        const semResultado = $('[data-unidades-sem-resultado]', raiz);
        const cartoes = $$('.unidade', lista);

        // saudação: nome de quem entrou (vem do servidor); sem nome fica só "Olá!"
        const definirModo = modo => {
            raiz.dataset.modo = modo;
            const editando = modo === 'editar';
            pergunta.textContent = editando ? 'Qual unidade você gostaria de editar?' : 'Qual unidade você gostaria de acessar?';
            botaoCancelar.hidden = !editando;
        };

        if (botaoEditar) botaoEditar.addEventListener('click', () => {
            definirModo('editar');
            if (cartoes[0]) cartoes[0].focus();
        });

        if (botaoCancelar) botaoCancelar.addEventListener('click', () => {
            definirModo('acessar');
            if (botaoEditar) botaoEditar.focus();
        });

        document.addEventListener('keydown', evento => {
            if (evento.key === 'Escape' && raiz.dataset.modo === 'editar') definirModo('acessar');
        });

        // clicar num cartão: acessar (guarda a unidade escolhida) ou editar
        lista.addEventListener('click', evento => {
            const cartao = evento.target.closest('.unidade');
            if (!cartao) return;

            if (raiz.dataset.modo === 'editar') {
                evento.preventDefault();
                window.location.href = cartao.dataset.hrefEditar;
                return;
            }

            try {
                localStorage.setItem('skadi.unidadeAtual', JSON.stringify({ id: Number(cartao.dataset.id), nome: cartao.dataset.nome }));
            } catch (erro) { /* sem armazenamento: segue mesmo assim */ }
        });

        // pesquisa por CNPJ (com ou sem pontuação), nome ou cidade
        if (campoPesquisa) {
            campoPesquisa.addEventListener('input', () => {
                const termo = normalizar(campoPesquisa.value);
                const termoDigitos = somenteDigitos(campoPesquisa.value);
                let achou = 0;

                cartoes.forEach(cartao => {
                    const texto = normalizar(cartao.dataset.busca);
                    const bate = !termo || texto.includes(termo) || (termoDigitos && somenteDigitos(cartao.dataset.busca).includes(termoDigitos));
                    cartao.parentElement.hidden = !bate;
                    if (bate) achou += 1;
                });

                if (semResultado) semResultado.hidden = achou > 0 || cartoes.length === 0;
            });
        }
    }

    /* ---------- início ---------- */

    document.addEventListener('DOMContentLoaded', () => {
        mostrarUnidadeAtual();
        iniciarGuia();
        iniciarExclusao();
        iniciarTabela();
        iniciarUnidades();
        $$('form[data-form]').forEach(iniciarFormulario);
    });
})();
