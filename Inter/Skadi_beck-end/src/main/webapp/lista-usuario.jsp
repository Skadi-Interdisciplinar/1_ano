<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="paginaAtual" value="usuario"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Funcionários</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleCrud.css">
    <script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</head>

<body>
<%@ include file="/WEB-INF/jspf/icones-crud.jspf" %>
<div class="pagina">

<%@ include file="/WEB-INF/jspf/topo-crud.jspf" %>

    <main class="conteudo" data-crud>
        <section class="cartao" aria-label="Lista de funcionários">

<%@ include file="/WEB-INF/jspf/erro-lista.jspf" %>

            <div class="ferramentas">
                <div class="pesquisa">
                    <span class="pesquisa__icone"><img src="${pageContext.request.contextPath}/Imagens/lupa.svg" alt="" aria-hidden="true"></span>
                    <label class="so-leitor" for="pesquisa">Pesquisar funcionários</label>
                    <input class="pesquisa__campo" id="pesquisa" type="search" placeholder="Pesquisar..." autocomplete="off" data-crud-pesquisa>
                </div>

                <c:if test="${not somenteLeitura}">
                    <a class="botao-adicionar" href="${pageContext.request.contextPath}/usuario?acao=novo" aria-label="Cadastrar novo funcionário">
                    <svg><use href="#icone-adicionar"/></svg>
                </a>
                </c:if>
            </div>

            <div class="tabela">
                <div class="tabela__interna" role="table" aria-label="Funcionários" style="--colunas: 131.6fr 162.6fr 324.1fr 119.6fr 117.7fr 145.6fr 156.6fr 72fr">
                    <div class="tabela__cabeca" role="row">
                    <div role="columnheader">Nome</div>
                    <div role="columnheader">Username</div>
                    <div role="columnheader">E-mail</div>
                    <div role="columnheader">Senha</div>
                    <div role="columnheader">Acesso</div>
                    <div role="columnheader">CD</div>
                    <div role="columnheader">Gestor</div>
                    <div role="columnheader"><span class="so-leitor">Ações</span></div>
                    </div>

                    <div class="tabela__corpo" role="rowgroup" data-tabela-corpo data-tamanho-pagina="20">
                        <c:forEach items="${listaUsuarios}" var="u">
                        <c:set var="nomeGestor" value="—"/>
                        <c:forEach items="${listaUsuarios}" var="g">
                            <c:if test="${g.idUsuario == u.codGestor}"><c:set var="nomeGestor" value="${g.nome}"/></c:if>
                        </c:forEach>
                        <div class="tabela__linha" role="row" data-busca="<c:out value="${u.nome} ${u.username} ${u.email} ${u.nivelAcesso} ${u.nomeCd} ${nomeGestor}"/>">
                        <div class="tabela__celula" role="cell"><c:out value="${u.nome}"/></div>
                        <div class="tabela__celula" role="cell"><c:out value="${u.username}"/></div>
                        <div class="tabela__celula" role="cell"><c:out value="${u.email}"/></div>
                        <div class="tabela__celula" role="cell">*******</div>
                        <div class="tabela__celula" role="cell"><c:choose><c:when test="${u.nivelAcesso == 'operador'}">Operador</c:when><c:when test="${u.nivelAcesso == 'gestor'}">Gerente</c:when><c:when test="${u.nivelAcesso == 'admin'}">Admin</c:when><c:when test="${u.nivelAcesso == 'super_admin'}">Super Admin</c:when><c:otherwise><c:out value="${u.nivelAcesso}"/></c:otherwise></c:choose></div>
                        <div class="tabela__celula" role="cell"><c:out value="${u.nomeCd}" default="—"/></div>
                        <div class="tabela__celula" role="cell"><c:out value="${nomeGestor}"/></div>
                        <div class="tabela__celula tabela__acoes" role="cell">
                            <c:if test="${not somenteLeitura}"><a class="icone-acao" href="${pageContext.request.contextPath}/usuario?acao=editar&amp;id=${u.idUsuario}" aria-label="Editar funcionário <c:out value="${u.nome}"/>"><img src="${pageContext.request.contextPath}/Imagens/lapis.svg" alt="" aria-hidden="true"></a></c:if>
                            <c:if test="${not somenteLeitura}"><a class="icone-acao icone-acao--perigo" href="${pageContext.request.contextPath}/usuario?acao=excluir&amp;id=${u.idUsuario}" aria-label="Deletar funcionário <c:out value="${u.nome}"/>" data-confirmar="Deletar funcionário?" data-nome="<c:out value="${u.nome}"/>"><img src="${pageContext.request.contextPath}/Imagens/lixeira.svg" alt="" aria-hidden="true"></a></c:if>
                        </div>
                        </div>
                        </c:forEach>

                        <p class="tabela__vazio" data-tabela-vazio data-texto="Nenhum funcionário cadastrado ainda."<c:if test="${not empty listaUsuarios}"> hidden</c:if>>Nenhum funcionário cadastrado ainda.</p>
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
