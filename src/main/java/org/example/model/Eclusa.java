package org.example.model;

import org.example.controller.FilaDeEsperaController;
import org.example.view.SistemaGestaoEclusaView;

import java.util.ArrayList;
import java.util.List;

public class Eclusa {
    private String tamanho;
    private double capacidadeMaxima;
    private double capacidadeMinima;
    private double vazao;
    private String status;
    private double porcentagemOperacao;
    private int tempoOperacao;
    private Comporta comportaJusante;
    private Comporta comportaMontante;
    private SensorNivelAgua sensor;
    private RegistroEventos registro;
    private boolean nivelAlto;
    private double comprimento;
    private double largura;
    private double tempoDecorrido;
    private SistemaGestaoEclusaView sistemaGestaoEclusaView;

    public Eclusa(String tamanho, double capacidadeMaxima, double capacidadeMinima, double vazao, int tempoOperacao, double comprimento, double largura, SistemaGestaoEclusaView sistemaGestaoEclusaView) {
        this.tamanho = tamanho;
        this.capacidadeMaxima = capacidadeMaxima;
        this.capacidadeMinima = capacidadeMinima;
        this.vazao = vazao;
        this.tempoOperacao = tempoOperacao;
        this.status = "Parada";
        this.porcentagemOperacao = 0.0;
        this.comportaJusante = new Comporta("Jusante");
        this.comportaMontante = new Comporta("Montante");
        this.sensor = new SensorNivelAgua();
        this.registro = new RegistroEventos();
        this.nivelAlto = false;
        this.comprimento = comprimento;
        this.largura = largura;
        this.tempoDecorrido = 0;
        this.sistemaGestaoEclusaView = sistemaGestaoEclusaView;
    }

    public double calcularTempoEnchimento() {
        double volumeEnchimento = capacidadeMaxima - capacidadeMinima;
        return volumeEnchimento / vazao;
    }

    public double calcularTempoEsvaziamento() {
        double volumeEsvaziamento = capacidadeMaxima - capacidadeMinima;
        return volumeEsvaziamento / vazao;
    }

    public void atualizarProgresso(int progresso) {
        this.porcentagemOperacao = progresso;
    }

    public void exibirTempoOperacao(String operacao) {
        double tempo;
        if (operacao.equalsIgnoreCase("encher")) {
            tempo = calcularTempoEnchimento();
            sistemaGestaoEclusaView.MostrarRegistroEvento(">>> Tempo estimado para encher a eclusa: " + tempo + " minutos.");
        } else if (operacao.equalsIgnoreCase("esvaziar")) {
            tempo = calcularTempoEsvaziamento();
            sistemaGestaoEclusaView.MostrarRegistroEvento(">>> Tempo estimado para esvaziar a eclusa: " + tempo + " minutos.");
        } else {
            sistemaGestaoEclusaView.MostrarRegistroEvento("!!! Operação inválida.");
        }
    }

    public void iniciarOperacao() {
        this.status = "Em operação";
        this.porcentagemOperacao = 100.0;
        sistemaGestaoEclusaView.MostrarRegistroEvento("Eclusa iniciada.");
        registro.registrarEvento("Eclusa iniciada.");
    }

    public void operarEclusa(List<Embarcacao> embarcacoes, FilaDeEsperaController filaController) {
        if (!this.status.equalsIgnoreCase("Em operação")) {
            sistemaGestaoEclusaView.MostrarRegistroEvento("!!! A eclusa não está em operação. Operação cancelada.");
            registro.registrarEvento("Tentativa de operação com eclusa parada.");

            for (Embarcacao embarcacao : embarcacoes) {
                filaController.adicionarEmbarcacao(embarcacao);
            }
            return;
        }

        if (embarcacoes.isEmpty()) {
            sistemaGestaoEclusaView.MostrarRegistroEvento(">>> Nenhuma embarcação para processar.");
            return;
        }

        String sentidoPrimeiraEmbarcacao = embarcacoes.get(0).getSentido();
        for (int i = 1; i < embarcacoes.size(); i++) {
            if (!embarcacoes.get(i).getSentido().equalsIgnoreCase(sentidoPrimeiraEmbarcacao)) {
                sistemaGestaoEclusaView.MostrarRegistroEvento(">>> Embarcações com sentidos diferentes detectadas. Processando apenas a primeira embarcação.");
                registro.registrarEvento("Embarcações com sentidos diferentes detectadas. Processando apenas a primeira embarcação.");

                for (int j = 1; j < embarcacoes.size(); j++) {
                    filaController.adicionarEmbarcacao(embarcacoes.get(j));
                }

                processarEmbarcacao(embarcacoes.get(0), filaController);
                return;
            }
        }

        processarEmbarcacoes(embarcacoes, filaController);
    }

