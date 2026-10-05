package com.example.skadi_beckend.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Alerta { 
//      Atributos
    private int id_alerta;
    private String status;
    private Integer tempo_sobrevivencia;
    private Timestamp data_hora;
    private String tipo;
    private String nivel_gravidade;
    private String canal;
    private String notificacao;
    private Date data_envio;
    private int id_leitura;
    private Integer id_usuario;

    // Construtor vazio
    public Alerta() {}

    //      Construtor
    public Alerta(int id_alerta, String status, Integer tempo_sobrevivencia, Timestamp data_hora,
                  String tipo, String nivel_gravidade, String canal, String notificacao,
                  Date data_envio, int id_leitura, Integer id_usuario) {
        this.id_alerta = id_alerta;
        this.status = status;
        this.tempo_sobrevivencia = tempo_sobrevivencia;
        this.data_hora = data_hora;
        this.tipo = tipo;
        this.nivel_gravidade = nivel_gravidade;
        this.canal = canal;
        this.notificacao = notificacao;
        this.data_envio = data_envio;
        this.id_leitura = id_leitura;
        this.id_usuario = id_usuario;
    }
//      Getters e Setters
    public int getId_alerta() { return id_alerta; }
    public void setId_alerta(int id_alerta) { this.id_alerta = id_alerta; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTempo_sobrevivencia() { return tempo_sobrevivencia; }
    public void setTempo_sobrevivencia(Integer tempo_sobrevivencia) { this.tempo_sobrevivencia = tempo_sobrevivencia; }

    public Timestamp getData_hora() { return data_hora; }
    public void setData_hora(Timestamp data_hora) { this.data_hora = data_hora; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNivel_gravidade() { return nivel_gravidade; }
    public void setNivel_gravidade(String nivel_gravidade) { this.nivel_gravidade = nivel_gravidade; }

    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }

    public String getNotificacao() { return notificacao; }
    public void setNotificacao(String notificacao) { this.notificacao = notificacao; }

    public Date getData_envio() { return data_envio; }
    public void setData_envio(Date data_envio) { this.data_envio = data_envio; }

    public int getId_leitura() { return id_leitura; }
    public void setId_leitura(int id_leitura) { this.id_leitura = id_leitura; }

    public Integer getId_usuario() { return id_usuario; }
    public void setId_usuario(Integer id_usuario) { this.id_usuario = id_usuario; }
}