<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Formulário de frigorífico (servlet /frigorifico, ações "novo" e "editar").
  Na tela nova o formulário abre em uma janela dentro da própria lista, então:
    - com "&formato=dados" esta página devolve só as opções das listas do formulário;
    - sem isso (ex.: o servlet voltou com ?erro=validacao) ela volta para a lista,
      que mostra o aviso do erro.
--%>
<c:choose>
<c:when test="${param.formato == 'dados'}">
    <div hidden data-registros="cds">
        <c:forEach var="cd" items="${listaCds}">
            <i data-id="${cd.idCd}" data-nome="<c:out value='${cd.nome}'/>" data-cnpj="<c:out value='${cd.cnpj}'/>"></i>
        </c:forEach>
    </div>
</c:when>
<c:otherwise>
    <c:redirect url="/frigorifico">
        <c:param name="acao" value="listar"/>
        <c:if test="${not empty param.erro}"><c:param name="erro" value="${param.erro}"/></c:if>
    </c:redirect>
</c:otherwise>
</c:choose>
