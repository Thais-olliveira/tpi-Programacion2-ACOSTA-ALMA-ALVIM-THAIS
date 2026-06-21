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
public class PedidoMenu extends MenuBase {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public PedidoMenu(PedidoService pedidoService, UsuarioService usuarioService,
                      ProductoService productoService, Scanner scanner) {
        super(scanner);
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

   @Override
    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n=== PEDIDOS ===");
            System.out.println("1. Listar todos");
            System.out.println("2. Listar por usuario");
            System.out.println("3. Crear pedido (con detalles)");
            System.out.println("4. Ver detalles de pedido");
            System.out.println("5. Actualizar estado / forma de pago");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            opcion = leerEntero();
            switch (opcion) {
                case 1 -> listar();
                case 2 -> listarPorUsuario();
                case 3 -> crear();
                case 4 -> verDetalles();
                case 5 -> actualizarEstadoPago();
                case 6 -> eliminar();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
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
        if (usuarioService.listarActivos().isEmpty()) {
            System.out.println("No hay usuarios cargados.");
            return;
        }
        usuarioService.listarActivos().forEach(System.out::println);
        Long uid = leerLong("ID de usuario: ");
        List<Pedido> lista = pedidoService.listarPorUsuario(uid);
        if (lista.isEmpty()) {
            System.out.println("Sin pedidos para ese usuario.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    // ────────────────────────────────────────
    private void crear() {
        Pedido pedido = null;
        try {
            // 1) Seleccionar usuario (con guarda si no hay ninguno)
            if (usuarioService.listarActivos().isEmpty()) {
                System.out.println("No hay usuarios cargados. Cree un usuario primero.");
                return;
            }
            System.out.println("Usuarios disponibles:");
            usuarioService.listarActivos().forEach(System.out::println);
            Long uid = leerLong("ID de usuario: ");
            Usuario usuario = usuarioService.buscarPorId(uid);

            // 2) Verificar que existan productos ANTES de crear el pedido
            if (productoService.listar().isEmpty()) {
                System.out.println("No hay productos cargados. Cree un producto primero.");
                return;
            }

            // 3) Elegir forma de pago y crear el pedido vacío
            FormaPago formaPago = elegirFormaPago();
            pedido = pedidoService.crear(usuario, formaPago);
            System.out.println("Pedido creado con ID: " + pedido.getId() + ". Agregue detalles:");

            // 4) Bucle de detalles — SIN try interno
            boolean agregarMas = true;
            while (agregarMas) {
                System.out.println("\nProductos disponibles:");
                productoService.listar().forEach(System.out::println);
                Long pid = leerLong("ID de producto (0 para finalizar): ");
                if (pid == 0) {
                    break;
                }
                Producto prod = productoService.buscarPorId(pid);
                int cant = leerEntero("Cantidad: ");

                pedidoService.agregarDetalle(pedido, cant, prod);
                System.out.printf("Detalle agregado. Subtotal: $%.2f%n", cant * prod.getPrecio());

                String mas = leerTexto("¿Agregar otro producto? (S/N): ");
                agregarMas = mas.equalsIgnoreCase("S");
            }

            // 5) Calcular el total mediante la interfaz Calculable
            pedido.calcularTotal();
            System.out.printf("Pedido finalizado. Total: $%.2f%n", pedido.getTotal());

        } catch (Exception e) {
            System.out.println("Error al crear el pedido: " + e.getMessage());
            // Rollback: se da de baja lógica al pedido para no dejar datos inconsistentes
            if (pedido != null && pedido.getId() != null) {
                try {
                    pedidoService.eliminar(pedido.getId());
                    System.out.println("Pedido cancelado para mantener la consistencia");
                } catch (Exception ex) {
                  
                }
            }
        }
    }

    // ── Ver detalles ──────────────────────────────────────────────────────
    private void verDetalles() {
        listar();
        Long id = leerLong("ID de pedido: ");
        try {
            Pedido p = pedidoService.buscarPorId(id);
            System.out.println(p);
            List<DetallePedido> detalles = p.getDetalles().stream()
                .filter(d -> !d.isEliminado()).toList();
            if (detalles.isEmpty()) {
                System.out.println("  (sin detalles)");
            } else {
                detalles.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── HU-PED-03 Actualizar estado / forma de pago ───────────────────────
    private void actualizarEstadoPago() {
        listar();
        Long id = leerLong("ID de pedido a actualizar: ");
        try {
            System.out.println("¿Qué desea actualizar?");
            System.out.println("1. Estado");
            System.out.println("2. Forma de pago");
            System.out.println("3. Ambos");
            int op = leerEntero("Seleccione: ");

            if (op == 1 || op == 3) {
                pedidoService.actualizarEstado(id, elegirEstado());
            }
            if (op == 2 || op == 3) {
                pedidoService.actualizarFormaPago(id, elegirFormaPago());
            }
            System.out.println("Pedido actualizado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── HU-PED-04 Eliminar ────────────────────────────────────────────────
    private void eliminar() {
        listar();
        Long id = leerLong("ID de pedido a eliminar: ");
        String confirma = leerTexto("¿Confirmar? (S/N): ");
        if (!confirma.equalsIgnoreCase("S")) {
            System.out.println("Cancelado.");
            return;
        }
        try {
            pedidoService.eliminar(id);
            System.out.println("Pedido eliminado (baja lógica).");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private FormaPago elegirFormaPago() {
        System.out.println("Forma de pago: 1=TARJETA  2=TRANSFERENCIA  3=EFECTIVO");
        int f = leerEntero("Seleccione: ");
        return switch (f) {
            case 1 -> FormaPago.TARJETA;
            case 2 -> FormaPago.TRANSFERENCIA;
            default -> FormaPago.EFECTIVO;
        };
    }

    private Estado elegirEstado() {
        System.out.println("Estado: 1=PENDIENTE  2=CONFIRMADO  3=TERMINADO  4=CANCELADO");
        int e = leerEntero("Seleccione: ");
        return switch (e) {
            case 2 -> Estado.CONFIRMADO;
            case 3 -> Estado.TERMINADO;
            case 4 -> Estado.CANCELADO;
            default -> Estado.PENDIENTE;
        };
    }
}
