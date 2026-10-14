<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.skadi_beckend.model.Termometro" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Termômetros</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn { padding: 5px 10px; text-decoration: none; border-radius: 3px; background: #007bff; color: white; margin-right: 5px; }
        .btn-danger { background: #dc3545; }
        .status-ativo { color: green; font-weight: bold; }
        .status-inativo { color: red; font-weight: bold; }
        .status-manutencao { color: orange; font-weight: bold; }
    </style>
</head>
<body>

    <h2>Lista de Termômetros</h2>

    <form action="termometro" method="get" style="margin-bottom: 20px;">
        <input type="hidden" name="acao" value="buscarPorFrigorifico">
        <label>Buscar por ID do Frigorífico:</label>
        <input type="number" name="idFrigorificoBusca" placeholder="ID...">
        <button type="submit">Pesquisar</button>
        <a href="termometro?acao=listar" class="btn" style="background: #6c757d;">Limpar Busca</a>
    </form>

    <a href="termometro?acao=novo" class="btn" style="background: #28a745;">+ Novo Termômetro</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Modelo</th>
                <th>Status</th>
                <th>ID Frigorífico</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Termometro> lista = (List<Termometro>) request.getAttribute("listaTermometros");
                if (lista != null && !lista.isEmpty()) {
                    for (Termometro t : lista) {
                        String statusClass = "status-" + t.getStatus().toLowerCase();
            %>
            <tr>
                <td><%= t.getIdTermometro() %></td>
                <td><%= t.getModelo() %></td>
                <td class="<%= statusClass %>"><%= t.getStatus().toUpperCase() %></td>
                <td><%= t.getIdFrigorifico() %></td>
                <td>
                    <a href="termometro?acao=editar&id=<%= t.getIdTermometro() %>" class="btn">Editar</a>
                    <a href="termometro?acao=excluir&id=<%= t.getIdTermometro() %>" class="btn btn-danger" onclick="return confirm('Tem certeza que deseja excluir?');">Excluir</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="5">Nenhum termômetro encontrado.</td>
            </tr>
            <% } %>
        </tbody>
    </table>

</body>
</html>