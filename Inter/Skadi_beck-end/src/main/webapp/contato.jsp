<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Contato</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleContato.css">
</head>
<body>
  <main class="tela">
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>
    <div class="blob blob-3"></div>

    <div class="card-cadastro">
      <header class="topo">
        <a href="${pageContext.request.contextPath}/landing.jsp" class="voltar" aria-label="Voltar">
          <img src="${pageContext.request.contextPath}/Imagens/seta_branca.svg" alt="">
        </a>
        <h1>Inicie sua jornada conosco!</h1>
      </header>

      <%-- O ContatoServlet devolve "erro=..." pela URL quando o envio não deu certo --%>
      <c:choose>
        <c:when test="${param.erro == 'validacao'}">
          <p class="mensagem-erro" role="alert">Preencha todos os campos. A mensagem precisa ter pelo menos 10 caracteres.</p>
        </c:when>
        <c:when test="${param.erro == 'aguarde'}">
          <p class="mensagem-erro" role="alert">Você acabou de enviar uma mensagem. Aguarde alguns segundos para enviar outra.</p>
        </c:when>
        <c:when test="${param.erro == 'email'}">
          <p class="mensagem-erro" role="alert">Não foi possível enviar a mensagem agora. Tente novamente em instantes.</p>
        </c:when>
      </c:choose>

      <form class="formulario" id="formSignIn" action="${pageContext.request.contextPath}/contato" method="post">
        <input type="text" name="nome" placeholder="Nome" autocomplete="given-name" maxlength="100" required>
        <input type="text" name="sobrenome" placeholder="Sobrenome" autocomplete="family-name" maxlength="100" required>
        <input type="text" name="empresa" placeholder="Nome da empresa" autocomplete="organization" maxlength="100" required>
        <textarea name="mensagem" placeholder="Mensagem" minlength="10" maxlength="2000" required></textarea>

        <section class="btn-container">
          <button type="submit" class="btn-enviar">Enviar</button>
        </section>
      </form>

      <section class="login">
        <p>Já tem uma conta?</p>
        <a href="${pageContext.request.contextPath}/login-cliente.jsp" class="btn-entrar">Entrar</a>
      </section>
    </div>
  </main>

  <%-- Aviso de sucesso: aparece quando o servlet volta com ?enviado=1 --%>
  <div class="modal-overlay${param.enviado == '1' ? ' ativo' : ''}" id="modalSucesso">
    <iframe src="${pageContext.request.contextPath}/msg-enviada.jsp" class="mensagem-iframe" title="Mensagem enviada com sucesso"></iframe>
  </div>
</body>
</html>
