<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Termômetros (servlet /termometro): admin vê os da própria CD; super_admin vê todos.
  Com "&formato=dados" devolve só os registros (o crudServidor.js usa para recarregar a tabela).
--%>
<%-- Aberta direto pelo endereço (sem passar pelo servlet): vai para o servlet, que confere o login e carrega os dados --%>
<c:if test="${empty requestScope['jakarta.servlet.forward.request_uri']}">
    <c:redirect url="/termometro?acao=listar"/>
</c:if>
<c:if test="${param.formato != 'dados'}">
<c:set var="paginaAtual" value="termometro"/>
<c:set var="tituloPagina" value="Termômetros"/>
<c:set var="scriptPagina" value="termometros.js"/>
<c:set var="rotuloPlural" value="termômetros"/>
<c:set var="rotuloNovo" value="Cadastrar novo termômetro"/>
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
<div hidden data-registros="termometros">
    <c:forEach var="t" items="${listaTermometros}">
        <i data-id="${t.idTermometro}" data-modelo="<c:out value='${t.modelo}'/>" data-status="<c:out value='${t.status}'/>"
           data-id-frigorifico="${t.idFrigorifico}"></i>
    </c:forEach>
</div>
<%-- Frigoríficos visíveis (nome na tabela e lista do formulário) --%>
<div hidden data-registros="frigorificos">
    <c:forEach var="f" items="${listaFrigorificos}">
        <i data-id="${f.idFrigorifico}" data-nome="<c:out value='${f.nome}'/>" data-localizacao="<c:out value='${f.localizacao}'/>" data-id-cd="${f.idCd}"></i>
    </c:forEach>
</div>
<c:if test="${param.formato != 'dados'}">
</body>
</html>
</c:if>
