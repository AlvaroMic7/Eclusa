package org.example;

import org.example.controller.EclusaController;
import org.example.controller.FilaDeEsperaController;
import org.example.controller.PagamentoController;
import org.example.model.Eclusa;
import org.example.model.FilaDeEspera;
import org.example.model.Pagamento;
import org.example.view.SistemaGestaoEclusaView;

public class SistemaGestaoEclusa {
    public static void main(String[] args) {
        SistemaGestaoEclusaView sistema = new SistemaGestaoEclusaView();
        Eclusa eclusa = new Eclusa("Grande", 5000, 1000, 100, 10, 150, 20, sistema);
        FilaDeEspera filaDeEspera = new FilaDeEspera();
        Pagamento pagamento = new Pagamento();


        EclusaController eclusaController = new EclusaController(eclusa);
        FilaDeEsperaController filaController = new FilaDeEsperaController(filaDeEspera);
        PagamentoController pagamentoController = new PagamentoController(pagamento);



    }
}