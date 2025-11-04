/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.componentes;

import java.util.Map;

/**
 * Interfaz para la Unidad Central de Procesamiento (CPU) de la computadora virtual.
 *
 * @author Andrew López Herrera
 */
public interface CPU {
    public void EjecutarInstruccion() throws Exception;
    public void CambiarRegistros(Map<String, String> registros);
    public Map<String, String> ObtenerRegistros();
    public void AsignarBUS(BUS2 busAsignado) throws Exception;
    
}
