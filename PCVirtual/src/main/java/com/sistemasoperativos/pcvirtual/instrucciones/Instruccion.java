/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.instrucciones;

import java.util.Map;

/**
 *
 * @author andre
 */
public interface Instruccion {
    public boolean EjecutarInstruccion(String instruccion, Map<String, String> registros) throws Exception;
}
