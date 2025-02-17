package org.example;

import java.util.ArrayList;
import java.util.List;

public class RegistroEventos {
    private List<String> eventos = new ArrayList<>();


    public List<String> getEventos() {
        return eventos;
    }


    public void registrarEvento(String evento) {
        eventos.add(evento);
        System.out.println(">>> Evento registrado: " + evento);
    }


    public void exibirEventos() {
        System.out.println("\n========== Registro de Eventos ==========");
        for (String e : eventos) {
            System.out.println(e);
        }
        System.out.println("===========================================");
    }
}
