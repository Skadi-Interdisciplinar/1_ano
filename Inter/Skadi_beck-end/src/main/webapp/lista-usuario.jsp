<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Funcionários (servlet /usuario): admin vê os da própria CD; super_admin vê todos.
  Com "&formato=dados" devolve só os registros (o crudServidor.js usa para recarregar a tabela).
--%>
<%-- Aberta direto pelo endereço (sem passar pelo servlet): vai para o servlet, que confere o login e carrega os dados --%>
<c:if test="${empty requestScope['jakarta.servlet.forward.request_uri']}">
    <c:redirect url="/usuario?acao=listar"/>
</c:if>
<c:if test="${param.formato != 'dados'}">
<c:set var="paginaAtual" value="usuario"/>
<c:set var="tituloPagina" value="Funcionários"/>
<c:set var="scriptPagina" value="funcionarios.js"/>
<c:set var="rotuloPlural" value="funcionários"/>
<c:set var="rotuloNovo" value="Cadastrar novo funcionário"/>
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
<%-- Registros da tabela (a senha nunca vai para a página, só se ela existe) --%>
<div hidden data-registros="usuarios">
    <c:forEach var="u" items="${listaUsuarios}">
        <i data-id="${u.idUsuario}" data-nome="<c:out value='${u.nome}'/>" data-username="<c:out value='${u.username}'/>"
           data-email="<c:out value='${u.email}'/>" data-cpf="<c:out value='${u.cpf}'/>" data-cargo="<c:out value='${u.cargo}'/>"
           data-nivel="<c:out value='${u.nivelAcesso}'/>" data-id-cd="${u.idCd}" data-nome-cd="<c:out value='${u.nomeCd}'/>"
           data-cod-gestor="${u.codGestor}" data-tem-senha="${not empty u.senha}"></i>
    </c:forEach>
</div>
<c:if test="${param.formato != 'dados'}">
</body>
</html>
</c:if>
