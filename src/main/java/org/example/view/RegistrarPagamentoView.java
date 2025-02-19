package org.example.view;

import org.example.controller.FilaDeEsperaController;
import org.example.controller.PagamentoController;
import org.example.model.Embarcacao;
import org.example.model.Pagamento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        frame = new JFrame("Registrar Pagamento");
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(0, 2, 10, 10));

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBackground(new Color(38, 42, 54));

        panel.add(createLabel("Código da Embarcação:"));
        codigoPagamentoField = createTextField();
        panel.add(codigoPagamentoField);

        panel.add(createLabel("Valor do Pagamento:"));
        valorPagamentoField = createTextField();
        panel.add(valorPagamentoField);

        JButton pagarButton = createButton("Pagar");
        pagarButton.addActionListener(e -> registrarPagamento());
        pagarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPagamento();
            }
        });

        panel.add(pagarButton);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void registrarPagamento() {
        try {
            String codigo = codigoPagamentoField.getText();
            double valor = Double.parseDouble(valorPagamentoField.getText());

            Embarcacao barcoParaPagar = filaController.buscarEmbarcacao(codigo);
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
