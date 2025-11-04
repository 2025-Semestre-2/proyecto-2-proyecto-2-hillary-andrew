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
public class Pop extends InstruccionComunUnParametro implements Instruccion{

    BUS Bus;

    public Pop(Conversor conversor, int peso, BUS bus) {
        super(conversor, peso);
        Bus = bus;
    }

    @Override
    public boolean EjecutarInstruccion(String instruccion, Map<String, String> registros) throws Exception {
        Registros = registros;
        if(AplicarPeso())
            return false;
        Desestructurar(instruccion);
        String direccion = Registros.get("01000");
        int nuevaDireccion = ConversorAsignado.ConvertirBitsAInteger(direccion) - 1;
        String nuevaDireccionBits = ConversorAsignado.ConvertirIntegerABits(nuevaDireccion);
        Registros.put("01000", nuevaDireccionBits);
        String dato = Bus.LeerDatoRAM(nuevaDireccionBits);
        if(dato.length() != 16)
            throw new Exception("El pop está accediendo al código del programa y no a la sección de la pila");
        Registros.put(Param1, dato);
        return true;
    }
    
}
