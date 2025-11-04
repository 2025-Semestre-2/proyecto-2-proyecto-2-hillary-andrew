/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package balanceador;

import com.sistemasoperativos.pcvirtual.componentes.CPU;
import com.sistemasoperativos.pcvirtual.procesos.BCP;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author andrewdeni
 */
public class BalanceadorModelo1 implements Balanceador {
    private List<CPU> CPUs;
    
    public BalanceadorModelo1(){
        CPUs = new ArrayList<>();
    }

    @Override
    public boolean AsignarProcesoACPU(BCP bcp) {
        List<CPU> cpus = new ArrayList<>();
        for(CPU cpu : CPUs){
            Map<String, String> registros = cpu.ObtenerRegistros();
            if(registros.get("00000").equals("0000000000000000")){
                cpus.add(cpu);
            }
        }
        if(cpus.isEmpty()){
            return false;
        }
        Random random = new Random();
        Integer cpuAleatorio = random.nextInt(cpus.size() + 1);
        CPU cpuSeleccionado = cpus.get(cpuAleatorio);
        Map<String, String> registros = cpuSeleccionado.ObtenerRegistros();
        cpuSeleccionado.CambiarRegistros(registros);
        bcp.setCpuAsignado(cpuAleatorio.toString());
        return true;
    }

    @Override
    public void AsignarCPU(CPU cpu) {
       CPUs.add(cpu);
    }

    @Override
    public boolean AsignarProcesoACPU(BCP bcp, int numberCPU) {
        if(CPUs.size() > numberCPU){
            return false;
        }
        CPU cpu = CPUs.get(numberCPU);
        cpu.CambiarRegistros(bcp.getRegistros());
        return true;
    }

    @Override
    public boolean EstanCPUsOcupados() {
        for(CPU cpu : CPUs){
            if(cpu.ObtenerRegistros().get("00000").equals("0000000000000000")){
                return false;
            }
        }
        return true;
    }
    
}
