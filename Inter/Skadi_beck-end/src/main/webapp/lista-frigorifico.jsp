<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Frigoríficos (servlet /frigorifico): admin vê os da própria CD; super_admin vê todos.
  Com "&formato=dados" devolve só os registros (o crudServidor.js usa para recarregar a tabela).
--%>
<%-- Aberta direto pelo endereço (sem passar pelo servlet): vai para o servlet, que confere o login e carrega os dados --%>
<c:if test="${empty requestScope['jakarta.servlet.forward.request_uri']}">
    <c:redirect url="/frigorifico?acao=listar"/>
</c:if>
<c:if test="${param.formato != 'dados'}">
<c:set var="paginaAtual" value="frigorifico"/>
<c:set var="tituloPagina" value="Frigoríficos"/>
<c:set var="scriptPagina" value="frigorificos.js"/>
<c:set var="rotuloPlural" value="frigoríficos"/>
<c:set var="rotuloNovo" value="Adicionar frigorífico"/>
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
<div hidden data-registros="frigorificos">
    <c:forEach var="f" items="${listaFrigorificos}">
        <i data-id="${f.idFrigorifico}" data-nome="<c:out value='${f.nome}'/>" data-modelo="<c:out value='${f.modelo}'/>" data-localizacao="<c:out value='${f.localizacao}'/>"
           data-temperatura-min="${f.temperaturaMin}" data-temperatura-max="${f.temperaturaMax}" data-id-cd="${f.idCd}"></i>
    </c:forEach>
</div>
<c:if test="${param.formato != 'dados'}">
</body>
</html>
</c:if>
