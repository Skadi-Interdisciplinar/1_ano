<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.skadi_beckend.model.Frigorifico" %>
<!DOCTYPE html>
<html lang="pt-PT">
<head>
    <meta charset="UTF-8">
    <title>Gerir Frigoríficos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn { padding: 5px 10px; text-decoration: none; border-radius: 3px; background: #007bff; color: white; margin-right: 5px; }
        .btn-danger { background: #dc3545; }
        .btn-warning { background: #ffc107; color: black; }
    </style>
</head>
<body>

    <h2>Lista de Frigoríficos</h2>

    <form action="frigorifico" method="get" style="margin-bottom: 20px;">
        <input type="hidden" name="acao" value="buscarPorNome">
        <label>Pesquisar por Nome:</label>
        <input type="text" name="buscaNome" placeholder="Trecho do nome...">
        <button type="submit">Pesquisar</button>
        <a href="frigorifico?acao=listar" class="btn" style="background: #6c757d;">Limpar</a>
        <a href="frigorifico?acao=listarAtivos" class="btn btn-warning">Listar c/ Termómetros Ativos</a>
    </form>

    <a href="frigorifico?acao=novo" class="btn" style="background: #28a745;">+ Novo Frigorífico</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Localização</th>
                <th>ID do CD</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Frigorifico> lista = (List<Frigorifico>) request.getAttribute("listaFrigorificos");
                if (lista != null && !lista.isEmpty()) {
                    for (Frigorifico f : lista) {
            %>
            <tr>
                <td><%= f.getIdFrigorifico() %></td>
                <td><%= f.getNome() %></td>
                <td><%= f.getLocalizacao() %></td>
                <td><%= f.getIdCd() %></td>
                <td>
                    <a href="frigorifico?acao=editar&id=<%= f.getIdFrigorifico() %>" class="btn">Editar</a>
                    <a href="frigorifico?acao=excluir&id=<%= f.getIdFrigorifico() %>" class="btn btn-danger" onclick="return confirm('Excluir?');">Excluir</a>
                    <a href="frigorifico?acao=excluirPorLocalizacao&localizacao=<%= f.getLocalizacao() %>" class="btn btn-danger" style="background: #856404;" onclick="return confirm('Excluir pela Localização?');">Excluir (Localização)</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="5">Nenhum frigorífico encontrado.</td>
            </tr>
            <% } %>
        </tbody>
    </table>

</body>
</html>