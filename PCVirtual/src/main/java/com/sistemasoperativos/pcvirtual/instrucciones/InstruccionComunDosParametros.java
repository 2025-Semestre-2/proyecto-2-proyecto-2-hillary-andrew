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
public class InstruccionComunDosParametros extends InstruccionComun{
    
    protected String Param2;

    public InstruccionComunDosParametros(Conversor conversor, int peso) {
        super(conversor, peso);
    }

    @Override
    protected void Desestructurar(String lineaBinaria) {
        Param1 = lineaBinaria.substring(5, 10);
        Param2 = lineaBinaria.substring(10);
    }
    
}
