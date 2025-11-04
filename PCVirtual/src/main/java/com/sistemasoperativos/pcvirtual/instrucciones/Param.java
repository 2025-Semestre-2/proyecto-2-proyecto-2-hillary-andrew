/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.instrucciones;

import com.sistemasoperativos.pcvirtual.componentes.BUS;
import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author andre
 */
public class Param extends InstruccionComunDosParametros implements Instruccion{

    private BUS BUSAsignado;
    private List<String> Parametros;
    private String Param3;
    
    public Param(Conversor conversor, int peso, BUS bus) {
        super(conversor, peso);
        BUSAsignado = bus;
        Parametros = new ArrayList<>();
    }

    @Override
    public boolean EjecutarInstruccion(String instruccion, Map<String, String> registros) throws Exception {
        Registros = registros;
        if(AplicarPeso())
            return false;
        Desestructurar(instruccion);
        String direccionBits = Registros.get("01000");
        int direccion = ConversorAsignado.ConvertirBitsAInteger(direccionBits);
        String limiteBits = Registros.get("01010");
        int limite = ConversorAsignado.ConvertirBitsAInteger(limiteBits);
        for(String parametro : Parametros){
            if(direccion > limite)
                throw new Error("Se ha desbordado la pila");
            BUSAsignado.EscribirDatoRAM(direccionBits, parametro);
            direccion++;
            direccionBits = ConversorAsignado.ConvertirIntegerABits(direccion);
        }
        Registros.put("01000", direccionBits);
        return true;
    }
    
    @Override
    protected void Desestructurar(String instruccion){
        Parametros.clear();
        Param1 = "";
        Param2 = "";
        Param3 = "";
        Param1 = instruccion.substring(5, 21);
        Parametros.add(Param1);
        if(instruccion.length() < 37)
            return;
        Param2 = instruccion.substring(21, 37);
        Parametros.add(Param2);
        if(instruccion.length() < 53)
            return;
        Param3 = instruccion.substring(37, 53);
        Parametros.add(Param3);
    }
}
