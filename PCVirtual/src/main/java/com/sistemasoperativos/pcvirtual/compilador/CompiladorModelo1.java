package com.sistemasoperativos.pcvirtual.compilador;

import com.sistemasoperativos.pcvirtual.componentes.Conversor;
import java.io.*;
import java.util.*;

/**
 * Compilador ensamblador → binario
 * Implementado en Java puro sin CUP/JFlex
 */
public class CompiladorModelo1 implements Compilador {
    
    Conversor ConversorAsignado = new Conversor();

    // === Tablas de opcodes (5 bits) ===
    private static final Map<String, String> OPCODES = Map.ofEntries(
        Map.entry("LOAD",  "00000"),
        Map.entry("STORE", "00001"),
        Map.entry("MOV",   "00010"),
        Map.entry("ADD",   "00011"),
        Map.entry("SUB",   "00100"),
        Map.entry("INC",   "00101"),
        Map.entry("DEC",   "00110"),
        Map.entry("SWAP",  "00111"),
        Map.entry("INT",   "01000"),
        Map.entry("JMP",   "01001"),
        Map.entry("CMP",   "01010"),
        Map.entry("JE",    "01011"),
        Map.entry("JNE",   "01100"),
        Map.entry("PARAM", "01101"),
        Map.entry("PUSH",  "01110"),
        Map.entry("POP",   "01111")
    );

    // === Códigos de registros (5 bits) ===
    private static final Map<String, String> REGISTROS = Map.ofEntries(
        Map.entry("AX", "00011"),
        Map.entry("BX", "00100"),
        Map.entry("CX", "00101"),
        Map.entry("DX", "00110"),
        Map.entry("AC", "00001")
    );

    // === Códigos de interrupciones ===
    private static final Map<String, String> INTS = Map.ofEntries(
        Map.entry("20H", "00000"),
        Map.entry("10H", "00001"),
        Map.entry("09H", "00010")
    );

    public CompiladorModelo1() {}

    @Override
    public List<String> Compilar(File archivo) throws Exception {
        List<String> instruccionesBinarias = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numLinea = 0;

            while ((linea = br.readLine()) != null) {
                numLinea++;
                linea = linea.trim();

                if (linea.isEmpty() || linea.startsWith(";")) {
                    continue; // comentarios o líneas vacías
                }

                String bin = traducirInstruccion(linea, numLinea);
                System.out.println("Línea: " + linea + "\nNumero Línea: " + numLinea + "\nBinario: " + bin + "\n==++==++==++==++");
                instruccionesBinarias.add(bin);
            }
        }

        return instruccionesBinarias;
    }

    /**
     * Traduce una línea ASM a binario.
     */
    private String traducirInstruccion(String linea, int numLinea) throws Exception {
        String[] partes = linea.split("\\s+|,\\s*"); // divide por espacios o comas
        String instruccion = partes[0].toUpperCase();

        if (!OPCODES.containsKey(instruccion)) {
            throw new Exception("Error en línea " + numLinea + ": instrucción desconocida '" + instruccion + "'");
        }

        String opcode = OPCODES.get(instruccion);

        // === Casos según la cantidad de argumentos ===
        switch (instruccion) {
            case "INC":
            case "DEC":
                if (partes.length == 1) {
                    return opcode; // sin argumentos
                } else if (partes.length == 2) {
                    return opcode + obtenerRegistroONumero(partes[1], numLinea);
                } else {
                    throw new Exception("Error en línea " + numLinea + ": uso incorrecto de " + instruccion);
                }

            case "LOAD":
            case "STORE":
            case "ADD":
            case "SUB":
            case "JMP":
            case "JE":
            case "JNE":
            case "PUSH":
            case "POP":
                if (partes.length != 2) {
                    throw new Exception("Error en línea " + numLinea + ": " + instruccion + " requiere 1 argumento");
                }
                return opcode + obtenerRegistroONumero(partes[1], numLinea);

            case "MOV":
            case "SWAP":
            case "CMP":
                if (partes.length != 3) {
                    throw new Exception("Error en línea " + numLinea + ": " + instruccion + " requiere 2 argumentos");
                }
                return opcode + obtenerRegistroONumero(partes[1], numLinea)
                              + obtenerRegistroONumero(partes[2], numLinea);

            case "PARAM":
                if (partes.length < 2) {
                    throw new Exception("Error en línea " + numLinea + ": PARAM requiere argumentos");
                }
                StringBuilder args = new StringBuilder();
                for (int i = 1; i < partes.length; i++) {
                    args.append(obtenerNumero(partes[i], numLinea));
                }
                return opcode + args.toString();

            case "INT":
                if (partes.length != 2) {
                    throw new Exception("Error en línea " + numLinea + ": INT requiere un valor (20H, 10H, 09H)");
                }
                String intCode = INTS.get(partes[1].toUpperCase());
                if (intCode == null) {
                    throw new Exception("Error en línea " + numLinea + ": interrupción desconocida " + partes[1]);
                }
                return opcode + intCode;

            default:
                throw new Exception("Error interno: instrucción '" + instruccion + "' no manejada.");
        }
    }

    /**
     * Obtiene el código binario de un registro o número literal.
     */
    private String obtenerRegistroONumero(String arg, int numLinea) throws Exception {
        arg = arg.toUpperCase();

        if (REGISTROS.containsKey(arg)) {
            return REGISTROS.get(arg);
        }
        if (arg.matches("-?\\d+")) {
            return obtenerNumero(arg, numLinea);
        }
        throw new Exception("Error en línea " + numLinea + ": argumento inválido '" + arg + "'");
    }

    /**
     * Convierte un número decimal a binario de 8 bits.
     */
    private String obtenerNumero(String num, int numLinea) throws Exception {
        try {
            int valor = Integer.parseInt(num);
            return ConversorAsignado.ConvertirIntegerABits(valor);
        } catch (NumberFormatException e) {
            throw new Exception("Error en línea " + numLinea + ": número inválido '" + num + "'");
        }
    }
}
