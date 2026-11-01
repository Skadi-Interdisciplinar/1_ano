<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<%-- Passo 3 da recuperação de senha: nova senha.
     Sem e-mail volta ao início; sem código (e sem erro de senha) mostra a tela do código. --%>
<c:if test="${empty param.email}">
  <c:redirect url="/esqueci-senha.jsp"/>
</c:if>
<c:if test="${empty param.codigo and param.erro != 'senhaInvalida' and param.erro != 'banco'}">
  <jsp:forward page="/esqueci-codigo.jsp"/>
</c:if>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Redefina a senha</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styleEsqueci.css">
</head>
<body>

  <!-- Botão de voltar (seta azul) -->
    <a href="${pageContext.request.contextPath}/redefinir-senha.jsp?email=<c:out value='${param.email}'/>" class="back-btn" aria-label="Voltar">
      <img src="${pageContext.request.contextPath}/Imagens/seta_azul.svg" alt="">
    </a>

  <div class="login-container">

    <!-- Bolas suaves de fundo -->
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>
    <div class="blob blob-3"></div>

    <!-- LADO ESQUERDO: lobo -->
    <div class="left-side">
      <img class="illustration" src="${pageContext.request.contextPath}/Imagens/lobao.svg" alt="Lobo Skadi">
    </div>

    <!-- LADO DIREITO: conteúdo -->
    <div class="right-side">
      <a href="${pageContext.request.contextPath}/area-restrita" class="logo"><img src="${pageContext.request.contextPath}/Imagens/logo.svg" alt="Skadi"></a>

      <h2 class="titulo">Redefina a senha</h2>
      <p class="subtitulo">Digite a sua nova senha para voltar a ter acesso a sua conta</p>
      
      
      <form class="form" id="form-senha" action="${pageContext.request.contextPath}/recuperar-senha" method="post">
        <input type="hidden" name="acao" value="redefinir">
        <input type="hidden" name="email" value="<c:out value='${param.email}'/>">
        <input type="hidden" name="codigo" id="codigo" value="<c:out value='${param.codigo}'/>">

        <c:choose>
          <c:when test="${param.erro == 'senhaInvalida'}">
            <p class="mensagem mensagem--erro" role="alert">A senha precisa ter 8 caracteres ou mais, com pelo menos uma letra maiúscula e um número.</p>
          </c:when>
          <c:when test="${param.erro == 'banco'}">
            <p class="mensagem mensagem--erro" role="alert">Não foi possível salvar a nova senha. Tente novamente.</p>
          </c:when>
        </c:choose>
        
        <!-- Campo: Nova senha -->
         <div class="input-group">
          <input type="password" id="nova-senha" name="novaSenha" placeholder="Nova senha" autocomplete="new-password" required>

          <button type="button" class="olho" aria-label="Mostrar nova senha" aria-pressed="false" data-target="nova-senha">
            <img class="olho-aberto" src="${pageContext.request.contextPath}/Imagens/olho_ac.svg" alt="">
            <img class="olho-fechado" src="${pageContext.request.contextPath}/Imagens/olho_fc.svg" alt="">
          </button>
        </div>

        <!-- Campo: Confirmar senha -->
          <div class="input-group">
            <input type="password" id="confirma-senha" placeholder="Confirme a senha" autocomplete="new-password" required>
            <button type="button" class="olho" aria-label="Mostrar confirmação da senha" aria-pressed="false" data-target="confirma-senha">
              <img class="olho-aberto" src="${pageContext.request.contextPath}/Imagens/olho_ac.svg" alt="">
              <img class="olho-fechado" src="${pageContext.request.contextPath}/Imagens/olho_fc.svg" alt="">
            </button>
          </div>
          
          <button type="submit" class="btn btn-primary">Voltar para login</button>
        </form>
      </div>
    </div>


<script>
const novaSenha = document.getElementById('nova-senha');
const confirmaSenha = document.getElementById('confirma-senha');

// Mesma regra do Validator do back-end: 8+ caracteres, uma maiúscula e um número
const REGRA_SENHA = /^(?=.*[0-9])(?=.*[A-Z]).{8,}$/;

// Validar a regra da senha e se as duas senhas são iguais
function validarSenhas() {
  novaSenha.setCustomValidity(
    REGRA_SENHA.test(novaSenha.value)
      ? '' : 'Use 8 caracteres ou mais, com uma letra maiúscula e um número.');
  confirmaSenha.setCustomValidity(
    confirmaSenha.value !== novaSenha.value
      ? 'As senhas não coincidem.': '');}

// Se a senha foi recusada, o código digitado no passo anterior continua guardado nesta aba
const campoCodigo = document.getElementById('codigo');
if (!campoCodigo.value) {
  campoCodigo.value = sessionStorage.getItem('codigoRecuperacao') || '';
  if (!campoCodigo.value) {
    window.location.replace('${pageContext.request.contextPath}/redefinir-senha.jsp?email=' + encodeURIComponent('<c:out value="${param.email}"/>'));
  }
}

novaSenha.addEventListener('input', validarSenhas);
confirmaSenha.addEventListener('input', validarSenhas);

// Mostrar ou ocultar a senha de cada campo
document.querySelectorAll('.olho').forEach((botaoOlho) => {
  botaoOlho.addEventListener('click', () => {
    const campo = document.getElementById(
      botaoOlho.dataset.target);

    const mostrando = campo.type === 'text';
    campo.type = mostrando ? 'password' : 'text';
    botaoOlho.classList.toggle('ativo', !mostrando);
    botaoOlho.setAttribute('aria-pressed', String(!mostrando));
    botaoOlho.setAttribute('aria-label',mostrando ? 'Mostrar senha' : 'Ocultar senha');
  })
});

// Envio do formulário
document.getElementById('form-senha').addEventListener('submit', (e) => {
  e.preventDefault();
  validarSenhas();
  if (!novaSenha.checkValidity()) {
    novaSenha.reportValidity();
    return;
  }
  if (!confirmaSenha.checkValidity()) {
    confirmaSenha.reportValidity();
    return;
  }

  sessionStorage.removeItem('codigoRecuperacao');
  e.target.submit();
});
</script>

</body>
</html>