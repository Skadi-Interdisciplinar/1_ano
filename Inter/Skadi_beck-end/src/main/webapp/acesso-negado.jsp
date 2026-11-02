<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
  Para onde a classe Acesso manda quem está logado mas não tem permissão
  (ex.: admin tentando uma ação só do super_admin). Usa o mesmo visual da mensagem de sucesso.
  O body leva a conta que está logada agora: o crudServidor.js compara com a conta da tela
  para avisar quando outra conta entrou neste navegador.
--%>
<% response.setStatus(HttpServletResponse.SC_FORBIDDEN); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Acesso negado | Skadi</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleMensagem.css">
</head>
<body data-nivel="<c:out value='${sessionScope.usuarioLogado.nivelAcesso}'/>" data-usuario="<c:out value='${sessionScope.usuarioLogado.nome}'/>">

<main class="cartao">

  <!-- LINHAS DECORATIVAS -->
  <svg class="decoracao" viewBox="0 0 502 733" preserveAspectRatio="none" aria-hidden="true">
    <path class="linha linha-topo" d="M199 -5 C183 70 161 132 119 178 M63 224 C45 244 26 259 8 265"/>
    <path class="linha linha-baixo" d="M510 512 C465 535 421 583 402 633 M389 684 C381 710 381 723 385 740"/>
  </svg>

  <div class="icone">
    <img src="${pageContext.request.contextPath}/Imagens/logo.svg" alt="Skadi">
  </div>

  <h1>Acesso<br>negado</h1>

  <p class="subtitulo">Você não tem permissão para abrir esta página.<br><span class="frase-unica">Fale com o administrador da sua unidade.</span></p>

  <a href="${pageContext.request.contextPath}/cd?acao=listar" class="btn-voltar">Voltar para as unidades</a>

</main>

</body>
</html>
