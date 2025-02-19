package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class FilaDeEspera {
    private List<Embarcacao> listaEmbarcacoes = new ArrayList<>();

    public double calcularTempoTotalAtendimento(Eclusa eclusa) {
        double tempoTotal = 0;

        for (Embarcacao embarcacao : listaEmbarcacoes) {
            double tempoOperacao;
            if (embarcacao.getSentido().equalsIgnoreCase("ascendente")) {
                tempoOperacao = eclusa.calcularTempoEnchimento();
            } else if (embarcacao.getSentido().equalsIgnoreCase("descendente")) {
                tempoOperacao = eclusa.calcularTempoEsvaziamento();
            } else {
                tempoOperacao = 0;
            }

            tempoOperacao += 5;

            tempoTotal += tempoOperacao;
        }

        return tempoTotal;
    }

    public void adicionarEmbarcacao(Embarcacao embarcacao) {
        listaEmbarcacoes.add(embarcacao);
    }

    public void removerEmbarcacao(Embarcacao embarcacao) {
        listaEmbarcacoes.remove(embarcacao);
    }

    public double calcularPrevisaoAtendimento() {
        return listaEmbarcacoes.size() * 10.0;
    }

    public Embarcacao proximaEmbarcacao() {
        if (!listaEmbarcacoes.isEmpty()) {
            return listaEmbarcacoes.remove(0);
        }
        return null;
    }

    public Embarcacao buscarEmbarcacao(String codigo) {
        for (Embarcacao embarcacao : listaEmbarcacoes) {
            if (embarcacao.getCodigoIdentificacao().equals(codigo)) {
                return embarcacao;
            }
        }
        return null;
    }

    public List<Embarcacao> getListaEmbarcacoes() {
        return listaEmbarcacoes;
    }
}