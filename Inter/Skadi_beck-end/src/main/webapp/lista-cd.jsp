<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.skadi_beckend.model.CD" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Gerenciar CDs</title>
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

    <h2>Lista de Centros de Distribuição (CD)</h2>

    <form action="cd" method="get" style="margin-bottom: 20px;">
        <input type="hidden" name="acao" value="buscarPorNome">
        <label>Buscar por Nome:</label>
        <input type="text" name="buscaNome" placeholder="Digite um trecho do nome...">
        <button type="submit">Pesquisar</button>
        <a href="cd?acao=listar" class="btn" style="background: #6c757d;">Limpar Busca</a>
    </form>

    <a href="cd?acao=novo" class="btn" style="background: #28a745;">+ Novo CD</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>CNPJ</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<CD> lista = (List<CD>) request.getAttribute("listaCds");
                if (lista != null && !lista.isEmpty()) {
                    for (CD cd : lista) {
            %>
            <tr>
                <td><%= cd.getIdCd() %></td>
                <td><%= cd.getNome() %></td>
                <td><%= cd.getCnpj() %></td>
                <td>
                    <a href="cd?acao=editar&id=<%= cd.getIdCd() %>" class="btn">Editar</a>

                    <!-- Excluir por ID padrão -->
                    <a href="cd?acao=excluir&id=<%= cd.getIdCd() %>" class="btn btn-danger" onclick="return confirm('Excluir por ID?');">Excluir</a>

                    <!-- Excluir diversificado por CNPJ  -->
                    <a href="cd?acao=excluirPorCnpj&cnpj=<%= cd.getCnpj() %>" class="btn btn-danger" style="background: #856404;" onclick="return confirm('Excluir por CNPJ?');">Excluir (CNPJ)</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="4">Nenhum CD cadastrado ou encontrado na busca.</td>
            </tr>
            <% } %>
        </tbody>
    </table>

</body>
</html>