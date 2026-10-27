# Porte HTML → JSP · SKADI

As páginas da pasta `HTMLs/` agora existem como JSP dentro de `src/main/webapp/`, ligadas aos servlets que já
estavam no projeto. A pasta `HTMLs/` foi mantida como estava (protótipos); o que vai para o servidor é o `webapp`.

Requisitos (os mesmos do `pom.xml`): Tomcat 10.1+ (Jakarta Servlet 6), JSTL `jakarta.servlet.jsp.jstl` 3.0.

## Estrutura do webapp

```
src/main/webapp/
├── css/            styleCadastro.css · styleCrud.css · styleLanding.css · styleLoginCliente.css · styleLoginSistema.css
│                   styleMensagem.css · styleSobreNos.css · styleTrimmy.css · styleUnidades.css · monitoramento.css
├── js/             crud.js · cadastro.js · senha.js · landing.js · monitoramento.js · sobreNos.js
├── Imagens/        todas as imagens e fontes (mesmos nomes de antes; só "gr#U00e1fico.svg" virou "grafico.svg")
├── WEB-INF/
│   ├── web.xml     página inicial, UTF-8 e limpeza de espaços dos JSPs
│   └── jspf/       icones-crud.jspf (sprite SVG) · topo-crud.jspf (cabeçalho + menu) · erro-form.jspf
├── landing.jsp · login-cliente.jsp · login-sistema.jsp · cadastro.jsp · msg-enviada.jsp · ajuda-trimmy.jsp · sobre-nos.jsp
├── login.jsp                  encaminha para login-cliente.jsp (o LoginServlet redireciona para "login.jsp")
├── esqueci-senha.jsp · redefinir-senha.jsp    (já existiam; não foram alterados)
└── lista-*.jsp · form-*.jsp   usuario · frigorifico · termometro · categoria · lote · endereco · cd
```

## HTML → JSP

| HTML (`HTMLs/`) | JSP | Observação |
|---|---|---|
| `landing.html` | `landing.jsp` | página inicial do contexto (`welcome-file`) |
| `loginCliente.html` | `login-cliente.jsp` | `POST /login` · campos `login` e `senha` · mostra `?erro=credenciais` |
| `loginSistema.html` | `login-sistema.jsp` | idem |
| `cadastro.html` | `cadastro.jsp` | ainda sem servlet (veja Pendências) |
| `msgEnviada.html` | `msg-enviada.jsp` | |
| `ajudaTrimmy.html` | `ajuda-trimmy.jsp` | |
| `sobreNos.html` | `sobre-nos.jsp` | |
| `unidades` (`cd`) | `lista-cd.jsp` · `form-cd.jsp` | tela de unidades em cartões |
| `funcionarios.html` | `lista-usuario.jsp` · `form-usuario.jsp` | |
| `frigorificos.html` | `lista-frigorifico.jsp` · `form-frigorifico.jsp` | |
| `termometros.html` | `lista-termometro.jsp` · `form-termometro.jsp` | |
| `categorias.html` | `lista-categoria.jsp` · `form-categoria.jsp` | |
| `lotes.html` | `lista-lote.jsp` · `form-lote.jsp` | |
| (sem HTML) | `lista-endereco.jsp` · `form-endereco.jsp` | o back-end tem `EnderecoServlet`; entrou no menu como ENDEREÇOS |

Nomes de JSP em kebab-case (como `esqueci-senha.jsp`); CSS como `styleXxx.css`; JS em camelCase.

## Servlets ↔ JSPs (o contrato que cada tela segue)

| Rota | Lista (atributo) | Formulário (atributos) | Parâmetros do POST `acao=salvar` |
|---|---|---|---|
| `/usuario` | `lista-usuario.jsp` (`listaUsuarios`) | `form-usuario.jsp` (`usuario`, `listaCds`, `listaGestores`) | `id` `username` `nome` `cpf` `email` `cargo` `nivelAcesso` `idCd` `codGestor` `senha` |
| `/frigorifico` | `lista-frigorifico.jsp` (`listaFrigorificos`) | `form-frigorifico.jsp` (`frigorifico`, `listaCds`) | `id` `nome` `localizacao` `idCd` `temperaturaMax` `temperaturaMin` |
| `/termometro` | `lista-termometro.jsp` (`listaTermometros`, `listaFrigorificos`) | `form-termometro.jsp` (`termometro`, `listaFrigorificos`) | `id` `modelo` `status` `idFrigorifico` |
| `/categoria` | `lista-categoria.jsp` (`listaCategorias`) | `form-categoria.jsp` (`categoria`) | `id` `nome` `vidaUtilHoras` `temperaturaMax` `temperaturaMin` |
| `/lote` | `lista-lote.jsp` (`listaLotes`) | `form-lote.jsp` (`lote`, `listaCategorias`) | `id` `codigoLote` `idCategoria` `dataFabricacao` `dataValidade` `status` |
| `/endereco` | `lista-endereco.jsp` (`listaEnderecos`) | `form-endereco.jsp` (`endereco`, `listaCds`) | `id` `idCd` `rua` `numero` `complemento` `bairro` `cidade` `estado` `cep` |
| `/cd` | `lista-cd.jsp` (`listaCds`) | `form-cd.jsp` (`cd`) | `id` `nome` `cnpj` |
| `/login` | `login-cliente.jsp` / `login-sistema.jsp` | | `login` `senha` (`?acao=sair` encerra a sessão) |

