/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.compilador;

import com.sistemasoperativos.pcvirtual.componentes.BUS2;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author andre
 */
public class AdmnistradorProgramasNuevos {
    private LinkedList<String> NombreProgramas;
    private LinkedList<String> DireccionesProgramas;
    private BUS2 BUSAsignado;
    private Compilador CompiladorAsignado;

    public AdmnistradorProgramasNuevos(LinkedList NombreProgramas, LinkedList DireccionesProgramas, BUS2 BUSAsignado) {
        this.NombreProgramas = NombreProgramas;
        this.DireccionesProgramas = DireccionesProgramas;
        this.BUSAsignado = BUSAsignado;
        CompiladorAsignado = new CompiladorModelo1();
    }
    
    public void CargarPrograma(File archivo) throws Exception{
        String nombre = archivo.getName();
        List<String> programa = CompiladorAsignado.Compilar(archivo);
        System.out.println(programa.toString());
        String direccion = BUSAsignado.EscribirAlmacenamiento(programa);
        NombreProgramas.addFirst(nombre);
        DireccionesProgramas.addFirst(direccion);
    }
}
