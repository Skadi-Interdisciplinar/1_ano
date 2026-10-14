<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Skadi</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .login-container {
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 350px;
        }
        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #666;
            font-size: 14px;
        }
        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
        }
        button {
            width: 100%;
            padding: 10px;
            background-color: #0056b3;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 10px;
        }
        button:hover {
            background-color: #004494;
        }
        .error-message {
            color: #721c24;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
            text-align: center;
            font-size: 14px;
        }
        .forgot-password {
            display: block;
            text-align: center;
            margin-top: 15px;
            font-size: 14px;
            text-decoration: none;
            color: #0056b3;
        }
        .forgot-password:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="login-container">
    <h2>Acesso ao Sistema</h2>

    <%-- O Servlet manda "erro=credenciais" pela URL se o login falhar --%>
    <% if ("credenciais".equals(request.getParameter("erro"))) { %>
        <div class="error-message">
            E-mail, CPF ou senha incorretos.
        </div>
    <% } %>

    <form action="login" method="post">
        <div class="form-group">
            <label for="login">E-mail, Username ou CPF</label>
            <%-- O 'name="login"' é exatamente o que o LoginServlet vai buscar --%>
            <input type="text" id="login" name="login" required placeholder="Digite seu acesso">
        </div>

        <div class="form-group">
            <label for="senha">Senha</label>
            <input type="password" id="senha" name="senha" required placeholder="Digite sua senha">
        </div>

        <button type="submit">Entrar</button>
    </form>

    <a href="esqueci-senha.jsp" class="forgot-password">Esqueci minha senha</a>
</div>

</body>
</html>