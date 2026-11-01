// ============================
// MENU MOBILE
// ============================

const toggle = document.querySelector('.nav-toggle');
const menu = document.getElementById('menu');

function fecharMenu() {
  menu?.classList.remove('aberto');
  toggle?.setAttribute('aria-expanded', 'false');
}

toggle?.addEventListener('click', () => {
  const aberto = menu.classList.toggle('aberto');
  toggle.setAttribute('aria-expanded', aberto);
});

menu?.querySelectorAll('a').forEach(a => a.addEventListener('click', fecharMenu));

document.addEventListener('keydown', event => {
  if (event.key === 'Escape') fecharMenu();
});


// ============================
// CARROSSEL DA EQUIPE
// Rola com a rodinha do mouse, arrastando com o mouse,
// com o dedo ou com as setas do teclado.
// ============================

const track = document.querySelector('.membros');

if (track) {

  // Rodinha: vertical vira horizontal.
  // No começo ou no fim da lista, a página volta a rolar normalmente.
  track.addEventListener('wheel', event => {
    if (Math.abs(event.deltaX) > Math.abs(event.deltaY)) return;

    const max = track.scrollWidth - track.clientWidth;
    const goingRight = event.deltaY > 0;
    const atStart = track.scrollLeft <= 0 && !goingRight;
    const atEnd = track.scrollLeft >= max - 1 && goingRight;

    if (atStart || atEnd) return;

    event.preventDefault();
    track.scrollLeft += event.deltaY * (event.deltaMode === 1 ? 32 : 1);
  }, { passive: false });

  // Arrastar com o mouse
  let dragging = false;
  let startX = 0;
  let startScroll = 0;

  track.addEventListener('pointerdown', event => {
    if (event.pointerType !== 'mouse') return;
    dragging = true;
    startX = event.clientX;
    startScroll = track.scrollLeft;
    track.classList.add('arrastando');
  });

  window.addEventListener('pointermove', event => {
    if (dragging) track.scrollLeft = startScroll - (event.clientX - startX);
  });

  window.addEventListener('pointerup', () => {
    dragging = false;
    track.classList.remove('arrastando');
  });

  // Setas do teclado
  track.addEventListener('keydown', event => {
    const gap = parseFloat(getComputedStyle(track).columnGap) || 0;
    const step = track.querySelector('li').offsetWidth + gap;

    if (event.key === 'ArrowRight') track.scrollBy({ left: step, behavior: 'smooth' });
    if (event.key === 'ArrowLeft') track.scrollBy({ left: -step, behavior: 'smooth' });
  });
}