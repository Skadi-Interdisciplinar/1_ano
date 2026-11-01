<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Formulário de unidade (servlet /cd, ações "novo" e "editar").
  Na tela nova o formulário abre em uma janela dentro da lista de unidades, então:
    - admin ainda sem CD (1º acesso): mostra a tela de unidades já com a janela de cadastro aberta;
    - com "&formato=dados": não há listas no formulário, devolve vazio;
    - nos outros casos volta para a lista (que mostra o aviso de ?erro=...).
--%>
<c:choose>
<c:when test="${param.formato == 'dados'}">
    <div hidden data-registros="cds"></div>
</c:when>
<c:when test="${sessionScope.usuarioLogado.nivelAcesso == 'admin' and empty sessionScope.usuarioLogado.idCd}">
    <jsp:forward page="/lista-cd.jsp">
        <jsp:param name="primeiroAcesso" value="sim"/>
    </jsp:forward>
</c:when>
<c:otherwise>
    <c:redirect url="/cd">
        <c:param name="acao" value="listar"/>
        <c:if test="${not empty param.erro}"><c:param name="erro" value="${param.erro}"/></c:if>
    </c:redirect>
</c:otherwise>
</c:choose>
