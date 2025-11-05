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
import java.util.Queue;

/**
 *
 * @author males
 */

/**
 * Planificador SJT (Shortest Job Time / Shortest Job First).
 * Selecciona el proceso con el menor tiempo total de ejecución (tiempo de servicio)
 * y lo ejecuta completamente antes de pasar al siguiente.
 */

public class SJF extends Planificador implements Algoritmo {

    public SJF(Cargador cargador, List<CPU> cpus, Balanceador balanceador) {
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
                    EjecutarSJF();
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        hilo.start();
    }

    private void EjecutarSJF() throws Exception{
        if(BalanceadorAsignado.EstanCPUsOcupados() || Cola.estaVacia())
            return;
        Queue<BCP> procesos = Cola.getProcesos();
        BCP proceso = ElegirMenorRafaga(procesos);
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
    
    private BCP ElegirMenorRafaga(Queue<BCP> procesos){
        List<BCP> procesosLista = (List) procesos;
        BCP bcpMenor = procesosLista.get(0);
        for(BCP bcpActual : procesosLista){
            if(bcpMenor.getRafaga() > bcpActual.getRafaga()){
                bcpMenor = bcpActual;
            }
        }
        procesos.remove(bcpMenor);
        return bcpMenor;
    }
}
