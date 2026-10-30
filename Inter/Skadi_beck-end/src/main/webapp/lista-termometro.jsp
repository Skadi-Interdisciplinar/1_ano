<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<fmt:setLocale value="pt_BR"/>
<c:set var="paginaAtual" value="termometro"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Termômetros | SKADI</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleCrud.css">
    <script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</head>

<body>
<%@ include file="/WEB-INF/jspf/icones-crud.jspf" %>
<div class="pagina">

<%@ include file="/WEB-INF/jspf/topo-crud.jspf" %>

    <main class="conteudo" data-crud>
        <section class="cartao" aria-label="Lista de termômetros">

            <div class="ferramentas">
                <div class="pesquisa">
                    <span class="pesquisa__icone"><svg class="icone-lupa"><use href="#icone-lupa"/></svg></span>
                    <label class="so-leitor" for="pesquisa">Pesquisar termômetros</label>
                    <input class="pesquisa__campo" id="pesquisa" type="search" placeholder="Pesquisar..." autocomplete="off" data-crud-pesquisa>
                </div>

                <a class="botao-adicionar" href="${pageContext.request.contextPath}/termometro?acao=novo" aria-label="Cadastrar novo termômetro">
                    <svg><use href="#icone-adicionar"/></svg>
                </a>
            </div>

            <div class="tabela">
                <div class="tabela__interna" role="table" aria-label="Termômetros" style="--colunas: 242.8fr 285fr 274.5fr 355.5fr 72fr">
                    <div class="tabela__cabeca" role="row">
                    <div role="columnheader">Modelo</div>
                    <div role="columnheader">Localização</div>
                    <div role="columnheader">Status</div>
                    <div role="columnheader">Atualizado em</div>
                    <div role="columnheader"><span class="so-leitor">Ações</span></div>
                    </div>

                    <div class="tabela__corpo" role="rowgroup" data-tabela-corpo data-tamanho-pagina="20">
                        <c:forEach items="${listaTermometros}" var="t">
                        <c:set var="nomeFrigorifico" value="Frigorífico ${t.idFrigorifico}"/>
                        <c:forEach items="${listaFrigorificos}" var="fr">
                            <c:if test="${fr.idFrigorifico == t.idFrigorifico}"><c:set var="nomeFrigorifico" value="${fr.nome}"/></c:if>
                        </c:forEach>
                        <div class="tabela__linha" role="row" data-busca="<c:out value="${t.modelo} ${nomeFrigorifico} ${t.status}"/>">
                        <div class="tabela__celula" role="cell"><c:out value="${t.modelo}"/></div>
                        <div class="tabela__celula" role="cell"><c:out value="${nomeFrigorifico}"/></div>
                        <div class="tabela__celula" role="cell"><c:choose><c:when test="${t.status == 'ativo'}">Ativo</c:when><c:when test="${t.status == 'inativo'}">Inativo</c:when><c:when test="${t.status == 'manutencao'}">Manutenção</c:when><c:otherwise><c:out value="${t.status}"/></c:otherwise></c:choose></div>
                        <div class="tabela__celula" role="cell"><fmt:formatDate value="${t.dataAtualizacao}" pattern="dd/MM/yyyy HH:mm"/></div>
                        <div class="tabela__celula tabela__acoes" role="cell">
                            <a class="icone-acao" href="${pageContext.request.contextPath}/termometro?acao=editar&amp;id=${t.idTermometro}" aria-label="Editar termômetro <c:out value="${t.modelo}"/>"><svg class="icone-lapis"><use href="#icone-lapis"/></svg></a>
                            <a class="icone-acao icone-acao--perigo" href="${pageContext.request.contextPath}/termometro?acao=excluir&amp;id=${t.idTermometro}" aria-label="Deletar termômetro <c:out value="${t.modelo}"/>" data-confirmar="Deletar termômetro?" data-nome="<c:out value="${t.modelo}"/>"><svg class="icone-lixeira"><use href="#icone-lixeira"/></svg></a>
                        </div>
                        </div>
                        </c:forEach>

                        <p class="tabela__vazio" data-tabela-vazio data-texto="Nenhum termômetro cadastrado ainda."<c:if test="${not empty listaTermometros}"> hidden</c:if>>Nenhum termômetro cadastrado ainda.</p>
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
