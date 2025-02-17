package org.example;

public class SensorNivelAgua {
    public boolean verificarNivelAdequado(String sentido, boolean nivelAlto) {
        boolean nivelAdequado;
        if (sentido.equalsIgnoreCase("ascendente")) {
            // Para o sentido ascendente, a eclusa deve estar vazia (nível baixo) ao entrar
            nivelAdequado = !nivelAlto;
        } else if (sentido.equalsIgnoreCase("descendente")) {
            // Para o sentido descendente, a eclusa deve estar cheia (nível alto) ao entrar
            nivelAdequado = nivelAlto;
        } else {
            // Sentido inválido
            nivelAdequado = false;
        }
        System.out.println(">>> Sensor de nível: " + (nivelAdequado ? "Nível adequado para sentido " + sentido : "Nível inadequado para sentido " + sentido));
        return nivelAdequado;
    }
}
