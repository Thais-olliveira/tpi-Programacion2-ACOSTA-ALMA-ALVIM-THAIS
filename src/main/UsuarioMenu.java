/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import entities.Usuario;
import enums.Rol;
import java.util.List;
import java.util.Scanner;
import service.UsuarioService;

/**
 *
 * @author Alma
 */
/**
 * Vista/Menú para la gestión de Usuarios.
 */
public class UsuarioMenu {

    private final UsuarioService service;
    private final Scanner scanner;

    public UsuarioMenu(UsuarioService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    UsuarioMenu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n=== USUARIOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            int opcion = scanner.nextInt();
           // int opcion = Util.leerEntero(scanner);
            switch (opcion) {
                case 1 ->
                    listar();
                case 2 ->
                    crear();
                case 3 ->
                    editar();
                case 4 ->
                    eliminar();
                case 0 ->
                    volver = true;
                default ->
                    System.out.println("⚠ Opción inválida.");
            }
        }
    }

    // ── HU-USR-01 Listar ──────────────────────────────────────────────────
    private void listar() {
        List<Usuario> lista = service.listarActivos();
        if (lista.isEmpty()) {
            System.out.println("ℹ No hay usuarios cargados.");
        } else {
            System.out.println("\n── Usuarios activos ──");
            lista.forEach(System.out::println);
        }
    }

    // ── HU-USR-02 Crear ───────────────────────────────────────────────────
    private void crear() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine().trim();
            System.out.print("Mail: ");
            String mail = scanner.nextLine().trim();
            System.out.print("Celular: ");
            String celular = scanner.nextLine().trim();
            System.out.print("Contraseña: ");
            String pass = scanner.nextLine().trim();
            Rol rol = elegirRol();

            Usuario nuevo = service.crear(nombre, apellido, mail, celular, pass, rol);
            System.out.println("✔ Usuario creado con ID: " + nuevo.getId());
        } catch (Exception e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    // ── HU-USR-03 Editar ──────────────────────────────────────────────────
    private void editar() {
        try {
            listar();
            System.out.print("ID de usuario a editar: ");
            long id = scanner.nextLong();
                    //Util.leerLong(scanner);

            System.out.print("Nuevo nombre (Enter para conservar): ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Nuevo apellido (Enter para conservar): ");
            String apellido = scanner.nextLine().trim();
            System.out.print("Nuevo mail (Enter para conservar): ");
            String mail = scanner.nextLine().trim();
            System.out.print("Nuevo celular (Enter para conservar): ");
            String celular = scanner.nextLine().trim();
            System.out.print("Nueva contraseña (Enter para conservar): ");
            String pass = scanner.nextLine().trim();
            System.out.print("Nuevo rol (1=ADMIN, 2=USUARIO, Enter para conservar): ");
            String rolStr = scanner.nextLine().trim();
            Rol rol = rolStr.equals("1") ? Rol.ADMIN
                    : rolStr.equals("2") ? Rol.USUARIO
                    : null;

            service.editar(id,
                    nombre.isEmpty() ? null : nombre,
                    apellido.isEmpty() ? null : apellido,
                    mail.isEmpty() ? null : mail,
                    celular.isEmpty() ? null : celular,
                    pass.isEmpty() ? null : pass,
                    rol);
            System.out.println("✔ Usuario actualizado.");
        } catch (Exception e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    // ── HU-USR-04 Eliminar ────────────────────────────────────────────────
    private void eliminar() {
        try {
            listar();
            System.out.print("ID de usuario a eliminar: ");
            long id = scanner.nextLong();
                    //Util.leerLong(scanner);
            System.out.print("¿Confirmar? (S/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
                System.out.println("ℹ Cancelado.");
                return;
            }
            service.eliminar(id);
            System.out.println("✔ Usuario eliminado (baja lógica).");
        } catch (Exception e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private Rol elegirRol() {
        System.out.println("Roles: 1=ADMIN  2=USUARIO");
        System.out.print("Seleccione rol: ");
        int r = scanner.nextInt() ;
        return r == 1 ? Rol.ADMIN : Rol.USUARIO;
    }
}
