<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Unidades (servlet /cd): admin vê e gerencia as CDs dele (cadastra a primeira no 1º acesso);
  super_admin vê todas, cadastra, edita e exclui.
  O endereço da unidade é salvo junto, pelo servlet /endereco (ver cds.js).
  Com "&formato=dados" devolve só os registros (o crudServidor.js usa para recarregar).
--%>
<%-- Aberta direto pelo endereço (sem passar pelo servlet): vai para o servlet, que confere o login e carrega os dados --%>
<c:if test="${empty requestScope['jakarta.servlet.forward.request_uri']}">
    <c:redirect url="/cd?acao=listar"/>
</c:if>
<c:if test="${param.formato != 'dados'}">
<c:set var="tituloPagina" value="Unidades"/>
<c:set var="scriptPagina" value="cds.js"/>
<c:set var="estiloPagina" value="styleCds.css"/>
<!DOCTYPE html>
<html lang="pt-br">
<%@ include file="/WEB-INF/jspf/cabeca-crud.jspf" %>

<body data-nivel="<c:out value='${sessionScope.usuarioLogado.nivelAcesso}'/>" data-id-cd="${sessionScope.usuarioLogado.idCd}" data-usuario="<c:out value='${sessionScope.usuarioLogado.nome}'/>"<c:if test="${not empty param.primeiroAcesso}"> data-primeiro-acesso="true"</c:if>>
<div class="cds-pagina">

    <!-- CABEÇALHO -->
    <header class="topo topo--cds">
        <a class="topo__sair" href="${pageContext.request.contextPath}/login?acao=sair">
            Sair
        </a>

        <a class="topo__logo" href="${pageContext.request.contextPath}/landing.jsp" aria-label="SKADI">
            <span data-icone="logo"></span>
        </a>
    </header>

    <!-- UNIDADES -->
    <main class="cds" data-crud>
        <h1 class="cds__ola">Olá, <span data-cds-usuario><c:out value="${sessionScope.usuarioLogado.nome}"/></span>!</h1>
        <h2 class="cds__pergunta" data-cds-pergunta>Qual unidade você gostaria de acessar?</h2>

        <div class="cds__ferramentas">
            <div class="cds__pesquisa">
                <label class="so-leitor" for="pesquisa">Pesquisar filial por CNPJ</label>
                <input class="cds__campo" id="pesquisa" type="search" placeholder="Pesquisar filial por CNPJ" autocomplete="off" data-crud-pesquisa>
            </div>

            <button type="button" class="cds__adicionar" data-crud-adicionar aria-label="Cadastrar nova unidade">
                <span data-icone="adicionar"></span>
            </button>

            <button type="button" class="cds__lapis" data-cds-editar aria-label="Editar unidades" aria-pressed="false">
                <span data-icone="pencil"></span>
            </button>
        </div>

        <!-- cartões das unidades (montados pelo cds.js) -->
        <ul class="cds__lista" data-cds-lista aria-label="Unidades"></ul>

        <!-- estado vazio: "primeiro acesso" -->
        <p class="cds__vazio" data-cds-vazio role="status" hidden></p>

        <button type="button" class="cds__cancelar" data-cds-cancelar hidden>Cancelar edição</button>
    </main>

</div>
</c:if>
<div hidden data-registros="cds">
    <c:forEach var="cd" items="${listaCds}">
        <%-- admin: o CDServlet manda o endereço e as contagens de cada CD dele (resumoCds) --%>
        <c:set var="resumo" value="${resumoCds[cd.idCd]}"/>
        <c:set var="enderecoCd" value="${resumo.endereco}"/>
        <i data-id="${cd.idCd}" data-nome="<c:out value='${cd.nome}'/>" data-cnpj="<c:out value='${cd.cnpj}'/>"<c:if test="${not empty resumo}"> data-resumo="sim" data-frigorificos-ativos="${resumo.frigorificosAtivos}" data-frigorificos-total="${resumo.frigorificosTotal}" data-funcionarios-total="${resumo.funcionariosTotal}"<c:if test="${not empty enderecoCd}"> data-endereco-id="${enderecoCd.idEndereco}" data-cep="<c:out value='${enderecoCd.cep}'/>" data-estado="<c:out value='${enderecoCd.estado}'/>" data-cidade="<c:out value='${enderecoCd.cidade}'/>" data-bairro="<c:out value='${enderecoCd.bairro}'/>" data-rua="<c:out value='${enderecoCd.rua}'/>" data-numero="<c:out value='${enderecoCd.numero}'/>" data-complemento="<c:out value='${enderecoCd.complemento}'/>"</c:if></c:if>></i>
    </c:forEach>
</div>
<c:if test="${param.formato != 'dados'}">
</body>
</html>
</c:if>
