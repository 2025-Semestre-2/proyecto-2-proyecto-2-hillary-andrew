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
 * Algoritmo Round Robin (RR)
 * Asigna a cada proceso un tiempo fijo de CPU (quantum).
 * Si el proceso no finaliza, se coloca nuevamente al final de la cola.
 * 
 * @author males
 */

public class RR extends Thread {

    private final ColaProcesos cola;
    private final BUSModelo2 bus;
    private final long quantum; // tiempo de CPU asignado por turno
    private boolean enEjecucion = false;

    public RR(ColaProcesos cola, BUSModelo2 bus, long quantum) {
        this.cola = cola;
        this.bus = bus;
        this.quantum = quantum;
    }

    public void IniciarEjecucion() {
        if (!enEjecucion) {
            enEjecucion = true;
            this.start();
        }
    }

    public void DetenerEjecucion() {
        enEjecucion = false;
    }

    @Override
    public void run() {
        System.out.println("Planificador RR iniciado con quantum = " + quantum + " ms");

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

            // Tomar el primer proceso de la cola
            BCP proceso = lista.poll();
            if (proceso == null) continue;

            // Ejecutar un turno
            ejecutarCuanto(proceso);

            // Si no terminó, volver a encolar
            if (proceso.getEstado() != EstadoBCP.FINALIZADO) {
                proceso.marcarPreparado();
                lista.add(proceso);
            }
        }

        System.out.println("Planificador RR finalizado.");
    }

    private void ejecutarCuanto(BCP proceso) {
        if (proceso == null) return;

        proceso.marcarEjecucion();
        System.out.println(" [RR] Ejecutando " + proceso.getNombre());

        long ciclosRestantes = proceso.getTiempoRestante();
        long ciclosEjecutar = Math.min(quantum, ciclosRestantes);

        for (long i = 0; i < ciclosEjecutar; i++) {
            try {
                bus.EscribirDatoRAM("00000",
                        proceso.getNombre() + " ejecutando ciclo " + (proceso.getTiempoEjecutado() + 1));
            } catch (Exception e) {
                System.err.println("[RR] Error al escribir en bus: " + e.getMessage());
            }

            proceso.setTiempoEjecutado(proceso.getTiempoEjecutado() + 1);
            proceso.setTiempoRestante(proceso.getTiempoTotalEjecucion() - proceso.getTiempoEjecutado());

            try {
                Thread.sleep(500); // simula un ciclo de CPU
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Si terminó antes del quantum, se rompe
            if (proceso.getTiempoEjecutado() >= proceso.getTiempoTotalEjecucion()) {
                proceso.marcarFinalizado();
                System.out.println(" [RR] Proceso " + proceso.getNombre() + " completado.");
                return;
            }
        }

        // Si aún no termina, se pausa
        System.out.println("[RR] Quantum terminado para " + proceso.getNombre() +
                " (" + proceso.getTiempoEjecutado() + "/" + proceso.getTiempoTotalEjecucion() + ")");
    }
}
