<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.skadi_beckend.model.Categoria" %>
<%
    Categoria c = (Categoria) request.getAttribute("categoria");

    String id = (c != null) ? String.valueOf(c.getIdCategoria()) : "";
    String nome = (c != null) ? c.getNome() : "";
    String vidaUtil = (c != null) ? c.getVidaUtilHoras() : "";
    String tempMax = (c != null) ? String.valueOf(c.getTemperaturaMax()) : "";
    String tempMin = (c != null) ? String.valueOf(c.getTemperaturaMin()) : "";

    String titulo = (c != null) ? "Editar Categoria" : "Nova Categoria";
    String erro = request.getParameter("erro");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title><%= titulo %></title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="number"] { width: 300px; padding: 8px; }
        button { padding: 10px 15px; background: #28a745; color: white; border: none; cursor: pointer; }
        a { margin-left: 10px; color: #dc3545; text-decoration: none; }
        .alerta { color: red; font-weight: bold; margin-bottom: 15px; }
    </style>
</head>
<body>

    <h2><%= titulo %></h2>

    <% if ("temperatura".equals(erro)) { %>
        <div class="alerta">Erro: A Temperatura Máxima DEVE ser maior que a Mínima!</div>
    <% } else if ("validacao".equals(erro)) { %>
        <div class="alerta">Erro: Preencha todos os campos corretamente!</div>
    <% } else if ("formato".equals(erro)) { %>
        <div class="alerta">Erro: Digite apenas números nos campos de temperatura (use ponto para decimais).</div>
    <% } %>

    <form action="categoria" method="post">
        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="id" value="<%= id %>">

        <div class="form-group">
            <label>Nome da Categoria:</label>
            <input type="text" name="nome" value="<%= nome %>" placeholder="Ex: Carne Bovina Moida" required>
        </div>

        <div class="form-group">
            <label>Temperatura Mínima (°C):</label>
            <input type="number" step="0.01" name="temperaturaMin" value="<%= tempMin %>" placeholder="Ex: -2.5" required>
        </div>

        <div class="form-group">
            <label>Temperatura Máxima (°C):</label>
            <input type="number" step="0.01" name="temperaturaMax" value="<%= tempMax %>" placeholder="Ex: 4.0" required>
        </div>

        <div class="form-group">
            <label>Tempo de Vida Útil:</label>
            <input type="text" name="vidaUtilHoras" value="<%= vidaUtil %>" placeholder="Ex: 48:00:00 ou 10 days" required>
            <small style="display:block; color:#666;">Formato aceito pelo banco: "HH:MM:SS" (ex: 24:00:00) ou texto (ex: "7 days").</small>
        </div>

        <button type="submit">Salvar Categoria</button>
        <a href="categoria?acao=listar">Cancelar</a>
    </form>

</body>
</html>