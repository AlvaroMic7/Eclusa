package org.example;

public class NavioCarga extends Embarcacao {
    private double tonelagem;
    private double comprimento;
    private double largura;


    public NavioCarga(String codigo, String capitao, String origem, String destino, String pais, String sentido, double tonelagem, double comprimento, double largura) {
        super(codigo, capitao, origem, destino, pais, sentido);
        this.tonelagem = tonelagem;
        this.comprimento = comprimento;
        this.largura = largura;
    }


    @Override
    public double calcularTaxa() {
        return 500 + (tonelagem * 2);
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