    private void processarEmbarcacao(Embarcacao embarcacao, FilaDeEsperaController filaController) {
        List<Embarcacao> embarcacaoUnica = new ArrayList<>();
        embarcacaoUnica.add(embarcacao);
        processarEmbarcacoes(embarcacaoUnica, filaController);
    }

    private void processarEmbarcacoes(List<Embarcacao> embarcacoes, FilaDeEsperaController filaController) {
        for (Embarcacao embarcacao : embarcacoes) {
            if (!embarcacao.isPago()) {
                sistemaGestaoEclusaView.MostrarRegistroEvento("!!! Embarcação " + embarcacao.getCodigoIdentificacao() + " não efetuou o pagamento. Operação cancelada.");
                registro.registrarEvento("Pagamento não efetuado para embarcação " + embarcacao.getCodigoIdentificacao());

                for (Embarcacao e : embarcacoes) {
                    filaController.adicionarEmbarcacao(e);
                }
                sistemaGestaoEclusaView.MostrarRegistroEvento(" Todas as embarcações devolvidas à fila de espera.");
                return;
            }
        }

        String sentido = embarcacoes.get(0).getSentido();
        if (!sensor.verificarNivelAdequado(sentido, nivelAlto)) {
            ajustarNivelAgua(sentido);
        }

        if (sentido.equalsIgnoreCase("ascendente")) {
            operarSentidoAscendente(embarcacoes);
        } else if (sentido.equalsIgnoreCase("descendente")) {
            operarSentidoDescendente(embarcacoes);
        } else {
            sistemaGestaoEclusaView.MostrarRegistroEvento(" Sentido inválido. Operação cancelada.");
            registro.registrarEvento("Sentido inválido para embarcações.");
            return;
        }
    }

    private void ajustarNivelAgua(String sentido) {
        if (sentido.equalsIgnoreCase("ascendente")) {
            sistemaGestaoEclusaView.MostrarRegistroEvento(" Ajustando nível da água: Esvaziando a eclusa.");
            nivelAlto = false;
            registro.registrarEvento("Eclusa esvaziando para nível baixo.");
        } else if (sentido.equalsIgnoreCase("descendente")) {
            sistemaGestaoEclusaView.MostrarRegistroEvento(" Ajustando nível da água: Enchendo a eclusa.");
            nivelAlto = true;
            registro.registrarEvento("Eclusa enchendo para nível alto.");
        }
    }

