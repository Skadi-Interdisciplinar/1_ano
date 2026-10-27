// Menu mobile
const toggle = document.querySelector('.nav-toggle');
const menu = document.getElementById('menu');
toggle?.addEventListener('click', () => {
  const aberto = menu.classList.toggle('aberto');
  toggle.setAttribute('aria-expanded', aberto);
});
menu?.querySelectorAll('a').forEach(a => a.addEventListener('click', () => {
  menu.classList.remove('aberto');
  toggle.setAttribute('aria-expanded', 'false');
}));

// Botão "Precisa de ajuda?" leva ao contato do rodapé
(function () {
  var botao = document.getElementById('agente');
  var janela = document.getElementById('ajuda-janela');
  var frame = document.getElementById('ajuda-frame');
  if (!botao || !janela || !frame) return;
 
  // Posiciona a janela direto pelo JS (fixa no canto inferior direito da tela),
  // sem depender do CSS, porque no HTML ela só tem id e o CSS usa a classe .ajuda-janela
  Object.assign(janela.style, {
    position: 'fixed', right: '2.3125rem', bottom: '1.5rem', top: 'auto', left: 'auto',
    width: '34.625rem', height: '35.75rem',
    maxWidth: 'calc(100vw - 2rem)', maxHeight: 'calc(100vh - 2rem)',
    zIndex: '9999'
  });
  Object.assign(frame.style, {
    width: '100%', height: '100%', border: '0',
    background: 'transparent', borderRadius: '1.875rem'
  });
 
  // Sem botão de fechar: se ele ainda estiver no HTML, remove
  document.getElementById('ajuda-fechar')?.remove();
 
  function abrir() {
    if (!frame.getAttribute('src')) frame.setAttribute('src', frame.getAttribute('data-src'));
    janela.hidden = false;
    botao.classList.add('oculto');
    botao.setAttribute('aria-expanded', 'true');
  }
  function fechar() {
    janela.hidden = true;
    botao.classList.remove('oculto');
    botao.setAttribute('aria-expanded', 'false');
  }
 
  // fase de captura: roda antes de qualquer outro clique
  document.addEventListener('click', function (e) {
    if (e.target.closest('#agente')) { e.preventDefault(); e.stopPropagation(); abrir(); return; }
    if (!janela.hidden && !janela.contains(e.target)) fechar();
  }, true);
 
  document.addEventListener('keydown', function (e) { if (e.key === 'Escape') fechar(); });
})();

// Entrada suave das seções
const observador = new IntersectionObserver(entradas => {
  entradas.forEach(e => {
    if (e.isIntersecting) {
      const el = e.target;
      el.style.opacity = 1;
      el.style.transform = 'none';
      setTimeout(() => el.removeAttribute('style'), 600);
      observador.unobserve(el);
    }
  });
}, { threshold: 0.15 });
document.querySelectorAll('.card-beneficio, .func, .plano').forEach(el => {
  el.style.opacity = 0;
  el.style.transform = 'translateY(1rem)';
  el.style.transition = 'opacity .5s ease, transform .5s ease';
  observador.observe(el);
});