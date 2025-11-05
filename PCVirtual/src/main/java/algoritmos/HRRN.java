/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author males
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


public class HRRN extends Planificador implements Algoritmo {
    
    public HRRN(Cargador cargador, List<CPU> cpus, Balanceador balanceador){
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
                    EjecutaHRRN();
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        hilo.start();
    }
    
    private void EjecutaHRRN(){
        if(BalanceadorAsignado.EstanCPUsOcupados() || Cola.estaVacia())
            return;
        CalcularPrioridadBCPs(Cola.getProcesos());
        BCP proceso = ElegirBCPMayorPrioridad(Cola.getProcesos());
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
    
    private void CalcularPrioridadBCPs(Queue<BCP> procesos){
        List<BCP> procesosLista = (List) procesos;
        for(BCP proceso : procesosLista){
            int prioridad = ((TiempoCPU0 - proceso.getTiempoLlegada()) + TiempoCPU0) / TiempoCPU0;
            proceso.setPrioridad(prioridad);
        }
    }
    
    private BCP ElegirBCPMayorPrioridad(Queue<BCP> procesos){
        List<BCP> procesosLista = (List) procesos;
        BCP procesoMayor = procesosLista.get(0);
        for(BCP proceso : procesosLista){
            if(procesoMayor.getPrioridad() < proceso.getPrioridad()){
                procesoMayor = proceso;
            }
        }
        procesos.remove(procesoMayor);
        return procesoMayor;
    }
}
