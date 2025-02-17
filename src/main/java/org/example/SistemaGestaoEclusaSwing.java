package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SistemaGestaoEclusaSwing {
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

    // Paleta de cores inspirada no mockup conceitual
    private static final Color BACKGROUND_COLOR = new Color(32, 36, 47);  // cor de fundo principal
    private static final Color PANEL_COLOR = new Color(38, 42, 54);  // cor do painel lateral
    private static final Color TEXT_AREA_BACKGROUND = new Color(46, 50, 62);  // fundo da área de texto
    private static final Color TEXT_AREA_FOREGROUND = new Color(220, 220, 220); // texto claro
    private static final Color BUTTON_COLOR = new Color(54, 59, 73);  // fundo dos botões
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;            // cor do texto dos botões
    private static final Color ACCENT_COLOR = new Color(49, 199, 178);// cor de destaque (teal)

    public SistemaGestaoEclusaSwing() {
        // Inicializa os objetos de domínio
        eclusa = new Eclusa("Grande", 5000, 1000, 100, 10, 150, 20, this);
        filaDeEspera = new FilaDeEspera();
        pagamento = new Pagamento();

        eclusaController = new EclusaController(eclusa);
        filaController = new FilaDeEsperaController(filaDeEspera);
        pagamentoController = new PagamentoController(pagamento);

        initialize();
    }

    private void initialize() {
        // Tenta definir o Look and Feel Nimbus (opcional)
        setCustomLookAndFeel();

        frame = new JFrame("Sistema de Gestão de Eclusa");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Top Panel: Cabeçalho e barra de progresso (inspirado no mockup)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cabeçalho com o título do sistema
        JLabel headerLabel = new JLabel("Sistema de Gestão de Eclusa");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(TEXT_AREA_FOREGROUND);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(headerLabel, BorderLayout.NORTH);

        // Barra de progresso
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressBar.setBackground(new Color(30, 34, 45));
        progressBar.setForeground(ACCENT_COLOR);
        progressBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(progressBar, BorderLayout.SOUTH);
        frame.add(topPanel, BorderLayout.NORTH);

        // Lateral Esquerda (barra de botões)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(PANEL_COLOR);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(leftPanel, BorderLayout.WEST);

        // Área de saída (centro)
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setBackground(TEXT_AREA_BACKGROUND);
        outputArea.setForeground(TEXT_AREA_FOREGROUND);
        outputArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // remove borda
        frame.add(scrollPane, BorderLayout.CENTER);

        // Adiciona os botões na barra lateral
        leftPanel.add(createButton("Iniciar Operação da Eclusa", e -> {
            eclusaController.iniciarOperacao();
            MostrarRegistroEvento("Eclusa iniciando operação...\n");
        }));

        leftPanel.add(createButton("Adicionar Embarcação à Fila", e -> {
            adicionarEmbarcacao();
        }));

        leftPanel.add(createButton("Registrar Pagamento de Embarcação", e -> {
            registrarPagamento();
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

        leftPanel.add(Box.createVerticalStrut(10)); // espaçamento

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
        button.setAlignmentX(Component.LEFT_ALIGNMENT); // alinha no BoxLayout
        button.addActionListener(action);
        return button;
    }

    private void setCustomLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    // Ajusta algumas cores do Nimbus para combinar com o tema escuro
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
            // Se der erro ou não achar o Nimbus, segue o padrão
        }
    }

    private void atualizarProgresso(int progresso) {
        SwingUtilities.invokeLater(() -> progressBar.setValue(progresso));
    }

    private void limparTerminal() {
        outputArea.setText("");
    }

    private void adicionarEmbarcacao() {
        JFrame adicionarFrame = new JFrame("Adicionar Embarcação");
        adicionarFrame.setSize(400, 500);
        adicionarFrame.setLocationRelativeTo(frame);
        adicionarFrame.setLayout(new GridLayout(0, 2, 10, 10));

        // Painel de fundo escuro para o frame
        JPanel contentPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        adicionarFrame.setContentPane(contentPanel);

        // Labels e campos
        JLabel tipoLabel = createLabel("Tipo de Embarcação:");
        contentPanel.add(tipoLabel);
        tipoEmbarcacaoComboBox = new JComboBox<>(new String[]{"Navio de Carga", "Navio de Passageiros", "Iate"});
        contentPanel.add(tipoEmbarcacaoComboBox);

        JLabel codigoLabel = createLabel("Código da Embarcação:");
        contentPanel.add(codigoLabel);
        codigoField = createTextField();
        contentPanel.add(codigoField);

        JLabel capitaoLabel = createLabel("Nome do Capitão:");
        contentPanel.add(capitaoLabel);
        capitaoField = createTextField();
        contentPanel.add(capitaoField);

        JLabel portoOrigemLabel = createLabel("Porto de Origem:");
        contentPanel.add(portoOrigemLabel);
        portoOrigemField = createTextField();
        contentPanel.add(portoOrigemField);

        JLabel portoDestinoLabel = createLabel("Porto de Destino:");
        contentPanel.add(portoDestinoLabel);
        portoDestinoField = createTextField();
        contentPanel.add(portoDestinoField);

        JLabel paisLabel = createLabel("País:");
        contentPanel.add(paisLabel);
        paisField = createTextField();
        contentPanel.add(paisField);

        JLabel sentidoLabel = createLabel("Sentido:");
        contentPanel.add(sentidoLabel);
        JComboBox<String> sentidoComboBox = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        contentPanel.add(sentidoComboBox);

        JLabel tonelagemLabel = createLabel("Tonelagem:");
        contentPanel.add(tonelagemLabel);
        tonelagemField = createTextField();
        contentPanel.add(tonelagemField);

        JLabel comprimentoLabel = createLabel("Comprimento:");
        contentPanel.add(comprimentoLabel);
        comprimentoField = createTextField();
        contentPanel.add(comprimentoField);

        JLabel larguraLabel = createLabel("Largura:");
        contentPanel.add(larguraLabel);
        larguraField = createTextField();
        contentPanel.add(larguraField);

        JLabel passageirosLabel = createLabel("Número de Passageiros:");
        contentPanel.add(passageirosLabel);
        passageirosField = createTextField();
        contentPanel.add(passageirosField);

        // Botão de Adicionar
        JButton adicionarButton = createButton("Adicionar", e -> {
            try {
                String tipo = (String) tipoEmbarcacaoComboBox.getSelectedItem();
                String codigo = codigoField.getText();
                String capitao = capitaoField.getText();
                String portoOrigem = portoOrigemField.getText();
                String portoDestino = portoDestinoField.getText();
                String pais = paisField.getText();
                String sentido = (String) sentidoComboBox.getSelectedItem();

                Embarcacao embarcacao = null;
                if (tipo.equals("Navio de Carga")) {
                    double tonelagem = Double.parseDouble(tonelagemField.getText());
                    double comprimento = Double.parseDouble(comprimentoField.getText());
                    double largura = Double.parseDouble(larguraField.getText());
                    embarcacao = new NavioCarga(codigo, capitao, portoOrigem, portoDestino, pais, sentido, tonelagem, comprimento, largura);
                } else if (tipo.equals("Navio de Passageiros")) {
                    int passageiros = Integer.parseInt(passageirosField.getText());
                    double comprimento = Double.parseDouble(comprimentoField.getText());
                    double largura = Double.parseDouble(larguraField.getText());
                    embarcacao = new NavioPassageiros(codigo, capitao, portoOrigem, portoDestino, pais, sentido, passageiros, comprimento, largura);
                } else if (tipo.equals("Iate")) {
                    double comprimento = Double.parseDouble(comprimentoField.getText());
                    double largura = Double.parseDouble(larguraField.getText());
                    embarcacao = new Iate(codigo, capitao, portoOrigem, portoDestino, pais, sentido, comprimento, largura);
                }

                if (embarcacao != null) {
                    filaController.adicionarEmbarcacao(embarcacao);
                    outputArea.append(">>> Embarcação adicionada à fila!\n");
                    outputArea.append(">>> O valor da taxa a ser pago por esta embarcação é:" + embarcacao.calcularTaxa() + "\n");

                    adicionarFrame.dispose();
                } else {
                    outputArea.append(">>> Tipo de embarcação inválido!\n");
                }
            } catch (NumberFormatException ex) {
                outputArea.append(">>> Erro: Valor inválido para tonelagem, comprimento ou largura.\n");
            }
        });

        // Adiciona o botão ao final (ocupando 2 colunas)
        contentPanel.add(adicionarButton);

        adicionarFrame.setVisible(true);
    }

    private void registrarPagamento() {
        JFrame pagamentoFrame = new JFrame("Registrar Pagamento");
        pagamentoFrame.setSize(300, 200);
        pagamentoFrame.setLocationRelativeTo(frame);
        pagamentoFrame.setLayout(new GridLayout(0, 2, 10, 10));

        // Painel de fundo escuro
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
                    outputArea.append(">>> " + resultadoPagamento + "\n"); // Exibe a mensagem de erro
                }
            }
            pagamentoFrame.dispose();
        });
        contentPanel.add(pagarButton);

        pagamentoFrame.setVisible(true);
    }

    private void processarEmbarcacoes() {
        List<Embarcacao> embarcacoesParaProcessar = new ArrayList<>();
        Embarcacao prox = filaController.obterProximaEmbarcacao();
        while (prox != null) {
            if (prox.isPago()) { // Verifica se a embarcação está paga
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
            // Atualiza o progresso durante a operação
            new Thread(() -> {
                for (int i = 0; i <= 100; i++) {
                    atualizarProgresso(i);
                    try {
                        Thread.sleep(50); // Simula o tempo de operação
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            outputArea.append(">>> Nenhuma embarcação na fila ou não há espaço na eclusa!\n");
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
        SwingUtilities.invokeLater(() -> new SistemaGestaoEclusaSwing());
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