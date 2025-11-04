/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.instrucciones;

import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import java.util.Map;

/**
 *
 * @author andre
 */
public class Swap extends InstruccionComunDosParametros implements Instruccion{

    public Swap(Conversor conversor, int peso) {
        super(conversor, peso);
    }

    @Override
    public boolean EjecutarInstruccion(String instruccion, Map<String, String> registros) throws Exception {
        Registros = registros;
        if(AplicarPeso())
            return false;
        Desestructurar(instruccion);
        String dato1 = Registros.get(Param1);
        String dato2 = Registros.get(Param2);
        Registros.put(Param1, dato2);
        Registros.put(Param2, dato1);
        return true;
    }
    
}
