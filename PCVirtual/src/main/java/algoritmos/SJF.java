/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import com.sistemasoperativos.pcvirtual.componentes.BUSModelo2;
import com.sistemasoperativos.pcvirtual.procesos.BCP;
import com.sistemasoperativos.pcvirtual.procesos.EstadoBCP;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author males
 */

/**
 * Planificador SJT (Shortest Job Time / Shortest Job First).
 * Selecciona el proceso con el menor tiempo total de ejecución (tiempo de servicio)
 * y lo ejecuta completamente antes de pasar al siguiente.
 */

public class SJF extends Thread {

    private final BUSModelo2 bus;
    private final Queue<BCP> listaProcesos;
    private boolean activo;

    public SJF(BUSModelo2 bus) {
        this.bus = bus;
        this.listaProcesos = new ConcurrentLinkedQueue<>();
        this.activo = true;
    }

    /**
     * Agrega un nuevo proceso a la cola de listos.
     */
    public void agregarProceso(BCP proceso) {
        proceso.marcarPreparado();
        listaProcesos.add(proceso);
        System.out.println("[SJF] Proceso agregado: " + proceso.getNombre());
    }

    /**
     * Detiene la ejecución del planificador.
     */
    public void detener() {
        activo = false;
    }

    @Override
    public void run() {
        while (activo) {
            try {
                BCP proceso = seleccionarProcesoSJF();

                if (proceso == null) {
                    Thread.sleep(500); // Esperar si no hay procesos listos
                    continue;
                }

                ejecutarProceso(proceso);

            } catch (Exception e) {
                System.err.println("[SJF] Error: " + e.getMessage());
            }
        }
    }

    /**
     * Selecciona el proceso con menor tiempo total de ejecución.
     */
    private BCP seleccionarProcesoSJF() {
        BCP seleccionado = null;
        long menorRafaga = Long.MAX_VALUE;

        for (BCP p : listaProcesos) {
            if (p.getEstado() == EstadoBCP.LISTO) {
                long tiempoRafaga = p.getTiempoTotalEjecucion();
                if (tiempoRafaga < menorRafaga) {
                    menorRafaga = tiempoRafaga;
                    seleccionado = p;
                }
            }
        }
        return seleccionado;
    }

    /**
     * Simula la ejecución completa de un proceso seleccionado.
     */
    private void ejecutarProceso(BCP proceso) {
        if (proceso == null) return;

        proceso.marcarEjecucion();
        System.out.println("[SJF] Ejecutando proceso: " + proceso.getNombre());

        long ciclos = proceso.getTiempoTotalEjecucion();

        for (long i = proceso.getTiempoEjecutado(); i < ciclos; i++) {
            try {
                bus.EscribirDatoRAM("00000", proceso.getNombre() + " ejecutando ciclo " + (i + 1));
            } catch (Exception e) {
                System.err.println("[SJF] Error al escribir en bus: " + e.getMessage());
            }

            proceso.setTiempoEjecutado(i + 1);

            try {
                Thread.sleep(500); // Simula un ciclo de CPU de 500 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        proceso.marcarFinalizado();
        System.out.println("[SJF] Proceso finalizado: " + proceso.getNombre());
    }

}
