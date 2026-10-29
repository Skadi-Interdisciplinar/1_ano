<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="paginaAtual" value="termometro"/>
<c:set var="edicao" value="${not empty termometro}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Termômetros</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleCrud.css">
    <script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</head>

<body>
<%@ include file="/WEB-INF/jspf/icones-crud.jspf" %>
<div class="pagina">

<%@ include file="/WEB-INF/jspf/topo-crud.jspf" %>

    <main class="formulario-pagina">
        <div class="janela janela--pagina janela--grande" role="dialog" aria-labelledby="titulo-formulario">

            <header class="janela__topo">
                <span class="janela__icone"><svg><use href="#icone-mais"/></svg></span>
                <h2 class="janela__titulo" id="titulo-formulario"><c:choose><c:when test="${edicao}">Editar termômetro</c:when><c:otherwise>Cadastrar novo termômetro</c:otherwise></c:choose></h2>
                <a class="janela__fechar" href="${pageContext.request.contextPath}/termometro?acao=listar" aria-label="Fechar"><svg><use href="#icone-fechar"/></svg></a>
            </header>

            <form class="formulario" id="formulario" data-form novalidate action="${pageContext.request.contextPath}/termometro" method="post">
                <input type="hidden" name="acao" value="salvar">
                <c:if test="${edicao}">
                    <input type="hidden" name="id" value="${termometro.idTermometro}">
                </c:if>

                <div class="janela__corpo">
<%@ include file="/WEB-INF/jspf/erro-form.jspf" %>
                    <div class="formulario__grade">
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-modelo">Modelo</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-modelo" name="modelo" type="text" maxlength="60" placeholder="Digite aqui..." required value="<c:out value="${termometro.modelo}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-idFrigorifico">Frigorífico</label>
                <div class="campo__controle">
                    <select class="campo__entrada campo__entrada--selecao" id="campo-idFrigorifico" name="idFrigorifico" required>
                        <option value=""<c:if test="${empty termometro.idFrigorifico}"> selected</c:if> disabled hidden>Escolha o frigorífico</option>
                        <c:forEach items="${listaFrigorificos}" var="fr">
                            <option value="${fr.idFrigorifico}"<c:if test="${termometro.idFrigorifico == fr.idFrigorifico}"> selected</c:if>><c:out value="${fr.nome}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-status">Status</label>
                <div class="campo__controle">
                    <select class="campo__entrada campo__entrada--selecao" id="campo-status" name="status" required>
                        <option value=""<c:if test="${empty termometro.status}"> selected</c:if> disabled hidden>Escolha o status</option>
                        <option value="ativo"<c:if test="${termometro.status == 'ativo'}"> selected</c:if>>Ativo</option>
                        <option value="inativo"<c:if test="${termometro.status == 'inativo'}"> selected</c:if>>Inativo</option>
                        <option value="manutencao"<c:if test="${termometro.status == 'manutencao'}"> selected</c:if>>Manutenção</option>
                    </select>
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
                    </div>
                </div>

                <footer class="janela__rodape">
                    <c:if test="${edicao}">
                        <a class="botao botao--perigo" href="${pageContext.request.contextPath}/termometro?acao=excluir&amp;id=${termometro.idTermometro}" data-confirmar="Deletar termômetro?" data-nome="<c:out value="${termometro.modelo}"/>">Deletar termômetro</a>
                    </c:if>
                    <span class="janela__espaco"></span>
                    <a class="botao botao--claro" href="${pageContext.request.contextPath}/termometro?acao=listar">Cancelar</a>
                    <button type="submit" class="botao botao--principal">
                        <svg><use href="#icone-salvar"/></svg>
                        <span><c:choose><c:when test="${edicao}">Salvar alterações</c:when><c:otherwise>Salvar termômetro</c:otherwise></c:choose></span>
                    </button>
                </footer>
            </form>

        </div>
    </main>

</div>
</body>
</html>
