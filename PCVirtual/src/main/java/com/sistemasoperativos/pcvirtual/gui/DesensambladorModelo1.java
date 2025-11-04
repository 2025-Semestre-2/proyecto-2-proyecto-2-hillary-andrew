package com.sistemasoperativos.pcvirtual.gui;

import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import java.util.*;


public class DesensambladorModelo1 {
    private static char TipoInstruccion;
    private static Conversor ConversorAsignado = new Conversor();
    
    public static String ConvertirInstruccion(String binario){
        if(binario.length() == 16){
            return DecodificarNumero(binario);
        }
        String instruccionBinaria = binario.substring(0, 5);
        String instruccion = DecodificarInstruccion(instruccionBinaria);
        if(TipoInstruccion == 'N'){
            if(binario.length() >= 10){
                String param = binario.substring(5, 10);
                instruccion += " " + DecodificarRegistro(param);
            }
            if(binario.length() == 15){
                String param = binario.substring(10);
                instruccion += ", " + DecodificarRegistro(param);
            }
            else if(binario.length() == 26){
                String param = binario.substring(10);
                instruccion += ", " + DecodificarNumero(param);
            }
        }
        else if(TipoInstruccion == 'J'){
            String param = binario.substring(5);
            instruccion += " " + DecodificarNumero(param);
        }
        else if(TipoInstruccion == 'P'){
            String param = binario.substring(5);
            instruccion += " " + DecodificarParam(param);
        }
        else if(TipoInstruccion == 'I'){
            String param = binario.substring(5);
            instruccion += " " + DecodificarInterrupcion(param);
        }
        return instruccion;
    }
    
    private static String DecodificarInstruccion(String binario) {
        TipoInstruccion = 'N';
        switch (binario) {
            case "00000": return "LOAD";
            case "00001": return "STORE";
            case "00010": return "MOV";
            case "00011": return "ADD";
            case "00100": return "SUB";
            case "00101": return "INC";
            case "00110": return "DEC";
            case "00111": return "SWAP";
            case "01000":
                TipoInstruccion = 'I';
                return "INT";
            case "01001":
                TipoInstruccion = 'J';
                return "JMP";
            case "01010": return "CMP";
            case "01011":
                TipoInstruccion = 'J';
                return "JE";
            case "01100":
                TipoInstruccion = 'J';
                return "JNE";
            case "01101":
                TipoInstruccion = 'P';
                return "PARAM";
            case "01110": return "PUSH";
            case "01111": return "POP";
            default: return "INSTRUCCIÓN DESCONOCIDA";
        }
    }
    
    
    private static String DecodificarRegistro(String binario) {
        switch (binario) {
            case "00000": return "PC";
            case "00001": return "AC";
            case "00010": return "IR";
            case "00011": return "AX";
            case "00100": return "BX";
            case "00101": return "CX";
            case "00110": return "DX";
            case "00111": return "CP";
            case "01000": return "SP";
            default: return "REGISTRO DESCONOCIDO";
        }
    }
    
    private static String DecodificarNumero(String binario){
        Integer numero = ConversorAsignado.ConvertirBitsAInteger(binario);
        return numero.toString();
    }
    
    private static String DecodificarInterrupcion(String binario){
        switch(binario){
            case "00000": return "20H";
            case "00001": return "10H";
            case "00010": return "09H";
            default: return "Interrupción desconocida";
        }
    }
    
    private static String DecodificarParam(String binario){
        String param = binario.substring(0, 16);
        String decodificacion = DecodificarNumero(param);
        if(binario.length() >= 32){
            param = binario.substring(16, 32);
            decodificacion += ", " + DecodificarNumero(param);
        }
        if(binario.length() >= 48){
            param = binario.substring(32, 48);
            decodificacion += ", " + DecodificarNumero(param);
        }
        return decodificacion;
    }

}
