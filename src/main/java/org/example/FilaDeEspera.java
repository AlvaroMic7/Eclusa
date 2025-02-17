package org.example;

import java.util.ArrayList;
import java.util.List;

public class FilaDeEspera {
    private List<Embarcacao> listaEmbarcacoes = new ArrayList<>();


    public double calcularTempoTotalAtendimento(Eclusa eclusa) {
        double tempoTotal = 0;


        for (Embarcacao embarcacao : listaEmbarcacoes) {
            // Tempo de operação da eclusa (encher ou esvaziar)
            double tempoOperacao;
            if (embarcacao.getSentido().equalsIgnoreCase("ascendente")) {
                tempoOperacao = eclusa.calcularTempoEnchimento();
            } else if (embarcacao.getSentido().equalsIgnoreCase("descendente")) {
                tempoOperacao = eclusa.calcularTempoEsvaziamento();
            } else {
                tempoOperacao = 0; // Sentido inválido
            }


            // Adiciona tempo adicional para abertura/fechamento das comportas e manobras
            tempoOperacao += 5; // 5 minutos adicionais por operação


            // Acumula o tempo total
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
