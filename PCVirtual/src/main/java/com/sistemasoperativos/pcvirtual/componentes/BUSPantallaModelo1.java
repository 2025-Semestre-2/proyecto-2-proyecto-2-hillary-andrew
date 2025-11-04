/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.componentes;

import com.sistemasoperativos.pcvirtual.controlador.Controlador;

/**
 *
 * @author andre
 */
public class BUSPantallaModelo1 implements BUSPantalla{
    
    private Controlador ControladorAsignado;
    private Conversor ConversorAsignado;
    
    public BUSPantallaModelo1(Controlador controlador){
        ControladorAsignado = controlador;
        ConversorAsignado = new Conversor();
    }

    @Override
    public void Escribir(String dato) {
        int datoEntero = ConversorAsignado.ConvertirBitsAInteger(dato);
        dato = Integer.toString(datoEntero);
        ControladorAsignado.Escribir(dato);
    }
    
}
