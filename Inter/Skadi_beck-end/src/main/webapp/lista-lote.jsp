<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<fmt:setLocale value="pt_BR"/>
<c:set var="paginaAtual" value="lote"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lotes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleCrud.css">
    <script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</head>

<body>
<%@ include file="/WEB-INF/jspf/icones-crud.jspf" %>
<div class="pagina">

<%@ include file="/WEB-INF/jspf/topo-crud.jspf" %>

    <main class="conteudo" data-crud>
        <section class="cartao" aria-label="Lista de lotes">

<%@ include file="/WEB-INF/jspf/erro-lista.jspf" %>

            <div class="ferramentas">
                <div class="pesquisa">
                    <span class="pesquisa__icone"><img src="${pageContext.request.contextPath}/Imagens/lupa.svg" alt="" aria-hidden="true"></span>
                    <label class="so-leitor" for="pesquisa">Pesquisar lotes</label>
                    <input class="pesquisa__campo" id="pesquisa" type="search" placeholder="Pesquisar..." autocomplete="off" data-crud-pesquisa>
                </div>

                <c:if test="${not somenteLeitura}">
                    <a class="botao-adicionar" href="${pageContext.request.contextPath}/lote?acao=novo" aria-label="Cadastrar lote">
                    <svg><use href="#icone-adicionar"/></svg>
                </a>
                </c:if>
            </div>

            <div class="tabela">
                <div class="tabela__interna" role="table" aria-label="Lotes" style="--colunas: 322.8fr 248fr 275fr 312fr 72fr">
                    <div class="tabela__cabeca" role="row">
                    <div role="columnheader">Código do lote</div>
                    <div role="columnheader">Data de fabricação</div>
                    <div role="columnheader">Data de validade</div>
                    <div role="columnheader">Status</div>
                    <div role="columnheader"><span class="so-leitor">Ações</span></div>
                    </div>

                    <div class="tabela__corpo" role="rowgroup" data-tabela-corpo data-tamanho-pagina="20">
                        <c:forEach items="${listaLotes}" var="l">
                        <div class="tabela__linha" role="row" data-busca="<c:out value="${l.codigoLote} ${l.status}"/>">
                        <div class="tabela__celula" role="cell"><c:out value="${l.codigoLote}"/></div>
                        <div class="tabela__celula" role="cell"><fmt:formatDate value="${l.dataFabricacao}" pattern="dd/MM/yyyy"/></div>
                        <div class="tabela__celula" role="cell"><fmt:formatDate value="${l.dataValidade}" pattern="dd/MM/yyyy"/></div>
                        <div class="tabela__celula" role="cell"><c:choose><c:when test="${l.status == 'ativo'}">Ativo</c:when><c:when test="${l.status == 'bloqueado'}">Bloqueado</c:when><c:when test="${l.status == 'expedido'}">Expedido</c:when><c:when test="${l.status == 'vencido'}">Vencido</c:when><c:otherwise><c:out value="${l.status}"/></c:otherwise></c:choose></div>
                        <div class="tabela__celula tabela__acoes" role="cell">
                            <c:if test="${not somenteLeitura}"><a class="icone-acao" href="${pageContext.request.contextPath}/lote?acao=editar&amp;id=${l.idLote}" aria-label="Editar lote <c:out value="${l.codigoLote}"/>"><img src="${pageContext.request.contextPath}/Imagens/lapis.svg" alt="" aria-hidden="true"></a></c:if>
                            <c:if test="${not somenteLeitura}"><a class="icone-acao icone-acao--perigo" href="${pageContext.request.contextPath}/lote?acao=excluir&amp;id=${l.idLote}" aria-label="Deletar lote <c:out value="${l.codigoLote}"/>" data-confirmar="Deletar lote?" data-nome="<c:out value="${l.codigoLote}"/>"><img src="${pageContext.request.contextPath}/Imagens/lixeira.svg" alt="" aria-hidden="true"></a></c:if>
                        </div>
                        </div>
                        </c:forEach>

                        <p class="tabela__vazio" data-tabela-vazio data-texto="Nenhum lote cadastrado ainda."<c:if test="${not empty listaLotes}"> hidden</c:if>>Nenhum lote cadastrado ainda.</p>
                    </div>
                </div>
            </div>

            <div class="paginacao">
                <span data-crud-intervalo aria-live="polite">0 - 0 de 0</span>
                <button type="button" class="paginacao__botao" data-crud-anterior aria-label="Página anterior">
                    <img src="${pageContext.request.contextPath}/Imagens/seta_azul.svg" alt="" aria-hidden="true">
                </button>
                <button type="button" class="paginacao__botao" data-crud-proxima aria-label="Próxima página">
                    <img class="icone--invertido" src="${pageContext.request.contextPath}/Imagens/seta_azul.svg" alt="" aria-hidden="true">
                </button>
            </div>

        </section>
    </main>

</div>
</body>
</html>
