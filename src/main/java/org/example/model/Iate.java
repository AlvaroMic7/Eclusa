package org.example.model;

public class Iate extends Embarcacao {
    private double comprimento;
    private double largura;


    public Iate(String codigo, String capitao, String origem, String destino, String pais, String sentido, double comprimento, double largura) {
        super(codigo, capitao, origem, destino, pais, sentido);
        this.comprimento = comprimento;
        this.largura = largura;
    }


    @Override
    public double calcularTaxa() {
        return 1500 + (comprimento * 10);
    }


    @Override
    public double getComprimento() {
        return comprimento;
    }


    @Override
    public double getLargura() {
        return largura;
    }
}
