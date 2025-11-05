/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import balanceador.Balanceador;
import cargadadoresprogramas.Cargador;
import com.sistemasoperativos.pcvirtual.componentes.CPU;
import com.sistemasoperativos.pcvirtual.procesos.BCP;
import com.sistemasoperativos.pcvirtual.procesos.EstadoBCP;
import java.util.List;

/**
 *
 * @author males
 */

/**
 * Algoritmo FCFS (First Come, First Served)
 * Los procesos se ejecutan en orden de llegada (FIFO) sin expulsión.
 * Cada CPU corre en su propio hilo utilizando BUS2.
 */
public class FCFS extends Planificador implements Algoritmo {

    /**
     * Constructor del algoritmo FCFS.
     * 
     * @param cola Cola de procesos listos.
     * @param cantidadCPUs Número de CPUs disponibles.
     */
    public FCFS(Cargador cargador, List<CPU> cpus, Balanceador balanceador) {
        super(cargador, cpus, balanceador);
    }

    @Override
    public void IniciarEjecucion() {
        EjecutarCPUs();
        Thread hilo = new Thread(() -> {
            while(true){
                try{
                    SacarPrograma();
                    CargarPrograma();
                    EjecutarFCFS();
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        hilo.start();
    }
    
    private void EjecutarFCFS() throws Exception{
        if(BalanceadorAsignado.EstanCPUsOcupados() || Cola.estaVacia())
            return;
        BCP proceso = Cola.obtener();
        int numeroCPU = BalanceadorAsignado.AsignarProcesoACPU(proceso);
        proceso.setEstado(EstadoBCP.EJECUTANDO);
        switch(numeroCPU){
            case 0: ProcesoCPU0 = proceso;
                break;
            case 1: ProcesoCPU1 = proceso;
                break;
            case 2: ProcesoCPU2 = proceso;
                break;
            case 3: ProcesoCPU3 = proceso;
                break;
        }
    }
}
