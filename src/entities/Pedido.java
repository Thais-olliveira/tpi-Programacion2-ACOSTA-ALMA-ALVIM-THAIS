/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;
import enums.Estado;
import enums.FormaPago;
import exception.EntidadNoEncontradaException;
import exception.StockInvalidoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alma
 */
/**
 * Representa un pedido realizado por un Usuario.
 * Hereda de Base e implementa Calculable.
 *
 * Composición 1:N con DetallePedido (los detalles no existen sin el pedido).
 * El total se recalcula con calcularTotal() sumando todos los subtotales.
 */
public class Pedido extends Base implements Calculable {

    private LocalDate fecha;
    private Estado estado;
    private double total;
    private FormaPago formaPago;
    private Usuario usuario;                          // dueño del pedido
    private List<DetallePedido> detalles = new ArrayList<>();    // composición

    public Pedido(Usuario usuario, FormaPago formaPago) {
        super();
        this.usuario   = usuario;
        this.formaPago = formaPago;
        this.estado    = Estado.PENDIENTE;
        this.fecha     = LocalDate.now();
        this.total     = 0.0;
    }

    // ── Métodos propios del dominio ────────────────────────────────────────
    public void addDetallePedido(int cantidad, double precioUnitario, Producto producto) throws StockInvalidoException {
        if (cantidad <= 0) {
            throw new StockInvalidoException("La cantidad debe ser mayor a 0.");
        }
        double subtotal = cantidad * precioUnitario;
        DetallePedido detalle = new DetallePedido(cantidad, subtotal, producto);
        detalles.add(detalle);
        calcularTotal();   // recalcula después de cada adición
    }

    public DetallePedido findeDetallePedidoByProducto(Producto producto) throws EntidadNoEncontradaException {
        return detalles.stream()
            .filter(d -> !d.isEliminado() && d.getProducto().getId().equals(producto.getId()))
            .findFirst()
            .orElseThrow(() -> new EntidadNoEncontradaException(
                "No se encontró detalle para el producto: " + producto.getNombre()));
    }

    public void deleteDetallePedidoByProducto(Producto producto) throws EntidadNoEncontradaException {
        DetallePedido detalle = findeDetallePedidoByProducto(producto);
        detalle.setEliminado(true);
        calcularTotal();
    }

    @Override
    public void calcularTotal() {
        this.total = detalles.stream()
            .filter(d -> !d.isEliminado())
            .mapToDouble(DetallePedido::getSubtotal)
            .sum();
    }

    // ── Getters y Setters ──────────────────────────────────────────────────
    public LocalDate getFecha() { 
        return fecha; }
    public Estado getEstado(){
        return estado; }
    public void setEstado(Estado estado) { 
        this.estado = estado; }
    public double getTotal() { 
        return total; }
    public FormaPago getFormaPago() {
        return formaPago; }
    public void setFormaPago(FormaPago f) { 
        this.formaPago = f; }
    public Usuario getUsuario() { 
        return usuario; }
    public List<DetallePedido> getDetalles() { 
        return detalles; }

    @Override
    public String toString() {
        String nombreUsuario = usuario != null
            ? usuario.getNombre() + " " + usuario.getApellido()
            : "Sin usuario";
        return String.format(
            "[ID: %d] Usuario: %-20s | Estado: %-12s | Pago: %-15s | Total: $%-8.2f | Fecha: %s",
            getId(), nombreUsuario, estado, formaPago, total, fecha
        );
    }
}

