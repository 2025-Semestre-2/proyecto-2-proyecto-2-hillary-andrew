/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package algoritmos;

import com.sistemasoperativos.pcvirtual.componentes.BUS2;
import java.util.List;
import java.util.Map;

/**
 *
 * @author andrewdeni
 */
public interface Algoritmo {
    public void IniciarEjecucion(); //Recordar que cada CPU debe estar en un hilo aparte
    public List<Map<String, String>> ObtenerBCPs();
    public void AsignarBUS(BUS2 bus);
}
