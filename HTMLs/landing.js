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
document.getElementById('agente')?.addEventListener('click', () => {
  document.querySelector('.rodape').scrollIntoView({ behavior: 'smooth' });
});

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
  el.style.transform = 'translateY(16px)';
  el.style.transition = 'opacity .5s ease, transform .5s ease';
  observador.observe(el);
});