/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import balanceador.Balanceador;
import cargadadoresprogramas.Cargador;
import com.sistemasoperativos.pcvirtual.componentes.BUS2;
import com.sistemasoperativos.pcvirtual.componentes.CPU;
import com.sistemasoperativos.pcvirtual.componentes.CPUModelo2;
import com.sistemasoperativos.pcvirtual.procesos.BCP;
import com.sistemasoperativos.pcvirtual.procesos.ColaProcesos;
import com.sistemasoperativos.pcvirtual.procesos.EstadoBCP;
import java.util.List;

/**
 *
 * @author andrewdeni
 */
public class Planificador {
    protected Cargador CargadorAsignado;
    protected ColaProcesos Cola;
    protected List<CPU> CPUs;
    protected BUS2 BUSAsignado;
    protected Balanceador BalanceadorAsignado;
    
    public Planificador(Cargador cargador, List<CPU> cpus, Balanceador balanceador){
        CargadorAsignado = cargador;
        Cola = new ColaProcesos();
        CPUs = cpus;
        BalanceadorAsignado = balanceador;
    }
    
    protected void CargarPrograma(){
        new Thread(() -> {
            try {
                int id = 0;
                while(true){
                    if(Cola.size() < 5){
                        BCP bcp = new BCP(id, "Proceso " + id, 0);
                        CargadorAsignado.CargarPrograma(bcp);
                        if(!bcp.getPC().equals("0000000000000000")){
                            id++;
                            Cola.agregar(bcp, BUSAsignado);
                        }
                    }   
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    protected void EjecutarCPUs(){
        for(CPU cpu : CPUs){
            new Thread(() -> {
                try {
                    while(true){
                        cpu.EjecutarInstruccion();
                        Thread.sleep(1000);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
