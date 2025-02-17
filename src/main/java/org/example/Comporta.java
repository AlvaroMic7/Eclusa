package org.example;

public class Comporta {
    private String localizacao;
    private String estado; // "aberta" ou "fechada"


    public Comporta(String localizacao) {
        this.localizacao = localizacao;
        this.estado = "fechada";
    }


    public void abrir() {
        estado = "aberta";
        System.out.println(">>> Comporta " + localizacao + " aberta.");
    }


    public void fechar() {
        estado = "fechada";
        System.out.println(">>> Comporta " + localizacao + " fechada.");
    }


    public String getEstado() {
        return estado;
    }
}
