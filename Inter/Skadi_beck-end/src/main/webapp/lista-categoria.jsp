<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.skadi_beckend.model.Categoria" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Categorias</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn { padding: 5px 10px; text-decoration: none; border-radius: 3px; background: #007bff; color: white; margin-right: 5px; }
        .btn-danger { background: #dc3545; }
    </style>
</head>
<body>

    <h2>Lista de Categorias</h2>

    <form action="categoria" method="get" style="margin-bottom: 20px;">
        <input type="hidden" name="acao" value="buscarPorNome">
        <label>Pesquisar por Nome:</label>
        <input type="text" name="buscaNome" placeholder="Digite o nome da categoria...">
        <button type="submit">Pesquisar</button>
        <a href="categoria?acao=listar" class="btn" style="background: #6c757d;">Limpar Busca</a>
    </form>

    <a href="categoria?acao=novo" class="btn" style="background: #28a745;">+ Nova Categoria</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Temp. Mínima (°C)</th>
                <th>Temp. Máxima (°C)</th>
                <th>Tempo de Vida Útil</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Categoria> lista = (List<Categoria>) request.getAttribute("listaCategorias");
                if (lista != null && !lista.isEmpty()) {
                    for (Categoria c : lista) {
            %>
            <tr>
                <td><%= c.getIdCategoria() %></td>
                <td><%= c.getNome() %></td>
                <td><%= c.getTemperaturaMin() %></td>
                <td><%= c.getTemperaturaMax() %></td>
                <td><%= c.getVidaUtilHoras() %></td>
                <td>
                    <a href="categoria?acao=editar&id=<%= c.getIdCategoria() %>" class="btn">Editar</a>
                    <a href="categoria?acao=excluir&id=<%= c.getIdCategoria() %>" class="btn btn-danger" onclick="return confirm('Tem certeza que deseja excluir?');">Excluir</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="6">Nenhuma categoria encontrada.</td>
            </tr>
            <% } %>
        </tbody>
    </table>

</body>
</html>