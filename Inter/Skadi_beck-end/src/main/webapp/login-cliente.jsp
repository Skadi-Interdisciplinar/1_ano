<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>LOGIN CLIENTE</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleLoginCliente.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>


  <a href="${pageContext.request.contextPath}/landing.jsp" class="back-btn" aria-label="Voltar">
  <img src="${pageContext.request.contextPath}/Imagens/seta.svg" alt="">
</a>


  <div class="login-container">

  <div class="blob blob-1"></div>
  <div class="blob blob-2"></div>

    <div class="left-side">
      <img class="illustration" src="${pageContext.request.contextPath}/Imagens/lobao.svg">
    </div>

    <!-- LADO DIREITO: Formulário de Login -->
    <div class="right-side">
    
      <!-- Título e Saudação -->
      <a href="${pageContext.request.contextPath}/login-super-admin.jsp" class="logo"><img src="${pageContext.request.contextPath}/Imagens/logo.svg"></a><br>
      <h2 class="welcome-text">Bem vindo de volta!</h2>

      <!-- Formulário -->
      <%-- O LoginServlet manda "erro=credenciais" pela URL quando o login falha --%>
      <c:if test="${param.erro == 'credenciais'}">
        <p class="mensagem-erro" role="alert">E-mail, CPF ou senha incorretos.</p>
      </c:if>
      <c:if test="${param.erro == 'sessao'}">
        <p class="mensagem-erro" role="alert">Sua sessão expirou. Entre novamente.</p>
      </c:if>
      <c:if test="${param.sucesso == 'senhaRedefinida'}">
        <p class="mensagem-sucesso" role="status">Senha redefinida! Entre com a nova senha.</p>
      </c:if>
      <c:if test="${param.sucesso == 'saiu'}">
        <p class="mensagem-sucesso" role="status">Você saiu do sistema.</p>
      </c:if>

      <form class="login-form" action="${pageContext.request.contextPath}/login" method="post">
        
        <!-- Campo: Usuário -->
        <div class="input-group">
          <input type="text" id="login" name="login" placeholder="Usuário" autocomplete="username" aria-label="Usuário" required>
        </div>

        <!-- Campo: Senha com ícone de visualizar -->
         <div class="input-group password-group">
        <input type="password" id="senha" name="senha" placeholder="Senha" autocomplete="current-password" required>
        <button type="button" class="olho" id="toggle-senha" aria-label="Mostrar senha" aria-pressed="false">
      <!-- olho aberto: senha escondida -->
      <svg class="olho-aberto" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
        <path d="M1.5 12s3.8-7 10.5-7 10.5 7 10.5 7-3.8 7-10.5 7S1.5 12 1.5 12z"/>
        <circle cx="12" cy="12" r="3.2"/>
      </svg>
      <!-- olho riscado: senha visível -->
      <svg class="olho-fechado" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
        <path d="M1.5 12s3.8-7 10.5-7 10.5 7 10.5 7-3.8 7-10.5 7S1.5 12 1.5 12z"/>
        <circle cx="12" cy="12" r="3.2"/>
        <line x1="3" y1="21" x2="21" y2="3"/>
      </svg>
    </button>
  </div>
         
        <!-- Botão: Entrar -->
        <button type="submit" class="btn btn-primary">Entrar</button>

        <!-- Link: Esqueceu a senha -->
        <a href="${pageContext.request.contextPath}/esqueci-senha.jsp" class="forgot-link">Esqueceu a senha?</a>

        <!-- Opção: Lembre-se de mim -->
        <div class="checkbox-group">
          <input id="remember" name="lembrar" type="checkbox">
          <label for="remember">Lembre-se de mim</label><br />
        </div>

        <!-- Divisor / Texto para criar conta -->
        <p class="signup-prompt">Ainda não tem uma conta?</p>

        <!-- Botão: Criar conta -->
        <button type="button" class="btn btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/contato.jsp'">Criar conta</button>
      </form>
    </div>
  </div>
<script src="${pageContext.request.contextPath}/js/senha.js"></script>

</body>
</html>