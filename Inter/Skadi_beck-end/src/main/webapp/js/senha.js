// Mostrar e ocultar a senha (login e cadastro)
const campoSenha = document.getElementById('senha');
const botaoOlho = document.getElementById('toggle-senha');

botaoOlho?.addEventListener('click', () => {
  const mostrando = campoSenha.type === 'text';
  campoSenha.type = mostrando ? 'password' : 'text';
  botaoOlho.classList.toggle('ativo', !mostrando);
  botaoOlho.setAttribute('aria-pressed', String(!mostrando));
  botaoOlho.setAttribute('aria-label', mostrando ? 'Mostrar senha' : 'Ocultar senha');
});
