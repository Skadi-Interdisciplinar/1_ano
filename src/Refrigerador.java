public class Refrigerador {
    private int temperatura_min;
    private int temperatura_max;
    private int id_refrigerador;
    private String nome;
    private String localizacao;

    public Refrigerador(int temperatura_min, int temperatura_max, int id_refrigerador, String nome, String localizacao) {
        this.temperatura_min = temperatura_min;
        this.temperatura_max = temperatura_max;
        this.id_refrigerador = id_refrigerador;
        this.nome = nome;
        this.localizacao = localizacao;
    }

}