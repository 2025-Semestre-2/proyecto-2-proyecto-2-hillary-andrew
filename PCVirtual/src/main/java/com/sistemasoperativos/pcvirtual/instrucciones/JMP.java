/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.instrucciones;

import com.sistemasoperativos.pcvirtual.componentes.BUS;
import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import java.util.Map;

/**
 *
 * @author andre
 */
public class JMP extends InstruccionComunUnParametro implements Instruccion{
    
    private BUS BUSAsignado;

    public JMP(Conversor conversor, int peso, BUS bus) {
        super(conversor, peso);
        BUSAsignado = bus;
    }

    @Override
    public boolean EjecutarInstruccion(String instruccion, Map<String, String> registros) throws Exception {
        Registros = registros;
        if(AplicarPeso())
            return false;
        Desestructurar(instruccion);
        String siguienteInstruccion = Registros.get("00000");
        int siguienteInstruccionEntero = ConversorAsignado.ConvertirBitsAInteger(siguienteInstruccion);
        int desplazamiento = ConversorAsignado.ConvertirBitsAInteger(Param1);
        int direccionDesplazada = (siguienteInstruccionEntero - 1) + desplazamiento;
        String bitsDesplazamiento = ConversorAsignado.ConvertirIntegerABits(direccionDesplazada);
        String siguienteInstruccionBits = BUSAsignado.LeerDatoRAM(bitsDesplazamiento);
        Registros.put("00010", siguienteInstruccionBits);
        bitsDesplazamiento = ConversorAsignado.ConvertirIntegerABits(direccionDesplazada + 1);
        Registros.put("00000", bitsDesplazamiento);
        return false;
    }
    
    @Override
    protected void Desestructurar(String instruccion){
        Param1 = instruccion.substring(5);
    }
}
