<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<c:set var="paginaAtual" value="endereco"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Endereços</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleCrud.css">
    <script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</head>

<body>
<%@ include file="/WEB-INF/jspf/icones-crud.jspf" %>
<div class="pagina">

<%@ include file="/WEB-INF/jspf/topo-crud.jspf" %>

    <main class="conteudo" data-crud>
        <section class="cartao" aria-label="Lista de endereços">

<%@ include file="/WEB-INF/jspf/erro-lista.jspf" %>

            <div class="ferramentas">
                <div class="pesquisa">
                    <span class="pesquisa__icone"><img src="${pageContext.request.contextPath}/Imagens/lupa.svg" alt="" aria-hidden="true"></span>
                    <label class="so-leitor" for="pesquisa">Pesquisar endereços</label>
                    <input class="pesquisa__campo" id="pesquisa" type="search" placeholder="Pesquisar..." autocomplete="off" data-crud-pesquisa>
                </div>

                <c:if test="${not somenteLeitura}">
                    <a class="botao-adicionar" href="${pageContext.request.contextPath}/endereco?acao=novo" aria-label="Cadastrar endereço">
                    <svg><use href="#icone-adicionar"/></svg>
                </a>
                </c:if>
            </div>

            <div class="tabela">
                <div class="tabela__interna" role="table" aria-label="Endereços" style="--colunas: 220fr 290fr 170fr 190fr 120fr 72fr">
                    <div class="tabela__cabeca" role="row">
                    <div role="columnheader">Unidade (CD)</div>
                    <div role="columnheader">Rua</div>
                    <div role="columnheader">Bairro</div>
                    <div role="columnheader">Cidade</div>
                    <div role="columnheader">CEP</div>
                    <div role="columnheader"><span class="so-leitor">Ações</span></div>
                    </div>

                    <div class="tabela__corpo" role="rowgroup" data-tabela-corpo data-tamanho-pagina="20">
                        <c:forEach items="${listaEnderecos}" var="e">
                        <div class="tabela__linha" role="row" data-busca="<c:out value="${e.nomeCd} ${e.rua} ${e.bairro} ${e.cidade} ${e.estado} ${e.cep}"/>">
                        <div class="tabela__celula" role="cell"><c:out value="${e.nomeCd}" default="—"/></div>
                        <div class="tabela__celula" role="cell"><c:out value="${e.rua}"/><c:if test="${not empty e.numero}">, <c:out value="${e.numero}"/></c:if></div>
                        <div class="tabela__celula" role="cell"><c:out value="${e.bairro}"/></div>
                        <div class="tabela__celula" role="cell"><c:out value="${e.cidade}"/> - <c:out value="${e.estado}"/></div>
                        <div class="tabela__celula" role="cell"><c:out value="${fn:substring(e.cep, 0, 5)}"/>-<c:out value="${fn:substring(e.cep, 5, 8)}"/></div>
                        <div class="tabela__celula tabela__acoes" role="cell">
                            <c:if test="${not somenteLeitura}"><a class="icone-acao" href="${pageContext.request.contextPath}/endereco?acao=editar&amp;id=${e.idEndereco}" aria-label="Editar endereço <c:out value="${e.rua}"/>"><img src="${pageContext.request.contextPath}/Imagens/lapis.svg" alt="" aria-hidden="true"></a></c:if>
                            <c:if test="${not somenteLeitura}"><a class="icone-acao icone-acao--perigo" href="${pageContext.request.contextPath}/endereco?acao=excluir&amp;id=${e.idEndereco}" aria-label="Deletar endereço <c:out value="${e.rua}"/>" data-confirmar="Deletar endereço?" data-nome="<c:out value="${e.rua}"/>"><img src="${pageContext.request.contextPath}/Imagens/lixeira.svg" alt="" aria-hidden="true"></a></c:if>
                        </div>
                        </div>
                        </c:forEach>

                        <p class="tabela__vazio" data-tabela-vazio data-texto="Nenhum endereço cadastrado ainda."<c:if test="${not empty listaEnderecos}"> hidden</c:if>>Nenhum endereço cadastrado ainda.</p>
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
