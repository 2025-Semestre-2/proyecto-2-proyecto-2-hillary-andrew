/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package algoritmos;

import com.sistemasoperativos.pcvirtual.componentes.BUSModelo2;
import com.sistemasoperativos.pcvirtual.procesos.BCP;
import com.sistemasoperativos.pcvirtual.procesos.ColaProcesos;
import com.sistemasoperativos.pcvirtual.procesos.EstadoBCP;

import java.util.Queue;

/**
 * Implementación del algoritmo SRT (Shortest Remaining Time)
 * para la planificación de procesos.
 *
 * Selecciona el proceso con el menor tiempo restante de ejecución.
 *
 * - tiempoTotalEjecucion está en BCP
 * - tiempoRestante se actualiza dinámicamente
 * - tiempoCPU representa el uptime del planificador
 * 
 * @author males
 *
 */

public class SRT extends Thread {

    private final ColaProcesos cola;
    private final BUSModelo2 bus;
    private boolean enEjecucion = false;

    public SRT(ColaProcesos cola, BUSModelo2 bus) {
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
        System.out.println("Planificador SRT iniciado...");

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

            BCP proceso = seleccionarProcesoSRT(lista);
            if (proceso == null) continue;

            ejecutarProceso(proceso);
        }

        System.out.println("Planificador SRT finalizado.");
    }

    /**
     * Selecciona el proceso con el menor tiempo restante.
     */
    private BCP seleccionarProcesoSRT(Queue<BCP> lista) {
        BCP seleccionado = null;
        long menorRestante = Long.MAX_VALUE;

        for (BCP p : lista) {
            if (p.getEstado() == EstadoBCP.FINALIZADO) continue;

            if (p.getTiempoRestante() < menorRestante) {
                menorRestante = p.getTiempoRestante();
                seleccionado = p;
            }
        }

        return seleccionado;
    }

    /**
     * Ejecuta un proceso hasta que cambie el contexto o termine.
     */
    private void ejecutarProceso(BCP proceso) {
        if (proceso == null) return;

        proceso.marcarEjecucion();
        System.out.println(" Ejecutando " + proceso.getNombre());

        // Ejecuta un ciclo a la vez para permitir preempción
        if (proceso.getTiempoRestante() > 0) {
            try {
                // Simula un ciclo de CPU
                long nuevoRestante = proceso.getTiempoRestante() - 1;
                proceso.setTiempoRestante(nuevoRestante);
                proceso.setTiempoEjecutado(proceso.getTiempoEjecutado() + 1);

                // Mostrar en el "bus" o consola
                try {
                    bus.EscribirDatoRAM("00000", proceso.getNombre() + " ejecutando ciclo "
                            + proceso.getTiempoEjecutado() + " (restante=" + nuevoRestante + ")");
                } catch (Exception e) {
                    System.out.println("[BUS] " + proceso.getNombre() + " ejecutando ciclo "
                            + proceso.getTiempoEjecutado());
                }

                Thread.sleep(500);

                // Si terminó el proceso
                if (proceso.getTiempoRestante() <= 0) {
                    proceso.marcarFinalizado();
                    System.out.println(" Proceso " + proceso.getNombre() + " completado.");
                } else {
                    proceso.marcarPreparado(); // vuelve a la cola
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void DetenerEjecucion() {
        enEjecucion = false;
    }
}
