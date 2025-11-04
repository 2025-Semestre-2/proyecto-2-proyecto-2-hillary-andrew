/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistemasoperativos.pcvirtual.procesos;

/**
 *
 * @author males
 */

import com.sistemasoperativos.pcvirtual.componentes.BUS;
import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ColaProcesos {
    private final Queue<BCP> cola;
    private Conversor ConversorAsignado;

    public ColaProcesos() {
        this.cola = new LinkedList<>();
        ConversorAsignado = new Conversor();
    }

    public void agregar(BCP proceso, BUS bus) throws Exception {
        cola.add(proceso);
        int idBCP = proceso.getID();
        int direccionEntera = (idBCP % 5) * 20;
        String direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //Escribir ID
        String dato = ConversorAsignado.ConvertirIntegerABits(idBCP);
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getBase();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getLimite();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getAC();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getIR();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getAX();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getBX();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getCX();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getDX();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getSP();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getBS();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getCP();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = proceso.getAC();
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = "00001";
        bus.EscribirDatoRAM(direccionBits, dato);
        direccionEntera++;
        direccionBits = ConversorAsignado.ConvertirIntegerABits(direccionEntera); //
        dato = ConversorAsignado.ConvertirIntegerABits(proceso.getPrioridad());
        bus.EscribirDatoRAM(direccionBits, dato);
    }

    public BCP obtener() throws Exception {
        if(cola.isEmpty())
            throw new Exception("No hay procesos en la cola de procesos");
        return cola.poll();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }
    
    public boolean estaLleno(){
        return cola.size() >= 5;
    }

    public int size() {
        return cola.size();
    }
    
    public Queue<BCP> getProcesosOrdenadorPorMemoria(){
        List<BCP> BCPs = new LinkedList<>(cola);
        Queue<BCP> colaOrdenada = new LinkedList<>();
        while(!BCPs.isEmpty()){
            BCP bcpMenor = BCPs.get(0);
            for(BCP bcp : BCPs){
                String direccionMenor = bcpMenor.getBS();
                int direccionMenorEntero = ConversorAsignado.ConvertirBitsAInteger(direccionMenor);
                String direccion = bcp.getBS();
                int direccionEntero = ConversorAsignado.ConvertirBitsAInteger(direccion);
                if(direccionEntero < direccionMenorEntero){
                    bcpMenor = bcp;
                }
            }
            BCPs.remove(bcpMenor);
            colaOrdenada.add(bcpMenor);
        }
        return colaOrdenada;
    }

    public void remover(BCP proceso) {
        cola.remove(proceso);
    }

    public Queue<BCP> getProcesos() {
        return cola;
    }

}

