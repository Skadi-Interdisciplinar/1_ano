<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unidades</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleCrud.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleUnidades.css">
    <script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</head>

<body>
<%@ include file="/WEB-INF/jspf/icones-crud.jspf" %>

    <%-- Cabeçalho só com a logo e o botão de sair --%>
    <header class="topo topo--simples">
        <a class="topo__sair" href="${pageContext.request.contextPath}/login?acao=sair">Sair</a>
        <a class="topo__logo" href="${pageContext.request.contextPath}/landing.jsp" aria-label="SKADI">
            <svg class="icone-logo"><use href="#icone-logo"/></svg>
        </a>
    </header>

    <main class="unidades" data-unidades data-modo="acessar">

        <h1 class="unidades__saudacao">Olá<c:if test="${not empty sessionScope.usuarioLogado.nome}">, <span><c:out value="${sessionScope.usuarioLogado.nome}"/></span></c:if>!</h1>
        <p class="unidades__pergunta" data-pergunta aria-live="polite">Qual unidade você gostaria de acessar?</p>

        <div class="unidades__barra">
            <label class="so-leitor" for="pesquisa">Pesquisar filial por CNPJ</label>
            <input class="unidades__pesquisa" id="pesquisa" type="search" placeholder="Pesquisar filial por CNPJ" autocomplete="off" data-crud-pesquisa>

            <a class="unidades__adicionar" href="${pageContext.request.contextPath}/cd?acao=novo" aria-label="Cadastrar nova unidade">
                <svg><use href="#icone-adicionar"/></svg>
            </a>

            <button type="button" class="unidades__editar" data-unidades-editar aria-label="Editar unidades">
                <img src="${pageContext.request.contextPath}/Imagens/lapis.svg" alt="" aria-hidden="true">
            </button>
        </div>

        <ul class="unidades__lista" data-unidades-lista aria-label="Unidades">
            <c:forEach items="${listaCds}" var="cd">
                <li>
                    <a class="unidade" href="${pageContext.request.contextPath}/usuario?acao=listar"
                       data-id="${cd.idCd}"
                       data-nome="<c:out value="${cd.nome}"/>"
                       data-busca="<c:out value="${cd.nome} ${cd.cnpj}"/>"
                       data-href-editar="${pageContext.request.contextPath}/cd?acao=editar&amp;id=${cd.idCd}">
                        <span class="unidade__nome"><c:out value="${cd.nome}"/></span>
                        <span class="unidade__info">
                            <span>CNPJ: <c:out value="${fn:substring(cd.cnpj, 0, 2)}"/>.<c:out value="${fn:substring(cd.cnpj, 2, 5)}"/>.<c:out value="${fn:substring(cd.cnpj, 5, 8)}"/>/<c:out value="${fn:substring(cd.cnpj, 8, 12)}"/>-<c:out value="${fn:substring(cd.cnpj, 12, 14)}"/></span>
                            <span>Atualizada em: <fmt:formatDate value="${cd.dataAtualizacao}" pattern="dd/MM/yyyy"/></span>
                        </span>
                    </a>
                </li>
            </c:forEach>
        </ul>

        <%-- Primeiro acesso: ainda não existe nenhuma unidade --%>
        <c:if test="${empty listaCds}">
            <p class="unidades__vazio">Ops! Parece que você não tem nenhuma unidade cadastrada ainda.</p>
        </c:if>
        <p class="unidades__vazio" data-unidades-sem-resultado hidden>Nenhuma unidade encontrada para esta busca.</p>

        <button type="button" class="botao botao--claro unidades__cancelar" data-unidades-cancelar hidden>Cancelar edição</button>

    </main>

</body>
</html>
