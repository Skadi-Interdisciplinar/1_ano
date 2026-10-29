<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Área restrita | Skadi</title>

  <!-- Fonte Manrope -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleLoginSuperAdmin.css">
</head>
<body>

  <!-- Botão de voltar -->
  <a href="${pageContext.request.contextPath}/landing.jsp" class="back-btn" aria-label="Voltar">
    <img src="${pageContext.request.contextPath}/Imagens/seta_branca.svg" alt="">
  </a>

  <!-- LADO ESQUERDO: Lobo -->
  <div class="left-side">
    <img class="illustration" src="${pageContext.request.contextPath}/Imagens/lobo_branca.svg" alt="Lobo Skadi">
  </div>

  <!-- LADO DIREITO: Cartão de login -->
  <section class="card">
    <img class="logo" src="${pageContext.request.contextPath}/Imagens/logo.svg" alt="Skadi">
    <h1 class="subtitle">Área restrita</h1>

    <%-- O LoginServlet manda "erro=credenciais" pela URL quando o login falha --%>
    <c:if test="${param.erro == 'credenciais'}">
      <p class="mensagem-erro" role="alert">Usuário ou senha incorretos.</p>
    </c:if>

    <form class="login-form" id="form-login" action="${pageContext.request.contextPath}/login" method="post">
      <%-- Esta tela é só da equipe Skadi: o servlet confere o nível de acesso --%>
      <input type="hidden" name="area" value="sistema">

      <!-- Campo: Usuário -->
      <div class="field">
        <input id="usuario" name="login" type="text" placeholder="Usuário" autocomplete="username" required>
      </div>

      <!-- Campo: Senha com olho -->
      <div class="field">
        <input id="senha" name="senha" type="password" placeholder="Senha" autocomplete="current-password" required>
        <button type="button" class="olho" id="toggle-senha" aria-label="Mostrar senha" aria-pressed="false">
          <img class="olho-aberto" src="${pageContext.request.contextPath}/Imagens/olho_a.svg" alt="">
          <img class="olho-fechado" src="${pageContext.request.contextPath}/Imagens/olho_f.svg" alt="">
        </button>
      </div>

      <!-- Lembre-se de mim + Esqueceu a senha -->
      <div class="row">
        <div class="checkbox-group">
          <input id="remember" name="lembrar" type="checkbox">
          <label for="remember">Lembre-se de mim</label>
        </div>
        <a href="${pageContext.request.contextPath}/esqueci-senha.jsp" class="forgot-link">Esqueceu a senha?</a>
      </div>

      <!-- Botão: Entrar -->
      <button type="submit" class="btn btn-primary">Entrar</button>
    </form>
  </section>

<script src="${pageContext.request.contextPath}/js/senha.js"></script>
</body>
</html>
