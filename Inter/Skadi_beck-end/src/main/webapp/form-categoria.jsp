<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Formulário de categoria (servlet /categoria, ações "novo" e "editar"; admin e super_admin).
  Na tela nova o formulário abre em uma janela dentro da própria lista, então:
    - com "&formato=dados" esta página devolve só as opções das listas do formulário;
    - sem isso (ex.: o servlet voltou com ?erro=validacao) ela volta para a lista,
      que mostra o aviso do erro.
--%>
<c:choose>
<c:when test="${param.formato == 'dados'}">
    <%-- Categoria não tem listas no formulário --%>
</c:when>
<c:otherwise>
    <c:redirect url="/categoria">
        <c:param name="acao" value="listar"/>
        <c:if test="${not empty param.erro}"><c:param name="erro" value="${param.erro}"/></c:if>
    </c:redirect>
</c:otherwise>
</c:choose>
