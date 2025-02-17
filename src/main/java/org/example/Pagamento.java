package org.example;

import java.util.HashMap;
import java.util.Map;

public class Pagamento {
    private double totalApuradoDia;
    private double totalApuradoMes;
    private double totalApuradoAno;
    private double troco;
    private Map<String, Double> paymentRecords = new HashMap<>();


    public boolean registrarPagamento(String codigo, double valor, double taxa) {
        if (valor < taxa) {
            return false; // Pagamento insuficiente
        }
        troco = valor - taxa;

        totalApuradoDia += taxa;
        totalApuradoMes += taxa;
        totalApuradoAno += taxa;
        paymentRecords.put(codigo, taxa);
        return true; // Pagamento registrado com sucesso
    }



    public double getTroco() {
        return troco;
    }

    public double getTotalApuradoDia() {
        return totalApuradoDia;
    }
    public double getTotalApuradoMes() {
        return totalApuradoMes;
    }
    public double getTotalApuradoAno() {
        return totalApuradoAno;
    }
}


