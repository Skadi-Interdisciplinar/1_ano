public class Leitura_temperatura extends Alerta {
    private int id_leitura;
    private int data;
    private int temperatura;
    private double hora;

    public Leitura_temperatura(int id_alerta, String status, double tempo_sobrevivencia, int data, double hora, String tipo, int nivel_gravidade, String notificacao, int id_leitura, int data1, int temperatura, double hora1) {
        super(id_alerta, status, tempo_sobrevivencia, data, hora, tipo, nivel_gravidade, notificacao);
        this.id_leitura = id_leitura;
        this.data = data1;
        this.temperatura = temperatura;
        this.hora = hora1;
    }

}
