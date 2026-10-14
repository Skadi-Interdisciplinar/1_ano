<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.skadi_beckend.model.Lote" %>
<%@ page import="com.example.skadi_beckend.model.Categoria" %>
<%@ page import="java.util.List" %>
<%
    Lote l = (Lote) request.getAttribute("lote");
    List<Categoria> listaCategorias = (List<Categoria>) request.getAttribute("listaCategorias");

    String id = (l != null) ? String.valueOf(l.getIdLote()) : "";
    String codigoLote = (l != null) ? l.getCodigoLote() : "";
    int idCategoriaSelecionada = (l != null) ? l.getIdCategoria() : -1;
    String dataFab = (l != null && l.getDataFabricacao() != null) ? l.getDataFabricacao().toString() : "";
    String dataVal = (l != null && l.getDataValidade() != null) ? l.getDataValidade().toString() : "";
    String status = (l != null) ? l.getStatus() : "ativo";

    String titulo = (l != null) ? "Editar Lote" : "Novo Lote";
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
        input[type="text"], input[type="date"], select { width: 300px; padding: 8px; }
        button { padding: 10px 15px; background: #28a745; color: white; border: none; cursor: pointer; }
        a { margin-left: 10px; color: #dc3545; text-decoration: none; }
        .alerta { color: red; font-weight: bold; margin-bottom: 15px; }
    </style>
</head>
<body>

    <h2><%= titulo %></h2>

    <% if ("datas".equals(erro)) { %>
        <div class="alerta">Erro: A data de validade não pode ser anterior à data de fabricação!</div>
    <% } else if ("validacao".equals(erro)) { %>
        <div class="alerta">Erro: Preencha todos os campos obrigatórios!</div>
    <% } else if ("formato".equals(erro)) { %>
        <div class="alerta">Erro: Formato de data ou número inválido.</div>
    <% } %>

    <form action="lote" method="post">
        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="id" value="<%= id %>">

        <div class="form-group">
            <label>Código do Lote (LOTE-XXXX):</label>
            <input type="text" name="codigoLote" value="<%= codigoLote %>" placeholder="Ex: LOTE-2026-001" required>
        </div>

        <div class="form-group">
            <label>Categoria do Produto:</label>
            <select name="idCategoria" required>
                <option value="">Selecione a Categoria...</option>
                <%
                    if (listaCategorias != null) {
                        for (Categoria cat : listaCategorias) {
                            String selected = (cat.getIdCategoria() == idCategoriaSelecionada) ? "selected" : "";
                %>
                            <option value="<%= cat.getIdCategoria() %>" <%= selected %>>
                                <%= cat.getNome() %> (Vida útil: <%= cat.getVidaUtilHoras() %>)
                            </option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <div class="form-group">
            <label>Data de Fabricação:</label>
            <input type="date" name="dataFabricacao" value="<%= dataFab %>" required>
        </div>

        <div class="form-group">
            <label>Data de Validade:</label>
            <input type="date" name="dataValidade" value="<%= dataVal %>" required>
        </div>

        <div class="form-group">
            <label>Status do Lote:</label>
            <select name="status" required>
                <option value="ativo" <%= "ativo".equalsIgnoreCase(status) ? "selected" : "" %>>Ativo</option>
                <option value="quarentena" <%= "quarentena".equalsIgnoreCase(status) ? "selected" : "" %>>Em Quarentena</option>
                <option value="vencido" <%= "vencido".equalsIgnoreCase(status) ? "selected" : "" %>>Vencido / Descartado</option>
            </select>
        </div>

        <button type="submit">Salvar Lote</button>
        <a href="lote?acao=listar">Cancelar</a>
    </form>

</body>
</html>