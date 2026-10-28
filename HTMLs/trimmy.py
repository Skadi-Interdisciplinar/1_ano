"""
Gera assets/trimmy-animado.svg a partir das imagens em assets/trimmy/.

Uso (na raiz do projeto):
    python gerar_trimmy_animado.py

As imagens (png, jpg, webp ou svg) entram em ordem alfabética.
A troca de uma pose para outra é um fade cruzado suave, com a Trimmy
flutuando e "respirando" de leve o tempo todo.

Ajustes:
    PASSO -> segundos de cada pose (incluindo a transição)
    FADE  -> segundos da transição (quanto maior, mais suave)
"""
import base64
import mimetypes
from pathlib import Path

PASTA = Path("assets/trimmy_1")
SAIDA = Path("assets/trimmy_2.svg")
LARGURA, ALTURA = 300, 420
PASSO = 3.0
FADE = 1.2

EXTENSOES = {".png", ".jpg", ".jpeg", ".webp", ".svg"}
arquivos = sorted(p for p in PASTA.iterdir() if p.suffix.lower() in EXTENSOES)
if not arquivos:
    raise SystemExit(f"Nenhuma imagem encontrada em {PASTA}/")

n = len(arquivos)
total = n * PASSO
fatia = 100 / n
fade = FADE / total * 100  # duração do fade em % do ciclo

imagens = []
for i, arq in enumerate(arquivos):
    mime = mimetypes.guess_type(arq.name)[0] or "image/png"
    dados = base64.b64encode(arq.read_bytes()).decode()
    imagens.append(
        f'<image class="pose" style="animation-delay:{i * PASSO:.2f}s" '
        f'width="{LARGURA}" height="{ALTURA}" preserveAspectRatio="xMidYMid meet" '
        f'href="data:{mime};base64,{dados}"/>'
    )

# Cada pose entra em fade enquanto a anterior sai, então nunca fica "vazio".
svg = f"""<svg xmlns="http://www.w3.org/2000/svg" width="{LARGURA}" height="{ALTURA}" viewBox="0 0 {LARGURA} {ALTURA}">
  <style>
    .pose {{ opacity: 0; animation: troca {total:.2f}s ease-in-out infinite; }}
    .flutua {{
      transform-box: fill-box;
      transform-origin: 50% 100%;
      animation: flutua 4s ease-in-out infinite;
    }}
    @keyframes troca {{
      0%   {{ opacity: 0; }}
      {fade:.3f}% {{ opacity: 1; }}
      {fatia:.3f}% {{ opacity: 1; }}
      {fatia + fade:.3f}% {{ opacity: 0; }}
      100% {{ opacity: 0; }}
    }}
    @keyframes flutua {{
      0%, 100% {{ transform: translateY(0) scale(1); }}
      50%      {{ transform: translateY(-7px) scale(1.012); }}
    }}
  </style>
  <g class="flutua">
    {chr(10).join("    " + t for t in imagens).strip()}
  </g>
</svg>
"""

SAIDA.write_text(svg, encoding="utf-8")
print(f"Pronto: {SAIDA} ({n} poses, {SAIDA.stat().st_size / 1024:.0f} KB)")