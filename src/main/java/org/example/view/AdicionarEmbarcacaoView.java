package org.example.view;

import org.example.controller.FilaDeEsperaController;
import org.example.model.Embarcacao;
import org.example.model.Iate;
import org.example.model.NavioCarga;
import org.example.model.NavioPassageiros;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdicionarEmbarcacaoView {
    private JFrame frame;
    private JTextField codigoField, capitaoField, portoOrigemField, portoDestinoField, paisField;
    private JTextField tonelagemField, comprimentoField, larguraField, passageirosField;
    private JComboBox<String> sentidoComboBox, tipoEmbarcacaoComboBox;
    private FilaDeEsperaController filaController;
    private JTextArea outputArea;

    private static final Color BUTTON_COLOR = new Color(54, 59, 73);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = new Color(38, 42, 54);

    public AdicionarEmbarcacaoView(FilaDeEsperaController filaController, JTextArea outputArea) {
        this.filaController = filaController;
        this.outputArea = outputArea;

        frame = new JFrame("Adicionar Embarcação");
        frame.setSize(450, 550);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Tipo de Embarcação:"), gbc);
        gbc.gridx = 1;
        tipoEmbarcacaoComboBox = new JComboBox<>(new String[]{"Navio de Carga", "Navio de Passageiros", "Iate"});
        formPanel.add(tipoEmbarcacaoComboBox, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Código da Embarcação:"), gbc);
        gbc.gridx = 1;
        codigoField = new JTextField();
        formPanel.add(codigoField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Nome do Capitão:"), gbc);
        gbc.gridx = 1;
        capitaoField = new JTextField();
        formPanel.add(capitaoField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Porto de Origem:"), gbc);
        gbc.gridx = 1;
        portoOrigemField = new JTextField();
        formPanel.add(portoOrigemField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Porto de Destino:"), gbc);
        gbc.gridx = 1;
        portoDestinoField = new JTextField();
        formPanel.add(portoDestinoField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("País:"), gbc);
        gbc.gridx = 1;
        paisField = new JTextField();
        formPanel.add(paisField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Sentido:"), gbc);
        gbc.gridx = 1;
        sentidoComboBox = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        formPanel.add(sentidoComboBox, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Tonelagem:"), gbc);
        gbc.gridx = 1;
        tonelagemField = new JTextField();
        formPanel.add(tonelagemField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Comprimento:"), gbc);
        gbc.gridx = 1;
        comprimentoField = new JTextField();
        formPanel.add(comprimentoField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Largura:"), gbc);
        gbc.gridx = 1;
        larguraField = new JTextField();
        formPanel.add(larguraField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Número de Passageiros:"), gbc);
        gbc.gridx = 1;
        passageirosField = new JTextField();
        formPanel.add(passageirosField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JButton adicionarButton = createButton("Adicionar");
        adicionarButton.addActionListener(e -> adicionarEmbarcacao());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(adicionarButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private void adicionarEmbarcacao() {
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
                outputArea.append(">>> O valor da taxa a ser pago por esta embarcação é: " + embarcacao.calcularTaxa() + "\n");
                frame.dispose();
            } else {
                outputArea.append(">>> Tipo de embarcação inválido!\n");
            }
        } catch (NumberFormatException ex) {
            outputArea.append(">>> Erro: Valor inválido para tonelagem, comprimento ou largura.\n");
        }
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }
}