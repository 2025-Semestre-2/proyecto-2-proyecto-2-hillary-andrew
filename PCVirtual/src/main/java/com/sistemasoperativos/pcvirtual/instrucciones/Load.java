/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.instrucciones;

import com.sistemasoperativos.pcvirtual.componentes.BUS;
import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import java.util.Map;

/**
 *
 * @author andre
 */
public class Load extends InstruccionComunUnParametro implements Instruccion{
    
    BUS Bus;

    public Load(Conversor conversor, int peso, BUS bus) {
        super(conversor, peso);
        Bus = bus;
    }

    @Override
    public boolean EjecutarInstruccion(String instruccion, Map<String, String> registros) throws Exception {
        Registros = registros;
        if(AplicarPeso())
            return false;
        Desestructurar(instruccion);
        String dato = Registros.get(Param1);
        Registros.put("00001", dato);
        return true;
    }
    
}
