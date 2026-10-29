<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Acesso negado</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleEsqueci.css">
</head>
<body>

  <div class="login-container">
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>
    <div class="blob blob-3"></div>

    <div class="left-side">
      <img class="illustration" src="${pageContext.request.contextPath}/Imagens/lobao.svg" alt="Lobo Skadi">
    </div>

    <div class="right-side">
      <a href="${pageContext.request.contextPath}/landing.jsp" class="logo"><img src="${pageContext.request.contextPath}/Imagens/logo.svg" alt="Skadi"></a>

      <h2 class="titulo">Acesso negado</h2>
      <p class="subtitulo">
        <c:choose>
          <c:when test="${not empty motivoNegado}"><c:out value="${motivoNegado}"/></c:when>
          <c:otherwise>Você não tem permissão para abrir esta página.</c:otherwise>
        </c:choose>
      </p>

      <div class="form">
        <c:choose>
          <c:when test="${not empty sessionScope.usuarioLogado and sessionScope.usuarioLogado.nivelAcesso != 'operador'}">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/cd?acao=listar">Voltar às unidades</a>
          </c:when>
          <c:otherwise>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/login?acao=sair">Sair</a>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>

</body>
</html>
