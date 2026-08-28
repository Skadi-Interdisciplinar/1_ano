public class Termometro extends Refrigerador{
    private int id_termometro;
    private String modelo;
    private String status;

    public Termometro(int temperatura_min, int temperatura_max, int id, String nome, String localizacao, int id_termometro, String modelo, String status) {
        super(temperatura_min, temperatura_max, id, nome, localizacao);
        this.id_termometro = id_termometro;
        this.modelo = modelo;
        this.status = status;
    }

}