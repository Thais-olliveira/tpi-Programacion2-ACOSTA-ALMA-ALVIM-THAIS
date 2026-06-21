/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import exception.EntidadNoEncontradaException;
import exception.StockInvalidoException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alma
 */
/**
 * Servicio que gestiona el CRUD de Pedidos en memoria.
 *
 * Reglas de negocio:
 *  - No se puede crear un pedido sin usuario.
 *  - addDetallePedido valida cantidad > 0 (lanzado desde Pedido).
 *  - calcularTotal() de la interfaz Calculable recalcula el total.
 *  - Soft-delete: eliminado = true (también en sus detalles).
 */
public class PedidoService {

    private final List<Pedido> pedidos = new ArrayList<>();

    // ── Listar ────────────────────────────────────────────────────────────
    public List<Pedido> listarActivos() {
        return pedidos.stream().filter(p -> !p.isEliminado()).toList();
    }

    public List<Pedido> listarPorUsuario(long usuarioId) {
        return pedidos.stream()
            .filter(p -> !p.isEliminado()
                && p.getUsuario() != null
                && p.getUsuario().getId() == usuarioId)
            .toList();
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────
    public Pedido buscarPorId(long id) throws EntidadNoEncontradaException {
        return pedidos.stream()
            .filter(p -> p.getId() == id && !p.isEliminado())
            .findFirst()
            .orElseThrow(() -> new EntidadNoEncontradaException(
                "No se encontró pedido activo con ID: " + id));
    }

    // ── Crear ─────────────────────────────────────────────────────────────
    public Pedido crear(Usuario usuario, FormaPago formaPago) throws EntidadNoEncontradaException {
        if (usuario == null || usuario.isEliminado()) {
            throw new EntidadNoEncontradaException("El usuario indicado no existe o está eliminado.");
        }
        Pedido nuevo = new Pedido(usuario, formaPago);
        pedidos.add(nuevo);
        return nuevo;
    }

    public void agregarDetalle(Pedido pedido, int cantidad, Producto producto) throws StockInvalidoException {
        pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);
    }

    // ── Actualizar estado / forma de pago ────────────────────────────────
    public void actualizarEstado(long id, Estado nuevoEstado) throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        p.setEstado(nuevoEstado);
    }

    public void actualizarFormaPago(long id, FormaPago nuevaFormaPago) throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        p.setFormaPago(nuevaFormaPago);
    }

    // ── Eliminar (soft-delete) ────────────────────────────────────────────
    public void eliminar(long id) throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        p.setEliminado(true);
        for (DetallePedido d : p.getDetalles()) {
            d.setEliminado(true);
        }
    }
}
