<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.skadi_beckend.model.Endereco" %>
<%@ page import="com.example.skadi_beckend.model.CD" %>
<%@ page import="java.util.List" %>
<%
    Endereco e = (Endereco) request.getAttribute("endereco");
    List<CD> listaCds = (List<CD>) request.getAttribute("listaCds");

    String id = (e != null) ? String.valueOf(e.getIdEndereco()) : "";
    int idCdSelecionado = (e != null) ? e.getIdCd() : -1;
    String rua = (e != null) ? e.getRua() : "";
    String numero = (e != null && e.getNumero() != null) ? String.valueOf(e.getNumero()) : "";
    String complemento = (e != null && e.getComplemento() != null) ? e.getComplemento() : "";
    String bairro = (e != null) ? e.getBairro() : "";
    String cidade = (e != null) ? e.getCidade() : "";
    String estado = (e != null) ? e.getEstado() : "";
    String cep = (e != null) ? e.getCep() : "";

    String titulo = (e != null) ? "Editar Endereço" : "Novo Endereço";
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

    <form action="endereco" method="post">
        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="id" value="<%= id %>">

        <div class="form-group">
            <label>Centro de Distribuição (CD):</label>
            <select name="idCd" required>
                <option value="">Selecione o CD...</option>
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
            <small style="color:red;">* Lembrete: Um CD só pode ter UM endereço ativo (Regra do Banco).</small>
        </div>

        <div class="form-group">
            <label>Rua/Avenida:</label>
            <input type="text" name="rua" value="<%= rua %>" required>
        </div>

        <div class="form-group">
            <label>Número (Opcional, deixe em branco se S/N):</label>
            <input type="text" name="numero" value="<%= numero %>" pattern="\d*" title="Apenas números">
        </div>

        <div class="form-group">
            <label>Complemento:</label>
            <input type="text" name="complemento" value="<%= complemento %>">
        </div>

        <div class="form-group">
            <label>Bairro:</label>
            <input type="text" name="bairro" value="<%= bairro %>" required>
        </div>

        <div class="form-group">
            <label>Cidade:</label>
            <input type="text" name="cidade" value="<%= cidade %>" required>
        </div>

        <div class="form-group">
            <label>Estado (Sigla de 2 letras - Ex: SP, RJ):</label>
            <input type="text" name="estado" value="<%= estado %>" maxlength="2" minlength="2" required style="text-transform: uppercase;">
        </div>

        <div class="form-group">
            <label>CEP (Apenas números ou com traço):</label>
            <input type="text" name="cep" value="<%= cep %>" placeholder="Ex: 01001-000" required>
        </div>

        <button type="submit">Salvar Endereço</button>
        <a href="endereco?acao=listar">Cancelar</a>
    </form>

</body>
</html>