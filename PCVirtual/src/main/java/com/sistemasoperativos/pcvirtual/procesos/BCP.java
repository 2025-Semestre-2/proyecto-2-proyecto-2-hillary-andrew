package com.sistemasoperativos.pcvirtual.procesos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BCP {

    private final int id;
    private final String nombre;
    private final int prioridad;
    private EstadoBCP estado;

    // -------- Registros --------
    private final Map<String, String> registros; // PC, AC, IR, AX, BX, CX, DX, SP, CP, BS, LI

    // -------- Información contable --------
    private String cpuAsignado;
    private long tiempoInicio;
    private long tiempoEjecutado;
    private long tiempoRestante;
    private long tiempoTotalEjecucion; 

    // -------- Información de E/S --------
    private final List<String> archivosAbiertos;

    // -------- Enlace al siguiente BCP (lista enlazada) --------
    private BCP siguiente;

    public BCP(int id, String nombre, int prioridad) {
        this.id = id;
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.estado = EstadoBCP.NUEVO;

        // Inicializar registros a "00000000"
        this.registros = new LinkedHashMap<>();
        String[] codigos = {
            "00000", // PC
            "00001", // AC
            "00010", // IR
            "00011", // AX
            "00100", // BX
            "00101", // CX
            "00110", // DX
            "00111", // CP
            "01000", // SP
            "01001", // BS
            "01010" // LI
        };
        for (String c : codigos) {
            registros.put(c, "0000000000000000");
        }

        // Info contable
        this.cpuAsignado = "CPU0"; // por defecto
        this.tiempoInicio = System.currentTimeMillis();
        this.tiempoEjecutado = 0;

        // Archivos abiertos vacíos
        this.archivosAbiertos = new ArrayList<>();

        // Sin siguiente al inicio
        this.siguiente = null;
    }

    // -------- Métodos de cambio de estado -----------
    public void marcarNuevo() {
        this.estado = EstadoBCP.NUEVO;
    }

    public void marcarPreparado() {
        this.estado = EstadoBCP.LISTO;
    }

    public void marcarEjecucion() {
        this.estado = EstadoBCP.EJECUTANDO;
    }

    public void marcarEsperando() {
        this.estado = EstadoBCP.ESPERANDO;
    }

    public void marcarFinalizado() {
        this.estado = EstadoBCP.FINALIZADO;
    }
    
    public int getID(){
        return id;
    }
    
    public int getPrioridad(){
        return prioridad;
    }

    // ----------- Manejo de archivos ----------- 
    public void abrirArchivo(String nombreArchivo) {
        archivosAbiertos.add(nombreArchivo);
    }

    public void cerrarArchivo(String nombreArchivo) {
        archivosAbiertos.remove(nombreArchivo);
    }

    // ----------- Acceso a registros (Map) ----------- 
    public Map<String, String> getRegistros() {
        return registros;
    }

    public String getRegistro(String codigo) {
        return registros.get(codigo);
    }
    
    public String getNombre(){
        return nombre;
    }

    public void setRegistro(String codigo, String valor) {
        registros.put(codigo, valor);
    }

    // ----------- Getters y Setters individuales de registros -----------
    public String getPC() {
        return registros.get("00000");
    }

    public void setPC(String valor) {
        registros.put("00000", valor);
    }

    public String getAC() {
        return registros.get("00001");
    }

    public void setAC(String valor) {
        registros.put("00001", valor);
    }
    
    public String getBS(){
        return registros.get("01001");
    }
    
    public void setBS(String valor){
        registros.put("01001", valor);
    }

    public String getIR() {
        return registros.get("00010");
    }

    public void setIR(String valor) {
        registros.put("00010", valor);
    }

    public String getAX() {
        return registros.get("00011");
    }

    public void setAX(String valor) {
        registros.put("00011", valor);
    }

    public String getBX() {
        return registros.get("00100");
    }

    public void setBX(String valor) {
        registros.put("00100", valor);
    }

    public String getCX() {
        return registros.get("00101");
    }

    public void setCX(String valor) {
        registros.put("00101", valor);
    }

    public String getDX() {
        return registros.get("00110");
    }

    public void setDX(String valor) {
        registros.put("00110", valor);
    }

    public String getCP() {
        return registros.get("00111");
    }

    public void setCP(String valor) {
        registros.put("00111", valor);
    }

    public String getSP() {
        return registros.get("01000");
    }

    public void setSP(String valor) {
        registros.put("01000", valor);
    }

    public String getBase() {
        return registros.get("01001");
    }

    public void setBase(String valor) {
        registros.put("01001", valor);
    }

    public String getLimite() {
        return registros.get("01010");
    }

    public void setLimite(String valor) {
        registros.put("01010", valor);
    }

    // -------- Información contable ----------
    public String getCpuAsignado() {
        return cpuAsignado;
    }

    public void setCpuAsignado(String cpuAsignado) {
        this.cpuAsignado = cpuAsignado;
    }

    public long getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(long tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public long getTiempoEjecutado() {
        return tiempoEjecutado;
    }

    public void setTiempoEjecutado(long tiempoEjecutado) {
        this.tiempoEjecutado = tiempoEjecutado;
    }

    // -------- Enlace a siguiente BCP ----------
    public BCP getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(BCP siguiente) {
        this.siguiente = siguiente;
    }

    public EstadoBCP getEstado() {
        return estado;
    }

    public void setEstado(EstadoBCP estado) {
        this.estado = estado;
    }

    public long getTiempoTotalEjecucion() {
        return tiempoTotalEjecucion;
    }

    public void setTiempoTotalEjecucion(long tiempoTotalEjecucion) {
        this.tiempoTotalEjecucion = tiempoTotalEjecucion;
        this.tiempoRestante = tiempoTotalEjecucion; // inicializar
    }

    public long getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(long tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }



    @Override
    public String toString() {
        return "Proceso " + id + " [" + nombre + "] - Estado: " + estado
                + " - Registros=" + registros;
    }
}
