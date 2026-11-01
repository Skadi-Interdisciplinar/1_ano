<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Lotes (servlet /lote): admin vê os da própria CD; super_admin vê todos.
  Com "&formato=dados" devolve só os registros (o crudServidor.js usa para recarregar a tabela).
--%>
<%-- Aberta direto pelo endereço (sem passar pelo servlet): vai para o servlet, que confere o login e carrega os dados --%>
<c:if test="${empty requestScope['jakarta.servlet.forward.request_uri']}">
    <c:redirect url="/lote?acao=listar"/>
</c:if>
<c:if test="${param.formato != 'dados'}">
<c:set var="paginaAtual" value="lote"/>
<c:set var="tituloPagina" value="Lotes"/>
<c:set var="scriptPagina" value="lotes.js"/>
<c:set var="rotuloPlural" value="lotes"/>
<c:set var="rotuloNovo" value="Cadastrar lote"/>
<!DOCTYPE html>
<html lang="pt-br">
<%@ include file="/WEB-INF/jspf/cabeca-crud.jspf" %>

<body data-nivel="<c:out value='${sessionScope.usuarioLogado.nivelAcesso}'/>" data-id-cd="${sessionScope.usuarioLogado.idCd}" data-usuario="<c:out value='${sessionScope.usuarioLogado.nome}'/>">
<div class="pagina">

    <!-- CABEÇALHO E MENU LATERAL -->
<%@ include file="/WEB-INF/jspf/topo-crud.jspf" %>

    <!-- TABELA -->
<%@ include file="/WEB-INF/jspf/tabela-crud.jspf" %>

</div>
</c:if>
<div hidden data-registros="lotes">
    <c:forEach var="l" items="${listaLotes}">
        <i data-id="${l.idLote}" data-codigo="<c:out value='${l.codigoLote}'/>" data-id-categoria="${l.idCategoria}"
           data-data-fabricacao="${l.dataFabricacao}" data-data-validade="${l.dataValidade}"
           data-status="<c:out value='${l.status}'/>" data-id-cd="${l.idCd}"></i>
    </c:forEach>
</div>
<c:if test="${param.formato != 'dados'}">
</body>
</html>
</c:if>
