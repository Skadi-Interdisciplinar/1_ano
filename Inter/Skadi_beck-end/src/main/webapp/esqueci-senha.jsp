<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Informe o email</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleEsqueci.css">
</head>
<body>

  <!-- Botão de voltar (volta para o login) -->
  <a href="${pageContext.request.contextPath}/login-cliente.jsp" class="back-btn" aria-label="Voltar">
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

      <h2 class="titulo">Esqueceu a senha</h2>
      <p class="subtitulo">Informe seu e-mail para redefinir a sua senha</p>

      <%-- O RecuperarSenhaServlet devolve "erro=..." pela URL --%>
      <c:choose>
        <c:when test="${param.erro == 'naoEncontrado'}">
          <p class="mensagem-erro" role="alert">Não encontramos uma conta com esse e-mail.</p>
        </c:when>
        <c:when test="${param.erro == 'emailInvalido'}">
          <p class="mensagem-erro" role="alert">Informe um e-mail válido.</p>
        </c:when>
        <c:when test="${param.erro == 'email'}">
          <p class="mensagem-erro" role="alert">Não foi possível enviar o e-mail agora. Tente novamente em instantes.</p>
        </c:when>
      </c:choose>

      <form class="form" action="${pageContext.request.contextPath}/recuperar-senha" method="post">
        <input type="hidden" name="acao" value="gerarCodigo">

        <div class="input-group">
          <input type="email" name="email" id="email" placeholder="E-mail" autocomplete="email" maxlength="150" required>
        </div>

        <button type="submit" class="btn btn-primary">Enviar</button>
      </form>
    </div>
  </div>

</body>
</html>
