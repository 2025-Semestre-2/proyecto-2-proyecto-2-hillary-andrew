/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.componentes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author andrewdeni
 */
public class AlmacenamientoModelo1 implements Almacenamiento {
    
    private Conversor ConversorAsignado;
    private int CantidadMemoria;
    private Map<String, String> Memoria;
    private int DireccionEscritura;
    
    public AlmacenamientoModelo1(int cantidadMemoria, Conversor conversor, int direccionInicial){
        CantidadMemoria = cantidadMemoria;
        ConversorAsignado = conversor;
        String datoInicial = ConversorAsignado.ConvertirIntegerABits(0);
        Memoria = new LinkedHashMap<>();
        for(int direccion = 0; direccion < CantidadMemoria; direccion++){
            String direccionBinaria = ConversorAsignado.ConvertirIntegerABits(direccion);
            Memoria.put(direccionBinaria, datoInicial);
        }
        DireccionEscritura = direccionInicial;
    }

    @Override
    public String GuardarPrograma(List<String> programa) throws Exception {
        String direccionInicio = ConversorAsignado.ConvertirIntegerABits(DireccionEscritura);
        int tamanoPrograma = programa.size();
        String tamanoBits = ConversorAsignado.ConvertirIntegerABits(tamanoPrograma);
        DireccionEscritura++;
        Memoria.put(direccionInicio, tamanoBits);
        for(String instruccion : programa){
            String direccionBits = ConversorAsignado.ConvertirIntegerABits(DireccionEscritura);
            Memoria.put(direccionBits, instruccion);
            DireccionEscritura++;
        }
        return direccionInicio;
    }

    @Override
    public List<String> BuscarPrograma(String direccionInicial) throws Exception {
        String tamanoBits = Memoria.get(direccionInicial);
        int tamano = ConversorAsignado.ConvertirBitsAInteger(tamanoBits);
        int direccionEntera = ConversorAsignado.ConvertirBitsAInteger(direccionInicial) + 1;
        List<String> instrucciones = new ArrayList<>();
        for(int indice = direccionEntera; indice < tamano + direccionEntera; indice++){
            String direccionBusqueda = ConversorAsignado.ConvertirIntegerABits(indice);
            String instruccion = Memoria.get(direccionBusqueda);
            instrucciones.add(instruccion);
        }
        return instrucciones;
    }
    
    @Override
    public Map<String, String> TraerAlmacenamiento(){
        return Memoria;
    }
}
