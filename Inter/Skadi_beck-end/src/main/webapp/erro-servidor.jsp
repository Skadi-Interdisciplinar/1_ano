<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Página de erro 500 (web.xml): aparece quando o servidor não consegue concluir uma ação,
  por exemplo quando o banco de dados falha. Usa o mesmo visual do acesso negado,
  no lugar da página padrão do Tomcat.
--%>
<% response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Erro no servidor | Skadi</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleMensagem.css">
</head>
<body>

<main class="cartao">

  <!-- LINHAS DECORATIVAS -->
  <svg class="decoracao" viewBox="0 0 502 733" preserveAspectRatio="none" aria-hidden="true">
    <path class="linha linha-topo" d="M199 -5 C183 70 161 132 119 178 M63 224 C45 244 26 259 8 265"/>
    <path class="linha linha-baixo" d="M510 512 C465 535 421 583 402 633 M389 684 C381 710 381 723 385 740"/>
  </svg>

  <div class="icone">
    <img src="${pageContext.request.contextPath}/Imagens/logo.svg" alt="Skadi">
  </div>

  <h1>Erro no<br>servidor</h1>

  <p class="subtitulo">Não foi possível concluir esta ação agora.<br><span class="frase-unica">Tente de novo em instantes.</span></p>

  <a href="${pageContext.request.contextPath}/cd?acao=listar" class="btn-voltar">Voltar para as unidades</a>

</main>

</body>
</html>
