// Cadastro: ao enviar, mostra a mensagem de sucesso (msg-enviada.jsp) em um modal
const formulario = document.getElementById('formCadastro');
const modalSucesso = document.getElementById('modalSucesso');

formulario?.addEventListener('submit', function (event) {
  event.preventDefault();
  modalSucesso.classList.add('ativo');
});
