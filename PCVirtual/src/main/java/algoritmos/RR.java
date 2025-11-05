/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import balanceador.Balanceador;
import cargadadoresprogramas.Cargador;
import com.sistemasoperativos.pcvirtual.componentes.BUSModelo2;
import com.sistemasoperativos.pcvirtual.componentes.CPU;
import com.sistemasoperativos.pcvirtual.procesos.BCP;
import com.sistemasoperativos.pcvirtual.procesos.ColaProcesos;
import com.sistemasoperativos.pcvirtual.procesos.EstadoBCP;
import java.util.List;
import java.util.Queue;

/**
 * Algoritmo Round Robin (RR)
 * Asigna a cada proceso un tiempo fijo de CPU (quantum).
 * Si el proceso no finaliza, se coloca nuevamente al final de la cola.
 * 
 * @author males
 */

public class RR extends Planificador implements Algoritmo {

    private final int quantum;
    private int quantumProceso0;
    private int quantumProceso1;
    private int quantumProceso2;
    private int quantumProceso3;

    public RR(Cargador cargador, List<CPU> cpus, Balanceador balanceador, int quantum) {
        super(cargador, cpus, balanceador);
        this.quantum = quantum;
        quantumProceso0 = 0;
        quantumProceso1 = 0;
        quantumProceso2 = 0;
        quantumProceso3 = 0;
    }

    @Override
    public void IniciarEjecucion() {
        EjecutarCPUs();
        Thread hilo = new Thread(() -> {
            while(true){
                try{
                    SacarPrograma();
                    CargarPrograma();
                    ValidarProcesosQuantumCumplido();
                    EjecutarRR();
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        hilo.start();
    }
    
    private void EjecutarRR() throws Exception{
        if(BalanceadorAsignado.EstanCPUsOcupados() || Cola.estaVacia())
            return;
        BCP proceso = Cola.obtener();
        int numeroCPU = BalanceadorAsignado.AsignarProcesoACPU(proceso);
        proceso.setEstado(EstadoBCP.EJECUTANDO);
        switch(numeroCPU){
            case 0: ProcesoCPU0 = proceso;
                quantumProceso0 = TiempoCPU0;
                break;
            case 1: ProcesoCPU1 = proceso;
                quantumProceso1 = TiempoCPU1;
                break;
            case 2: ProcesoCPU2 = proceso;
                quantumProceso2 = TiempoCPU2;
                break;
            case 3: ProcesoCPU3 = proceso;
                quantumProceso3 = TiempoCPU3;
                break;
        }
    }
    
    private void ValidarProcesosQuantumCumplido() throws Exception{
        Queue<BCP> procesos = Cola.getProcesos();
        if(ProcesoCPU0 != null){
            int quantumConsumido = TiempoCPU0 - quantumProceso0;
            if(quantumConsumido >= quantum){
                procesos.add(ProcesoCPU0);
                ProcesoCPU0 = Cola.obtener();
                BalanceadorAsignado.AsignarProcesoACPU(ProcesoCPU0, 0);
                quantumProceso0 = TiempoCPU0;
            }
        }
        if(ProcesoCPU1 != null){
            int quantumConsumido = TiempoCPU1 - quantumProceso1;
            if(quantumConsumido >= quantum){
                procesos.add(ProcesoCPU1);
                ProcesoCPU1 = Cola.obtener();
                BalanceadorAsignado.AsignarProcesoACPU(ProcesoCPU1, 0);
                quantumProceso1 = TiempoCPU1;
            }
        }
        if(ProcesoCPU2 != null){
            int quantumConsumido = TiempoCPU2 - quantumProceso2;
            if(quantumConsumido >= quantum){
               procesos.add(ProcesoCPU2);
                ProcesoCPU2 = Cola.obtener();
                BalanceadorAsignado.AsignarProcesoACPU(ProcesoCPU2, 0);
                quantumProceso2 = TiempoCPU2;
            }
        }
        if(ProcesoCPU3 != null){
            int quantumConsumido = TiempoCPU3 - quantumProceso3;
            if(quantumConsumido >= quantum){
                procesos.add(ProcesoCPU3);
                ProcesoCPU3 = Cola.obtener();
                BalanceadorAsignado.AsignarProcesoACPU(ProcesoCPU3, 0);
                quantumProceso3 = TiempoCPU3;
            }
        }
    }
}
