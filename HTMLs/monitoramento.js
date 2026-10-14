// Cards de monitoramento: ciclo de ~5,4 s (linha cresce, temperatura troca com fade, reinicia)
(function () {
  const CICLO = 5400;
  const reduz = matchMedia('(prefers-reduced-motion: reduce)').matches;

  document.querySelectorAll('.mon-card').forEach((card, i) => {
    const temps = card.dataset.temps.split('|');
    const linha = card.querySelector('.mon-linha');
    const camadas = card.querySelectorAll('.mon-temp');
    if (!linha || camadas.length < 2) return;

    const total = linha.getTotalLength();
    linha.style.strokeDasharray = total;
    let ativa = 0, atual = 0;

    const mostrar = n => {
      if (n === atual) return;
      atual = n;
      const nova = camadas[ativa ^ 1];
      nova.textContent = temps[n] + ' °C';
      nova.classList.add('on');
      camadas[ativa].classList.remove('on');
      ativa ^= 1;
    };

    if (reduz) { linha.style.strokeDashoffset = 0; return; }

    const fase = i * 180; // pequeno desencontro entre os cards
    const quadro = agora => {
      const t = ((agora + fase) % CICLO) / 1000;
      mostrar(t < 0.55 ? 0 : t < 1.9 ? 1 : t < 3.8 ? 2 : t < 5.0 ? 3 : 0);
      const p = t < 3.9 ? 0.24 + 0.76 * (t / 3.9) : 1;
      const op = t < 0.15 ? t / 0.15 : t < 4.7 ? 1 : t < 5.0 ? 1 - (t - 4.7) / 0.3 : 0;
      linha.style.strokeDashoffset = total * (1 - p);
      linha.style.opacity = op;
      requestAnimationFrame(quadro);
    };
    requestAnimationFrame(quadro);
  });
})();