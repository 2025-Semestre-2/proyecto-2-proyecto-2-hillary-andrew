package com.sistemasoperativos.pcvirtual.gui;

public class ConfigPC {
    private int ramMB;
    private int almacenamientoMB;
    private String algoritmoCPU;
    private String algoritmoMemoria;

    public ConfigPC(int ramMB, int almacenamientoMB, String algoritmoCPU, String algoritmoMemoria) {
        this.ramMB = ramMB;
        this.almacenamientoMB = almacenamientoMB;
        this.algoritmoCPU = algoritmoCPU;
        this.algoritmoMemoria = algoritmoMemoria;
    }

    public int getRamMB() { return ramMB; }
    public int getAlmacenamientoMB() { return almacenamientoMB; }
    public String getAlgoritmoCPU() { return algoritmoCPU; }
    public String getAlgoritmoMemoria() { return algoritmoMemoria; }
}
