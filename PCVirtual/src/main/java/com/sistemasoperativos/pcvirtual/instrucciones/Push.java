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
public class Push extends InstruccionComunUnParametro implements Instruccion{
    
    BUS BUSAsignado;

    public Push(Conversor conversor, int peso, BUS bus) {
        super(conversor, peso);
        BUSAsignado = bus;
    }

    @Override
    public boolean EjecutarInstruccion(String instruccion, Map<String, String> registros) throws Exception {
        Registros = registros;
        if(AplicarPeso())
            return false;
        Desestructurar(instruccion);
        String dato = Registros.get(Param1);
        String direccionBits = Registros.get("01000");
        int direccion = ConversorAsignado.ConvertirBitsAInteger(direccionBits);
        String limiteBits = Registros.get("01010");
        int limite = ConversorAsignado.ConvertirBitsAInteger(limiteBits);
        if(limite < direccion)
            throw new Error("Se ha desbordado la pila");
        BUSAsignado.EscribirDatoRAM(direccionBits, dato);
        direccion++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccion);
        Registros.put("01000", direccionBits);
        return true;
    }
    
}
