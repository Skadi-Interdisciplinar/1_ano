<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="paginaAtual" value="usuario"/>
<c:set var="edicao" value="${not empty usuario}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Funcionários | SKADI</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleCrud.css">
    <script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</head>

<body>
<%@ include file="/WEB-INF/jspf/icones-crud.jspf" %>
<div class="pagina">

<%@ include file="/WEB-INF/jspf/topo-crud.jspf" %>

    <main class="formulario-pagina">
        <div class="janela janela--pagina" role="dialog" aria-labelledby="titulo-formulario">

            <header class="janela__topo">
                <span class="janela__icone"><svg><use href="#icone-mais"/></svg></span>
                <h2 class="janela__titulo" id="titulo-formulario"><c:choose><c:when test="${edicao}">Editar funcionário</c:when><c:otherwise>Cadastrar novo funcionário</c:otherwise></c:choose></h2>
                <a class="janela__fechar" href="${pageContext.request.contextPath}/usuario?acao=listar" aria-label="Fechar"><svg><use href="#icone-fechar"/></svg></a>
            </header>

            <form class="formulario" id="formulario" data-form novalidate action="${pageContext.request.contextPath}/usuario" method="post">
                <input type="hidden" name="acao" value="salvar">
                <c:if test="${edicao}">
                    <input type="hidden" name="id" value="${usuario.idUsuario}">
                </c:if>

                <div class="janela__corpo">
<%@ include file="/WEB-INF/jspf/erro-form.jspf" %>
                    <div class="formulario__grade">
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-nome">Nome do funcionário</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-nome" name="nome" type="text" maxlength="100" placeholder="Digite aqui..." required value="<c:out value="${usuario.nome}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-email">E-mail</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-email" name="email" type="email" maxlength="120" placeholder="Digite aqui..." data-validar="email" required value="<c:out value="${usuario.email}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-username">Username</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-username" name="username" type="text" maxlength="50" placeholder="Digite aqui..." minlength="3" required value="<c:out value="${usuario.username}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-cpf">CPF</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-cpf" name="cpf" type="text" maxlength="14" placeholder="000.000.000-00" data-mascara="cpf" data-validar="cpf" inputmode="numeric" required value="<c:out value="${usuario.cpf}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-cargo">Cargo</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-cargo" name="cargo" type="text" maxlength="80" placeholder="Digite aqui..." value="<c:out value="${usuario.cargo}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-nivelAcesso">Nível de acesso</label>
                <div class="campo__controle">
                    <select class="campo__entrada campo__entrada--selecao" id="campo-nivelAcesso" name="nivelAcesso" required>
                        <option value=""<c:if test="${empty usuario.nivelAcesso}"> selected</c:if> disabled hidden>Escolha o nível de acesso</option>
                        <option value="operador"<c:if test="${usuario.nivelAcesso == 'operador'}"> selected</c:if>>Operador</option>
                        <option value="gestor"<c:if test="${usuario.nivelAcesso == 'gestor'}"> selected</c:if>>Gerente</option>
                        <option value="admin"<c:if test="${usuario.nivelAcesso == 'admin'}"> selected</c:if>>Administrador</option>
                        <option value="super_admin"<c:if test="${usuario.nivelAcesso == 'super_admin'}"> selected</c:if>>Super Admin</option>
                    </select>
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-idCd">Unidade (CD)</label>
                <div class="campo__controle">
                    <select class="campo__entrada campo__entrada--selecao" id="campo-idCd" name="idCd" required>
                        <option value=""<c:if test="${empty usuario.idCd}"> selected</c:if> disabled hidden>Escolha a unidade</option>
                        <c:forEach items="${listaCds}" var="cd">
                            <option value="${cd.idCd}"<c:if test="${usuario.idCd == cd.idCd}"> selected</c:if>><c:out value="${cd.nome}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-codGestor">Gestor</label>
                <div class="campo__controle">
                    <select class="campo__entrada campo__entrada--selecao" id="campo-codGestor" name="codGestor">
                        <option value="">Sem gestor</option>
                        <c:forEach items="${listaGestores}" var="g">
                            <option value="${g.idUsuario}"<c:if test="${usuario.codGestor == g.idUsuario}"> selected</c:if>><c:out value="${g.nome}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <c:if test="${not edicao}">
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-senha">Senha</label>
                <div class="campo__controle">
                    <input class="campo__entrada campo__entrada--senha" id="campo-senha" name="senha" type="password" minlength="8" autocomplete="new-password" placeholder="Digite aqui..." required>
                    <button type="button" class="campo__olho" data-olho aria-label="Mostrar senha" aria-pressed="false">
                        <svg><use href="#icone-olho"/></svg>
                    </button>
                </div>
                <p class="campo__ajuda">Mínimo de 8 caracteres.</p>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            </c:if>
                    </div>
                </div>

                <footer class="janela__rodape">
                    <c:if test="${edicao}">
                        <a class="botao botao--perigo" href="${pageContext.request.contextPath}/usuario?acao=excluir&amp;id=${usuario.idUsuario}" data-confirmar="Deletar funcionário?" data-nome="<c:out value="${usuario.nome}"/>">Deletar funcionário</a>
                    </c:if>
                    <span class="janela__espaco"></span>
                    <a class="botao botao--claro" href="${pageContext.request.contextPath}/usuario?acao=listar">Cancelar</a>
                    <button type="submit" class="botao botao--principal">
                        <svg><use href="#icone-salvar"/></svg>
                        <span><c:choose><c:when test="${edicao}">Salvar alterações</c:when><c:otherwise>Salvar funcionário</c:otherwise></c:choose></span>
                    </button>
                </footer>
            </form>

        </div>
    </main>

</div>
</body>
</html>
