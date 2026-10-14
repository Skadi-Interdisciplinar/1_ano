<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.skadi_beckend.model.CD" %>
<%
    CD cd = (CD) request.getAttribute("cd");

    String id = (cd != null) ? String.valueOf(cd.getIdCd()) : "";
    String nome = (cd != null) ? cd.getNome() : "";
    String cnpj = (cd != null) ? cd.getCnpj() : "";
    String titulo = (cd != null) ? "Editar CD" : "Novo CD";
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title><%= titulo %></title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input[type="text"] { width: 300px; padding: 8px; }
        button { padding: 10px 15px; background: #28a745; color: white; border: none; cursor: pointer; }
        a { margin-left: 10px; color: #dc3545; text-decoration: none; }
    </style>
</head>
<body>

    <h2><%= titulo %></h2>

    <form action="cd" method="post">
        <!-- O parâmetro 'acao' diz ao Servlet o que fazer -->
        <input type="hidden" name="acao" value="salvar">

        <input type="hidden" name="id" value="<%= id %>">

        <div class="form-group">
            <label>Nome do CD:</label>
            <input type="text" name="nome" value="<%= nome %>" required>
        </div>

        <div class="form-group">
            <label>CNPJ (Apenas números, 14 dígitos):</label>
            <input type="text" name="cnpj" value="<%= cnpj %>" placeholder="Ex: 00.000.000/0000-00" required>
        </div>

        <button type="submit">Salvar CD</button>
        <a href="cd?acao=listar">Cancelar e Voltar</a>
    </form>

</body>
</html>