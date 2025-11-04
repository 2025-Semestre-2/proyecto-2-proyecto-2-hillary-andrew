/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author males
 */

package algoritmos;

import com.sistemasoperativos.pcvirtual.componentes.BUSModelo2;
import com.sistemasoperativos.pcvirtual.procesos.BCP;
import com.sistemasoperativos.pcvirtual.procesos.ColaProcesos;
import com.sistemasoperativos.pcvirtual.procesos.EstadoBCP;

import java.util.Queue;

/**
 * Implementación del algoritmo HRRN (Highest Response Ratio Next)
 * para la planificación de procesos.
 *
 * HRR = (T_espera + T_servicio) / T_servicio
 * 
 * - T_servicio (tiempo de ejecución total) está en BCP (tiempoTotalEjecucion)
 * - T_espera se calcula y actualiza localmente en este planificador
 * - tiempoCPU representa el uptime del planificador
 * 
 * @author males
 */


public class HRRN extends Thread {

    private final ColaProcesos cola;
    private final BUSModelo2 bus;
    private boolean enEjecucion = false;

    public HRRN(ColaProcesos cola, BUSModelo2 bus) {
        this.cola = cola;
        this.bus = bus;
    }

    public void IniciarEjecucion() {
        if (!enEjecucion) {
            enEjecucion = true;
            this.start();
        }
    }

    @Override
    public void run() {
        System.out.println("Planificador HRRN iniciado...");

        while (enEjecucion) {
            Queue<BCP> lista = cola.getProcesos();

            if (lista.isEmpty()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }

            BCP proceso = seleccionarProcesoHRRN(lista);
            if (proceso == null) continue;

            ejecutarProceso(proceso);
        }

        System.out.println("Planificador HRRN finalizado.");
    }

    // Selecciona el proceso con mayor Response Ratio = (espera + servicio) / servicio
    private BCP seleccionarProcesoHRRN(Queue<BCP> lista) {
        double mayorRatio = -1;
        BCP seleccionado = null;

        long tiempoActual = System.currentTimeMillis();

        for (BCP p : lista) {
            if (p.getEstado() == EstadoBCP.FINALIZADO) continue;

            long tiempoEspera = tiempoActual - p.getTiempoInicio();
            long servicio = Math.max(1, p.getTiempoTotalEjecucion()); // evita división por 0
            double ratio = ((double) (tiempoEspera + servicio)) / servicio;

            if (ratio > mayorRatio) {
                mayorRatio = ratio;
                seleccionado = p;
            }
        }

        return seleccionado;
    }

    private void ejecutarProceso(BCP proceso) {
        if (proceso == null) return;

        proceso.marcarEjecucion();
        System.out.println(" Ejecutando " + proceso.getNombre());

        long ciclos = proceso.getTiempoTotalEjecucion();
        for (long i = proceso.getTiempoEjecutado(); i < ciclos; i++) {
            try {
                bus.EscribirDatoRAM("00000", proceso.getNombre() + " ejecutando ciclo " + (i + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        proceso.marcarFinalizado();
        System.out.println(" Proceso " + proceso.getNombre() + " completado.");
    }

    public void DetenerEjecucion() {
        enEjecucion = false;
    }
}
