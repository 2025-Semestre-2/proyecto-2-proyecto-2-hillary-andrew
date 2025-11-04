/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package algoritmos;

import com.sistemasoperativos.pcvirtual.componentes.Almacenamiento;
import com.sistemasoperativos.pcvirtual.componentes.BUSModelo2;
import com.sistemasoperativos.pcvirtual.procesos.BCP;
import com.sistemasoperativos.pcvirtual.procesos.ColaProcesos;
import java.util.List;
import java.util.Map;

/**
 * Clase de prueba para ejecutar los algoritmos de planificación:
 * FCFS, SJF, SRT, HRRN y RR.
 *
 * Se crean procesos de ejemplo con distintos tiempos de ejecución.
 * 
 * @author males
 */
public class TestPlanificadores {

    public static void main(String[] args) {
        // =============================
        // CREAR BUS DE COMUNICACIÓN
        // =============================
        Almacenamiento almacenamiento = new Almacenamiento() {
            @Override
            public String GuardarPrograma(List<String> programa) throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public List<String> BuscarPrograma(String direccion) throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public Map<String, String> TraerAlmacenamiento() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        };
        BUSModelo2 bus = new BUSModelo2(almacenamiento);

        // =============================
        // CREAR PROCESOS DE EJEMPLO
        // =============================
        BCP p1 = new BCP(1, "Proceso_A", 4);
        BCP p2 = new BCP(2, "Proceso_B", 2);
        BCP p3 = new BCP(3, "Proceso_C", 6);
        BCP p4 = new BCP(4, "Proceso_D", 3);
        BCP p5 = new BCP(5, "Proceso_E", 5);

        // =============================
        //        PRUEBA FCFS
        // =============================
        System.out.println("\n=============================");
        System.out.println("🔹 PRUEBA ALGORITMO FCFS");
        System.out.println("=============================\n");

        ColaProcesos colaFCFS = new ColaProcesos();
        try {
            colaFCFS.agregar(p1, bus);
            colaFCFS.agregar(p2, bus);
            colaFCFS.agregar(p3, bus);
            colaFCFS.agregar(p4, bus);
            colaFCFS.agregar(p5, bus);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FCFS fcfs = new FCFS(colaFCFS, 1);
        fcfs.AsignarBUS(bus);
        fcfs.IniciarEjecucion();

        dormir(15000); // Esperar que termine la simulación
        System.out.println("✔ FCFS finalizado.\n");

        // =============================
        //        PRUEBA SJF
        // =============================
        System.out.println("\n=============================");
        System.out.println("🔹 PRUEBA ALGORITMO SJF");
        System.out.println("=============================\n");

        SJF sjf = new SJF(bus);
        sjf.agregarProceso(new BCP(1, "P1", 6));
        sjf.agregarProceso(new BCP(2, "P2", 2));
        sjf.agregarProceso(new BCP(3, "P3", 4));
        sjf.start();

        dormir(10000);
        sjf.detener();
        System.out.println("✔ SJF finalizado.\n");

        // =============================
        //        PRUEBA SRT
        // =============================
        System.out.println("\n=============================");
        System.out.println("🔹 PRUEBA ALGORITMO SRT");
        System.out.println("=============================\n");

        ColaProcesos colaSRT = new ColaProcesos();
        try {
            colaSRT.agregar(new BCP(1, "P1", 8), bus);
            colaSRT.agregar(new BCP(2, "P2", 4), bus);
            colaSRT.agregar(new BCP(3, "P3", 3), bus);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SRT srt = new SRT(colaSRT, bus);
        srt.IniciarEjecucion();

        dormir(12000);
        srt.DetenerEjecucion();
        System.out.println("✔ SRT finalizado.\n");

        // =============================
        //        PRUEBA HRRN
        // =============================
        System.out.println("\n=============================");
        System.out.println("🔹 PRUEBA ALGORITMO HRRN");
        System.out.println("=============================\n");

        ColaProcesos colaHRRN = new ColaProcesos();
        try {
            colaHRRN.agregar(new BCP(1, "P1", 3), bus);
            colaHRRN.agregar(new BCP(2, "P2", 5), bus);
            colaHRRN.agregar(new BCP(3, "P3", 2), bus);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HRRN hrrn = new HRRN(colaHRRN, bus);
        hrrn.IniciarEjecucion();

        dormir(10000);
        hrrn.DetenerEjecucion();
        System.out.println("✔ HRRN finalizado.\n");

        // =============================
        //        PRUEBA RR
        // =============================
        System.out.println("\n=============================");
        System.out.println("🔹 PRUEBA ALGORITMO RR");
        System.out.println("=============================\n");

        ColaProcesos colaRR = new ColaProcesos();
        try {
            colaRR.agregar(new BCP(1, "P1", 8), bus);
            colaRR.agregar(new BCP(2, "P2", 5), bus);
            colaRR.agregar(new BCP(3, "P3", 6), bus);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RR rr = new RR(colaRR, bus, 2);
        rr.IniciarEjecucion();

        dormir(12000);
        rr.DetenerEjecucion();
        System.out.println("✔ RR finalizado.\n");

        System.out.println("\n✅ TODAS LAS PRUEBAS FINALIZADAS.\n");
    }

    /**
     * Método auxiliar para pausar la ejecución principal.
     */
    private static void dormir(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
