<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Endereços das unidades (servlet /endereco). Na tela, o endereço é editado junto com a unidade (lista-cd.jsp).
  Com "&formato=dados" devolve só os registros (o crudServidor.js usa para recarregar a tabela).
--%>
<%-- Aberta direto pelo endereço (sem passar pelo servlet): vai para o servlet, que confere o login e carrega os dados --%>
<c:if test="${empty requestScope['jakarta.servlet.forward.request_uri']}">
    <c:redirect url="/endereco?acao=listar"/>
</c:if>
<c:if test="${param.formato != 'dados'}">
<c:set var="paginaAtual" value="endereco"/>
<c:set var="tituloPagina" value="Endereços"/>
<c:set var="scriptPagina" value="enderecos.js"/>
<c:set var="rotuloPlural" value="endereços"/>
<c:set var="rotuloNovo" value="Cadastrar endereço"/>
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
<div hidden data-registros="enderecos">
    <c:forEach var="e" items="${listaEnderecos}">
        <i data-id="${e.idEndereco}" data-id-cd="${e.idCd}" data-nome-cd="<c:out value='${e.nomeCd}'/>" data-rua="<c:out value='${e.rua}'/>"
           data-numero="${e.numero}" data-complemento="<c:out value='${e.complemento}'/>" data-bairro="<c:out value='${e.bairro}'/>"
           data-cidade="<c:out value='${e.cidade}'/>" data-estado="<c:out value='${e.estado}'/>" data-cep="<c:out value='${e.cep}'/>"></i>
    </c:forEach>
</div>
<c:if test="${param.formato != 'dados'}">
</body>
</html>
</c:if>
