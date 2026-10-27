<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Cadastro</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&family=Poppins:wght@400;500&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleCadastro.css">
</head>
<body>
  <main class="tela">
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>
    <div class="blob blob-3"></div>

    <div class="card-cadastro">
      <header class="topo">
        <a href="${pageContext.request.contextPath}/landing.jsp" class="voltar" aria-label="Voltar">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"><polyline points="15 4 7 12 15 20"></polyline></svg>
        </a>
        <h1>Inicie sua jornada conosco!</h1>
      </header>

      <form class="formulario" id="formCadastro">
        <div class="linha">
          <input type="text" name="nome" placeholder="Nome" required>
          <input type="text" name="sobrenome" placeholder="Sobrenome" required>
        </div>

        <input type="text" name="cpf" placeholder="CPF" inputmode="numeric" required>

        <div class="select-wrap">
          <select name="nivel" required>
            <option value="" disabled selected>Escolha o nível de acesso</option>
            <option value="operador">Operador</option>
            <option value="gestor">Gerente</option>
            <option value="admin">Administrador</option>
            <option value="super_admin">Sistema</option>
          </select>
          <svg class="seta" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"><polyline points="4 8 12 16 20 8"></polyline></svg>
        </div>

        <input type="text" name="cargo" placeholder="Cargo" required>
        <input type="email" name="email" placeholder="Email" required>

        <div class="senha-wrap">
          <input type="password" id="senha" name="senha" placeholder="Senha" required>
          <button type="button" class="olho" id="toggle-senha" aria-label="Mostrar senha" aria-pressed="false">
            <svg class="olho-aberto" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M1.5 12s3.8-7 10.5-7 10.5 7 10.5 7-3.8 7-10.5 7S1.5 12 1.5 12z"/><circle cx="12" cy="12" r="3.2"/></svg>
            <svg class="olho-fechado" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M1.5 12s3.8-7 10.5-7 10.5 7 10.5 7-3.8 7-10.5 7S1.5 12 1.5 12z"/><circle cx="12" cy="12" r="3.2"/><line x1="3" y1="21" x2="21" y2="3"/></svg>
          </button>
        </div>

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

  <div class="modal-overlay" id="modalSucesso">
    <iframe src="${pageContext.request.contextPath}/msg-enviada.jsp" class="mensagem-iframe" title="Mensagem enviada com sucesso"></iframe>
  </div>

  <script src="${pageContext.request.contextPath}/js/senha.js"></script>
  <script src="${pageContext.request.contextPath}/js/cadastro.js"></script>
</body>
</html>