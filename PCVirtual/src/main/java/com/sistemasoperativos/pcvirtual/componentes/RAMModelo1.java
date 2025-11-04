/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.componentes;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Implementación del modelo 1 de la Memoria de Acceso Aleatorio (RAM) de la computadora virtual.
 *
 * @author Andrew López Herrera
 */
public class RAMModelo1 implements RAM{
    private Map<String, String> Memoria;
    private Integer CantidadMemoria;
    private Conversor ConversorUtilizado;
    
    /**
     * Constructor de la RAMModelo1.
     *
     * @param cantidadMemoria La cantidad de memoria que tendrá la RAM.
     */
    public RAMModelo1(Integer cantidadMemoria){
        ConversorUtilizado = new Conversor();
        CantidadMemoria = cantidadMemoria;
        Memoria = new LinkedHashMap();
        String datoInicial = ConversorUtilizado.ConvertirIntegerABits(0);
        for(int memoriaAsignada = 0; memoriaAsignada < CantidadMemoria; memoriaAsignada++){
            String direccion = ConversorUtilizado.ConvertirIntegerABits(memoriaAsignada);
            Memoria.put(direccion, datoInicial);
        }
    }
    
    /**
     * Escribe un dato en la dirección especificada de la memoria.
     *
     * @param direccion La dirección en la que se escribirá el dato.
     * @param dato El dato que se escribirá en la memoria.
     * @return true si la operación fue exitosa.
     * @throws Exception Si ocurre un error al escribir el dato.
     */
    @Override
    public Boolean EscribirDato(String direccion, String dato) throws Exception{
        int direccionDecimal = ConversorUtilizado.ConvertirBitsAInteger(direccion);
        if(!Memoria.containsKey(direccion))
            throw new Exception("No existe la dirección: " + direccionDecimal + " | " + direccion);
        if(direccionDecimal < CantidadMemoria && direccionDecimal > 0){
            Memoria.put(direccion, dato);
            return true;
        }
        return false;
    }
    
    /**
     * Lee un dato de la dirección especificada de la memoria.
     *
     * @param direccion La dirección desde la que se leerá el dato.
     * @return El dato leído de la memoria.
     * @throws Exception Si ocurre un error al leer el dato.
     */
    @Override
    public String LeerDato(String direccion) throws Exception{
        int direccionDecimal = ConversorUtilizado.ConvertirBitsAInteger(direccion);
        if(!Memoria.containsKey(direccion))
            throw new Exception("No existe la dirección: " + direccionDecimal + " | " + direccion);
        String dato = Memoria.get(direccion);
        return dato;
    }
    
    @Override
    public Map<String, String> TraerMemoriaRAM(){
        return Memoria;
    }
}
