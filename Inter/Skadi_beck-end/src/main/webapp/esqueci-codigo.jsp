<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<%-- Passo 2 da recuperação de senha.
     O RecuperarSenhaServlet redireciona para "redefinir-senha.jsp?email=...", que mostra esta tela
     enquanto o código ainda não foi digitado. --%>
<c:if test="${empty param.email}">
  <c:redirect url="/esqueci-senha.jsp"/>
</c:if>
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
      <a href="${pageContext.request.contextPath}/area-restrita" class="logo"><img src="${pageContext.request.contextPath}/Imagens/logo.svg" alt="Skadi"></a>

      <h2 class="titulo">Código de redefinição</h2>
      <p class="subtitulo">Digite o código que recebeu por e-mail para redefinir sua senha</p>

      <form class="form" id="form-codigo" action="${pageContext.request.contextPath}/redefinir-senha.jsp" method="post">
        <input type="hidden" name="email" value="<c:out value='${param.email}'/>">

        <c:choose>
          <c:when test="${param.erro == 'codigoInvalido'}">
            <p class="mensagem mensagem--erro" role="alert">Código inválido ou expirado. Confira o e-mail ou peça um novo código.</p>
          </c:when>
          <c:otherwise>
            <p class="mensagem mensagem--sucesso" role="status">Enviamos um código para <c:out value="${param.email}"/>.</p>
          </c:otherwise>
        </c:choose>
        <div class="input-group">
          <input type="text" id="codigo" name="codigo" placeholder="Código" inputmode="numeric" autocomplete="one-time-code" maxlength="6" pattern="[0-9]{6}" title="Digite os 6 números do código" required>
        </div>

        <button type="submit" class="primary">Prosseguir</button>
      </form>
    </div>
  </div>

<script>
document.getElementById('form-codigo').addEventListener('submit', (e) => {
  e.preventDefault();

  const codigo = document.getElementById('codigo');

  if (!codigo.checkValidity()) {
    codigo.reportValidity();
    return;
  }

  // Guarda o código só nesta aba, para não precisar digitá-lo de novo se a nova senha for recusada
  sessionStorage.setItem('codigoRecuperacao', codigo.value.trim());
  e.target.submit();
});
</script>

</body>
</html>