<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Aberta pelo AreaRestritaServlet quando AWS_URL_AREA_RESTRITA ainda está vazia.
  O CRUD do super_admin fica na AWS; sem o link, ninguém é levado ao CRUD dos clientes.
--%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Área restrita | Skadi</title>

  <!-- Fonte Manrope (o 500 é o peso do texto do aviso) -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleLoginSuperAdmin.css">
</head>
<body>

  <!-- Botão de voltar -->
  <a href="${pageContext.request.contextPath}/landing.jsp" class="back-btn" aria-label="Voltar">
    <img src="${pageContext.request.contextPath}/Imagens/seta_branca.svg" alt="">
  </a>

  <!-- LADO ESQUERDO: Lobo -->
  <div class="left-side">
    <img class="illustration" src="${pageContext.request.contextPath}/Imagens/lobo_branca.svg" alt="Lobo Skadi">
  </div>

  <!-- LADO DIREITO: Cartão com o aviso -->
  <section class="card card--aviso">
    <img class="logo" src="${pageContext.request.contextPath}/Imagens/logo.svg" alt="Skadi">
    <h1 class="subtitle">Área restrita</h1>

    <p class="aviso" role="status">O link da Área restrita está indisponível no momento. Tente novamente mais tarde.</p>

    <!-- Botão: Voltar ao início -->
    <a href="${pageContext.request.contextPath}/landing.jsp" class="btn btn-primary">Voltar ao início</a>
  </section>

</body>
</html>
