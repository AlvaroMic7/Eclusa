package org.example;

import java.util.List;

public class FilaDeEsperaController {
    private FilaDeEspera filaDeEspera;

    public FilaDeEsperaController(FilaDeEspera filaDeEspera) {
        this.filaDeEspera = filaDeEspera;
    }

    public double calcularTempoTotalAtendimento(Eclusa eclusa) {
        return filaDeEspera.calcularTempoTotalAtendimento(eclusa);
    }

    public void adicionarEmbarcacao(Embarcacao embarcacao) {
        filaDeEspera.adicionarEmbarcacao(embarcacao);
    }

    public void removerEmbarcacao(Embarcacao embarcacao) {
        filaDeEspera.removerEmbarcacao(embarcacao);
    }

    public Embarcacao obterProximaEmbarcacao() {
        return filaDeEspera.proximaEmbarcacao();
    }

    public double getPrevisaoAtendimento() {
        return filaDeEspera.calcularPrevisaoAtendimento();
    }

    public Embarcacao buscarEmbarcacao(String codigo) {
        return filaDeEspera.buscarEmbarcacao(codigo);
    }

    public List<Embarcacao> getListaEmbarcacoes() {
        return filaDeEspera.getListaEmbarcacoes();
    }
}
