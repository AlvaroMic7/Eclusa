package org.example.controller;

import org.example.model.Eclusa;
import org.example.model.Embarcacao;

import java.util.List;

public class EclusaController {
    private Eclusa eclusa;


    public EclusaController(Eclusa eclusa) {
        this.eclusa = eclusa;
    }


    public Eclusa getEclusa() {
        return eclusa;
    }


    public void iniciarOperacao() {
        eclusa.iniciarOperacao();
    }


    public void operarEclusa(List<Embarcacao> embarcacoes, FilaDeEsperaController filaController) {
        eclusa.operarEclusa(embarcacoes, filaController);
    }


    public String getStatus() {
        return eclusa.getStatus();
    }


    public String exibirEventos() {
        return eclusa.exibirEventos();
    }
}
