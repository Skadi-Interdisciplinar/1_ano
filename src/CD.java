public class CD extends Refrigerador{
    private String nome;
    private int id_cd;
    private String endereco; /* VINICIUS FAÇA VETOR PARA ENDEREÇO */
    private int cnpj;

    public CD(int temperatura_min, int temperatura_max, int id, String nome, String localizacao, String nome1, int id_cd, String endereco, int cnpj) {
        super(temperatura_min, temperatura_max, id, nome, localizacao);
        this.nome = nome1;
        this.id_cd = id_cd;
        this.endereco = endereco;
        this.cnpj = cnpj;
    }

}
