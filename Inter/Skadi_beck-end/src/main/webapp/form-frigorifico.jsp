<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="paginaAtual" value="frigorifico"/>
<c:set var="edicao" value="${not empty frigorifico}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Frigoríficos | SKADI</title>
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
                <h2 class="janela__titulo" id="titulo-formulario"><c:choose><c:when test="${edicao}">Editar frigorífico</c:when><c:otherwise>Adicionar frigorífico</c:otherwise></c:choose></h2>
                <a class="janela__fechar" href="${pageContext.request.contextPath}/frigorifico?acao=listar" aria-label="Fechar"><svg><use href="#icone-fechar"/></svg></a>
            </header>

            <form class="formulario" id="formulario" data-form novalidate action="${pageContext.request.contextPath}/frigorifico" method="post">
                <input type="hidden" name="acao" value="salvar">
                <c:if test="${edicao}">
                    <input type="hidden" name="id" value="${frigorifico.idFrigorifico}">
                </c:if>

                <div class="janela__corpo">
<%@ include file="/WEB-INF/jspf/erro-form.jspf" %>
                    <div class="formulario__grade">
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-nome">Nome do frigorífico</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-nome" name="nome" type="text" maxlength="80" placeholder="Digite um nome autodescritivo" required value="<c:out value="${frigorifico.nome}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-localizacao">Localização</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-localizacao" name="localizacao" type="text" maxlength="80" placeholder="Ex.: Setor D - Corredor 4" required value="<c:out value="${frigorifico.localizacao}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-temperaturaMin">Temperatura mínima (°C)</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-temperaturaMin" name="temperaturaMin" type="number" placeholder="-20" step="0.01" min="-50" max="50" required value="<c:out value="${frigorifico.temperaturaMin}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-temperaturaMax">Temperatura máxima (°C)</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-temperaturaMax" name="temperaturaMax" type="number" placeholder="-18" step="0.01" min="-50" max="50" data-maior-que="temperaturaMin" required value="<c:out value="${frigorifico.temperaturaMax}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-idCd">Unidade (CD)</label>
                <div class="campo__controle">
                    <select class="campo__entrada campo__entrada--selecao" id="campo-idCd" name="idCd" required>
                        <option value=""<c:if test="${empty frigorifico.idCd}"> selected</c:if> disabled hidden>Escolha a unidade</option>
                        <c:forEach items="${listaCds}" var="cd">
                            <option value="${cd.idCd}"<c:if test="${frigorifico.idCd == cd.idCd}"> selected</c:if>><c:out value="${cd.nome}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
                    </div>
                </div>

                <footer class="janela__rodape">
                    <c:if test="${edicao}">
                        <a class="botao botao--perigo" href="${pageContext.request.contextPath}/frigorifico?acao=excluir&amp;id=${frigorifico.idFrigorifico}" data-confirmar="Deletar frigorífico?" data-nome="<c:out value="${frigorifico.nome}"/>">Deletar frigorífico</a>
                    </c:if>
                    <span class="janela__espaco"></span>
                    <a class="botao botao--claro" href="${pageContext.request.contextPath}/frigorifico?acao=listar">Cancelar</a>
                    <button type="submit" class="botao botao--principal">
                        <svg><use href="#icone-salvar"/></svg>
                        <span><c:choose><c:when test="${edicao}">Salvar alterações</c:when><c:otherwise>Salvar frigorífico</c:otherwise></c:choose></span>
                    </button>
                </footer>
            </form>

        </div>
    </main>

</div>
</body>
</html>
