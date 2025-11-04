package com.sistemasoperativos.pcvirtual.gui;

import com.sistemasoperativos.pcvirtual.controlador.Controlador;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;

/**
 * Diálogo para configurar y crear una PC virtual.
 * Permite definir la RAM, almacenamiento, y los algoritmos de CPU y Memoria.
 */
public class CrearPCDialog extends Dialog<ConfigPC> {

    private final Controlador controlador;

    public CrearPCDialog(Controlador controlador, Window owner) {
        this.controlador = controlador;

        setTitle("Crear PC");
        setHeaderText("Configure la PC Virtual");

        if (owner != null) {
            initOwner(owner);
        }

        // --- Botones ---
        ButtonType btCrear = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(btCrear, ButtonType.CANCEL);

        // --- Campos de texto ---
        TextField tfRam   = new TextField("1024");
        TextField tfDisco = new TextField("8192");

        tfRam.setPromptText("RAM (MB)");
        tfDisco.setPromptText("Almacenamiento (MB)");

        // --- ComboBox para CPU ---
        ComboBox<String> cbAlgoritmoCPU = new ComboBox<>();
        cbAlgoritmoCPU.getItems().addAll("FCFS", "SRT", "SJF", "RR", "HRRN");
        cbAlgoritmoCPU.setValue("FCFS");

        // --- ComboBox para Memoria ---
        ComboBox<String> cbAlgoritmoMemoria = new ComboBox<>();
        cbAlgoritmoMemoria.getItems().addAll(
                "Paginación (F/V)",
                "Segmentación (F)",
                "Fija (F/V)",
                "Dinámica (F/V)"
        );
        cbAlgoritmoMemoria.setValue("Paginación (F/V)");
        
        ComboBox<String> cbCantidadCPUs = new ComboBox<>();
        cbCantidadCPUs.getItems().addAll("1", "2", "3", "4");
        cbCantidadCPUs.setValue("1");

        // --- Layout ---
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(15));

        grid.addRow(0, new Label("Memoria RAM (MB):"), tfRam);
        grid.addRow(1, new Label("Almacenamiento (MB):"), tfDisco);
        grid.addRow(2, new Label("Algoritmo CPU:"), cbAlgoritmoCPU);
        grid.addRow(3, new Label("Algoritmo Memoria:"), cbAlgoritmoMemoria);
        grid.addRow(4, new Label("Cantidad de CPUs:"), cbCantidadCPUs);

        getDialogPane().setContent(grid);

        // --- Acción del botón Crear ---
        Button btnCrear = (Button) getDialogPane().lookupButton(btCrear);
        btnCrear.addEventFilter(javafx.event.ActionEvent.ACTION, evt -> {
            try {
                int ram   = parseEnteroPositivo(tfRam.getText());
                int disco = parseEnteroPositivo(tfDisco.getText());
                String algoritmoCPU = cbAlgoritmoCPU.getValue();
                String algoritmoMemoria = cbAlgoritmoMemoria.getValue();
                int algoritmoSeleccionado = 0;
                switch(algoritmoCPU){
                    case "FCFS": algoritmoSeleccionado = 0;
                        break;
                    case "SRT": algoritmoSeleccionado = 1;
                        break;
                    case "SJF": algoritmoSeleccionado = 2;
                        break;
                    case "RR": algoritmoSeleccionado = 3;
                        break;
                    case "HRRN": algoritmoSeleccionado = 4;
                        break;
                }
                int cargadorSeleccionado = 0;
                switch(algoritmoMemoria){
                    case "Paginación (F/V)": cargadorSeleccionado = 1;
                        break;
                    case "Segmentación (F)": cargadorSeleccionado = 2;
                        break;
                    case "Fija (F/V)": cargadorSeleccionado = 3;
                        break;
                    case "Dinámica (F/V)": cargadorSeleccionado = 4;
                        break;
                }
                int cantidadCPUs = Integer.parseInt(cbCantidadCPUs.getValue());
                controlador.CrearPC(cantidadCPUs, ram, disco, cargadorSeleccionado, algoritmoSeleccionado);
                System.out.println("Algoritmo CPU: " + algoritmoCPU);
                System.out.println("Algoritmo Memoria: " + algoritmoMemoria);

            } catch (IllegalArgumentException ex) {
                mostrarError("Entrada inválida", ex.getMessage());
                evt.consume();
            } catch (Exception ex) {
                mostrarError("No se pudo crear la PC",
                        "Error al crear la PC: " + ex.getMessage());
                evt.consume();
            }
        });

        // --- Resultado del diálogo ---
        setResultConverter(bt -> {
            if (bt == btCrear) {
                int ram   = parseEnteroPositivo(tfRam.getText());
                int disco = parseEnteroPositivo(tfDisco.getText());
                String algoritmoCPU = cbAlgoritmoCPU.getValue();
                String algoritmoMemoria = cbAlgoritmoMemoria.getValue();
                return new ConfigPC(ram, disco, algoritmoCPU, algoritmoMemoria);
            }
            return null;
        });
    }

    /** Parsea un entero positivo o lanza {@link IllegalArgumentException}. */
    private int parseEnteroPositivo(String s) {
        try {
            int v = Integer.parseInt(s.trim());
            if (v <= 0) throw new NumberFormatException();
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debe ingresar un entero positivo.");
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert a = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        a.setHeaderText(titulo);
        a.showAndWait();
    }
}
