package org.example;

import org.example.Eclusa;
import org.example.Embarcacao;
import org.example.FilaDeEspera;
import org.example.Pagamento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SistemaGestaoEclusa {
    public static void main(String[] args) {
        SistemaGestaoEclusaSwing sistema = new SistemaGestaoEclusaSwing();
        Eclusa eclusa = new Eclusa("Grande", 5000, 1000, 100, 10, 150, 20, sistema);
        FilaDeEspera filaDeEspera = new FilaDeEspera();
        Pagamento pagamento = new Pagamento();


        EclusaController eclusaController = new EclusaController(eclusa);
        FilaDeEsperaController filaController = new FilaDeEsperaController(filaDeEspera);
        PagamentoController pagamentoController = new PagamentoController(pagamento);



    }
}