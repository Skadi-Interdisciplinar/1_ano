<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="paginaAtual" value="endereco"/>
<c:set var="edicao" value="${not empty endereco}"/>
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

    <main class="formulario-pagina">
        <div class="janela janela--pagina" role="dialog" aria-labelledby="titulo-formulario">

            <header class="janela__topo">
                <span class="janela__icone"><svg><use href="#icone-mais"/></svg></span>
                <h2 class="janela__titulo" id="titulo-formulario"><c:choose><c:when test="${edicao}">Editar endereço</c:when><c:otherwise>Cadastrar endereço</c:otherwise></c:choose></h2>
                <a class="janela__fechar" href="${pageContext.request.contextPath}/endereco?acao=listar" aria-label="Fechar"><svg><use href="#icone-fechar"/></svg></a>
            </header>

            <form class="formulario" id="formulario" data-form novalidate action="${pageContext.request.contextPath}/endereco" method="post">
                <input type="hidden" name="acao" value="salvar">
                <c:if test="${edicao}">
                    <input type="hidden" name="id" value="${endereco.idEndereco}">
                </c:if>

                <div class="janela__corpo">
<%@ include file="/WEB-INF/jspf/erro-form.jspf" %>
                    <div class="formulario__grade">
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-idCd">Unidade (CD)</label>
                <div class="campo__controle">
                    <select class="campo__entrada campo__entrada--selecao" id="campo-idCd" name="idCd" required>
                        <option value=""<c:if test="${empty endereco.idCd and empty sessionScope.cdAtual}"> selected</c:if> disabled hidden>Escolha a unidade</option>
                        <c:forEach items="${listaCds}" var="cd">
                            <option value="${cd.idCd}"<c:if test="${endereco.idCd == cd.idCd or (empty endereco.idCd and sessionScope.cdAtual == cd.idCd)}"> selected</c:if>><c:out value="${cd.nome}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo campo--cheio">
                <label class="campo__rotulo" for="campo-rua">Rua</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-rua" name="rua" type="text" maxlength="150" placeholder="Digite aqui..." required value="<c:out value="${endereco.rua}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-numero">Número</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-numero" name="numero" type="number" placeholder="Ex.: 1200" min="0" step="1" required value="<c:out value="${endereco.numero}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-complemento">Complemento</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-complemento" name="complemento" type="text" maxlength="100" placeholder="Digite aqui..." value="<c:out value="${endereco.complemento}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-bairro">Bairro</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-bairro" name="bairro" type="text" maxlength="80" placeholder="Digite aqui..." required value="<c:out value="${endereco.bairro}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-cidade">Cidade</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-cidade" name="cidade" type="text" maxlength="80" placeholder="Digite aqui..." required value="<c:out value="${endereco.cidade}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-estado">Estado (sigla)</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-estado" name="estado" type="text" maxlength="2" placeholder="SP" data-mascara="uf" data-validar="uf" required value="<c:out value="${endereco.estado}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
            <div class="campo">
                <label class="campo__rotulo" for="campo-cep">CEP</label>
                <div class="campo__controle">
                    <input class="campo__entrada" id="campo-cep" name="cep" type="text" maxlength="9" placeholder="00000-000" data-mascara="cep" data-validar="cep" inputmode="numeric" required value="<c:out value="${endereco.cep}"/>">
                </div>
                <p class="campo__erro" role="alert" hidden></p>
            </div>
                    </div>
                </div>

                <footer class="janela__rodape">
                    <c:if test="${edicao}">
                        <a class="botao botao--perigo" href="${pageContext.request.contextPath}/endereco?acao=excluir&amp;id=${endereco.idEndereco}" data-confirmar="Deletar endereço?" data-nome="<c:out value="${endereco.rua}"/>">Deletar endereço</a>
                    </c:if>
                    <span class="janela__espaco"></span>
                    <a class="botao botao--claro" href="${pageContext.request.contextPath}/endereco?acao=listar">Cancelar</a>
                    <button type="submit" class="botao botao--principal">
                        <svg><use href="#icone-salvar"/></svg>
                        <span><c:choose><c:when test="${edicao}">Salvar alterações</c:when><c:otherwise>Salvar endereço</c:otherwise></c:choose></span>
                    </button>
                </footer>
            </form>

        </div>
    </main>

</div>
</body>
</html>
