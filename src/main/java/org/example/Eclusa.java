package org.example;

import java.util.ArrayList;
import java.util.List;

public class Eclusa {
    private String tamanho;
    private double capacidadeMaxima; // Capacidade máxima em m³
    private double capacidadeMinima; // Capacidade mínima em m³
    private double vazao; // Vazão em m³/min
    private String status;
    private double porcentagemOperacao;
    private int tempoOperacao;
    private Comporta comportaJusante;
    private Comporta comportaMontante;
    private SensorNivelAgua sensor;
    private AlarmeSeguranca alarme;
    private RegistroEventos registro;
    private boolean nivelAlto;
    private double comprimento;
    private double largura;
    private double tempoDecorrido; // Tempo decorrido em minutos
    private SistemaGestaoEclusaSwing sistemaGestaoEclusaSwing;

    public Eclusa(String tamanho, double capacidadeMaxima, double capacidadeMinima, double vazao, int tempoOperacao, double comprimento, double largura, SistemaGestaoEclusaSwing sistemaGestaoEclusaSwing) {
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
        this.alarme = new AlarmeSeguranca();
        this.registro = new RegistroEventos();
        this.nivelAlto = false;
        this.comprimento = comprimento;
        this.largura = largura;
        this.tempoDecorrido = 0;
        this.sistemaGestaoEclusaSwing = sistemaGestaoEclusaSwing;
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
            sistemaGestaoEclusaSwing.MostrarRegistroEvento(">>> Tempo estimado para encher a eclusa: " + tempo + " minutos.");
        } else if (operacao.equalsIgnoreCase("esvaziar")) {
            tempo = calcularTempoEsvaziamento();
            sistemaGestaoEclusaSwing.MostrarRegistroEvento(">>> Tempo estimado para esvaziar a eclusa: " + tempo + " minutos.");
        } else {
            sistemaGestaoEclusaSwing.MostrarRegistroEvento("!!! Operação inválida.");
        }
    }

    public void iniciarOperacao() {
        this.status = "Em operação";
        this.porcentagemOperacao = 100.0;
        sistemaGestaoEclusaSwing.MostrarRegistroEvento("Eclusa iniciada.");
        registro.registrarEvento("Eclusa iniciada.");
    }

    public void operarEclusa(List<Embarcacao> embarcacoes, FilaDeEsperaController filaController) {
        if (!this.status.equalsIgnoreCase("Em operação")) {
            sistemaGestaoEclusaSwing.MostrarRegistroEvento("!!! A eclusa não está em operação. Operação cancelada.");
            registro.registrarEvento("Tentativa de operação com eclusa parada.");

            for (Embarcacao embarcacao : embarcacoes) {
                filaController.adicionarEmbarcacao(embarcacao);
            }
            return;
        }

        if (embarcacoes.isEmpty()) {
            sistemaGestaoEclusaSwing.MostrarRegistroEvento(">>> Nenhuma embarcação para processar.");
            return;
        }

        String sentidoPrimeiraEmbarcacao = embarcacoes.get(0).getSentido();
        for (int i = 1; i < embarcacoes.size(); i++) {
            if (!embarcacoes.get(i).getSentido().equalsIgnoreCase(sentidoPrimeiraEmbarcacao)) {
                sistemaGestaoEclusaSwing.MostrarRegistroEvento(">>> Embarcações com sentidos diferentes detectadas. Processando apenas a primeira embarcação.");
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
                sistemaGestaoEclusaSwing.MostrarRegistroEvento("!!! Embarcação " + embarcacao.getCodigoIdentificacao() + " não efetuou o pagamento. Operação cancelada.");
                registro.registrarEvento("Pagamento não efetuado para embarcação " + embarcacao.getCodigoIdentificacao());

                for (Embarcacao e : embarcacoes) {
                    filaController.adicionarEmbarcacao(e);
                }
                sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Todas as embarcações devolvidas à fila de espera.");
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
            sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Sentido inválido. Operação cancelada.");
            registro.registrarEvento("Sentido inválido para embarcações.");
            return;
        }
    }

    private void ajustarNivelAgua(String sentido) {
        if (sentido.equalsIgnoreCase("ascendente")) {
            sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Ajustando nível da água: Esvaziando a eclusa.");
            nivelAlto = false;
            registro.registrarEvento("Eclusa esvaziando para nível baixo.");
        } else if (sentido.equalsIgnoreCase("descendente")) {
            sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Ajustando nível da água: Enchendo a eclusa.");
            nivelAlto = true;
            registro.registrarEvento("Eclusa enchendo para nível alto.");
        }
    }

    private void operarSentidoAscendente(List<Embarcacao> embarcacoes) {
        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Iniciando operação para embarcações (sentido ascendente).");
        comportaJusante.abrir();
        sistemaGestaoEclusaSwing.MostrarRegistroEvento("Comporta jusante aberta. Embarcações liberadas.");
        comportaJusante.fechar();
        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Comporta Jusante fechada.");
        registro.registrarEvento("Comporta jusante fechada.");

        double tempoTotal = 4.0; // 4 segundos
        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Tempo estimado para encher a eclusa: " + tempoTotal + " segundos.");

        tempoDecorrido = 0;
        while (tempoDecorrido < tempoTotal) {
            try {
                Thread.sleep(100); // Simula o tempo de operação
                tempoDecorrido += 0.1;
                int progresso = (int) ((tempoDecorrido / tempoTotal) * 100);
                atualizarProgresso(progresso); // Atualiza o progresso
            } catch (InterruptedException e) {
                sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Operação interrompida.");
                return;
            }
        }

        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Ajustando nível da água: Enchendo a eclusa.");
        registro.registrarEvento("Eclusa enchendo para nível alto.");
        nivelAlto = true;
        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Eclusa cheia. Elevando as embarcações.");



        comportaMontante.abrir();
        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Comporta Montante aberta. Embarcações liberadas.");
        registro.registrarEvento("Comporta montante aberta. Embarcações liberadas.");

        // Reinicia a barra de progresso
        atualizarProgresso(0);
    }

    private void operarSentidoDescendente(List<Embarcacao> embarcacoes) {
        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Iniciando operação para embarcações (sentido descendente).");
        comportaMontante.abrir();
        sistemaGestaoEclusaSwing.MostrarRegistroEvento("Comporta montante aberta. Embarcações liberadas.");
        comportaMontante.fechar();
        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Comporta montante fechada.");
        registro.registrarEvento("Comporta montante fechada.");

        double tempoTotal = 4.0; // 4 segundos
        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Tempo estimado para esvaziar a eclusa: " + tempoTotal + " segundos.");

        tempoDecorrido = 0;
        while (tempoDecorrido < tempoTotal) {
            try {
                Thread.sleep(100); // Simula o tempo de operação
                tempoDecorrido += 0.1;
                int progresso = (int) ((tempoDecorrido / tempoTotal) * 100);
                atualizarProgresso(progresso); // Atualiza o progresso
            } catch (InterruptedException e) {
                sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Operação interrompida.");
                return;
            }
        }

        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Eclusa vazia. Baixando as embarcações.");
        nivelAlto = false;
        registro.registrarEvento("Eclusa esvaziando para nível baixo.");

        comportaJusante.abrir();
        sistemaGestaoEclusaSwing.MostrarRegistroEvento("Comporta jusante aberta. Embarcações liberadas.");

        registro.registrarEvento("Comporta jusante aberta. Embarcações liberadas.");

        // Reinicia a barra de progresso
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
        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Realizando manutenção de água da eclusa...");
        sistemaGestaoEclusaSwing.MostrarRegistroEvento(" Manutenção de água realizada.");
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