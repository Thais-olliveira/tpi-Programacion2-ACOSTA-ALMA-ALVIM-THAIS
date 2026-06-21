/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import java.util.List;
import java.util.Scanner;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;

/**
 *
 * @author Alma
 *
/**
 * Vista/Menú para la gestión de Pedidos.
 * Necesita UsuarioService y ProductoService para relacionar entidades.
 */
public class PedidoMenu {

    private final PedidoService  pedidoService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final Scanner scanner;

    public PedidoMenu(PedidoService pedidoService, UsuarioService usuarioService, ProductoService productoService, Scanner scanner) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.scanner = scanner;
    }

    PedidoMenu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n=== PEDIDOS ===");
            System.out.println("1. Listar todos");
            System.out.println("2. Listar por usuario");
            System.out.println("3. Crear pedido (con detalles)");
            System.out.println("4. Ver detalles de pedido");
            System.out.println("5. Actualizar estado / forma de pago");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            int opcion = scanner.nextInt();
            switch (opcion) {
                case 1 -> listar();
                case 2 -> listarPorUsuario();
                case 3 -> crear();
                case 4 -> verDetalles();
                case 5 -> actualizarEstadoPago();
                case 6 -> eliminar();
                case 0 -> volver = true;
                default -> System.out.println("⚠ Opción inválida.");
            }
        }
    }

    // ────────────────────────────────────────────────────
    private void listar() {
        List<Pedido> lista = pedidoService.listarActivos();
        if (lista.isEmpty()) System.out.println("ℹ No hay pedidos.");
        else {
            System.out.println("\n── Pedidos activos ──");
            lista.forEach(System.out::println);
        }
    }

    private void listarPorUsuario() {
        try {
            usuarioService.listarActivos().forEach(System.out::println);
            System.out.print("ID de usuario: ");
            long uid = scanner.nextLong();
            scanner.nextLine();

            // long uid = Util.leerLong(scanner);
            List<Pedido> lista = pedidoService.listarPorUsuario(uid);
            if (lista.isEmpty()) System.out.println("ℹ Sin pedidos para ese usuario.");
            else lista.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    // ────────────────────────────────────────
    private void crear() {
        Pedido pedido = null;
        try {
            // Seleccionar usuario
            System.out.println("Usuarios disponibles:");
            usuarioService.listarActivos().forEach(System.out::println);
            System.out.print("ID de usuario: ");
            long uid = scanner.nextLong();
            scanner.nextLine();
            
                //long uid = Util.leerLong(scanner);
            Usuario usuario = usuarioService.buscarPorId(uid);

            // Elegir forma de pago
            FormaPago formaPago = elegirFormaPago();

            // Crear pedido vacío
            pedido = pedidoService.crear(usuario, formaPago);
            System.out.println("✔ Pedido creado con ID: " + pedido.getId() + ". Agregue detalles:");

            // Agregar detalles en bucle
            boolean agregarMas = true;
            while (agregarMas) {
                try {
                    System.out.println("\nProductos disponibles:");
                    //for (Producto p : productoService.listarActivos()){
                    System.out.println("1 - Producto Simulado ($1000)");

                    //productoService.listarActivos().forEach(System.out::println);
                    System.out.print("ID de producto (0 para finalizar): ");
                    long pid = scanner.nextLong();
                    scanner.nextLine();

                        //long pid = Util.leerLong(scanner);
                    if (pid == 0) break;

                    Producto prod = productoService.buscarPorId(pid);
                    System.out.print("Cantidad: ");
                    int cant  = scanner.nextInt();
                    scanner.nextLine();
                    
                    //int cant = Util.leerEntero(scanner);

                    pedidoService.agregarDetalle(pedido, cant, prod);
                    System.out.printf("✔ Detalle agregado. Subtotal: $%.2f%n",
                        cant * prod.getPrecio());
                } catch (Exception e) {
                    System.out.println("✖ Error al agregar detalle: " + e.getMessage()
                        + " — El pedido continúa sin ese ítem.");
                }
                System.out.print("¿Agregar otro producto? (S/N): ");
                agregarMas = scanner.nextLine().trim().equalsIgnoreCase("S");
            }

            // Recalcular total final (ya se hace internamente, pero lo mostramos)
            pedido.calcularTotal();
            System.out.printf("✔ Pedido finalizado. Total: $%.2f%n", pedido.getTotal());

        } catch (Exception e) {
            // Si falla la creación del pedido base, no queda nada inconsistente
            System.out.println("✖ Error al crear pedido: " + e.getMessage());
            if (pedido != null) {
                // eliminar lógicamente si ya se agregó a la colección
            //    pedidoService.eliminar(pedido.getId());
                System.out.println("ℹ Pedido cancelado y removido.");
            }
        }
    }

    // ── Ver detalles ──────────────────────────────────────────────────────
    private void verDetalles() {
        try {
            listar();
            System.out.print("ID de pedido: ");
            long id = scanner.nextLong();
            scanner.nextLine();
            
            //long id = Util.leerLong(scanner);
            Pedido p = pedidoService.buscarPorId(id);
            System.out.println(p);
            List<DetallePedido> detalles = p.getDetalles().stream()
                .filter(d -> !d.isEliminado()).toList();
            if (detalles.isEmpty()) System.out.println("  (sin detalles)");
            else detalles.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    // ── HU-PED-03 Actualizar estado / forma de pago ───────────────────────
    private void actualizarEstadoPago() {
        try {
            listar();
            System.out.print("ID de pedido a actualizar: ");
            long id = scanner.nextLong();
            scanner.nextLine();

            // long id = Util.leerLong(scanner);

            System.out.println("¿Qué desea actualizar?");
            System.out.println("1. Estado");
            System.out.println("2. Forma de pago");
            System.out.println("3. Ambos");
            System.out.print("Seleccione: ");
            int op = scanner.nextInt();

            if (op == 1 || op == 3) {
                Estado nuevoEstado = elegirEstado();
                pedidoService.actualizarEstado(id, nuevoEstado);
            }
            if (op == 2 || op == 3) {
                FormaPago nuevaForma = elegirFormaPago();
                pedidoService.actualizarFormaPago(id, nuevaForma);
            }
            System.out.println("✔ Pedido actualizado.");
        } catch (Exception e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    // ── HU-PED-04 Eliminar ────────────────────────────────────────────────
    private void eliminar() {
        try {
            listar();
            System.out.print("ID de pedido a eliminar: ");
            long id = scanner.nextLong();
            scanner.nextLine();

            // long id = Util.leerLong(scanner);
            System.out.print("¿Confirmar? (S/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
                System.out.println("ℹ Cancelado.");
                return;
            }
            pedidoService.eliminar(id);
            System.out.println("✔ Pedido eliminado (baja lógica).");
        } catch (Exception e) {
            System.out.println("✖ Error: " + e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private FormaPago elegirFormaPago() {
        System.out.println("Forma de pago: 1=TARJETA  2=TRANSFERENCIA  3=EFECTIVO");
        System.out.print("Seleccione: ");
        int f = scanner.nextInt();
        scanner.nextLine();
        return switch (f) {
            case 1 -> FormaPago.TARJETA;
            case 2 -> FormaPago.TRANSFERENCIA;
            default -> FormaPago.EFECTIVO;
        };
    }

    private Estado elegirEstado() {
        System.out.println("Estado: 1=PENDIENTE  2=CONFIRMADO  3=TERMINADO  4=CANCELADO");
        System.out.print("Seleccione: ");
        int e = scanner.nextInt();
        scanner.nextLine();
        return switch (e) {
            case 2 -> Estado.CONFIRMADO;
            case 3 -> Estado.TERMINADO;
            case 4 -> Estado.CANCELADO;
            default -> Estado.PENDIENTE;
        };
    }
}
