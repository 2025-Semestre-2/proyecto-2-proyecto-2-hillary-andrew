/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.componentes;

import com.sistemasoperativos.pcvirtual.instrucciones.Instruccion;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author andre
 */
public class CPUModelo2 implements CPU{
    
    private Map<String, Instruccion> Instrucciones;
    
    /**
     * PC = 00000
     * AC = 00001
     * IR = 00010
     * AX = 00011
     * BX = 00100
     * CX = 00101
     * DX = 00110
     * CP = 00111
     * SP = 01000
     * BS = 01001
     * LI = 01010
     */
    private Map<String, String> Registros;
    private BUS2 BUSAsignado;
    private Conversor ConversorAsignado;
    
    public CPUModelo2(Map<String, Instruccion> instrucciones, Map<String, String> registros){
        Instrucciones = instrucciones;
        Registros = crearRegistrosPorDefecto();
        ConversorAsignado = new Conversor();
    }
    
    private Map<String, String> crearRegistrosPorDefecto() {
        Map<String, String> registros = new LinkedHashMap<>();
        registros.put("00000", "0000000000000000"); // Contador de programa
        registros.put("00001", "0000000000000000"); // Acumulador
        registros.put("00010", "0000000000000000"); // Registro de instrucción
        registros.put("00011", "0000000000000000");
        registros.put("00100", "0000000000000000");
        registros.put("00101", "0000000000000000");
        registros.put("00110", "0000000000000000");
        registros.put("00111", "0000000000000000"); // Contador de pila
        registros.put("01000", "0000000000000000"); // Stack pointer
        registros.put("01001", "0000000000000000");
        registros.put("01010", "0000000000000000");
        return registros;
    }
    
    @Override
    public void EjecutarInstruccion() throws Exception {
        String instruccionBits = Registros.get("00000");
        if(instruccionBits.equals("0000000000000000")){
            return;
        }
        if(!Instrucciones.containsKey(instruccionBits.substring(0, 5))){
            crearRegistrosPorDefecto();
            throw new Exception("La instrucción " + instruccionBits + " no existe");
        }
        Instruccion instruccion = Instrucciones.get(instruccionBits.substring(0, 5));
        boolean resultado = false;
        try{
            resultado = instruccion.EjecutarInstruccion(instruccionBits, Registros);
        }
        catch(Exception e){
            crearRegistrosPorDefecto();
            throw new Exception(e.getMessage());
        }
        IrSiguienteInstruccion(resultado);
    }
    
    private void IrSiguienteInstruccion(boolean resultado) throws Exception{
        if(resultado){
            String direccionActual = Registros.get("00000");
            String nuevaInstruccion = BUSAsignado.LeerDatoRAM(direccionActual);
            Registros.put("00010", nuevaInstruccion);
            int nuevaDireccionEntera = ConversorAsignado.ConvertirBitsAInteger(direccionActual) + 1;
            String nuevaDireccionBits = ConversorAsignado.ConvertirIntegerABits(nuevaDireccionEntera);
            Registros.put("00000", nuevaDireccionBits);
        }
    }
    
    @Override
    public void CambiarRegistros(Map<String, String> registros) {
        Registros = registros;
    }
    
    @Override
    public Map<String, String> ObtenerRegistros(){
        return Registros;
    }
    
    @Override
    public void AsignarBUS(BUS2 busAsignado) throws Exception{
        BUSAsignado = busAsignado;
    }
}
