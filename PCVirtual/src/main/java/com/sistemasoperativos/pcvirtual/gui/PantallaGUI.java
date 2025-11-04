package com.sistemasoperativos.pcvirtual.gui;

import com.sistemasoperativos.pcvirtual.controlador.Controlador;
import com.sistemasoperativos.pcvirtual.procesos.BCP;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PantallaGUI extends Application {

    private final Controlador controlador = new Controlador();
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // --- CPU 1–4 ---
    private TableView<MemoriaFila> tablaRegistrosCPU1;
    private TableView<MemoriaFila> tablaRegistrosCristianoCPU1;
    private ListView<String> listaBCP_CPU1;
    private ListView<String> listaBCP_Cristiano_CPU1;

    private TableView<MemoriaFila> tablaRegistrosCPU2;
    private TableView<MemoriaFila> tablaRegistrosCristianoCPU2;
    private ListView<String> listaBCP_CPU2;
    private ListView<String> listaBCP_Cristiano_CPU2;

    private TableView<MemoriaFila> tablaRegistrosCPU3;
    private TableView<MemoriaFila> tablaRegistrosCristianoCPU3;
    private ListView<String> listaBCP_CPU3;
    private ListView<String> listaBCP_Cristiano_CPU3;

    private TableView<MemoriaFila> tablaRegistrosCPU4;
    private TableView<MemoriaFila> tablaRegistrosCristianoCPU4;
    private ListView<String> listaBCP_CPU4;
    private ListView<String> listaBCP_Cristiano_CPU4;

    // --- Memoria y Almacenamiento compartidos ---
    private TableView<MemoriaFila> tablaMemoria;
    private TableView<MemoriaFila> tablaMemoriaCristiano;
    private TableView<MemoriaFila> tablaDisco;
    private TableView<MemoriaFila> tablaDiscoCristiano;

    private static TextArea pantallaSalida;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PC Virtual - 4 CPUs con memoria y almacenamiento compartidos");

        // ---------------- Barra superior ----------------
        Button btnCrearPC = new Button("Crear PC");
        Button btnCargar = new Button("Cargar archivo");
        Button btnEjecutar = new Button("Ejecutar automáticamente");
        Button btnEstadisticas = new Button("Estadísticas");

        HBox barra = new HBox(15, btnCrearPC, btnCargar, btnEjecutar, btnEstadisticas);
        barra.setAlignment(Pos.CENTER);
        barra.setPadding(new Insets(10));

        // ---------------- CPUs ----------------
        TabPane tabCPUs = new TabPane();
        tabCPUs.getTabs().add(crearTabCPU("CPU 1"));
        tabCPUs.getTabs().add(crearTabCPU("CPU 2"));
        tabCPUs.getTabs().add(crearTabCPU("CPU 3"));
        tabCPUs.getTabs().add(crearTabCPU("CPU 4"));

        // ---------------- Memoria y Disco compartidos ----------------
        VBox panelMemoria = new VBox(8);
        panelMemoria.setPadding(new Insets(10));

        Label lblMemoria = new Label("Memoria (RAM compartida)");
        tablaMemoria = crearTablaMemoriaDisco();
        Label lblMemoriaCristiano = new Label("Memoria Cristiano");
        tablaMemoriaCristiano = crearTablaMemoriaDisco();

        Label lblDisco = new Label("Almacenamiento (Disco compartido)");
        tablaDisco = crearTablaMemoriaDisco();
        Label lblDiscoCristiano = new Label("Almacenamiento Cristiano");
        tablaDiscoCristiano = crearTablaMemoriaDisco();

        panelMemoria.getChildren().addAll(
            lblMemoria, tablaMemoria,
            lblMemoriaCristiano, tablaMemoriaCristiano,
            lblDisco, tablaDisco,
            lblDiscoCristiano, tablaDiscoCristiano
        );
        
        // ---------------- Pantalla de salida ----------------
        pantallaSalida = new TextArea();
        pantallaSalida.setEditable(false);
        pantallaSalida.setPrefHeight(120);
        VBox panelSalida = new VBox(5, new Label("Pantalla"), pantallaSalida);
        panelSalida.setPadding(new Insets(10));

        // ---------------- Layout principal ----------------
        BorderPane root = new BorderPane();
        root.setTop(barra);
        root.setCenter(tabCPUs);
        root.setRight(panelMemoria);
        root.setBottom(panelSalida);

        Scene scene = new Scene(root, 1300, 750);
        primaryStage.setScene(scene);
        primaryStage.show();

        // ---------------- Acciones ----------------
        btnCargar.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Seleccionar programa");
            File archivo = fc.showOpenDialog(primaryStage);
            if (archivo != null) {
                try {
                    controlador.CargarPrograma(archivo);
                    escribir("Programa cargado: " + archivo.getName());
                    actualizarMemoria();
                    actualizarDisco();
                } catch (Exception ex) {
                    escribir("Error al cargar: " + ex.getMessage());
                }
            }
        });

        btnEjecutar.setOnAction(e -> {
            Runnable tarea = () -> {
                try {
                    controlador.EjecutarInstruccion();
                    actualizarTodo();
                } catch (Exception ex) {
                    escribir("Error al ejecutar: " + ex.getMessage());
                }
            };
            scheduler.scheduleAtFixedRate(tarea, 0, 1, TimeUnit.SECONDS);
        });

        btnEstadisticas.setOnAction(e -> {
            escribir("Estadísticas: (en desarrollo)");
        });
        
        btnCrearPC.setOnAction(e -> {
            CrearPCDialog dlg = new CrearPCDialog(controlador, primaryStage);
            dlg.showAndWait();
        });
    }

    // ---------------- Crear pestañas por CPU ----------------
    private Tab crearTabCPU(String nombre) {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));

        TableView<MemoriaFila> tablaReg = crearTablaMemoriaDisco();
        TableView<MemoriaFila> tablaRegC = crearTablaMemoriaDisco();
        ListView<String> listaBCP = new ListView<>();
        ListView<String> listaBCPC = new ListView<>();

        listaBCP.setPlaceholder(new Label("Sin BCP activo"));
        listaBCPC.setPlaceholder(new Label("Sin BCP Cristiano activo"));

        panel.getChildren().addAll(
            new Label("BCP"), listaBCP,
            new Label("BCP Cristiano"), listaBCPC,
            new Label("Registros"), tablaReg,
            new Label("Registros Cristiano"), tablaRegC
        );
        ScrollPane scroll = new ScrollPane(panel);
        scroll.setFitToWidth(true);
        Tab tab = new Tab(nombre, scroll);
        tab.setClosable(false);
        switch (nombre) {
            case "CPU 1":
                tablaRegistrosCPU1 = tablaReg;
                tablaRegistrosCristianoCPU1 = tablaRegC;
                listaBCP_CPU1 = listaBCP;
                listaBCP_Cristiano_CPU1 = listaBCPC;
                break;

            case "CPU 2":
                tablaRegistrosCPU2 = tablaReg;
                tablaRegistrosCristianoCPU2 = tablaRegC;
                listaBCP_CPU2 = listaBCP;
                listaBCP_Cristiano_CPU2 = listaBCPC;
                break;

            case "CPU 3":
                tablaRegistrosCPU3 = tablaReg;
                tablaRegistrosCristianoCPU3 = tablaRegC;
                listaBCP_CPU3 = listaBCP;
                listaBCP_Cristiano_CPU3 = listaBCPC;
                break;

            case "CPU 4":
                tablaRegistrosCPU4 = tablaReg;
                tablaRegistrosCristianoCPU4 = tablaRegC;
                listaBCP_CPU4 = listaBCP;
                listaBCP_Cristiano_CPU4 = listaBCPC;
                break;

            default:
                break;
        }
        return tab;
    }

    // ---------------- Crear tabla genérica ----------------
    private TableView<MemoriaFila> crearTablaMemoriaDisco() {
        TableView<MemoriaFila> tabla = new TableView<>();
        TableColumn<MemoriaFila, String> colLlave = new TableColumn<>("Dirección");
        TableColumn<MemoriaFila, String> colValor = new TableColumn<>("Valor");
        colLlave.setCellValueFactory(data -> data.getValue().llaveProperty());
        colValor.setCellValueFactory(data -> data.getValue().valorProperty());
        tabla.getColumns().addAll(colLlave, colValor);
        tabla.setPrefHeight(150);
        tabla.setSortPolicy(t -> null);
        return tabla;
    }

    // ---------------- Actualización de interfaz ----------------
    private void actualizarTodo() {
        actualizarMemoria();
        actualizarDisco();
        actualizarCPUs();
    }

    private void actualizarCPUs() {
        try {
            List<Map<String, String>> listaRegistros = controlador.ObtenerRegistros();
            List<Map<String, String>> listaBCPs = controlador.ObtenerBCPs();

            for (int i = 0; i < 4; i++) {
                Map<String, String> regs = listaRegistros.get(i);
                Map<String, String> bcp = listaBCPs.get(i);

                TableView<MemoriaFila> tablaReg;
                TableView<MemoriaFila> tablaRegC;

                switch (i) {
                    case 0:
                        tablaReg = tablaRegistrosCPU1;
                        tablaRegC = tablaRegistrosCristianoCPU1;
                        break;
                    case 1:
                        tablaReg = tablaRegistrosCPU2;
                        tablaRegC = tablaRegistrosCristianoCPU2;
                        break;
                    case 2:
                        tablaReg = tablaRegistrosCPU3;
                        tablaRegC = tablaRegistrosCristianoCPU3;
                        break;
                    default:
                        tablaReg = tablaRegistrosCPU4;
                        tablaRegC = tablaRegistrosCristianoCPU4;
                        break;
                }

                ObservableList<MemoriaFila> datos = FXCollections.observableArrayList();
                regs.forEach((k, v) -> datos.add(new MemoriaFila(k, v)));
                tablaReg.setItems(datos);

                Map<String, String> regsC = ConversorRegistros.ConvertirACristiano(regs);
                ObservableList<MemoriaFila> datosC = FXCollections.observableArrayList();
                regsC.forEach((k, v) -> datosC.add(new MemoriaFila(k, v)));
                tablaRegC.setItems(datosC);

                ListView<String> listaBCP;
                ListView<String> listaBCPC;

                switch (i) {
                    case 0:
                        listaBCP = listaBCP_CPU1;
                        listaBCPC = listaBCP_Cristiano_CPU1;
                        break;
                    case 1:
                        listaBCP = listaBCP_CPU2;
                        listaBCPC = listaBCP_Cristiano_CPU2;
                        break;
                    case 2:
                        listaBCP = listaBCP_CPU3;
                        listaBCPC = listaBCP_Cristiano_CPU3;
                        break;
                    default:
                        listaBCP = listaBCP_CPU4;
                        listaBCPC = listaBCP_Cristiano_CPU4;
                        break;
                }

                listaBCP.getItems().clear();
                listaBCPC.getItems().clear();

                if (bcp == null || bcp.isEmpty()) {
                    listaBCP.getItems().add("No hay BCP activo");
                    continue;
                }

                ObservableList<String> datosBCP = FXCollections.observableArrayList();
                bcp.forEach((k, v) -> datosBCP.add(k + ": " + v));
                listaBCP.setItems(datosBCP);

                Map<String, String> bcpC = ConversorRegistros.ConvertirACristiano(bcp);
                ObservableList<String> datosBCPC = FXCollections.observableArrayList();
                bcpC.forEach((k, v) -> datosBCPC.add(k + ": " + v));
                listaBCPC.setItems(datosBCPC);
            }

        } catch (Exception ex) {
            escribir("Error al actualizar CPUs: " + ex.getMessage());
        }
    }

    private void actualizarMemoria() {
        Map<String, String> memoria = controlador.TraerMemoria();
        ObservableList<MemoriaFila> datos = FXCollections.observableArrayList();
        memoria.forEach((k, v) -> datos.add(new MemoriaFila(k, v)));
        tablaMemoria.setItems(datos);

        Map<String, String> memC = ConversorMemoria.ConvertirACristiano(memoria);
        ObservableList<MemoriaFila> datosC = FXCollections.observableArrayList();
        memC.forEach((k, v) -> datosC.add(new MemoriaFila(k, v)));
        tablaMemoriaCristiano.setItems(datosC);
    }

    private void actualizarDisco() {
        Map<String, String> disco = controlador.TraerAlmacenamiento();
        ObservableList<MemoriaFila> datos = FXCollections.observableArrayList();
        disco.forEach((k, v) -> datos.add(new MemoriaFila(k, v)));
        tablaDisco.setItems(datos);

        Map<String, String> discoC = ConversorMemoria.ConvertirACristiano(disco);
        ObservableList<MemoriaFila> datosC = FXCollections.observableArrayList();
        discoC.forEach((k, v) -> datosC.add(new MemoriaFila(k, v)));
        tablaDiscoCristiano.setItems(datosC);
    }

    // ---------------- Utilidades ----------------
    public static void escribir(String texto) {
        pantallaSalida.appendText(texto + "\n");
    }

    public static class MemoriaFila {
        private final SimpleStringProperty llave;
        private final SimpleStringProperty valor;
        public MemoriaFila(String llave, String valor) {
            this.llave = new SimpleStringProperty(llave);
            this.valor = new SimpleStringProperty(valor);
        }
        public SimpleStringProperty llaveProperty() { return llave; }
        public SimpleStringProperty valorProperty() { return valor; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
