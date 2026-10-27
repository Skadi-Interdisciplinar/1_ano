<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<fmt:setLocale value="pt_BR"/>
<c:set var="paginaAtual" value="categoria"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Categorias | SKADI</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleCrud.css">
    <script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</head>

<body>
<%@ include file="/WEB-INF/jspf/icones-crud.jspf" %>
<div class="pagina">

<%@ include file="/WEB-INF/jspf/topo-crud.jspf" %>

    <main class="conteudo" data-crud>
        <section class="cartao" aria-label="Lista de categorias">

            <div class="ferramentas">
                <div class="pesquisa">
                    <span class="pesquisa__icone"><svg class="icone-lupa"><use href="#icone-lupa"/></svg></span>
                    <label class="so-leitor" for="pesquisa">Pesquisar categorias</label>
                    <input class="pesquisa__campo" id="pesquisa" type="search" placeholder="Pesquisar..." autocomplete="off" data-crud-pesquisa>
                </div>

                <a class="botao-adicionar" href="${pageContext.request.contextPath}/categoria?acao=novo" aria-label="Cadastrar categoria">
                    <svg><use href="#icone-adicionar"/></svg>
                </a>
            </div>

            <div class="tabela">
                <div class="tabela__interna" role="table" aria-label="Categorias" style="--colunas: 322.8fr 248fr 275fr 312fr 72fr">
                    <div class="tabela__cabeca" role="row">
                    <div role="columnheader">Categoria</div>
                    <div role="columnheader">Vida útil em horas</div>
                    <div role="columnheader">Temperatura ideal</div>
                    <div role="columnheader">Atualizado em</div>
                    <div role="columnheader"><span class="so-leitor">Ações</span></div>
                    </div>

                    <div class="tabela__corpo" role="rowgroup" data-tabela-corpo data-tamanho-pagina="20">
                        <c:forEach items="${listaCategorias}" var="cat">
                        <div class="tabela__linha" role="row" data-busca="<c:out value="${cat.nome} ${cat.vidaUtilHoras} ${cat.temperaturaMax} ${cat.temperaturaMin}"/>">
                        <div class="tabela__celula" role="cell"><c:out value="${cat.nome}"/></div>
                        <div class="tabela__celula" role="cell"><c:out value="${cat.vidaUtilHoras}"/></div>
                        <div class="tabela__celula" role="cell"><fmt:formatNumber value="${cat.temperaturaMax}" maxFractionDigits="2"/>°C a <fmt:formatNumber value="${cat.temperaturaMin}" maxFractionDigits="2"/>°C</div>
                        <div class="tabela__celula" role="cell"><fmt:formatDate value="${cat.dataAtualizacao}" pattern="dd/MM/yyyy HH:mm"/></div>
                        <div class="tabela__celula tabela__acoes" role="cell">
                            <a class="icone-acao" href="${pageContext.request.contextPath}/categoria?acao=editar&amp;id=${cat.idCategoria}" aria-label="Editar categoria <c:out value="${cat.nome}"/>"><svg class="icone-lapis"><use href="#icone-lapis"/></svg></a>
                            <a class="icone-acao icone-acao--perigo" href="${pageContext.request.contextPath}/categoria?acao=excluir&amp;id=${cat.idCategoria}" aria-label="Deletar categoria <c:out value="${cat.nome}"/>" data-confirmar="Deletar categoria?" data-nome="<c:out value="${cat.nome}"/>"><svg class="icone-lixeira"><use href="#icone-lixeira"/></svg></a>
                        </div>
                        </div>
                        </c:forEach>

                        <p class="tabela__vazio" data-tabela-vazio data-texto="Nenhuma categoria cadastrada ainda."<c:if test="${not empty listaCategorias}"> hidden</c:if>>Nenhuma categoria cadastrada ainda.</p>
                    </div>
                </div>
            </div>

            <div class="paginacao">
                <span data-crud-intervalo aria-live="polite">0 - 0 de 0</span>
                <button type="button" class="paginacao__botao" data-crud-anterior aria-label="Página anterior">
                    <svg><use href="#icone-seta-esquerda"/></svg>
                </button>
                <button type="button" class="paginacao__botao" data-crud-proxima aria-label="Próxima página">
                    <svg><use href="#icone-seta-direita"/></svg>
                </button>
            </div>

        </section>
    </main>

</div>
</body>
</html>
