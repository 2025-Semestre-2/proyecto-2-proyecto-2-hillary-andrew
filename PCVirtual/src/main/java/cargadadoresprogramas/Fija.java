/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cargadadoresprogramas;

import com.sistemasoperativos.pcvirtual.componentes.Almacenamiento;
import com.sistemasoperativos.pcvirtual.componentes.BUS;
import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import com.sistemasoperativos.pcvirtual.procesos.BCP;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author andrewdeni
 */
public class Fija implements Cargador {
    private final int TamanoMarco;
    private final LinkedHashMap<Integer, Boolean> TablaAsignacion;
    private final int TamanoMemoria;
    private final Almacenamiento Disco;
    private final Conversor ConversorAsignado;
    private final List<String> DireccionesProgramas;
    private int IndicePrograma = 0;
    private final BUS BusAsignado;
    
    public Fija(int tamanoMarco, int tamanoMemoria, Almacenamiento disco, BUS bus, List<String> direccionesProgramas){
        TamanoMarco = tamanoMarco;
        TamanoMemoria = tamanoMemoria;
        TablaAsignacion = new LinkedHashMap<>();
        for(int base = 100; base < TamanoMemoria - tamanoMarco; base += tamanoMarco){
            TablaAsignacion.put(base, Boolean.FALSE);
        }
        Disco = disco;
        ConversorAsignado = new Conversor();
        BusAsignado = bus;
        DireccionesProgramas = direccionesProgramas;
    }

    @Override
    public void CargarPrograma(BCP bcp) throws Exception {
        for(Map.Entry<Integer, Boolean> direccion : TablaAsignacion.entrySet()){
            if(!direccion.getValue()){
                String baseBits = ConversorAsignado.ConvertirIntegerABits(direccion.getKey());
                bcp.setBase(baseBits);
                bcp.setPC(baseBits);
                EscribirPrograma(direccion.getKey(), bcp);
                direccion.setValue(Boolean.TRUE);
                break;
            }
        }
    }
    
    private void EscribirPrograma(int direccionBase, BCP bcp) throws Exception{
        String direccion = DireccionesProgramas.get(IndicePrograma);
        IndicePrograma++;
        List<String> programa = Disco.BuscarPrograma(direccion);
        for(int indice = 0; indice < TamanoMarco && indice < programa.size(); indice++){
            String instruccion = programa.get(indice);
            String direccionEscritura = ConversorAsignado.ConvertirIntegerABits(direccionBase + indice);
            BusAsignado.EscribirDatoRAM(direccionEscritura, instruccion);
            bcp.setLimite(direccionEscritura);
        }
        if(programa.size() <= TamanoMarco + 5)
            DefinirStackPointer(bcp); 
    }
    
    private void DefinirStackPointer(BCP bcp){
        String limiteBits = bcp.getLimite();
        int limite = ConversorAsignado.ConvertirBitsAInteger(limiteBits);
        String nuevoLimite = ConversorAsignado.ConvertirIntegerABits(limite + 5);
        String nuevoSP = ConversorAsignado.ConvertirIntegerABits(limite + 1);
        bcp.setLimite(nuevoLimite);
        bcp.setSP(nuevoSP);
    }

    @Override
    public void LiberarPrograma(BCP bcp) {
        String baseBits = bcp.getBase();
        int baseEntero = ConversorAsignado.ConvertirBitsAInteger(baseBits);
        TablaAsignacion.put(baseEntero, Boolean.FALSE);
    }
}
