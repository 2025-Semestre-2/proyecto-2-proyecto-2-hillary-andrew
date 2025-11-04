package com.sistemasoperativos.pcvirtual.controlador;

import com.sistemasoperativos.pcvirtual.compilador.AdmnistradorProgramasNuevos;
import com.sistemasoperativos.pcvirtual.componentes.Almacenamiento;
import com.sistemasoperativos.pcvirtual.componentes.AlmacenamientoModelo1;
import com.sistemasoperativos.pcvirtual.componentes.BUS2;
import com.sistemasoperativos.pcvirtual.componentes.BUSModelo2;
import com.sistemasoperativos.pcvirtual.componentes.BUSPantalla;
import com.sistemasoperativos.pcvirtual.componentes.BUSPantallaModelo1;
import com.sistemasoperativos.pcvirtual.componentes.CPU;
import com.sistemasoperativos.pcvirtual.componentes.CPUModelo2;
import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import com.sistemasoperativos.pcvirtual.componentes.RAM;
import com.sistemasoperativos.pcvirtual.componentes.RAMModelo1;
import com.sistemasoperativos.pcvirtual.gui.PantallaGUI;
import com.sistemasoperativos.pcvirtual.instrucciones.Add;
import com.sistemasoperativos.pcvirtual.instrucciones.Dec;
import com.sistemasoperativos.pcvirtual.instrucciones.Inc;
import com.sistemasoperativos.pcvirtual.instrucciones.Instruccion;
import com.sistemasoperativos.pcvirtual.instrucciones.Int;
import com.sistemasoperativos.pcvirtual.instrucciones.JE;
import com.sistemasoperativos.pcvirtual.instrucciones.JMP;
import com.sistemasoperativos.pcvirtual.instrucciones.JNE;
import com.sistemasoperativos.pcvirtual.instrucciones.Load;
import com.sistemasoperativos.pcvirtual.instrucciones.Mov;
import com.sistemasoperativos.pcvirtual.instrucciones.Pop;
import com.sistemasoperativos.pcvirtual.instrucciones.Push;
import com.sistemasoperativos.pcvirtual.instrucciones.Store;
import com.sistemasoperativos.pcvirtual.instrucciones.Sub;
import com.sistemasoperativos.pcvirtual.instrucciones.Swap;
import com.sistemasoperativos.pcvirtual.instrucciones.CMP;
import com.sistemasoperativos.pcvirtual.instrucciones.Param;
import com.sistemasoperativos.pcvirtual.procesos.GestorProcesos;
import com.sistemasoperativos.pcvirtual.procesos.ColaProcesos;
import algoritmos.FCFS;
import algoritmos.HRRN;
import algoritmos.RR;
import algoritmos.SRT;
import algoritmos.SJF;
//import algoritmos.SRR;
import algoritmos.Algoritmo;
import cargadadoresprogramas.Cargador;
import cargadadoresprogramas.Dinamica;
import cargadadoresprogramas.Fija;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Controlador de la PC virtual.
 *
 * Nota: el método CrearInstrucciones() llena el mapa de instrucciones con las
 * claves (mnemonicos) usadas en el proyecto. Actualmente coloca valores
 * {@code null} como "placeholders"; reemplaza esos {@code null} por
 * instancias concretas de clases que implementen {@link Instruccion} en tu
 * proyecto (por ejemplo: new LoadInstruction(...)).
 *
 * Esto evita que la clase dependa directamente de la implementación concreta
 * de cada instrucción y permite inicializar el mapa desde aquí.
 *
 * Si quieres, puedo ayudarte a implementar las clases concretas de cada
 * instrucción (LOAD, STORE, MOV, ...). Dime cuáles quieres primero.
 *
 * @author andrewdeni
 */
public class Controlador {
    
    private AdmnistradorProgramasNuevos AdministradorProgramas;
    private BUS2 BUSAsignado;
    private GestorProcesos Gestor;
    private BUSModelo2 bus;
    private final ColaProcesos colaListos = new ColaProcesos(); // cola de listos
    private Thread hiloPlanificador;                       // para HRRN/RR/SJF/SRT
    private FCFS fcfs;   

    public Controlador(){
        AdministradorProgramas = null;
    }

