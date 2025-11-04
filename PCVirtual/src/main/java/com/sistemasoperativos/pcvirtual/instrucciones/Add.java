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
public class Add extends InstruccionComunUnParametro implements Instruccion {

    public Add(Conversor conversor, int peso) {
        super(conversor, peso);
    }

    @Override
    public boolean EjecutarInstruccion(String instruccion, Map<String, String> registros) throws Exception {
        Registros = registros;
        if(AplicarPeso())
            return false;
        Desestructurar(instruccion);
        String param1 = Registros.get("00001");
        int num1 = ConversorAsignado.ConvertirBitsAInteger(param1);
        String param2 = Registros.get(Param1);
        int num2 = ConversorAsignado.ConvertirBitsAInteger(param2);
        int resultado = num1 + num2;
        String resultadoBinario = ConversorAsignado.ConvertirIntegerABits(resultado);
        Registros.put("00001", resultadoBinario);
        return true;
    }
    
    
}
