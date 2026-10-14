<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.skadi_beckend.model.Endereco" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Endereços</title>
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

    <h2>Lista de Endereços</h2>

    <form action="endereco" method="get" style="margin-bottom: 20px;">
        <input type="hidden" name="acao" value="buscarPorCidade">
        <label>Filtrar por Cidade:</label>
        <input type="text" name="buscaCidade" placeholder="Digite a cidade...">
        <button type="submit">Pesquisar</button>
        <a href="endereco?acao=listar" class="btn" style="background: #6c757d;">Limpar Busca</a>
    </form>

    <a href="endereco?acao=novo" class="btn" style="background: #28a745;">+ Novo Endereço</a>

    <table>
        <thead>
            <tr>
                <th>CD Associado</th>
                <th>Rua / Número</th>
                <th>Bairro</th>
                <th>Cidade / UF</th>
                <th>CEP</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Endereco> lista = (List<Endereco>) request.getAttribute("listaEnderecos");
                if (lista != null && !lista.isEmpty()) {
                    for (Endereco e : lista) {
                        String numStr = (e.getNumero() != null) ? String.valueOf(e.getNumero()) : "S/N";
            %>
            <tr>
                <td><%= (e.getNomeCd() != null) ? e.getNomeCd() : "CD ID: " + e.getIdCd() %></td>
                <td><%= e.getRua() %>, <%= numStr %> <%= (e.getComplemento() != null) ? " - " + e.getComplemento() : "" %></td>
                <td><%= e.getBairro() %></td>
                <td><%= e.getCidade() %> / <%= e.getEstado() %></td>
                <td><%= e.getCep() %></td>
                <td>
                    <a href="endereco?acao=editar&id=<%= e.getIdEndereco() %>" class="btn">Editar</a>
                    <a href="endereco?acao=excluir&id=<%= e.getIdEndereco() %>" class="btn btn-danger" onclick="return confirm('Tem certeza que deseja excluir?');">Excluir</a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="6">Nenhum endereço encontrado.</td>
            </tr>
            <% } %>
        </tbody>
    </table>

</body>
</html>