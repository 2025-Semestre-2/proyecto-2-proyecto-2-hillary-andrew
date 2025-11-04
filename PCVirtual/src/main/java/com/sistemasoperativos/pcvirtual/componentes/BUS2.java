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
public interface BUS2 extends BUS {
    public String EscribirAlmacenamiento(List<String> programa) throws Exception; //Se espera la dirección en la que se guardó el programa
    public List<String> LeerAlmacenamiento(String direccion) throws Exception; //Se espera el programa guardado en el almacenamiento.
    public Map<String, String> TraerMemoriaRAM();
    public Map<String, String> TraerAlmacenamiento();
}
