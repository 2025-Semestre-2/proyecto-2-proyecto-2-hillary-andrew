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
import com.sistemasoperativos.pcvirtual.procesos.ColaProcesos;
import com.sistemasoperativos.pcvirtual.procesos.EstadoBCP;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author andrewdeni
 */
public abstract class Planificador implements Algoritmo{
    protected Cargador CargadorAsignado;
    protected ColaProcesos Cola;
    protected List<CPU> CPUs;
    protected BUS2 BUSAsignado;
    protected Balanceador BalanceadorAsignado;
    protected int TiempoCPU0;
    protected int TiempoCPU1;
    protected int TiempoCPU2;
    protected int TiempoCPU3;
    protected BCP ProcesoCPU0;
    protected BCP ProcesoCPU1;
    protected BCP ProcesoCPU2;
    protected BCP ProcesoCPU3;
    private int CantidadProcesos;
    
    public Planificador(Cargador cargador, List<CPU> cpus, Balanceador balanceador){
        CargadorAsignado = cargador;
        Cola = new ColaProcesos();
        CPUs = cpus;
        BalanceadorAsignado = balanceador;
        TiempoCPU0 = 0;
        TiempoCPU1 = 0;
        TiempoCPU2 = 0;
        TiempoCPU3 = 0;
        ProcesoCPU0 = null;
        ProcesoCPU1 = null;
        ProcesoCPU2 = null;
        ProcesoCPU3 = null;
        CantidadProcesos = 0;
    }
    
    protected void CargarPrograma() throws Exception{
        double probabilidad = Math.random();
        if(Cola.size() < 5 && probabilidad <= 0.3){
            BCP bcp = new BCP(CantidadProcesos, "Proceso " + CantidadProcesos, 0);
            CargadorAsignado.CargarPrograma(bcp);
            if(!bcp.getPC().equals("0000000000000000")){
                CantidadProcesos++;
                Cola.agregar(bcp, BUSAsignado);
                bcp.setTiempoLlegada(TiempoCPU0);
                bcp.setEstado(EstadoBCP.LISTO);
            }
        }   
    }
    
    protected void EjecutarCPUs(){
        for (int i = 0; i < CPUs.size(); i++) {
            final int indice = i; // Necesario para usar dentro de la lambda
            CPU cpu = CPUs.get(i);
            new Thread(() -> {
                try {
                    while (true) {
                        cpu.EjecutarInstruccion();
                        System.out.println("CPU " + indice + " corriendo");
                        switch (indice) {
                            case 0:
                                TiempoCPU0++;
                                ActualizarTiempoBCP(ProcesoCPU0);
                                break;
                            case 1:
                                TiempoCPU1++;
                                ActualizarTiempoBCP(ProcesoCPU1);
                                break;
                            case 2:
                                TiempoCPU2++;
                                ActualizarTiempoBCP(ProcesoCPU2);
                                break;
                            case 3:
                                TiempoCPU3++;
                                ActualizarTiempoBCP(ProcesoCPU3);
                                break;
                        }

                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    
    private void ActualizarTiempoBCP(BCP bcp){
        if(bcp != null)
            bcp.sumarTiempo();
    }

    @Override
    public abstract void IniciarEjecucion();

    @Override
    public List<Map<String, String>> ObtenerBCPs() {
        List<Map<String, String>> lista = new ArrayList<>();
        Queue<BCP> colaAux = Cola.getProcesos();
        if(ProcesoCPU0 != null){
            lista.add(ProcesoCPU0.GetInfo());
        }
        else{
            lista.add(null);
        }
        if(ProcesoCPU1 != null){
            lista.add(ProcesoCPU1.GetInfo());
        }
        else{
            lista.add(null);
        }
        if(ProcesoCPU2 != null){
            lista.add(ProcesoCPU2.GetInfo());
        }
        else{
            lista.add(null);
        }
        if(ProcesoCPU3 != null){
            lista.add(ProcesoCPU3.GetInfo());
        }
        else{
            lista.add(null);
        }
        for (BCP bcp : colaAux) {
            lista.add(bcp.GetInfo());
        }
        return lista;
    }

    @Override
    public void AsignarBUS(BUS2 bus) {
        BUSAsignado = bus;
    }
    
    protected void SacarPrograma(){
        if(ProcesoCPU0 != null){
            Map<String, String> registro0 = ProcesoCPU0.getRegistros();
            if(registro0.get("00000").equals("0000000000000000")){
                ProcesoCPU0.setEstado(EstadoBCP.FINALIZADO);
                CargadorAsignado.LiberarPrograma(ProcesoCPU0);
                ProcesoCPU0 = null;
            }
        }
        if(ProcesoCPU1 != null){
            Map<String, String> registro0 = ProcesoCPU1.getRegistros();
            if(registro0.get("00000").equals("0000000000000000")){
                ProcesoCPU1.setEstado(EstadoBCP.FINALIZADO);
                CargadorAsignado.LiberarPrograma(ProcesoCPU0);
                ProcesoCPU1 = null;
            }
        }
        if(ProcesoCPU2 != null){
            Map<String, String> registro0 = ProcesoCPU2.getRegistros();
            if(registro0.get("00000").equals("0000000000000000")){
                ProcesoCPU2.setEstado(EstadoBCP.FINALIZADO);
                CargadorAsignado.LiberarPrograma(ProcesoCPU0);
                ProcesoCPU2 = null;
            }
        }
        if(ProcesoCPU3 != null){
            Map<String, String> registro0 = ProcesoCPU3.getRegistros();
            if(registro0.get("00000").equals("0000000000000000")){
                ProcesoCPU3.setEstado(EstadoBCP.FINALIZADO);
                CargadorAsignado.LiberarPrograma(ProcesoCPU0);
                ProcesoCPU3 = null;
            }
        }
    }
}
