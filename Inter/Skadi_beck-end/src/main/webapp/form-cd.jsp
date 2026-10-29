<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="paginaAtual" value=""/>
<c:set var="edicao" value="${not empty cd}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unidades</title>
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
                <h2 class="janela__titulo" id="titulo-formulario"><c:choose><c:when test="${edicao}">Editar unidade</c:when><c:otherwise>Cadastrar nova unidade</c:otherwise></c:choose></h2>
                <a class="janela__fechar" href="${pageContext.request.contextPath}/cd?acao=listar" aria-label="Fechar"><svg><use href="#icone-fechar"/></svg></a>
            </header>

            <form class="formulario" id="formulario" data-form novalidate action="${pageContext.request.contextPath}/cd" method="post">
                <input type="hidden" name="acao" value="salvar">
                <c:if test="${edicao}">
                    <input type="hidden" name="id" value="${cd.idCd}">
                </c:if>

                <div class="janela__corpo">
<%@ include file="/WEB-INF/jspf/erro-form.jspf" %>
                    <div class="formulario__grade">
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-nome">Nome da unidade</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-nome" name="nome" type="text" maxlength="100" placeholder="Digite aqui..." required value="<c:out value="${cd.nome}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-cnpj">CNPJ</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-cnpj" name="cnpj" type="text" maxlength="18" placeholder="00.000.000/0000-00" data-mascara="cnpj" data-validar="cnpj" inputmode="numeric" required value="<c:out value="${cd.cnpj}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
                    </div>
                </div>

                <footer class="janela__rodape">
                    <c:if test="${edicao}">
                        <a class="botao botao--perigo" href="${pageContext.request.contextPath}/cd?acao=excluir&amp;id=${cd.idCd}" data-confirmar="Deletar unidade?" data-nome="<c:out value="${cd.nome}"/>">Deletar unidade</a>
                    </c:if>
                    <span class="janela__espaco"></span>
                    <a class="botao botao--claro" href="${pageContext.request.contextPath}/cd?acao=listar">Cancelar</a>
                    <button type="submit" class="botao botao--principal">
                        <svg><use href="#icone-salvar"/></svg>
                        <span><c:choose><c:when test="${edicao}">Salvar alterações</c:when><c:otherwise>Salvar unidade</c:otherwise></c:choose></span>
                    </button>
                </footer>
            </form>

        </div>
    </main>

</div>
</body>
</html>