    public void CrearPC(int cantidadCPU, int tamanoRAM, int tamanoAlmacenamiento, int tipoCargador, int algoritmo) throws Exception{
        System.out.println("Creando PC...");
        int direccionEscrituraAlmacenamiento = 0;
        Conversor conversor = new Conversor();
        RAM ram = new RAMModelo1(tamanoRAM);
        Almacenamiento almacenamiento = new AlmacenamientoModelo1(
            tamanoAlmacenamiento, conversor, direccionEscrituraAlmacenamiento);
        LinkedList<String> direccionesProgramas = new LinkedList();
        LinkedList<String> nombresProgramas = new LinkedList();
        Algoritmo planificador = null;
        bus = new BUSModelo2(almacenamiento);
        bus.AsignarRAM(ram);
        BUSPantalla busPantalla = new BUSPantallaModelo1(this);
        AdministradorProgramas = new AdmnistradorProgramasNuevos(nombresProgramas, direccionesProgramas, bus);
        BUSAsignado = bus;
        Gestor = new GestorProcesos(planificador);
        //Crear los CPUs
        for(int cantidadCreada = 0; cantidadCreada < cantidadCPU; cantidadCreada++){
            Conversor conversorInstrucciones = new Conversor();
            Map<String, Instruccion> instruccionesCPU = CrearInstrucciones(conversorInstrucciones, busPantalla);
            Map<String, String> registros = CrearRegistros();
            CPU cpu = new CPUModelo2(instruccionesCPU, registros);
            bus.AsignarCPU(cpu);
        }
        //Crear el algoritmo escogido
        Cargador cargador = null;
        switch(tipoCargador){
            case 1:
                cargador = new Fija(50, tamanoRAM, almacenamiento, bus, direccionesProgramas);
                break;
            case 2:
                cargador = new Dinamica(tamanoRAM, almacenamiento, bus, direccionesProgramas);
                break;
            case 3:
                cargador = new Fija(50, tamanoRAM, almacenamiento, bus, direccionesProgramas);
                break;
            case 4:
                cargador = new Fija(50, tamanoRAM, almacenamiento, bus, direccionesProgramas);
                break;
            default:
                cargador = new Fija(50, tamanoRAM, almacenamiento, bus, direccionesProgramas);
        }
        // guarda bus en campo, lo usan los algoritmos
        this.bus = new BUSModelo2(almacenamiento);

        // === Selección de algoritmo ===
        final long QUANTUM_DEF = 3; // quantum

        // apaga planificador anterior si existe
        if (hiloPlanificador != null && hiloPlanificador.isAlive()) {
            hiloPlanificador.interrupt();
            hiloPlanificador = null;
        }
        fcfs = null;

        switch (algoritmo) {
            case 0: { // FCFS
                fcfs = new FCFS(colaListos, /*cantidadCPUs*/ 1);
                fcfs.AsignarBUS(this.bus);
                fcfs.IniciarEjecucion();                 // crea CPUs internas y lanza el planificador
                System.out.println("FCFS iniciado");
                break;
            }
            case 1: { // SRT
                SRT srt = new SRT(colaListos, this.bus);
                srt.IniciarEjecucion();
                hiloPlanificador = srt;
                System.out.println("SRT iniciado");
                break;
            }
            case 2: { // SJF
                SJF sjf = new SJF(this.bus);
                sjf.start();              // SJF extiende Thread
                hiloPlanificador = sjf;
                System.out.println("SJF iniciado");
                break;
            }
            case 3: { // RR
                RR rr = new RR(colaListos, this.bus, QUANTUM_DEF);
                rr.IniciarEjecucion();
                hiloPlanificador = rr;
                System.out.println(" RR iniciado (q=" + QUANTUM_DEF + ")");
                break;
            }
            case 4: { // HRRN
                HRRN hrrn = new HRRN(colaListos, this.bus);
                hrrn.IniciarEjecucion();
                hiloPlanificador = hrrn;
                System.out.println(" HRRN iniciado");
                break;
            }
            case 5: { // SRR
                // SRR srr = new SRR(colaListos, this.bus, QUANTUM_DEF);
                // srr.IniciarEjecucion();
                // hiloPlanificador = srr;
                // System.out.println("[CTRL] SRR iniciado");
                System.out.println("[CTRL] SRR no implementado aún; usando FCFS por defecto");
                fcfs = new FCFS(colaListos, 1);
                fcfs.AsignarBUS(this.bus);
                fcfs.IniciarEjecucion();
                break;
            }
            default: {
                fcfs = new FCFS(colaListos, 1);
                fcfs.AsignarBUS(this.bus);
                fcfs.IniciarEjecucion();
            }
        }
        
    }

