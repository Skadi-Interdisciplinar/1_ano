<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Redefinir Senha</title>
    <style>
        body { font-family: Arial; background-color: #f4f4f9; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .container { background-color: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 100%; max-width: 350px; }
        input, button { width: 100%; padding: 10px; margin-top: 10px; box-sizing: border-box; }
        button { background-color: #28a745; color: white; border: none; cursor: pointer; }
        .erro { color: red; text-align: center; font-size: 14px; margin-bottom: 10px;}
    </style>
</head>
<body>
<div class="container">
    <h2>Nova Senha</h2>

    <% if (request.getParameter("erro") != null) { %>
        <div class="erro">Código inválido ou expirado!</div>
    <% } %>

    <form action="recuperar-senha" method="post">
        <input type="hidden" name="acao" value="salvarNovaSenha">

        <!-- O e-mail vem da tela anterior -->
        <input type="hidden" name="email" value="<%= request.getParameter("email") %>">

        <label>Código de 6 dígitos:</label>
        <input type="text" name="codigo" required maxlength="6">

        <label>Nova Senha:</label>
        <input type="password" name="novaSenha" required>

        <button type="submit">Atualizar Senha</button>
    </form>
</div>
</body>
</html>