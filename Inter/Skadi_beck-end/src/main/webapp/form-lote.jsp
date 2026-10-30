<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="paginaAtual" value="lote"/>
<c:set var="edicao" value="${not empty lote}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lotes | SKADI</title>
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
                <h2 class="janela__titulo" id="titulo-formulario"><c:choose><c:when test="${edicao}">Editar lote</c:when><c:otherwise>Cadastrar lote</c:otherwise></c:choose></h2>
                <a class="janela__fechar" href="${pageContext.request.contextPath}/lote?acao=listar" aria-label="Fechar"><svg><use href="#icone-fechar"/></svg></a>
            </header>

            <form class="formulario" id="formulario" data-form novalidate action="${pageContext.request.contextPath}/lote" method="post">
                <input type="hidden" name="acao" value="salvar">
                <c:if test="${edicao}">
                    <input type="hidden" name="id" value="${lote.idLote}">
                </c:if>

                <div class="janela__corpo">
<%@ include file="/WEB-INF/jspf/erro-form.jspf" %>
                    <div class="formulario__grade">
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-codigoLote">Código do lote</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-codigoLote" name="codigoLote" type="text" maxlength="50" placeholder="Digite aqui..." required value="<c:out value="${lote.codigoLote}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-idCategoria">Categoria</label>
                <div class="campo__controle">
                    <select class="campo__entrada campo__entrada--selecao" id="campo-idCategoria" name="idCategoria" required>
                        <option value=""<c:if test="${empty lote.idCategoria}"> selected</c:if> disabled hidden>Escolha a categoria</option>
                        <c:forEach items="${listaCategorias}" var="cat">
                            <option value="${cat.idCategoria}"<c:if test="${lote.idCategoria == cat.idCategoria}"> selected</c:if>><c:out value="${cat.nome}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-status">Status</label>
                <div class="campo__controle">
                    <select class="campo__entrada campo__entrada--selecao" id="campo-status" name="status" required>
                        <option value=""<c:if test="${empty lote.status}"> selected</c:if> disabled hidden>Escolha o status</option>
                        <option value="ativo"<c:if test="${lote.status == 'ativo'}"> selected</c:if>>Ativo</option>
                        <option value="bloqueado"<c:if test="${lote.status == 'bloqueado'}"> selected</c:if>>Bloqueado</option>
                        <option value="expedido"<c:if test="${lote.status == 'expedido'}"> selected</c:if>>Expedido</option>
                        <option value="vencido"<c:if test="${lote.status == 'vencido'}"> selected</c:if>>Vencido</option>
                    </select>
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-dataFabricacao">Data de fabricação</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-dataFabricacao" name="dataFabricacao" type="date" required value="<c:out value="${lote.dataFabricacao}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-dataValidade">Data de validade</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-dataValidade" name="dataValidade" type="date" data-nao-antes-de="dataFabricacao" required value="<c:out value="${lote.dataValidade}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
                    </div>
                </div>

                <footer class="janela__rodape">
                    <c:if test="${edicao}">
                        <a class="botao botao--perigo" href="${pageContext.request.contextPath}/lote?acao=excluir&amp;id=${lote.idLote}" data-confirmar="Deletar lote?" data-nome="<c:out value="${lote.codigoLote}"/>">Deletar lote</a>
                    </c:if>
                    <span class="janela__espaco"></span>
                    <a class="botao botao--claro" href="${pageContext.request.contextPath}/lote?acao=listar">Cancelar</a>
                    <button type="submit" class="botao botao--principal">
                        <svg><use href="#icone-salvar"/></svg>
                        <span><c:choose><c:when test="${edicao}">Salvar alterações</c:when><c:otherwise>Salvar lote</c:otherwise></c:choose></span>
                    </button>
                </footer>
            </form>

        </div>
    </main>

</div>
</body>
</html>