    /**
     * PC = 00000
     * AC = 00001
     * IR = 00010
     * AX = 00011
     * BX = 00100
     * CX = 00101
     * DX = 00110
     * CP = 00111
     * SP = 01000
     */
    private Map<String, String> CrearRegistros() {
        Map<String, String> registros = new HashMap<>();
        registros.put("PC", "00000000");
        registros.put("AC", "00000000");
        registros.put("IR", "00000000");
        registros.put("AX", "00000000");
        registros.put("BX", "00000000");
        registros.put("CX", "00000000");
        registros.put("DX", "00000000");
        registros.put("CP", "00000000");
        registros.put("SP", "00000000");
        return registros;
    }

    /**
     * Rellena el mapa de instrucciones con las claves (mnemonicos) usadas en la
     * VM. Por ahora se insertan valores {@code null} como marcadores — debes
     * reemplazarlos por instancias reales de las clases que implementen
     * {@link Instruccion} en tu proyecto.
     *
     * Tabla de opcodes (5 bits) — referencia:
     *
     * Instrucción   Binario   Decimal
     * --------------------------------
     * LOAD          00000     0
     * STORE         00001     1
     * MOV           00010     2
     * ADD           00011     3
     * SUB           00100     4
     * INC           00101     5
     * DEC           00110     6
     * SWAP          00111     7
     * INT           01000     8
     * JMP           01001     9
     * CMP           01010     10
     * JE            01011     11
     * JNE           01100     12
     * PARAM         01101     13
     * PUSH          01110     14
     * POP           01111     15
     *
     * (Códigos disponibles: 16–31)
     *
     * @param instrucciones mapa a llenar (clave: mnemonico, valor: instancia)
     */
    private Map<String, Instruccion> CrearInstrucciones(Conversor conversor, BUSPantalla busPantalla){
        Map<String, Instruccion> instrucciones = new HashMap<>();
        instrucciones.put("00000", new Load(conversor, 1, bus)); // LOAD
        instrucciones.put("00001", new Store(conversor, 1, bus)); // STORE
        instrucciones.put("00010", new Mov(conversor, 1)); // MOV
        instrucciones.put("00011", new Add(conversor, 1)); // ADD
        instrucciones.put("00100", new Sub(conversor, 1)); // SUB
        instrucciones.put("00101", new Inc(conversor, 1)); // INC
        instrucciones.put("00110", new Dec(conversor, 1)); // DEC
        instrucciones.put("00111", new Swap(conversor, 1)); // SWAP
        instrucciones.put("01000", new Int(conversor, 1, busPantalla, bus)); // INT
        instrucciones.put("01001", new JMP(conversor, 1, bus)); // JMP
        instrucciones.put("01010", new CMP(conversor, 1)); // CMP
        instrucciones.put("01011", new JE(conversor, 1, bus)); // JE
        instrucciones.put("01100", new JNE(conversor, 1, bus)); // JNE
        instrucciones.put("01101", new Param(conversor, 1, bus)); // PARAM
        instrucciones.put("01110", new Push(conversor, 1, bus)); // PUSH
        instrucciones.put("01111", new Pop(conversor, 1, bus)); // POP
        return instrucciones;
    }
    
    public void EjecutarInstruccion() throws Exception{
        Gestor.Ejecutar();
    }
    
    public void CargarPrograma(File archivo) throws Exception{
        AdministradorProgramas.CargarPrograma(archivo);
    }
    
    public Map<String, String> TraerMemoria(){
        return BUSAsignado.TraerMemoriaRAM();
    }
    
    public Map<String, String> TraerAlmacenamiento(){
        return BUSAsignado.TraerAlmacenamiento();
    }
    
    public List<Map<String, String>> ObtenerBCPs(){
        return Gestor.ObtenerBCPs();
    }
    
    public List<Map<String, String>> ObtenerRegistros() throws Exception{
        return BUSAsignado.ObtenerRegistrosCPU();
    }
    
    public void Escribir(String dato){
        PantallaGUI.escribir(dato);
    }
}

