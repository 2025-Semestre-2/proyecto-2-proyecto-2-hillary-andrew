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

/**
 *
 * @author andrewdeni
 */
public class Dinamica implements Cargador{
    private final LinkedHashMap<Integer, Boolean> TablaAsignacion;
    private final int TamanoMemoria;
    private final Almacenamiento Disco;
    private final Conversor ConversorAsignado;
    private final List<String> DireccionesProgramas;
    private int IndicePrograma = 0;
    private final BUS BusAsignado;
    
    public Dinamica(int tamanoMemoria, Almacenamiento disco, BUS bus, List<String> direccionesProgramas){
        TamanoMemoria = tamanoMemoria;
        TablaAsignacion = new LinkedHashMap<>();
        for(int base = 100; base < TamanoMemoria; base++){
            TablaAsignacion.put(base, Boolean.FALSE);
        }
        Disco = disco;
        ConversorAsignado = new Conversor();
        BusAsignado = bus;
        DireccionesProgramas = direccionesProgramas;
    }

    @Override
    public void CargarPrograma(BCP bcp) throws Exception {
        String direccionPrograma = DireccionesProgramas.get(IndicePrograma);
        IndicePrograma++;
        List<String> programa = Disco.BuscarPrograma(direccionPrograma);
        int base = BuscarEspacio(programa.size());
        if (base == 0){
            IndicePrograma--;
            throw new Exception("No hay más espacio en la memoria.");
        }
        int limite = base;
        for(;limite < programa.size() + 5; limite++){
            TablaAsignacion.put(base, Boolean.TRUE);
            String instruccion = programa.get(base);
            String direccion = ConversorAsignado.ConvertirIntegerABits(limite);
            BusAsignado.EscribirDatoRAM(direccion, instruccion);
        }
    }
    
    private int BuscarEspacio(int tamanoPrograma){
        int resultado = 100;
        boolean buscando = true;
        while(buscando && TablaAsignacion.containsKey(resultado)){
            buscando = false;
            for(int base = resultado; base < tamanoPrograma + 5; base++){
                if(!TablaAsignacion.get(base)){
                    buscando = true;
                    resultado = base + 1;
                    break;
                }
            }
        }
        if(!TablaAsignacion.containsKey(resultado)){
            return 0;
        }
        return resultado;
    }

    @Override
    public void LiberarPrograma(BCP bcp) {
        String baseBits = bcp.getBase();
        int base = ConversorAsignado.ConvertirBitsAInteger(baseBits);
        String limiteBits = bcp.getLimite();
        int limite = ConversorAsignado.ConvertirBitsAInteger(limiteBits);
        for(int indice = base; indice <= limite; indice++){
            TablaAsignacion.put(indice, Boolean.FALSE);
        }
    }
     
}
