package org.example;

public class PagamentoController {
    private Pagamento pagamento;


    public PagamentoController(Pagamento pagamento) {
        this.pagamento = pagamento;
    }


    public String registrarPagamento(Embarcacao embarcacao, double valor) {
        double taxa = embarcacao.calcularTaxa(); // Supondo que a embarcação tem um método para calcular a taxa
        if (valor < taxa) {
            return "Pagamento insuficiente. Faltam: " + (taxa - valor);
        }
        boolean sucesso = pagamento.registrarPagamento(embarcacao.getCodigoIdentificacao(), valor, taxa);
        if (sucesso) {
            embarcacao.marcarComoPago(); // Marca a embarcação como paga
            return "Sucesso: Pagamento registrado.";
        } else {
            return "Erro: Não foi possível registrar o pagamento.";
        }
    }

}
