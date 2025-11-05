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

public class SRT extends Planificador implements Algoritmo {

    private int IndiceBCPMayor;
    
    public SRT(Cargador cargador, List<CPU> cpus, Balanceador balanceador){
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
                    EjecutarSRTCPUDesocupado();
                    EjecutarSRTExpropiación();
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        hilo.start();
    }

    private void EjecutarSRTCPUDesocupado(){
        if(BalanceadorAsignado.EstanCPUsOcupados() || Cola.estaVacia())
            return;
        Queue<BCP> procesos = Cola.getProcesos();
        BCP proceso = ElegirMenorRafaga(procesos);
        procesos.remove(proceso);
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
    
    private void EjecutarSRTExpropiación(){
        if(Cola.estaVacia())
            return;
        Queue<BCP> procesos = Cola.getProcesos();
        BCP bcpEjecutando = ElegirBCPEjecutanMayor();
        BCP bcpListo = ElegirMenorRafaga(procesos);
        if(bcpEjecutando.getRafaga() - bcpEjecutando.getTiempoTranscurrido() > bcpListo.getRafaga() - bcpListo.getTiempoTranscurrido()){
            procesos.remove(bcpListo);
            bcpEjecutando.setEstado(EstadoBCP.LISTO);
            bcpListo.setEstado(EstadoBCP.EJECUTANDO);
            switch(IndiceBCPMayor){
                case 0:
                    procesos.add(bcpListo);
                    ProcesoCPU0 = bcpListo;
                case 1:
                    procesos.add(bcpListo);
                    ProcesoCPU1 = bcpListo;
                case 2:
                    procesos.add(bcpListo);
                    ProcesoCPU2 = bcpListo;
                case 3:
                    procesos.add(bcpListo);
                    ProcesoCPU3 = bcpListo;
            }
        }
    }
    
    private BCP ElegirBCPEjecutanMayor(){
        BCP bcpMayor = ProcesoCPU0;
        IndiceBCPMayor = 0;
        if(bcpMayor.getRafaga() - bcpMayor.getTiempoTranscurrido() > ProcesoCPU1.getRafaga() - ProcesoCPU1.getTiempoTranscurrido()){
            bcpMayor = ProcesoCPU1;
            IndiceBCPMayor = 1;
        }
        if(bcpMayor.getRafaga() - bcpMayor.getTiempoTranscurrido() > ProcesoCPU2.getRafaga() - ProcesoCPU2.getTiempoTranscurrido()){
            bcpMayor = ProcesoCPU2;
            IndiceBCPMayor = 2;
        }
        if(bcpMayor.getRafaga() - bcpMayor.getTiempoTranscurrido() > ProcesoCPU3.getRafaga() - ProcesoCPU3.getTiempoTranscurrido()){
            bcpMayor = ProcesoCPU3;
            IndiceBCPMayor = 3;
        }
        return bcpMayor;
    }
    
    private BCP ElegirMenorRafaga(Queue<BCP> procesos){
        List<BCP> procesosLista = (List) procesos;
        BCP bcpMenor = procesosLista.get(0);
        for(BCP bcpActual : procesosLista){
            int rafagaRestanteMenor = bcpMenor.getRafaga() - bcpMenor.getTiempoTranscurrido();
            int rafagaRestanteActual = bcpActual.getRafaga() - bcpActual.getTiempoTranscurrido();
            if(rafagaRestanteMenor > rafagaRestanteActual){
                bcpMenor = bcpActual;
            }
        }
        return bcpMenor;
    }
}
