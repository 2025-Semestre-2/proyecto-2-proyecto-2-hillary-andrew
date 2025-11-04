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
 * Implementación del BUS de la computadora virtual.
 *
 * @author Andrew López Herrera
 */
public class BUSModelo1 implements BUS {
    protected RAM RAMInstalada;
    List<CPU> CPUs;

    public BUSModelo1() {
        CPUs = new ArrayList<>();
    }

    /**
     * Escribe un dato en la RAM en la dirección especificada.
     *
     * @param direccion La dirección en la RAM donde se escribirá el dato.
     * @param dato El dato a escribir en la RAM.
     * @throws Exception Si la RAM no está disponible o si ocurre un error al escribir el dato.
     */
    @Override
    public void EscribirDatoRAM(String direccion, String dato) throws Exception {
        if (RAMInstalada == null){
            throw new Exception("RAM no está disponible");
        }
        RAMInstalada.EscribirDato(direccion, dato);
    }

    /**
     * Lee un dato de la RAM en la dirección especificada.
     *
     * @param direccion La dirección en la RAM desde donde se leerá el dato.
     * @return El dato leído de la RAM.
     * @throws Exception Si la RAM no está disponible o si ocurre un error al leer el dato.
     */
    @Override
    public String LeerDatoRAM(String direccion) throws Exception {
        if (RAMInstalada == null){
            throw new Exception("RAM no está disponible");
        }
        return RAMInstalada.LeerDato(direccion);
    }

    /**
     * Lee un dato de la CPU.
     *
     * @param informacion La información a leer de la CPU.
     * @return El dato leído de la CPU.
     * @throws Exception Si la CPU no está disponible o si ocurre un error al leer el dato.
     */
    @Override
    public List<Map<String, String>> ObtenerRegistrosCPU() throws Exception {
        List<Map<String, String>> registrosCPUs = new ArrayList<>();
        for(CPU cpu : CPUs){
            Map<String, String> registros = cpu.ObtenerRegistros();
            Map<String, String> copia = new LinkedHashMap<>();
            for(Map.Entry<String, String> registro : registros.entrySet()){
                String nombre = registro.getKey();
                String valor = registro.getValue();
                copia.put(nombre, valor);
            }
            registrosCPUs.add(copia);
        }
        return registrosCPUs;
    }

    /**
     * Ejecuta una instrucción en la CPU.
     *
     * @throws Exception Si la CPU no está disponible o si ocurre un error al ejecutar la instrucción.
     */
    @Override
    public void EjecutarInstruccionCPU1() throws Exception{
        CPUs.get(0).EjecutarInstruccion();
    }
    
    @Override
    public void EjecutarInstruccionCPU2() throws Exception{
        CPUs.get(1).EjecutarInstruccion();
    }
    
    @Override
    public void EjecutarInstruccionCPU3() throws Exception{
        CPUs.get(2).EjecutarInstruccion();
    }
    
    @Override
    public void EjecutarInstruccionCPU4() throws Exception{
        CPUs.get(3).EjecutarInstruccion();
    }
    
    @Override
    public void AsignarRAM(RAM ram){
        RAMInstalada = ram;
    }
    
    @Override
    public void AsignarCPU(CPU cpu){
        CPUs.add(cpu);
    }
}
