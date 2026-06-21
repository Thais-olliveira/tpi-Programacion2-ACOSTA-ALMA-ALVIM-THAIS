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
public class UsuarioMenu extends MenuBase {

    private final UsuarioService service;
    
  public UsuarioMenu(UsuarioService service, Scanner scanner) {
        super(scanner);                      
        this.service = service;
        } 
    
    @Override
    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n=== USUARIOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            opcion = leerEntero();
            switch (opcion) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> eliminar();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
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
            String nombre   = leerTextoObligatorio("Nombre: ");
            String apellido = leerTextoObligatorio("Apellido: ");
            String mail     = leerTextoObligatorio("Mail: ");
            String celular  = leerTexto("Celular: ");
            String pass     = leerTextoObligatorio("Contraseña: ");
            Rol rol = elegirRol();

            Usuario nuevo = service.crear(nombre, apellido, mail, celular, pass, rol);
            System.out.println("Usuario creado con ID: " + nuevo.getId());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── HU-USR-03 Editar ──────────────────────────────────────────────────
    private void editar() {
        listar();
        Long id = leerLong("ID de usuario a editar: ");
        try {
            String nombre   = leerTexto("Nuevo nombre (Enter para conservar): ");
            String apellido = leerTexto("Nuevo apellido (Enter para conservar): ");
            String mail     = leerTexto("Nuevo mail (Enter para conservar): ");
            String celular  = leerTexto("Nuevo celular (Enter para conservar): ");
            String pass     = leerTexto("Nueva contraseña (Enter para conservar): ");
            String rolStr   = leerTexto("Nuevo rol (1=ADMIN, 2=USUARIO, Enter para conservar): ");
            Rol rol = rolStr.equals("1") ? Rol.ADMIN
                    : rolStr.equals("2") ? Rol.USUARIO
                    : null;

            service.editar(id,
                    nombre.isEmpty()   ? null : nombre,
                    apellido.isEmpty() ? null : apellido,
                    mail.isEmpty()     ? null : mail,
                    celular.isEmpty()  ? null : celular,
                    pass.isEmpty()     ? null : pass,
                    rol);
            System.out.println("Usuario actualizado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── HU-USR-04 Eliminar ────────────────────────────────────────────────
    private void eliminar() {
        listar();
        Long id = leerLong("ID de usuario a eliminar: ");
        String confirma = leerTexto("¿Confirmar? (S/N): ");
        if (!confirma.equalsIgnoreCase("S")) {
            System.out.println("Cancelado.");
            return;
        }
        try {
            service.eliminar(id);
            System.out.println("Usuario eliminado (baja lógica).");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private Rol elegirRol() {
        System.out.println("Roles: 1=ADMIN  2=USUARIO");
        int r = leerEntero("Seleccione rol: ");
        return r == 1 ? Rol.ADMIN : Rol.USUARIO;
    }
}
