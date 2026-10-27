/* ============================================================
   SKADI · DADOS DE EXEMPLO DOS CRUDs
   Usado enquanto o back-end não responde em JSON.
   Cada cadastro guarda seus dados no localStorage do navegador e
   as telas se relacionam por id, como no banco:

       Frigorifico  <-  Termometro        (termometro.frigorificoId)
       Categoria    <-  Lote              (lote.categoriaId)

   Com o back-end pronto, troque DadosExemplo.repositorio(...) por
   Crud.repositorioApi(...) em cada tela e este arquivo deixa de ser usado.
   ============================================================ */

const DadosExemplo = (() => {

    const dia = 24 * 60 * 60 * 1000;
    const hoje = new Date();
    const iso = data => data.toISOString().slice(0, 10);
    const diasAntes = n => iso(new Date(hoje.getTime() - n * dia));
    const diasDepois = (dataIso, n) => iso(new Date(new Date(dataIso + 'T12:00:00Z').getTime() + n * dia));

    /* ---------- frigoríficos (55) ---------- */

function semearFrigorificos() {
    const tipos = [
        { nome: 'Câmara Fria - Carnes',        minima: -20, maxima: -18 },
        { nome: 'Freezer - Congelados',        minima: -22, maxima: -18 },
        { nome: 'Câmara Fria - Laticínios',    minima: 2,   maxima: 6 },
        { nome: 'Câmara Resfriada - Hortifruti', minima: 4, maxima: 8 },
        { nome: 'Câmara Fria - Pescados',      minima: -2,  maxima: 2 },
        { nome: 'Câmara Resfriada - Frios',    minima: 0,   maxima: 4 }
    ];
    const setores = ['Setor A', 'Setor B', 'Setor C', 'Setor D', 'Setor E'];

    const lista = [];
    for (let i = 0; i < 55; i++) {
        const tipo = tipos[i % tipos.length];
        const faixa = tipo.maxima - tipo.minima;

        // quase todos dentro da faixa; alguns levemente fora, para a lista parecer real
        const passo = ((i * 37) % 11) / 10;              // 0,0 a 1,0
        const desvio = i % 13 === 0 ? 1.2 : 0;
        const atual = Math.round((tipo.minima + faixa * (0.15 + passo * 0.7) - desvio) * 10) / 10;

        lista.push({
            nome: tipo.nome + ' ' + (i + 1),
            localizacao: setores[(i * 3) % setores.length] + ' - Corredor ' + (1 + (i * 5) % 8),
            temperaturaAtual: atual,
            temperaturaMinima: tipo.minima,
            temperaturaMaxima: tipo.maxima
        });
    }

    // o primeiro item do Figma
    lista[15] = {
        nome: 'Câmara Fria - Carnes 16',
        localizacao: 'Setor D - Corredor 4',
        temperaturaAtual: -19.4,
        temperaturaMinima: -20,
        temperaturaMaxima: -18
    };
    return lista;
}

    /* ---------- categorias (55) ---------- */

    function semearCategorias() {
        const produtos = [
            { nome: 'Picanha Bovina',      tipo: 'carne' },
            { nome: 'Contrafilé Bovino',   tipo: 'carne' },
            { nome: 'Alcatra Bovina',      tipo: 'carne' },
            { nome: 'Frango Inteiro',      tipo: 'ave' },
            { nome: 'Peito de Frango',     tipo: 'ave' },
            { nome: 'Coxa e Sobrecoxa',    tipo: 'ave' },
            { nome: 'Linguiça Suína',      tipo: 'carne' },
            { nome: 'Pernil Suíno',        tipo: 'carne' },
            { nome: 'Bacon Fatiado',       tipo: 'carne' },
            { nome: 'Hambúrguer Bovino',   tipo: 'carne' },
            { nome: 'Empanado de Frango',  tipo: 'ave' },
            { nome: 'Salmão',              tipo: 'pescado' },
            { nome: 'Tilápia',             tipo: 'pescado' },
            { nome: 'Camarão',             tipo: 'pescado' },
            { nome: 'Queijo Mussarela',    tipo: 'laticinio' },
            { nome: 'Iogurte Natural',     tipo: 'laticinio' },
            { nome: 'Manteiga',            tipo: 'laticinio' }
        ];
        const marcas = ['Friboi', 'Seara', 'Sadia', 'Swift', 'Aurora', 'Perdigão', 'Marfrig', 'Minerva'];

        const lista = [];
        const usados = new Set();

        for (let i = 0; i < 55; i++) {
            const produto = produtos[i % produtos.length];
            let marca = marcas[(i * 3) % marcas.length];
            let estado, minima, maxima, vidaUtil;

            if (produto.tipo === 'laticinio') {
                estado = 'Refrigerado'; minima = 2; maxima = 6; vidaUtil = 360 + (i % 5) * 72;
            } else if (produto.tipo === 'pescado') {
                estado = i % 2 ? 'Resfriado' : 'Congelado';
                [minima, maxima, vidaUtil] = estado === 'Congelado' ? [-22, -18, 4320] : [-2, 2, 96 + (i % 3) * 24];
            } else if (i % 2) {
                estado = 'Resfriada'; minima = 0; maxima = 4; vidaUtil = 168 + (i % 4) * 72;
            } else {
                estado = 'Congelada'; minima = -20; maxima = -18; vidaUtil = 4320 + (i % 5) * 1080;
            }

            // nome único: produto + marca + estado
            let nome = produto.nome + ' ' + marca + ' ' + estado;
            let tentativa = 0;
            while (usados.has(nome)) {
                tentativa += 1;
                marca = marcas[(i * 3 + tentativa) % marcas.length];
                nome = produto.nome + ' ' + marca + ' ' + estado;
            }
            usados.add(nome);

            lista.push({ nome, vidaUtilHoras: vidaUtil, temperaturaMinima: minima, temperaturaMaxima: maxima });
        }

        // primeira linha do Figma
        lista[0] = { nome: 'Picanha Bovina Friboi Congelada', vidaUtilHoras: 1160, temperaturaMinima: -20, temperaturaMaxima: -18 };
        return lista;
    }

    /* ---------- lotes (55) ---------- */

    function semearLotes() {
        const status = ['ativo', 'ativo', 'ativo', 'ativo', 'bloqueado', 'ativo', 'ativo', 'expedido', 'ativo'];
        const lista = [];

        for (let i = 0; i < 55; i++) {
            const fabricacao = diasAntes(5 + i * 3);
            const validade = diasDepois(fabricacao, 45 + (i * 17) % 220);
            let situacao = status[i % status.length];
            if (validade < iso(hoje)) situacao = 'vencido';

            lista.push({
                codigo: 'LT-FRB-2026-' + String(i + 1).padStart(4, '0'),
                categoriaId: (i * 13) % 37 + 1,      // usa as categorias 1 a 37; as demais ficam sem lotes
                dataFabricacao: fabricacao,
                dataValidade: validade,
                status: situacao
            });
        }
        return lista;
    }

    /* ---------- termômetros (55) ---------- */

    function semearTermometros() {
        const modelos = ['Sensor DHT-2200', 'Sensor DS18B20', 'Termopar K-100', 'Sensor PT100 Pro', 'Datalogger TL-500'];
        const frigorificos = semearFrigorificos();
        const lista = [];

        for (let i = 0; i < 55; i++) {
            let situacao = 'ativo';
            if (i % 11 === 3) situacao = 'inativo';
            if (i % 17 === 5) situacao = 'manutencao';

            const frigorifico = frigorificos[i];     // 1 termômetro por frigorífico (id = posição + 1)
            const leitura = Math.round((frigorifico.temperaturaAtual + (i % 3 - 1) * 0.1) * 10) / 10;

            lista.push({
                modelo: modelos[i % modelos.length],
                frigorificoId: i + 1,
                status: situacao,
                temperaturaAtual: situacao === 'ativo' ? leitura : null
            });
        }

        // primeira linha do Figma: "Sensor DHT-2200 · Câmara Fria - Carnes 16 · Ativo · -19,4"
        lista[15] = { modelo: 'Sensor DHT-2200', frigorificoId: 16, status: 'ativo', temperaturaAtual: -19.4 };
        return lista;
    }

    /* ---------- repositórios ---------- */

    const cadastros = {
        frigorificos: { chave: 'skadi.frigorificos.v1', semente: semearFrigorificos },
        termometros:  { chave: 'skadi.termometros.v1',  semente: semearTermometros },
        categorias:   { chave: 'skadi.categorias.v1',   semente: semearCategorias },
        lotes:        { chave: 'skadi.lotes.v1',        semente: semearLotes }
    };

    return {
        repositorio: nome => Crud.repositorioLocal(cadastros[nome].chave, cadastros[nome].semente)
    };
})();
