<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.skadi_beckend.model.Usuario" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Usuários</title>
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

    <h2>Lista de Usuários</h2>

    <form action="usuario" method="get" style="margin-bottom: 20px;">
        <input type="hidden" name="acao" value="buscarPorNome">
        <label>Pesquisar por Nome:</label>
        <input type="text" name="buscaNome" placeholder="Trecho do nome...">
        <button type="submit">Pesquisar</button>
        <a href="usuario?acao=listar" class="btn" style="background: #6c757d;">Limpar Busca</a>
    </form>

    <a href="usuario?acao=novo" class="btn" style="background: #28a745;">+ Novo Usuário</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Username</th>
                <th>CPF</th>
                <th>Cargo / Nível</th>
                <th>Centro de Distribuição</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Usuario> lista = (List<Usuario>) request.getAttribute("listaUsuarios");
                if (lista != null && !lista.isEmpty()) {
                    for (Usuario u : lista) {
            %>
            <tr>
                <td><%= u.getIdUsuario() %></td>
                <td><%= u.getNome() %></td>
                <td><%= u.getUsername() %></td>
                <td><%= u.getCpf() %></td>
                <td><%= u.getCargo() %> (<%= u.getNivelAcesso() %>)</td>
                <td><%= (u.getNomeCd() != null) ? u.getNomeCd() : "CD " + u.getIdCd() %></td>
                <td>
                    <a href="usuario?acao=editar&id=<%= u.getIdUsuario() %>" class="btn">Editar</a>
                    <a href="usuario?acao=excluir&id=<%= u.getIdUsuario() %>" class="btn btn-danger" onclick="return confirm('Excluir por ID?');">Excluir</a>
                    <a href="usuario?acao=excluirPorCpf&cpf=<%= u.getCpf() %>" class="btn btn-danger" style="background: #856404;" onclick="return confirm('Excluir por CPF?');">Excluir (CPF)</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="7">Nenhum usuário encontrado.</td>
            </tr>
            <% } %>
        </tbody>
    </table>

</body>
</html>