/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.gui;

import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import static com.sistemasoperativos.pcvirtual.gui.ConversorMemoria.ConversorAsignado;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author andrewdeni
 */
public class ConversorRegistros {
    static Conversor ConversorAsignado = new Conversor();
    
    static public Map<String, String> ConvertirACristiano(Map<String, String> registros){
        Map<String, String> mapaCristiano = new LinkedHashMap<>();
        System.out.println(registros.toString());
        for(Map.Entry<String, String> registro : registros.entrySet()){
            String nombre = ConvertirRegistros(registro.getKey());
            String instruccionBinaria = registro.getValue();
            String instruccion = DesensambladorModelo1.ConvertirInstruccion(instruccionBinaria);
            mapaCristiano.put(nombre, instruccion);
        }
        return mapaCristiano;
    }
    
    static private String ConvertirRegistros(String registro){
        switch(registro){
            case "00000":
                return "PC";
            case "00001":
                return "AC";
            case "00010":
                return "IR";
            case "00011":
                return "AX";
            case "00100":
                return "BX";
            case "00101":
                return "CX";
            case "00110":
                return "DX";
            case "00111":
                return "CP";
            case "01000":
                return "SP";
            case "01001":
                return "BS";
            case "01010":
                return "LI";
        }
        return "null";
    }
}
