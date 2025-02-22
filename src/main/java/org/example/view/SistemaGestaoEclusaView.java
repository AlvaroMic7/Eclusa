package org.example.view;

import org.example.controller.EclusaController;
import org.example.controller.FilaDeEsperaController;
import org.example.controller.PagamentoController;
import org.example.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SistemaGestaoEclusaView {
    private Eclusa eclusa;
    private FilaDeEspera filaDeEspera;
    private Pagamento pagamento;
    private EclusaController eclusaController;
    private FilaDeEsperaController filaController;
    private PagamentoController pagamentoController;

    private JFrame frame;
    private JTextArea outputArea;
    private JTextField codigoField, capitaoField, portoOrigemField, portoDestinoField, paisField, sentidoField;
    private JComboBox<String> tipoEmbarcacaoComboBox;
    private JTextField tonelagemField, comprimentoField, larguraField, passageirosField;
    private JTextField valorPagamentoField;
    private JProgressBar progressBar;

    private static final Color BACKGROUND_COLOR = new Color(32, 36, 47);
    private static final Color PANEL_COLOR = new Color(38, 42, 54);
    private static final Color TEXT_AREA_BACKGROUND = new Color(46, 50, 62);
    private static final Color TEXT_AREA_FOREGROUND = new Color(220, 220, 220);
    private static final Color BUTTON_COLOR = new Color(54, 59, 73);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(49, 199, 178);

    public SistemaGestaoEclusaView() {
        eclusa = new Eclusa("Grande", 5000, 1000, 100, 10, 150, 20, this);
        filaDeEspera = new FilaDeEspera();
        pagamento = new Pagamento();

        eclusaController = new EclusaController(eclusa);
        filaController = new FilaDeEsperaController(filaDeEspera);
        pagamentoController = new PagamentoController(pagamento);

        initialize();
    }

    private void initialize() {
        setCustomLookAndFeel();

        frame = new JFrame("Sistema de Gestão de Eclusa");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel headerLabel = new JLabel("Sistema de Gestão de Eclusa");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(TEXT_AREA_FOREGROUND);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(headerLabel, BorderLayout.NORTH);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressBar.setBackground(new Color(30, 34, 45));
        progressBar.setForeground(ACCENT_COLOR);
        progressBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(progressBar, BorderLayout.SOUTH);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(PANEL_COLOR);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(leftPanel, BorderLayout.WEST);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setBackground(TEXT_AREA_BACKGROUND);
        outputArea.setForeground(TEXT_AREA_FOREGROUND);
        outputArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane, BorderLayout.CENTER);

        leftPanel.add(createButton("Iniciar Operação da Eclusa", e -> {
            MostrarRegistroEvento("Eclusa iniciando operação...");
            eclusaController.iniciarOperacao();
        }));

        leftPanel.add(createButton("Adicionar Embarcação à Fila", e -> {
            new AdicionarEmbarcacaoView(filaController, outputArea);
        }));

        leftPanel.add(createButton("Registrar Pagamento de Embarcação", e -> {
            new RegistrarPagamentoView(pagamentoController, filaController, outputArea);
        }));

        leftPanel.add(createButton("Exibir Status da Eclusa", e -> {
            outputArea.append(">>> Status da Eclusa: " + eclusaController.getStatus() + "\n");
        }));

        leftPanel.add(createButton("Exibir Previsão de Atendimento", e -> {
            double tempoTotal = filaController.calcularTempoTotalAtendimento(eclusaController.getEclusa());
            outputArea.append(">>> Previsão de Atendimento: " + tempoTotal + " minutos para todas as embarcações na fila.\n");
        }));

        leftPanel.add(createButton("Exibir Totais de Pagamento", e -> {
            exibirTotais();
        }));

        leftPanel.add(createButton("Processar Próxima(s) Embarcação(ões)", e -> {
            processarEmbarcacoes();
        }));

        leftPanel.add(createButton("Exibir Registro de Eventos", e -> {
            exibirEventos();
        }));

        leftPanel.add(createButton("Exibir Lista de Embarcações na Fila", e -> {
            exibirFila();
        }));

        leftPanel.add(createButton("Limpar Tela", e -> {
            limparTerminal();
        }));

        leftPanel.add(Box.createVerticalStrut(10));

        leftPanel.add(createButton("Sair", e -> {
            frame.dispose();
        }));

        frame.setVisible(true);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(action);
        return button;
    }

    private void setCustomLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.put("control", BACKGROUND_COLOR);
                    UIManager.put("text", TEXT_AREA_FOREGROUND);
                    UIManager.put("nimbusBase", PANEL_COLOR);
                    UIManager.put("nimbusFocus", ACCENT_COLOR);
                    UIManager.put("nimbusLightBackground", PANEL_COLOR);
                    UIManager.put("info", BACKGROUND_COLOR);
                    UIManager.put("nimbusAlertYellow", ACCENT_COLOR);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    private void atualizarProgresso(int progresso) {
        SwingUtilities.invokeLater(() -> progressBar.setValue(progresso));
    }

    private void limparTerminal() {
        outputArea.setText("");
    }

    private void registrarPagamento() {
        JFrame pagamentoFrame = new JFrame("Registrar Pagamento");
        pagamentoFrame.setSize(300, 200);
        pagamentoFrame.setLocationRelativeTo(frame);
        pagamentoFrame.setLayout(new GridLayout(0, 2, 10, 10));

        JPanel contentPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        pagamentoFrame.setContentPane(contentPanel);

        JLabel codigoLabel = createLabel("Código da Embarcação:");
        contentPanel.add(codigoLabel);
        JTextField codigoPagamentoField = createTextField();
        contentPanel.add(codigoPagamentoField);

        JLabel valorLabel = createLabel("Valor do Pagamento:");
        contentPanel.add(valorLabel);
        valorPagamentoField = createTextField();
        contentPanel.add(valorPagamentoField);

        JButton pagarButton = createButton("Pagar", e -> {
            String codigo = codigoPagamentoField.getText();
            double valor = Double.parseDouble(valorPagamentoField.getText());

            Embarcacao barcoParaPagar = filaController.buscarEmbarcacao(codigo);
            if (barcoParaPagar == null) {
                outputArea.append(">>> Embarcação não encontrada na fila ou já processada.\n");
            } else {
                String resultadoPagamento = pagamentoController.registrarPagamento(barcoParaPagar, valor);
                if (resultadoPagamento.startsWith("Sucesso")) {
                    outputArea.append(">>> Pagamento efetuado para a embarcação " + codigo + "\n");
                    if (pagamento.getTroco() > 0) {
                        outputArea.append(">>> Pagamento registrado. Troco: " + pagamento.getTroco() + "\n");
                    } else {
                        outputArea.append(">>> Pagamento registrado sem troco.\n");
                    }
                } else {
                    outputArea.append(">>> " + resultadoPagamento + "\n");
                }
            }
            pagamentoFrame.dispose();
        });
        contentPanel.add(pagarButton);

        pagamentoFrame.setVisible(true);
    }

    private void processarEmbarcacoes() {
        if(eclusaController.getOperacaoIniciada()) {
            List<Embarcacao> embarcacoesParaProcessar = new ArrayList<>();
            Embarcacao prox = filaController.obterProximaEmbarcacao();
            while (prox != null) {
                if (prox.isPago()) {
                    embarcacoesParaProcessar.add(prox);
                    if (!eclusaController.getEclusa().podeAcomodarEmbarcacoes(embarcacoesParaProcessar)) {
                        embarcacoesParaProcessar.remove(embarcacoesParaProcessar.size() - 1);
                        outputArea.append(">>> Embarcação " + prox.getCodigoIdentificacao() + " não cabe na eclusa. Devolvida à fila.\n");
                        filaController.adicionarEmbarcacao(prox);
                        break;
                    }
                } else {
                    outputArea.append(">>> Embarcação " + prox.getCodigoIdentificacao() + " não foi paga. Ignorada.\n");
                }
                prox = filaController.obterProximaEmbarcacao();
            }

            if (!embarcacoesParaProcessar.isEmpty()) {
                eclusaController.operarEclusa(embarcacoesParaProcessar, filaController);
                new Thread(() -> {
                    for (int i = 0; i <= 100; i++) {
                        atualizarProgresso(i);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                outputArea.append(">>> Nenhuma embarcação na fila ou não há espaço na eclusa!\n");
            }
        }else{
            outputArea.append(">>> A eclusa não foi iniciada!\n");
        }
    }

    private void exibirFila() {
        List<Embarcacao> embarcacoesNaFila = filaController.getListaEmbarcacoes();
        if (embarcacoesNaFila.isEmpty()) {
            outputArea.append(">>> Nenhuma embarcação na fila de espera.\n");
        } else {
            outputArea.append("\n========== Lista de Embarcações na Fila ==========\n");
            double tempoTotalFila = filaController.calcularTempoTotalAtendimento(eclusaController.getEclusa());
            for (Embarcacao embarcacaoNaFila : embarcacoesNaFila) {
                outputArea.append("Código: " + embarcacaoNaFila.getCodigoIdentificacao() + "\n");
                outputArea.append("Capitão: " + embarcacaoNaFila.getCapitao() + "\n");
                outputArea.append("Porto de Origem: " + embarcacaoNaFila.getPortoOrigem() + "\n");
                outputArea.append("Porto de Destino: " + embarcacaoNaFila.getPortoDestino() + "\n");
                outputArea.append("País: " + embarcacaoNaFila.getPais() + "\n");
                outputArea.append("Sentido: " + embarcacaoNaFila.getSentido() + "\n");
                outputArea.append("Pagamento: " + (embarcacaoNaFila.isPago() ? "Pago" : "Pendente") + "\n");
                outputArea.append("Tempo estimado para atendimento: " + tempoTotalFila + " minutos\n");
                outputArea.append("---------------------------------------------\n");
            }
            outputArea.append("=================================================\n");
        }
    }

    private void exibirEventos() {
        String eventos = eclusaController.exibirEventos();
        outputArea.append(eventos + "\n");
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(60, 64, 78));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 95)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaGestaoEclusaView());
    }

    public void MostrarRegistroEvento(String evento) {
        outputArea.append(">>> Evento registrado: " + evento + "\n");
    }

    public void exibirTotais() {
        outputArea.append("========== Totais de Pagamento ==========\n");
        outputArea.append("Dia: " + pagamento.getTotalApuradoDia() + "\n");
        outputArea.append("Mês: " + pagamento.getTotalApuradoMes() + "\n");
        outputArea.append("Ano: " + pagamento.getTotalApuradoAno() + "\n");
        outputArea.append("=========================================\n");
    }
}