<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.skadi_beckend.model.Frigorifico" %>
<%@ page import="com.example.skadi_beckend.model.CD" %>
<%@ page import="java.util.List" %>
<%
    Frigorifico f = (Frigorifico) request.getAttribute("frigorifico");
    List<CD> listaCds = (List<CD>) request.getAttribute("listaCds");

    String id = (f != null) ? String.valueOf(f.getIdFrigorifico()) : "";
    String nome = (f != null) ? f.getNome() : "";
    String localizacao = (f != null) ? f.getLocalizacao() : "";
    int idCdSelecionado = (f != null) ? f.getIdCd() : -1;
    String titulo = (f != null) ? "Editar Frigorífico" : "Novo Frigorífico";
%>
<!DOCTYPE html>
<html lang="pt-PT">
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

    <form action="frigorifico" method="post">
        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="id" value="<%= id %>">

        <div class="form-group">
            <label>Nome do Frigorífico:</label>
            <input type="text" name="nome" value="<%= nome %>" required>
        </div>

        <div class="form-group">
            <label>Localização (Corredor/Setor):</label>
            <input type="text" name="localizacao" value="<%= localizacao %>" required>
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
                            <option value="<%= cd.getIdCd() %>" <%= selected %>>
                                <%= cd.getNome() %> (CNPJ: <%= cd.getCnpj() %>)
                            </option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <button type="submit">Salvar Frigorífico</button>
        <a href="frigorifico?acao=listar">Cancelar e Voltar</a>
    </form>

</body>
</html>