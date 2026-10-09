// ============================
// MENU MOBILE
// ============================

const menuButton = document.querySelector('.menu-toggle');
const navigation = document.getElementById('navigation');

function closeMenu() {

    navigation.classList.remove('open');

    menuButton.setAttribute('aria-expanded', 'false');
    menuButton.setAttribute('aria-label', 'Abrir menu');

}

menuButton.addEventListener('click', () => {

    const open = navigation.classList.toggle('open');

    menuButton.setAttribute('aria-expanded', String(open));
    menuButton.setAttribute('aria-label', open ? 'Fechar menu' : 'Abrir menu');

});

navigation.querySelectorAll('a').forEach(link => {

    link.addEventListener('click', closeMenu);

});

// Fecha com a tecla Esc
document.addEventListener('keydown', event => {

    if (event.key === 'Escape') closeMenu();

});


// ============================
// CARROSSEL DA EQUIPE
// Rola com a rodinha do mouse (quando o cursor está em cima),
// arrastando com o mouse, com o dedo ou com as setas do teclado.
// ============================

const track = document.querySelector('.membros');

// Rodinha do mouse: vertical vira horizontal.
// Quando chega no começo ou no fim, a página volta a rolar normalmente.
track.addEventListener('wheel', event => {

    // Gesto já horizontal (touchpad): o navegador cuida sozinho
    if (Math.abs(event.deltaX) > Math.abs(event.deltaY)) return;

    const max = track.scrollWidth - track.clientWidth;
    const goingRight = event.deltaY > 0;

    const atStart = track.scrollLeft <= 0 && !goingRight;
    const atEnd = track.scrollLeft >= max - 1 && goingRight;

    if (atStart || atEnd) return;

    event.preventDefault();

    // deltaMode 1 = linhas (alguns mouses/Firefox)
    const factor = event.deltaMode === 1 ? 32 : 1;

    track.scrollLeft += event.deltaY * factor;

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

    if (!dragging) return;

    track.scrollLeft = startScroll - (event.clientX - startX);

});

window.addEventListener('pointerup', () => {

    dragging = false;
    track.classList.remove('arrastando');

});

// Setas do teclado quando a lista está em foco
track.addEventListener('keydown', event => {

    const step = track.querySelector('li').offsetWidth + 114;

    if (event.key === 'ArrowRight') track.scrollBy({ left: step, behavior: 'smooth' });
    if (event.key === 'ArrowLeft') track.scrollBy({ left: -step, behavior: 'smooth' });

});
