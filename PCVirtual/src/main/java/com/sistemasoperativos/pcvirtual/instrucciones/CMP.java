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
public class CMP extends InstruccionComunDosParametros implements Instruccion{

    public CMP(Conversor conversor, int peso) {
        super(conversor, peso);
    }

    @Override
    public boolean EjecutarInstruccion(String instruccion, Map<String, String> registros) throws Exception {
        Registros = registros;
        if(AplicarPeso())
            return false;
        Desestructurar(instruccion);
        String numBits1 = registros.get(Param1);
        String numBits2 = registros.get(Param2);
        int num1 = ConversorAsignado.ConvertirBitsAInteger(numBits1);
        int num2 = ConversorAsignado.ConvertirBitsAInteger(numBits2);
        System.out.println(num1 + " " + num2);
        if(num1 == num2){
            String bandera = ConversorAsignado.ConvertirIntegerABits(1);
            registros.put("00111", bandera);
        }
        else{
            String bandera = ConversorAsignado.ConvertirIntegerABits(0);
            registros.put("00111", bandera);
        }
        return true;
    }
    
}
