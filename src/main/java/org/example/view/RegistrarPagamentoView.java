package org.example.view;

import org.example.controller.FilaDeEsperaController;
import org.example.controller.PagamentoController;
import org.example.model.Embarcacao;

import javax.swing.*;
import java.awt.*;

public class RegistrarPagamentoView {
    private JFrame frame;
    private JTextField codigoPagamentoField, valorPagamentoField;
    private PagamentoController pagamentoController;
    private FilaDeEsperaController filaController;
    private JTextArea outputArea;

    public RegistrarPagamentoView(PagamentoController pagamentoController, FilaDeEsperaController filaController, JTextArea outputArea) {
        this.pagamentoController = pagamentoController;
        this.filaController = filaController;
        this.outputArea = outputArea;

        // Configura o frame
        frame = new JFrame("Registrar Pagamento");
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Painel principal com BorderLayout e margem
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(38, 42, 54));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de formulário com GridLayout para os labels e campos de texto
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(new Color(38, 42, 54));

        formPanel.add(createLabel("Código da Embarcação:"));
        codigoPagamentoField = createTextField();
        formPanel.add(codigoPagamentoField);

        formPanel.add(createLabel("Valor do Pagamento:"));
        valorPagamentoField = createTextField();
        formPanel.add(valorPagamentoField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Painel para o botão, centralizando-o
        JButton pagarButton = createButton("Pagar");
        pagarButton.addActionListener(e -> registrarPagamento());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(38, 42, 54));
        buttonPanel.add(pagarButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private void registrarPagamento() {
        try {
            String codigo = codigoPagamentoField.getText();
            double valor = Double.parseDouble(valorPagamentoField.getText());

            var barcoParaPagar = filaController.buscarEmbarcacao(codigo);
            if (barcoParaPagar == null) {
                outputArea.append(">>> Embarcação não encontrada na fila ou já processada.\n");
            } else {
                String resultadoPagamento = pagamentoController.registrarPagamento(barcoParaPagar, valor);
                outputArea.append(">>> " + resultadoPagamento + "\n");

                if (resultadoPagamento.startsWith("Sucesso")) {
                    outputArea.append(">>> Pagamento efetuado para a embarcação " + codigo + "\n");
                    frame.dispose();
                }
            }
        } catch (NumberFormatException ex) {
            outputArea.append(">>> Erro: Valor de pagamento inválido.\n");
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(60, 64, 78));
        field.setForeground(Color.WHITE);
        return field;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(54, 59, 73));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }
}