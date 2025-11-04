/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.componentes;

import java.util.List;
import java.util.Map;

/**
 *
 * @author andrewdeni
 */
public interface Almacenamiento {
    public String GuardarPrograma(List<String> programa) throws Exception; //Devuelve la dirección de guardado;
    public List<String> BuscarPrograma(String direccion) throws Exception; //Devuelve el programa en forma de lista de instrucciones binarias
    public Map<String, String> TraerAlmacenamiento();
}
