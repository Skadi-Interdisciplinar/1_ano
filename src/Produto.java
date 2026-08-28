public class Produto extends Refrigerador {
    private String nome;
    private int id_produto;
    private int temperatura_ideal;
    private int validade;
    private String categoria;

    public Produto(int temperatura_min, int temperatura_max, int id, String nome, String localizacao, String nome1, int id_produto, int temperatura_ideal, int validade, String categoria) {
        super(temperatura_min, temperatura_max, id, nome, localizacao);
        this.nome = nome1;
        this.id_produto = id_produto;
        this.temperatura_ideal = temperatura_ideal;
        this.validade = validade;
        this.categoria = categoria;
    }


}
