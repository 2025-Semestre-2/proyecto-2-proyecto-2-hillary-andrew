/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.procesos;

import algoritmos.Algoritmo;
import java.util.List;
import java.util.Map;

/**
 *
 * @author males
 */


public class GestorProcesos {
    private final Algoritmo Planificador;

    public GestorProcesos(Algoritmo planificador) {
        this.Planificador = planificador;
    }
    
    public void Ejecutar(){
        Planificador.IniciarEjecucion();
    }

    public List<Map<String, String>> ObtenerBCPs() {
        return Planificador.ObtenerBCPs();
    }
}

