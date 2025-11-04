/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.componentes;

/**
 * Clase para convertir entre representaciones binarias y enteros.
 *
 * @author Andrew López Herrera
 */
public class Conversor {
    /**
     * Constructor de la clase Conversor.
     */
    public Conversor(){
        
    }

    /**
     * Convierte una representación binaria en un entero. El primer bit indica el signo (0 para negativo, 1 para positivo).
     *
     * @param bits La representación binaria.
     * @return El entero resultante.
     */
    public Integer ConvertirBitsAInteger(String bits){
        String bitsSinSigno = bits.substring(1);
        Integer numero = Integer.parseInt(bitsSinSigno, 2);
        if(bits.charAt(0) == '0')
            numero *= -1;
        return numero;
    }

    /**
     * Convierte un entero en una representación binaria. El primer bit indica el signo (0 para negativo, 1 para positivo).
     *
     * @param numero El entero a convertir.
     * @return La representación binaria del entero.
     */
    public String ConvertirIntegerABits(Integer numero){
        String bits = "";
        if(numero > 0)
            bits += "1";
        else{
            bits += "0";
            numero *= -1;
        }
        bits += String.format("%15s", Integer.toBinaryString(numero)).replace(' ', '0');
        return bits;
    }
}