Deletar é `GET /<rota>?acao=excluir&id=...` (como o servlet já esperava); o `crud.js` pede confirmação antes de seguir o link.

## Como as páginas funcionam

- **Listas:** a tabela é desenhada no servidor (JSTL). O `crud.js` só acrescenta pesquisa e paginação (20 por página) sobre as linhas recebidas.
- **Formulários:** são páginas normais (`form-*.jsp`), enviadas ao servlet. O `crud.js` aplica máscaras, mostra os erros mais cedo e evita duplo clique; o servlet continua validando. Erros do servidor (`?erro=validacao|formato|temperatura|datas`) aparecem no topo do formulário.
- **Unidades:** clicar num cartão guarda a unidade em `localStorage['skadi.unidadeAtual']` e abre a lista de funcionários; o nome aparece no cabeçalho das telas de cadastro. O lápis liga o modo de edição.
- **Ícones:** um sprite SVG (`icones-crud.jspf`) usa `currentColor`, sem depender de JavaScript.
- **Caminhos:** tudo usa `${pageContext.request.contextPath}`, então funciona em qualquer contexto do Tomcat (`/`, `/skadi`...).

## Defeitos dos HTMLs corrigidos no porte

- Imagens com caminho absoluto do computador (`C:\Users\...\Imagens\...`) → `Imagens/` do projeto (URL-encode de espaços).
- Links quebrados: `login.html`, `registro.html`, `sobreNos.html␣␣`, `landing.html#equipe`, `#beneficios` no lugar de `#planos`, `/Imagens/...` com barra inicial.
- `<img>...</img>` e `<polyline>` solto dentro de `<img>` (login do cliente).
- "Lembre-se de mim" era `type="radio"` (e com vírgula no atributo) → `checkbox`.
- Formulários de login sem `action`, sem `method` e sem `name` → ligados ao `LoginServlet`.
- Valores do nível de acesso no cadastro (`admin` para "Operador"...) → valores do banco (`operador`, `gestor`, `admin`, `super_admin`).
- Duas cópias do script de mostrar/ocultar senha → um só `senha.js`.
- Nomes de rota/arquivo: `form-frigorfico.jsp` (faltava o "i"), enquanto o `FrigorificoServlet` encaminha para `form-frigorifico.jsp` → era 404 ao abrir "novo/editar frigorífico". Agora o arquivo se chama `form-frigorifico.jsp`.
- Valores antigos de formulário que o banco rejeita (status de lote `quarentena`).

## Alterações no back-end (mínimas)

1. `web.xml`: `welcome-file` (`landing.jsp`), `request/response-character-encoding` UTF-8 e `trim-directive-whitespaces`.
   Sem o UTF-8, o Tomcat 10.1 lê o POST como ISO-8859-1 e "Zélia" é gravado como "ZÃ©lia" (nenhum servlet define a codificação).
2. `TermometroServlet`: as ações de listar e buscar agora também enviam `listaFrigorificos`, para a tela mostrar o nome do frigorífico.
   Sem isso a tela mostra "Frigorífico <id>". Todo o back-end foi compilado com essa alteração.

## O que foi verificado

- Os 24 JSPs compilam no Jasper do Tomcat 10.1 (JSTL 3.0 / `jakarta.tags.*`) sem erros.
- Teste de ponta a ponta no navegador com Tomcat embarcado e servlets de teste que seguem o contrato acima: login (certo e errado), unidades (acessar/editar), cadastrar, editar, deletar com confirmação, pesquisa, paginação, validações (CPF, CNPJ, CEP, UF, temperaturas, datas, intervalo), acentos, e nenhum recurso com erro 404.
- Não foi possível testar contra o PostgreSQL real; as consultas dos DAOs não foram executadas.

## Pendências e decisões (não alteradas)

**Back-end**
- Senha em texto puro: `UsuarioDAO.autenticar` compara `senha = ?` e o cadastro grava sem hash (o `BCrypt` está importado mas comentado). Recomenda-se hash.
- Nenhuma rota exige login (não há filtro): as URLs `/usuario`, `/lote`... abrem sem sessão. Excluir por GET também facilita CSRF.
- `RecuperarSenhaServlet`: falta `return;` depois do redirect de "e-mail não encontrado" (gera `IllegalStateException`), e a ação `salvarNovaSenha` do `redefinir-senha.jsp` não existe.
- `cadastro.jsp` (captação de novos clientes) não tem servlet: hoje só mostra o modal de sucesso.
- Unidades: o Figma tem gestor e endereço no mesmo cadastro, mas `CD` só tem nome e CNPJ (o endereço é outra tabela/servlet) e o banco aceita CNPJ só numérico (o Figma usa o alfanumérico novo).
- Informações que o Figma mostra mas o back-end não envia: temperatura atual, status de operação, quantidade de frigoríficos/funcionários ativos por unidade, total de lotes por categoria. As colunas foram trocadas por dados que existem ("Atualizado em", faixa ideal).
- `Categoria.vidaUtilHoras` é um intervalo do PostgreSQL (texto): o formulário aceita `1160` (vira `1160:00:00`) ou `HH:MM:SS`.

**Telas**
- Os formulários de funcionário, frigorífico e endereço pedem a unidade (`idCd`), que o servlet exige; o termômetro pede o frigorífico.
- Fontes do Google e Font Awesome continuam vindo de CDN nas páginas públicas (como no HTML original).
