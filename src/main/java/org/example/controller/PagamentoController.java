package org.example.controller;

import org.example.model.Embarcacao;
import org.example.model.Pagamento;

public class PagamentoController {
    private Pagamento pagamento;


    public PagamentoController(Pagamento pagamento) {
        this.pagamento = pagamento;
    }


    public String registrarPagamento(Embarcacao embarcacao, double valor) {
        double taxa = embarcacao.calcularTaxa();
        if (valor < taxa) {
            return "Pagamento insuficiente. Faltam: " + (taxa - valor);
        }
        boolean sucesso = pagamento.registrarPagamento(embarcacao.getCodigoIdentificacao(), valor, taxa);
        if (sucesso) {
            embarcacao.marcarComoPago();
            return "Sucesso: Pagamento registrado.";
        } else {
            return "Erro: Não foi possível registrar o pagamento.";
        }
    }
    public double getTroco() { return pagamento.getTroco(); }
}
