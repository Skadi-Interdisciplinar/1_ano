<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Formulário de termômetro (servlet /termometro, ações "novo" e "editar").
  Na tela nova o formulário abre em uma janela dentro da própria lista, então:
    - com "&formato=dados" esta página devolve só as opções das listas do formulário;
    - sem isso (ex.: o servlet voltou com ?erro=validacao) ela volta para a lista,
      que mostra o aviso do erro.
--%>
<c:choose>
<c:when test="${param.formato == 'dados'}">
    <div hidden data-registros="frigorificos">
        <c:forEach var="f" items="${listaFrigorificos}">
            <i data-id="${f.idFrigorifico}" data-nome="<c:out value='${f.nome}'/>" data-id-cd="${f.idCd}"></i>
        </c:forEach>
    </div>
</c:when>
<c:otherwise>
    <c:redirect url="/termometro">
        <c:param name="acao" value="listar"/>
        <c:if test="${not empty param.erro}"><c:param name="erro" value="${param.erro}"/></c:if>
    </c:redirect>
</c:otherwise>
</c:choose>