    private void operarSentidoAscendente(List<Embarcacao> embarcacoes) {
        sistemaGestaoEclusaView.MostrarRegistroEvento(" Iniciando operação para embarcações (sentido ascendente).");
        comportaJusante.abrir();
        sistemaGestaoEclusaView.MostrarRegistroEvento("Comporta jusante aberta. Embarcações liberadas.");
        comportaJusante.fechar();
        sistemaGestaoEclusaView.MostrarRegistroEvento(" Comporta Jusante fechada.");
        registro.registrarEvento("Comporta jusante fechada.");

        double tempoTotal = 4.0;
        sistemaGestaoEclusaView.MostrarRegistroEvento(" Tempo estimado para encher a eclusa: " + tempoTotal + " segundos.");

        tempoDecorrido = 0;
        while (tempoDecorrido < tempoTotal) {
            try {
                Thread.sleep(100);
                tempoDecorrido += 0.1;
                int progresso = (int) ((tempoDecorrido / tempoTotal) * 100);
                atualizarProgresso(progresso);
            } catch (InterruptedException e) {
                sistemaGestaoEclusaView.MostrarRegistroEvento(" Operação interrompida.");
                return;
            }
        }

        sistemaGestaoEclusaView.MostrarRegistroEvento(" Ajustando nível da água: Enchendo a eclusa.");
        registro.registrarEvento("Eclusa enchendo para nível alto.");
        nivelAlto = true;
        sistemaGestaoEclusaView.MostrarRegistroEvento(" Eclusa cheia. Elevando as embarcações.");

        comportaMontante.abrir();
        sistemaGestaoEclusaView.MostrarRegistroEvento(" Comporta Montante aberta. Embarcações liberadas.");
        registro.registrarEvento("Comporta montante aberta. Embarcações liberadas.");

        atualizarProgresso(0);
    }

    private void operarSentidoDescendente(List<Embarcacao> embarcacoes) {
        sistemaGestaoEclusaView.MostrarRegistroEvento(" Iniciando operação para embarcações (sentido descendente).");
        comportaMontante.abrir();
        sistemaGestaoEclusaView.MostrarRegistroEvento("Comporta montante aberta. Embarcações liberadas.");
        comportaMontante.fechar();
        sistemaGestaoEclusaView.MostrarRegistroEvento(" Comporta montante fechada.");
        registro.registrarEvento("Comporta montante fechada.");

        double tempoTotal = 4.0;
        sistemaGestaoEclusaView.MostrarRegistroEvento(" Tempo estimado para esvaziar a eclusa: " + tempoTotal + " segundos.");

        tempoDecorrido = 0;
        while (tempoDecorrido < tempoTotal) {
            try {
                Thread.sleep(100);
                tempoDecorrido += 0.1;
                int progresso = (int) ((tempoDecorrido / tempoTotal) * 100);
                atualizarProgresso(progresso);
            } catch (InterruptedException e) {
                sistemaGestaoEclusaView.MostrarRegistroEvento(" Operação interrompida.");
                return;
            }
        }

        sistemaGestaoEclusaView.MostrarRegistroEvento(" Eclusa vazia. Baixando as embarcações.");
        nivelAlto = false;
        registro.registrarEvento("Eclusa esvaziando para nível baixo.");

        comportaJusante.abrir();
        sistemaGestaoEclusaView.MostrarRegistroEvento("Comporta jusante aberta. Embarcações liberadas.");

        registro.registrarEvento("Comporta jusante aberta. Embarcações liberadas.");

        atualizarProgresso(0);
    }

    public boolean podeAcomodarEmbarcacoes(List<Embarcacao> embarcacoes) {
        double somaComprimentos = 0;
        double larguraMaxima = 0;

        for (Embarcacao embarcacao : embarcacoes) {
            somaComprimentos += embarcacao.getComprimento();
            if (embarcacao.getLargura() > larguraMaxima) {
                larguraMaxima = embarcacao.getLargura();
            }
        }

        return somaComprimentos <= this.comprimento && larguraMaxima <= this.largura;
    }

    private void realizarManutencaoAgua() {
        sistemaGestaoEclusaView.MostrarRegistroEvento(" Realizando manutenção de água da eclusa...");
        sistemaGestaoEclusaView.MostrarRegistroEvento(" Manutenção de água realizada.");
        registro.registrarEvento("Manutenção de água realizada.");
    }

    public String getStatus() {
        return status;
    }

    public double getPorcentagemOperacao() {
        return porcentagemOperacao;
    }

    public int getTempoOperacao() {
        return tempoOperacao;
    }

    public String exibirEventos() {
        StringBuilder eventosStr = new StringBuilder();
        eventosStr.append("\n========== Registro de Eventos ==========\n");
        for (String evento : registro.getEventos()) {
            eventosStr.append(evento).append("\n");
        }
        eventosStr.append("===========================================\n");
        return eventosStr.toString();
    }
}