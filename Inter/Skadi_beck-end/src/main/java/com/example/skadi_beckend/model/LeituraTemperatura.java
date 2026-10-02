package com.example.skadi_beckend.model;

import java.sql.Date;

public class LeituraTemperatura {
//      Atributos
    private int id_leitura;
    private double temperatura;
    private Date data;
    private int id_termometro;
//     Construtor vazio
    public LeituraTemperatura() {}

//    Construtor
    public LeituraTemperatura(int id_leitura, double temperatura, Date data, int id_termometro) {
        this.id_leitura = id_leitura;
        this.temperatura = temperatura;
        this.data = data;
        this.id_termometro = id_termometro;
    }
//      Getters e Setters
    public int getId_leitura() { return id_leitura; }
    public void setId_leitura(int id_leitura) { this.id_leitura = id_leitura; }

    public double getTemperatura() { return temperatura; }
    public void setTemperatura(double temperatura) { this.temperatura = temperatura; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public int getId_termometro() { return id_termometro; }
    public void setId_termometro(int id_termometro) { this.id_termometro = id_termometro; }
}