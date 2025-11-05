package com.sistemasoperativos.pcvirtual.procesos;

import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BCP {
    private Conversor ConversorAsignado;

    private final int id;
    private final String nombre;
    private int prioridad;
    private EstadoBCP estado;

    // -------- Registros --------
    private final Map<String, String> registros; // PC, AC, IR, AX, BX, CX, DX, SP, CP, BS, LI

    // -------- Información contable --------
    private String cpuAsignado;
    private int rafaga;
    private int tiempoTranscurrido;
    private int tiempoLlegada;

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
        this.cpuAsignado = "";
        this.rafaga = 0;
        this.tiempoTranscurrido = 0;
        this.tiempoLlegada = 0;
    }

    public Conversor getConversorAsignado() {
        return ConversorAsignado;
    }

    public void setConversorAsignado(Conversor ConversorAsignado) {
        this.ConversorAsignado = ConversorAsignado;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(int tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
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

    public void setPrioridad(int prioridad){
        this.prioridad = prioridad;
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

    public EstadoBCP getEstado() {
        return estado;
    }

    public void setEstado(EstadoBCP estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Proceso " + id + " [" + nombre + "] - Estado: " + estado
                + " - Registros=" + registros;
    }
    
    public Map<String, String> GetInfo() {
        Map<String, String> info = new LinkedHashMap<>();

        // Información general del proceso
        info.put("ID", String.valueOf(id));
        info.put("Nombre", nombre);
        info.put("Prioridad", String.valueOf(prioridad));
        info.put("Estado", estado.toString());
        info.put("CPU asignado", cpuAsignado);

        // Información contable
        info.put("Ráfaga", String.valueOf(rafaga));
        info.put("Tiempo transcurrido", String.valueOf(tiempoTranscurrido));

        // -------- Registros --------
        // Se asume que ConversorAsignado convierte una cadena binaria a entero
        info.put("PC", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getPC())));
        info.put("AC", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getAC())));
        info.put("IR", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getIR())));
        info.put("AX", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getAX())));
        info.put("BX", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getBX())));
        info.put("CX", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getCX())));
        info.put("DX", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getDX())));
        info.put("SP", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getSP())));
        info.put("CP", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getCP())));
        info.put("BS", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getBS())));
        info.put("LI", String.valueOf(ConversorAsignado.ConvertirBitsAInteger(getLimite())));

        return info;
    }


    public int getRafaga() {
        return rafaga;
    }

    public void setRafaga(int rafaga) {
        this.rafaga = rafaga;
    }

    public int getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }

    public void setTiempoTranscurrido(int tiempoTranscurrido) {
        this.tiempoTranscurrido = tiempoTranscurrido;
    }
    
    public void sumarTiempo(){
        this.tiempoTranscurrido++;
    }
}
