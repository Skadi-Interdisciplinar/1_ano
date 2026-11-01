<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Contato</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleContato.css">
</head>
<body>
  <main class="tela">
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>
    <div class="blob blob-3"></div>

    <div class="card-cadastro">
      <header class="topo">
        <a href="${pageContext.request.contextPath}/landing.jsp" class="voltar" aria-label="Voltar">
            <img src="${pageContext.request.contextPath}/Imagens/seta_branca.svg" alt="">
        </a>
        <h1>Inicie sua jornada conosco!</h1>
      </header>

      <form class="formulario" id="formSignIn" action="${pageContext.request.contextPath}/contato" method="post" novalidate>
        <p class="mensagem mensagem--erro" id="erroContato" role="alert" hidden></p>
        <input type="text" name="nome" placeholder="Nome Completo" autocomplete="name" maxlength="150" required>
        <input type="email" id="email" name="email" placeholder="E-mail" autocomplete="email" maxlength="254" required>
        <input type="text" name="empresa" placeholder="Nome da empresa" autocomplete="organization" maxlength="150" required>
        <textarea name="mensagem" placeholder="Mensagem" minlength="10" maxlength="5000" required></textarea>

        <section class="btn-container">
          <button type="submit" class="btn-enviar">Enviar</button>
        </section>
      </form>

      <section class="login">
        <p>Já tem uma conta?</p>
        <a href="${pageContext.request.contextPath}/login.jsp" class="btn-entrar">Entrar</a>
      </section>
    </div>
  </main>

  <div class="modal-overlay" id="modalSucesso">
    <iframe src="${pageContext.request.contextPath}/msg-enviada.jsp" class="mensagem-iframe" title="Mensagem enviada com sucesso"></iframe>
  </div>

  <script>
    const formulario = document.getElementById('formSignIn');
    const modalSucesso = document.getElementById('modalSucesso');

    const erroContato = document.getElementById('erroContato');
    const botaoEnviar = formulario.querySelector('.btn-enviar');

    function mostrarErro(texto) {
      erroContato.textContent = texto;
      erroContato.hidden = !texto;
    }

    // Envia para o ContatoServlet (POST /contato), que responde em JSON e manda o e-mail para a equipe
    formulario.addEventListener('submit', async function(event) {
      event.preventDefault();
      mostrarErro('');
      if (!formulario.checkValidity()) {
        formulario.reportValidity();
        return;
      }

      const campos = formulario.elements;
      const dados = new URLSearchParams();
      dados.append('nomeCompleto', campos.nome.value.trim());
      dados.append('email', campos.email.value.trim());
      dados.append('nomeEmpresa', campos.empresa.value.trim());
      dados.append('mensagem', campos.mensagem.value.trim());

      botaoEnviar.disabled = true;
      try {
        const resposta = await fetch(formulario.action, {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
          body: dados
        });
        const retorno = await resposta.json().catch(() => ({}));
        if (resposta.ok && retorno.sucesso) {
          formulario.reset();
          modalSucesso.classList.add('ativo');
        } else {
          mostrarErro(retorno.mensagem || 'Não foi possível enviar sua mensagem agora.');
        }
      } catch (erro) {
        mostrarErro('Sem conexão com o servidor. Tente novamente.');
      } finally {
        botaoEnviar.disabled = false;
      }
    });
  </script>
</body>
</html>