/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.gui;

import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author andrewdeni
 */
public class ConversorMemoria {
    static Conversor ConversorAsignado = new Conversor();
    
    static public Map<String, String> ConvertirACristiano(Map<String, String> MemoriaBinaria){
        Map<String, String> mapaCristiano = new LinkedHashMap<>();
        for(Map.Entry<String, String> posicion : MemoriaBinaria.entrySet()){
            String direccionBinaria = posicion.getKey();
            Integer direccion = ConversorAsignado.ConvertirBitsAInteger(direccionBinaria);
            String valorBinario = posicion.getValue();
            String valor = DesensambladorModelo1.ConvertirInstruccion(valorBinario);
            mapaCristiano.put(direccion.toString(), valor);
        }
        return mapaCristiano;
    }
}