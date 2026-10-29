<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%-- Só chega aqui quem já informou o e-mail e o código certo --%>
<c:if test="${empty sessionScope.recuperarEmail}">
  <c:redirect url="/esqueci-senha.jsp"/>
</c:if>
<c:if test="${empty sessionScope.recuperarCodigoOk}">
  <c:redirect url="/esqueci-codigo.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Redefina a senha</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleEsqueci.css">
</head>
<body>

  <!-- Botão de voltar -->
  <a href="${pageContext.request.contextPath}/esqueci-codigo.jsp" class="back-btn" aria-label="Voltar">
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

      <h2 class="titulo">Redefina a senha</h2>
      <p class="subtitulo">Digite a sua nova senha para voltar a ter acesso a sua conta</p>

      <c:choose>
        <c:when test="${param.erro == 'senhaInvalida'}">
          <p class="mensagem-erro" role="alert">A senha precisa ter pelo menos 8 caracteres, com uma letra maiúscula e um número.</p>
        </c:when>
        <c:when test="${param.erro == 'senhasDiferentes'}">
          <p class="mensagem-erro" role="alert">As senhas não coincidem.</p>
        </c:when>
        <c:when test="${param.erro == 'banco'}">
          <p class="mensagem-erro" role="alert">Não foi possível salvar a nova senha. Tente novamente.</p>
        </c:when>
      </c:choose>

      <form class="form" id="form-senha" action="${pageContext.request.contextPath}/recuperar-senha" method="post">
        <input type="hidden" name="acao" value="redefinir">

        <!-- Campo: Nova senha -->
        <div class="input-group">
          <input type="password" id="nova-senha" name="novaSenha" placeholder="Nova senha" autocomplete="new-password" required>

          <button type="button" class="olho" aria-label="Mostrar nova senha" aria-pressed="false" data-target="nova-senha">
            <img class="olho-aberto" src="${pageContext.request.contextPath}/Imagens/olho_ac.svg" alt="">
            <img class="olho-fechado" src="${pageContext.request.contextPath}/Imagens/olho_fc.svg" alt="">
          </button>
        </div>

        <!-- Campo: Confirmar senha -->
        <div class="input-group">
          <input type="password" id="confirma-senha" name="confirmarSenha" placeholder="Confirme a senha" autocomplete="new-password" required>

          <button type="button" class="olho" aria-label="Mostrar confirmação da senha" aria-pressed="false" data-target="confirma-senha">
            <img class="olho-aberto" src="${pageContext.request.contextPath}/Imagens/olho_ac.svg" alt="">
            <img class="olho-fechado" src="${pageContext.request.contextPath}/Imagens/olho_fc.svg" alt="">
          </button>
        </div>

        <button type="submit" class="btn btn-primary">Voltar para login</button>
      </form>
    </div>
  </div>

<script src="${pageContext.request.contextPath}/js/redefinir-senha.js"></script>
</body>
</html>
