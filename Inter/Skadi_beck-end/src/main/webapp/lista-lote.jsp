<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.skadi_beckend.model.Lote" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Lotes</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn { padding: 5px 10px; text-decoration: none; border-radius: 3px; background: #007bff; color: white; margin-right: 5px; }
        .btn-danger { background: #dc3545; }
        .status-ativo { color: green; font-weight: bold; }
        .status-quarentena { color: orange; font-weight: bold; }
        .status-vencido { color: red; font-weight: bold; }
    </style>
</head>
<body>

    <h2>Lista de Lotes</h2>

    <form action="lote" method="get" style="margin-bottom: 20px;">
        <input type="hidden" name="acao" value="buscarPorCategoria">
        <label>Buscar por ID da Categoria:</label>
        <input type="number" name="idCategoriaBusca" placeholder="ID Categoria...">
        <button type="submit">Pesquisar</button>
        <a href="lote?acao=listar" class="btn" style="background: #6c757d;">Limpar Busca</a>
    </form>

    <a href="lote?acao=novo" class="btn" style="background: #28a745;">+ Novo Lote</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Código do Lote</th>
                <th>ID Categoria</th>
                <th>Data Fabricação</th>
                <th>Data Validade</th>
                <th>Status</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Lote> lista = (List<Lote>) request.getAttribute("listaLotes");
                if (lista != null && !lista.isEmpty()) {
                    for (Lote l : lista) {
                        String statusClass = "status-" + l.getStatus().toLowerCase();
            %>
            <tr>
                <td><%= l.getIdLote() %></td>
                <td><strong><%= l.getCodigoLote() %></strong></td>
                <td><%= l.getIdCategoria() %></td>
                <td><%= l.getDataFabricacao() %></td>
                <td><%= l.getDataValidade() %></td>
                <td class="<%= statusClass %>"><%= l.getStatus().toUpperCase() %></td>
                <td>
                    <a href="lote?acao=editar&id=<%= l.getIdLote() %>" class="btn">Editar</a>
                    <a href="lote?acao=excluir&id=<%= l.getIdLote() %>" class="btn btn-danger" onclick="return confirm('Tem certeza que deseja excluir este lote?');">Excluir</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="7">Nenhum lote encontrado.</td>
            </tr>
            <% } %>
        </tbody>
    </table>

</body>
</html>