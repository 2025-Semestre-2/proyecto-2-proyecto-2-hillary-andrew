/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import balanceador.Balanceador;
import cargadadoresprogramas.Cargador;
import com.sistemasoperativos.pcvirtual.componentes.BUS2;
import com.sistemasoperativos.pcvirtual.componentes.CPU;
import com.sistemasoperativos.pcvirtual.procesos.BCP;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author males
 */

/**
 * Algoritmo FCFS (First Come, First Served)
 * Los procesos se ejecutan en orden de llegada (FIFO) sin expulsión.
 * Cada CPU corre en su propio hilo utilizando BUS2.
 */
public class FCFS extends Planificador implements Algoritmo, Runnable {

    private final List<BCP> procesosFinalizados;
    private boolean ejecutando = false;

    /**
     * Constructor del algoritmo FCFS.
     * 
     * @param cola Cola de procesos listos.
     * @param cantidadCPUs Número de CPUs disponibles.
     */
    public FCFS(Cargador cargador, List<CPU> cpus, Balanceador balanceador) {
        super(cargador, cpus, balanceador);
        this.procesosFinalizados = new ArrayList<>();
    }

    @Override
    public void AsignarBUS(BUS2 bus) {
        this.BUSAsignado = bus;
    }

    @Override
    public void IniciarEjecucion() {
        if (BUSAsignado == null) {
            System.err.println("[FCFS] Error: No se ha asignado el BUS antes de iniciar.");
            return;
        }

        if (!ejecutando) {
            ejecutando = true;

            

            // Lanzar el planificador
            Thread planificador = new Thread(this, "Planificador_FCFS");
            planificador.start();
            System.out.println("[FCFS] Planificador iniciado con " + CPUs.size() + " CPUs virtuales.");
        }
    }

    @Override
    public void run() {
        while (ejecutando) {
            if (!Cola.estaVacia()) {
                BCP proceso = null;
                try {
                    proceso = Cola.obtener();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (proceso != null) {
                    proceso.marcarEjecucion();
                    ejecutarEnCPUDisponible(proceso);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void ejecutarEnCPUDisponible(BCP proceso) {
        BalanceadorAsignado.AsignarProcesoACPU(proceso);
    }

    @Override
    public List<Map<String, String>> ObtenerBCPs() {
        List<Map<String, String>> lista = new ArrayList<>();

        for (BCP bcp : procesosFinalizados) {
            Map<String, String> datos = new LinkedHashMap<>();
            datos.put("ID", String.valueOf(bcp.getID()));
            datos.put("Nombre", bcp.getNombre());
            datos.put("CPU", bcp.getCpuAsignado());
            datos.put("Estado", bcp.getEstado().toString());
            lista.add(datos);
        }

        return lista;
    }
}
