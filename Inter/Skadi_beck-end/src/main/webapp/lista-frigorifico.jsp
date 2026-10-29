<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<fmt:setLocale value="pt_BR"/>
<c:set var="paginaAtual" value="frigorifico"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Frigoríficos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleCrud.css">
    <script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</head>

<body>
<%@ include file="/WEB-INF/jspf/icones-crud.jspf" %>
<div class="pagina">

<%@ include file="/WEB-INF/jspf/topo-crud.jspf" %>

    <main class="conteudo" data-crud>
        <section class="cartao" aria-label="Lista de frigoríficos">

            <div class="ferramentas">
                <div class="pesquisa">
                    <span class="pesquisa__icone"><img src="${pageContext.request.contextPath}/Imagens/lupa.svg" alt="" aria-hidden="true"></span>
                    <label class="so-leitor" for="pesquisa">Pesquisar frigoríficos</label>
                    <input class="pesquisa__campo" id="pesquisa" type="search" placeholder="Pesquisar..." autocomplete="off" data-crud-pesquisa>
                </div>

                <a class="botao-adicionar" href="${pageContext.request.contextPath}/frigorifico?acao=novo" aria-label="Adicionar frigorífico">
                    <svg><use href="#icone-adicionar"/></svg>
                </a>
            </div>

            <div class="tabela">
                <div class="tabela__interna" role="table" aria-label="Frigoríficos" style="--colunas: 242.8fr 285fr 274.5fr 355.5fr 72fr">
                    <div class="tabela__cabeca" role="row">
                    <div role="columnheader">Frigorífico</div>
                    <div role="columnheader">Localização</div>
                    <div role="columnheader">Temperatura ideal</div>
                    <div role="columnheader">Atualizado em</div>
                    <div role="columnheader"><span class="so-leitor">Ações</span></div>
                    </div>

                    <div class="tabela__corpo" role="rowgroup" data-tabela-corpo data-tamanho-pagina="20">
                        <c:forEach items="${listaFrigorificos}" var="f">
                        <div class="tabela__linha" role="row" data-busca="<c:out value="${f.nome} ${f.localizacao} ${f.temperaturaMax} ${f.temperaturaMin}"/>">
                        <div class="tabela__celula" role="cell"><c:out value="${f.nome}"/></div>
                        <div class="tabela__celula" role="cell"><c:out value="${f.localizacao}"/></div>
                        <div class="tabela__celula" role="cell"><fmt:formatNumber value="${f.temperaturaMax}" maxFractionDigits="2"/>°C a <fmt:formatNumber value="${f.temperaturaMin}" maxFractionDigits="2"/>°C</div>
                        <div class="tabela__celula" role="cell"><fmt:formatDate value="${f.dataAtualizacao}" pattern="dd/MM/yyyy HH:mm"/></div>
                        <div class="tabela__celula tabela__acoes" role="cell">
                            <a class="icone-acao" href="${pageContext.request.contextPath}/frigorifico?acao=editar&amp;id=${f.idFrigorifico}" aria-label="Editar frigorífico <c:out value="${f.nome}"/>"><img src="${pageContext.request.contextPath}/Imagens/lapis.svg" alt="" aria-hidden="true"></a>
                            <a class="icone-acao icone-acao--perigo" href="${pageContext.request.contextPath}/frigorifico?acao=excluir&amp;id=${f.idFrigorifico}" aria-label="Deletar frigorífico <c:out value="${f.nome}"/>" data-confirmar="Deletar frigorífico?" data-nome="<c:out value="${f.nome}"/>"><img src="${pageContext.request.contextPath}/Imagens/lixeira.svg" alt="" aria-hidden="true"></a>
                        </div>
                        </div>
                        </c:forEach>

                        <p class="tabela__vazio" data-tabela-vazio data-texto="Nenhum frigorífico cadastrado ainda."<c:if test="${not empty listaFrigorificos}"> hidden</c:if>>Nenhum frigorífico cadastrado ainda.</p>
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
