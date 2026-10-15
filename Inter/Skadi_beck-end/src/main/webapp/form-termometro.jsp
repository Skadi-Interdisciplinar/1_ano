<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.skadi_beckend.model.Termometro" %>
<%@ page import="com.example.skadi_beckend.model.Frigorifico" %>
<%@ page import="java.util.List" %>
<%
    Termometro t = (Termometro) request.getAttribute("termometro");
    List<Frigorifico> listaFrigorificos = (List<Frigorifico>) request.getAttribute("listaFrigorificos");

    String id = (t != null) ? String.valueOf(t.getIdTermometro()) : "";
    String modelo = (t != null) ? t.getModelo() : "";
    String status = (t != null) ? t.getStatus() : "ativo";
    int idFrigorificoSelecionado = (t != null) ? t.getIdFrigorifico() : -1;

    String titulo = (t != null) ? "Editar Termômetro" : "Novo Termômetro";
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
        input[type="text"], select { width: 300px; padding: 8px; }
        button { padding: 10px 15px; background: #28a745; color: white; border: none; cursor: pointer; }
        a { margin-left: 10px; color: #dc3545; text-decoration: none; }
    </style>
</head>
<body>

    <h2><%= titulo %></h2>

    <form action="termometro" method="post">
        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="id" value="<%= id %>">

        <div class="form-group">
            <label>Modelo do Aparelho (Marca/Versão):</label>
            <input type="text" name="modelo" value="<%= modelo %>" required>
        </div>

        <div class="form-group">
            <label>Status do Termômetro:</label>
            <select name="status" required>
                <option value="ativo" <%= "ativo".equalsIgnoreCase(status) ? "selected" : "" %>>Ativo</option>
                <option value="inativo" <%= "inativo".equalsIgnoreCase(status) ? "selected" : "" %>>Inativo</option>
                <option value="manutencao" <%= "manutencao".equalsIgnoreCase(status) ? "selected" : "" %>>Em Manutenção</option>
            </select>
        </div>

        <div class="form-group">
            <label>Frigorífico Instalado:</label>
            <select name="idFrigorifico" required>
                <option value="">Selecione o Frigorífico...</option>
                <%
                    if (listaFrigorificos != null) {
                        for (Frigorifico f : listaFrigorificos) {
                            String selected = (f.getIdFrigorifico() == idFrigorificoSelecionado) ? "selected" : "";
                %>
                            <option value="<%= f.getIdFrigorifico() %>" <%= selected %>>
                                <%= f.getNome() %> (Local: <%= f.getLocalizacao() %>)
                            </option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <button type="submit">Salvar Termômetro</button>
        <a href="termometro?acao=listar">Cancelar</a>
    </form>

</body>
</html>