<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%-- O e-mail fica guardado na sessão pelo RecuperarSenhaServlet; sem ele, volta para o primeiro passo --%>
<c:if test="${empty sessionScope.recuperarEmail}">
  <c:redirect url="/esqueci-senha.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Informe o código</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleEsqueci.css">
</head>
<body>

  <!-- Botão de voltar (volta para informar o e-mail) -->
  <a href="${pageContext.request.contextPath}/esqueci-senha.jsp" class="back-btn" aria-label="Voltar">
    <img src="${pageContext.request.contextPath}/Imagens/seta_azul.svg" alt="">
  </a>

  <div class="login-container">

    <!-- Bolas suaves de fundo -->
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>
    <div class="blob blob-3"></div>

    <!-- LADO ESQUERDO: lobo -->
    <div class="left-side">
      <img class="illustration" src="${pageContext.request.contextPath}/Imagens/lobao.svg" alt="Lobo Skadi">
    </div>

    <!-- LADO DIREITO: conteúdo -->
    <div class="right-side">
      <a href="${pageContext.request.contextPath}/landing.jsp" class="logo"><img src="${pageContext.request.contextPath}/Imagens/logo.svg" alt="Skadi"></a>

      <h2 class="titulo">Código de redefinição</h2>
      <p class="subtitulo">Digite o código que recebeu por e-mail para redefinir sua senha</p>

      <c:choose>
        <c:when test="${param.erro == 'codigoInvalido'}">
          <p class="mensagem-erro" role="alert">Código incorreto. Confira o e-mail e tente de novo.</p>
        </c:when>
        <c:when test="${param.erro == 'codigoExpirado'}">
          <p class="mensagem-erro" role="alert">O código expirou. Peça um novo código.</p>
        </c:when>
        <c:when test="${param.erro == 'tentativas'}">
          <p class="mensagem-erro" role="alert">Muitas tentativas. Peça um novo código.</p>
        </c:when>
        <c:when test="${param.erro == 'aguarde'}">
          <p class="mensagem-erro" role="alert">Um código já foi enviado há pouco. Aguarde alguns segundos para pedir outro.</p>
        </c:when>
      </c:choose>

      <form class="form" action="${pageContext.request.contextPath}/recuperar-senha" method="post">
        <input type="hidden" name="acao" value="validarCodigo">

        <div class="input-group">
          <input type="text" name="codigo" id="codigo" placeholder="Código" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" autocomplete="one-time-code" title="Digite os 6 números do código" required>
        </div>

        <button type="submit" class="btn btn-primary">Prosseguir</button>
      </form>
    </div>
  </div>

</body>
</html>
