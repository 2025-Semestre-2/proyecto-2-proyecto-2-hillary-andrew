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
public abstract class InstruccionComun {
    protected Map<String, String> Registros;
    protected Conversor ConversorAsignado;
    protected String Param1;
    private int Peso;
    private int Contador;
    
    public InstruccionComun(Conversor conversor, int peso){
        Registros = null;
        ConversorAsignado = conversor;
        Peso = peso;
        Contador = 0;
    }
    
    protected boolean AplicarPeso(){
        Contador++;
        if(Contador < Peso){
            return true;
        }
        Contador = 0;
        return false;
    }
    
    protected abstract void Desestructurar(String lineaBinaria);
    
    public int ObtenerPeso(){
        return Peso;
    }
}
