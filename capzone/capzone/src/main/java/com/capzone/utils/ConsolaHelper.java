package com.capzone.utils;

import java.util.Scanner;

/** Encapsula el Scanner: siempre lee líneas completas, así se evita el bug de nextInt()+nextLine(). */
public class ConsolaHelper {
    private final Scanner scanner = new Scanner(System.in);

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor, ingresa un número válido.");
            }
        }
    }

    public void imprimir(String texto) {
        System.out.println(texto);
    }
}
