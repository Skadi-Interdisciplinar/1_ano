<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %><!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Recuperar Senha - Skadi</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f9; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 100%; max-width: 400px; text-align: center; }
        input[type="email"] { width: 90%; padding: 10px; margin: 15px 0; border: 1px solid #ccc; border-radius: 4px; }
        button { background-color: #0056b3; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; width: 100%; font-size: 16px; }
        button:hover { background-color: #004494; }
        .msg-sucesso { color: green; margin-bottom: 15px; font-weight: bold; }
        .msg-erro { color: red; margin-bottom: 15px; font-weight: bold; }
        a { display: block; margin-top: 15px; text-decoration: none; color: #0056b3; }
    </style>
</head>
<body>

    <div class="container">
        <h2>Esqueci minha senha</h2>
        <p>Digite seu e-mail para receber o código de recuperação.</p>

        <!-- Mostra mensagem de sucesso ou erro vinda do Servlet -->
        <c:if test="${not empty mensagemSucesso}">
            <div class="msg-sucesso">${mensagemSucesso}</div>
        </c:if>
        <c:choose>
            <c:when test="${param.erro eq 'naoEncontrado'}">
                <div class="msg-erro">O e-mail informado não está cadastrado.</div>
            </c:when>
            <c:when test="${param.erro eq 'email'}">
                <div class="msg-erro">Não foi possível enviar o código. Tente novamente mais tarde.</div>
            </c:when>
            <c:when test="${not empty mensagemErro}">
                <div class="msg-erro">${mensagemErro}</div>
            </c:when>
        </c:choose>


        <form action="recuperar-senha" method="POST">
        <input type="hidden" name="acao" value="gerarCodigo">

         <input type="email" name="email" placeholder="Seu e-mail cadastrado" required>
         <button type="submit">Enviar Código</button>
        </form>

        <a href="login.jsp">Voltar para o Login</a>
    </div>

</body>
</html>
