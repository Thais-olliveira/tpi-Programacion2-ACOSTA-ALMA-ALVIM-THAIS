package main;

import java.util.Scanner;

public abstract class MenuBase {

    protected Scanner sc;

    public MenuBase(Scanner sc) {
        this.sc = sc;
    }

    public abstract void mostrar();

    protected int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    protected int leerEntero(String prompt) {
        System.out.print(prompt);
        return leerEntero();
    }

    protected Long leerLong(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Long.parseLong(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un id válido: ");
            }
        }
    }

    protected double leerDouble(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    protected String leerTexto(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    protected String leerTextoObligatorio(String prompt) {
        System.out.print(prompt);
        String texto = sc.nextLine().trim();
        while (texto.isEmpty()) {
            System.out.println("El campo no puede estar vacío");
            System.out.print(prompt);
            texto = sc.nextLine().trim();
        }
        return texto;
    }
}