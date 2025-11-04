/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.componentes;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el BUS de la computadora virtual.
 *
 * @author Andrew López Herrera
 */
public interface BUS {
    public void EscribirDatoRAM(String direccion, String dato) throws Exception;
    public String LeerDatoRAM(String direccion) throws Exception;
    public List<Map<String, String>> ObtenerRegistrosCPU() throws Exception;
    public void EjecutarInstruccionCPU1() throws Exception;
    public void EjecutarInstruccionCPU2() throws Exception;
    public void EjecutarInstruccionCPU3() throws Exception;
    public void EjecutarInstruccionCPU4() throws Exception;
    public void AsignarRAM(RAM ram);
    public void AsignarCPU(CPU cpu);
}
