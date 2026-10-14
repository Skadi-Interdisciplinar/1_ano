<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.skadi_beckend.model.Usuario" %>
<%@ page import="com.example.skadi_beckend.model.CD" %>
<%@ page import="java.util.List" %>
<%
    Usuario u = (Usuario) request.getAttribute("usuario");
    List<CD> listaCds = (List<CD>) request.getAttribute("listaCds");
    List<Usuario> listaGestores = (List<Usuario>) request.getAttribute("listaGestores");

    String id = (u != null) ? String.valueOf(u.getIdUsuario()) : "";
    String nome = (u != null) ? u.getNome() : "";
    String username = (u != null) ? u.getUsername() : "";
    String cpf = (u != null) ? u.getCpf() : "";
    String email = (u != null) ? u.getEmail() : "";
    String cargo = (u != null) ? u.getCargo() : "";
    String nivelAcesso = (u != null) ? u.getNivelAcesso() : "operador";
    int idCdSelecionado = (u != null) ? u.getIdCd() : -1;
    Integer idGestorSelecionado = (u != null) ? u.getCodGestor() : null;

    String titulo = (u != null) ? "Editar Usuário" : "Novo Usuário";
    boolean isEdicao = (u != null);
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
        input[type="text"], input[type="email"], input[type="password"], select { width: 300px; padding: 8px; }
        button { padding: 10px 15px; background: #28a745; color: white; border: none; cursor: pointer; }
        a { margin-left: 10px; color: #dc3545; text-decoration: none; }
    </style>
</head>
<body>

    <h2><%= titulo %></h2>

    <form action="usuario" method="post">
        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="id" value="<%= id %>">

        <div class="form-group">
            <label>Nome Completo:</label>
            <input type="text" name="nome" value="<%= nome %>" required>
        </div>

        <div class="form-group">
            <label>Username (Login):</label>
            <input type="text" name="username" value="<%= username %>" required>
        </div>

        <div class="form-group">
            <label>CPF (Apenas 11 números):</label>
            <input type="text" name="cpf" value="<%= cpf %>" placeholder="Ex: 000.000.000-00" required>
        </div>

        <div class="form-group">
            <label>E-mail:</label>
            <input type="email" name="email" value="<%= email %>" required>
        </div>

        <% if (!isEdicao) { %>
        <div class="form-group">
            <label>Senha:</label>
            <input type="password" name="senha" required>
        </div>
        <% } else { %>
            <p><small><em>A senha não pode ser alterada por esta tela por motivos de segurança.</em></small></p>
        <% } %>

        <div class="form-group">
            <label>Cargo:</label>
            <input type="text" name="cargo" value="<%= cargo %>">
        </div>

        <div class="form-group">
            <label>Nível de Acesso:</label>
            <select name="nivelAcesso" required>
                <option value="operador" <%= "operador".equals(nivelAcesso) ? "selected" : "" %>>Operador</option>
                <option value="gestor" <%= "gestor".equals(nivelAcesso) ? "selected" : "" %>>Gestor</option>
                <option value="admin" <%= "admin".equals(nivelAcesso) ? "selected" : "" %>>Admin</option>
                <option value="super_admin" <%= "super_admin".equals(nivelAcesso) ? "selected" : "" %>>Super Admin</option>
            </select>
        </div>

        <div class="form-group">
            <label>Centro de Distribuição (CD):</label>
            <select name="idCd" required>
                <option value="">Selecione um CD...</option>
                <%
                    if (listaCds != null) {
                        for (CD cd : listaCds) {
                            String selected = (cd.getIdCd() == idCdSelecionado) ? "selected" : "";
                %>
                            <option value="<%= cd.getIdCd() %>" <%= selected %>><%= cd.getNome() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <div class="form-group">
            <label>Gestor (Opcional):</label>
            <select name="codGestor">
                <option value="">Nenhum (Livre)</option>
                <%
                    if (listaGestores != null) {
                        for (Usuario gestor : listaGestores) {
                            if (isEdicao && gestor.getIdUsuario() == u.getIdUsuario()) continue;

                            String selected = (idGestorSelecionado != null && gestor.getIdUsuario() == idGestorSelecionado) ? "selected" : "";
                %>
                            <option value="<%= gestor.getIdUsuario() %>" <%= selected %>><%= gestor.getNome() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <button type="submit">Salvar Usuário</button>
        <a href="usuario?acao=listar">Cancelar</a>
    </form>

</body>
</html>