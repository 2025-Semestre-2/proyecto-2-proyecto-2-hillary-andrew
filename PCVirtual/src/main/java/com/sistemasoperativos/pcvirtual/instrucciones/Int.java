/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.instrucciones;

import com.sistemasoperativos.pcvirtual.componentes.BUS2;
import com.sistemasoperativos.pcvirtual.componentes.BUSPantalla;
import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import java.util.Map;

/**
 *
 * @author andre
 */
public class Int extends InstruccionComunUnParametro implements Instruccion{
    
    BUSPantalla BusPantalla;
    BUS2 BUSAsignado;

    public Int(Conversor conversor, int peso,BUSPantalla busPantalla, BUS2 bus) {
        super(conversor, peso);
        BusPantalla = busPantalla;
    }

    @Override
    public boolean EjecutarInstruccion(String instruccion, Map<String, String> registros) throws Exception {
        Registros = registros;
        if(AplicarPeso())
            return false;
        Desestructurar(instruccion);
        switch (Param1) {
            case "00001":
                return EjecutarINT10H();
            case "00010":
                return EjecutarINT20H();
            default:
                return true;
        }
    }
    
    private boolean EjecutarINT10H() throws Exception{
        String dato = Registros.get("00110");
        BusPantalla.Escribir(dato);
        return true;
    }
    
    private boolean EjecutarINT20H() throws Exception{
        Registros.put("00000", "0000000000000000");
        return false;
    }
    
}
