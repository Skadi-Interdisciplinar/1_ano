<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Categorias (servlet /categoria): catálogo global; admin e super_admin cadastram, editam e excluem
  (o admin não mexe em categoria usada por lotes de outro cliente).
  Com "&formato=dados" devolve só os registros (o crudServidor.js usa para recarregar a tabela).
--%>
<%-- Aberta direto pelo endereço (sem passar pelo servlet): vai para o servlet, que confere o login e carrega os dados --%>
<c:if test="${empty requestScope['jakarta.servlet.forward.request_uri']}">
    <c:redirect url="/categoria?acao=listar"/>
</c:if>
<c:if test="${param.formato != 'dados'}">
<c:set var="paginaAtual" value="categoria"/>
<c:set var="tituloPagina" value="Categorias"/>
<c:set var="scriptPagina" value="categorias.js"/>
<c:set var="rotuloPlural" value="categorias"/>
<c:set var="rotuloNovo" value="Cadastrar categoria"/>
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
<div hidden data-registros="categorias">
    <c:forEach var="cat" items="${listaCategorias}">
        <i data-id="${cat.idCategoria}" data-nome="<c:out value='${cat.nome}'/>" data-vida-util-horas="<c:out value='${cat.vidaUtilHoras}'/>"
           data-temperatura-min="${cat.temperaturaMin}" data-temperatura-max="${cat.temperaturaMax}"
           data-outro-cliente="${categoriasDeOutroCliente.contains(cat.idCategoria)}"></i>
    </c:forEach>
</div>
<c:if test="${param.formato != 'dados'}">
</body>
</html>
</c:if>
