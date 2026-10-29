// Tela "Redefina a senha": confere se as duas senhas são iguais e mostra/oculta cada campo
const novaSenha = document.getElementById('nova-senha');
const confirmaSenha = document.getElementById('confirma-senha');

function validarSenhas() {
  confirmaSenha.setCustomValidity(
    confirmaSenha.value !== novaSenha.value ? 'As senhas não coincidem.' : ''
  );
}

novaSenha.addEventListener('input', validarSenhas);
confirmaSenha.addEventListener('input', validarSenhas);

document.querySelectorAll('.olho').forEach((botaoOlho) => {
  botaoOlho.addEventListener('click', () => {
    const campo = document.getElementById(botaoOlho.dataset.target);
    const mostrando = campo.type === 'text';

    campo.type = mostrando ? 'password' : 'text';
    botaoOlho.classList.toggle('ativo', !mostrando);
    botaoOlho.setAttribute('aria-pressed', String(!mostrando));
    botaoOlho.setAttribute('aria-label', mostrando ? 'Mostrar senha' : 'Ocultar senha');
  });
});
