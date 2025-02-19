package org.example.view;

import org.example.controller.FilaDeEsperaController;
import org.example.model.Embarcacao;
import org.example.model.Iate;
import org.example.model.NavioCarga;
import org.example.model.NavioPassageiros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdicionarEmbarcacaoView {
    private JFrame frame;
    private JTextField codigoField, capitaoField, portoOrigemField, portoDestinoField, paisField;
    private JTextField tonelagemField, comprimentoField, larguraField, passageirosField;
    private JComboBox<String> sentidoComboBox, tipoEmbarcacaoComboBox;
    private FilaDeEsperaController filaController;
    private JTextArea outputArea;

    private static final Color BUTTON_COLOR = new Color(54, 59, 73);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;

    public AdicionarEmbarcacaoView(FilaDeEsperaController filaController, JTextArea outputArea) {
        this.filaController = filaController;
        this.outputArea = outputArea;

        frame = new JFrame("Adicionar Embarcação");
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(0, 2, 10, 10));

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        panel.add(new JLabel("Tipo de Embarcação:"));
        tipoEmbarcacaoComboBox = new JComboBox<>(new String[]{"Navio de Carga", "Navio de Passageiros", "Iate"});
        panel.add(tipoEmbarcacaoComboBox);

        panel.add(new JLabel("Código da Embarcação:"));
        codigoField = new JTextField();
        panel.add(codigoField);

        panel.add(new JLabel("Nome do Capitão:"));
        capitaoField = new JTextField();
        panel.add(capitaoField);

        panel.add(new JLabel("Porto de Origem:"));
        portoOrigemField = new JTextField();
        panel.add(portoOrigemField);

        panel.add(new JLabel("Porto de Destino:"));
        portoDestinoField = new JTextField();
        panel.add(portoDestinoField);

        panel.add(new JLabel("País:"));
        paisField = new JTextField();
        panel.add(paisField);

        panel.add(new JLabel("Sentido:"));
        sentidoComboBox = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        panel.add(sentidoComboBox);

        panel.add(new JLabel("Tonelagem:"));
        tonelagemField = new JTextField();
        panel.add(tonelagemField);

        panel.add(new JLabel("Comprimento:"));
        comprimentoField = new JTextField();
        panel.add(comprimentoField);

        panel.add(new JLabel("Largura:"));
        larguraField = new JTextField();
        panel.add(larguraField);

        panel.add(new JLabel("Número de Passageiros:"));
        passageirosField = new JTextField();
        panel.add(passageirosField);

        JButton adicionarButton = createButton("Adicionar");
        adicionarButton.addActionListener(e -> adicionarEmbarcacao());

        panel.add(adicionarButton);
        frame.add(panel);
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
                frame.dispose(); // Fecha a janela após adicionar
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
