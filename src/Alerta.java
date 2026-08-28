public class Alerta {
    private int id_alerta;
    private String status;
    private double tempo_sobrevivencia;
    private int data;
    private double hora;
    private String tipo;
    private int nivel_gravidade;
    private String notificacao; /* Possui 3 opções (status, canal e data_envio)*/

    public Alerta(int id_alerta, String status, double tempo_sobrevivencia, int data, double hora, String tipo, int nivel_gravidade, String notificacao) {
        this.id_alerta = id_alerta;
        this.status = status;
        this.tempo_sobrevivencia = tempo_sobrevivencia;
        this.data = data;
        this.hora = hora;
        this.tipo = tipo;
        this.nivel_gravidade = nivel_gravidade;
        this.notificacao = notificacao;
    }

}