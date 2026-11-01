<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>LOGIN CLIENTE</title>

  <!-- Fonte Manrope -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleLoginCliente.css">
</head>
<body>

  <!-- Bolas suaves de fundo -->
   <div class="blob blob-1"></div>
   <div class="blob blob-2"></div>
   <div class="blob blob-3"></div>
   
   <div class="login-container"></div>

    <!-- Botão de voltar (seta azul) -->
    <a href="${pageContext.request.contextPath}/landing.jsp" class="back-btn" aria-label="Voltar">
      <img src="${pageContext.request.contextPath}/Imagens/seta_azul.svg" alt="">
    </a>

    <!-- LADO ESQUERDO: Ilustração do lobo -->
<div class="left-side">
  <img class="illustration" src="${pageContext.request.contextPath}/Imagens/lobao.svg" alt="Lobo Skadi">
</div>

<!-- LADO DIREITO: Formulário de Login -->
<div class="right-side">

  <!-- Logo e saudação -->
  <a href="${pageContext.request.contextPath}/area-restrita" class="logo" aria-label="Skadi">
    <img src="${pageContext.request.contextPath}/Imagens/logo.svg" alt="Skadi">
  </a>
  <h2 class="welcome-text">Bem vindo de volta!</h2>

  <!-- Formulário -->
  <form class="login-form" id="form-login" action="${pageContext.request.contextPath}/login" method="post">
    <%-- Janela dos clientes; a equipe Skadi (super_admin) entra pela Área restrita --%>
    <input type="hidden" name="area" value="cliente">

    <%-- Mensagens que o LoginServlet e o RecuperarSenhaServlet mandam pela URL --%>
    <c:choose>
      <c:when test="${param.erro == 'credenciais'}">
        <p class="mensagem mensagem--erro" role="alert">Usuário ou senha incorretos.</p>
      </c:when>
      <c:when test="${param.erro == 'areaRestrita'}">
        <p class="mensagem mensagem--erro" role="alert">Contas da equipe Skadi entram pela Área restrita.</p>
      </c:when>
      <c:when test="${param.erro == 'sessao'}">
        <p class="mensagem mensagem--erro" role="alert">Sua sessão expirou. Entre novamente.</p>
      </c:when>
      <c:when test="${param.sucesso == 'senhaRedefinida'}">
        <p class="mensagem mensagem--sucesso" role="status">Senha redefinida! Entre com a nova senha.</p>
      </c:when>
    </c:choose>

    <!-- Campo: Usuário -->
    <div class="input-group">
      <input type="text" id="login" name="login" value="<c:out value='${cookie.lembrarEmail.value}'/>" placeholder="Usuário" aria-label="Usuário (e-mail, CPF ou username)" autocomplete="username" required>
    </div>

    <!-- Campo: Senha com ícone de visualizar -->
    <div class="input-group password-group">
      <input type="password" id="senha" name="senha" placeholder="Senha" aria-label="Senha" autocomplete="current-password" required>
      <button type="button" class="olho" id="toggle-senha" aria-label="Mostrar senha" aria-pressed="false">
        <!-- olho aberto: senha escondida -->
        <img class="olho-aberto" src="${pageContext.request.contextPath}/Imagens/olho_ac.svg" alt="">
        <!-- olho riscado: senha visível -->
        <img class="olho-fechado" src="${pageContext.request.contextPath}/Imagens/olho_fc.svg" alt="">
      </button>
    </div>

    <!-- Botão: Entrar -->
    <button type="submit" class="btn btn-primary">Entrar</button>

    <!-- Link: Esqueceu a senha -->
    <a href="${pageContext.request.contextPath}/esqueci-senha.jsp" class="forgot-link">Esqueceu a senha?</a>

    <!-- Opção: Lembre-se de mim -->
    <div class="checkbox-group">
      <input id="remember" name="lembrar" type="checkbox"<c:if test="${not empty cookie.lembrarEmail.value}"> checked</c:if>>
      <label for="remember">Lembre-se de mim</label>
    </div>

    <!-- Texto para criar conta -->
    <p class="signup-prompt">Ainda não tem uma conta?</p>

    <!-- Botão: Criar conta -->
    <button type="button" class="btn btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/contato.jsp'">Criar conta</button>
  </form>
</div>
  </div>

<script>
const campoSenha = document.getElementById('senha');
const botaoOlho = document.getElementById('toggle-senha');

botaoOlho.addEventListener('click', () => {
  const mostrando = campoSenha.type === 'text';
  campoSenha.type = mostrando ? 'password' : 'text';
  botaoOlho.classList.toggle('ativo', !mostrando);
  botaoOlho.setAttribute('aria-pressed', String(!mostrando));
  botaoOlho.setAttribute('aria-label', mostrando ? 'Mostrar senha' : 'Ocultar senha');
});
</script>

</body>
</html>