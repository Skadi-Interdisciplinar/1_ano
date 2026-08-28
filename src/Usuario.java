public class Usuario extends Alerta {
    private String email;
    private int id_usuario;
    private String cargo;
    private int cpf;
    private String nome;
    private int nivel_acesso;

    public Usuario(int id_alerta, String status, double tempo_sobrevivencia, int data, double hora, String tipo, int nivel_gravidade, String notificacao, String email, int id_usuario, String cargo, int cpf, String nome, int nivel_acesso) {
        super(id_alerta, status, tempo_sobrevivencia, data, hora, tipo, nivel_gravidade, notificacao);
        this.email = email;
        this.id_usuario = id_usuario;
        this.cargo = cargo;
        this.cpf = cpf;
        this.nome = nome;
        this.nivel_acesso = nivel_acesso;
    }

}
