package org.example;

public class NavioPassageiros extends Embarcacao {
    private int numeroPassageiros;
    private double comprimento;
    private double largura;


    public NavioPassageiros(String codigo, String capitao, String origem, String destino, String pais, String sentido, int numeroPassageiros, double comprimento, double largura) {
        super(codigo, capitao, origem, destino, pais, sentido);
        this.numeroPassageiros = numeroPassageiros;
        this.comprimento = comprimento;
        this.largura = largura;
    }


    @Override
    public double calcularTaxa() {
        return 1000 + (numeroPassageiros * 5);
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
