package org.example;

public class SensorNivelAgua {
    public boolean verificarNivelAdequado(String sentido, boolean nivelAlto) {
        boolean nivelAdequado;
        if (sentido.equalsIgnoreCase("ascendente")) {
            nivelAdequado = !nivelAlto;
        } else if (sentido.equalsIgnoreCase("descendente")) {
            nivelAdequado = nivelAlto;
        } else {
            nivelAdequado = false;
        }
        System.out.println(">>> Sensor de nível: " + (nivelAdequado ? "Nível adequado para sentido " + sentido : "Nível inadequado para sentido " + sentido));
        return nivelAdequado;
    }
}
