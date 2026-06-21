/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

/**
 *
 * @author Alma
 */
/**
 * Representa una línea dentro de un Pedido.
 * Hereda de Base.
 * Relación N:1 con Producto.
 *
 * El subtotal se calcula automáticamente: cantidad × precio del producto.
 */
public class DetallePedido extends Base {

    private int cantidad;
    private double subtotal;
    private Producto producto;  // Relación N:1

    public DetallePedido(int cantidad, double subtotal, Producto producto) {
        super();
        this.cantidad  = cantidad;
        this.subtotal  = subtotal;
        this.producto  = producto;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────
    public int      getCantidad(){ 
        return cantidad; }
    public void     setCantidad(int cantidad){ 
        this.cantidad = cantidad; }
    public double   getSubtotal(){ 
        return subtotal; }
    public void     setSubtotal(double s){ 
        this.subtotal = s; }
    public Producto getProducto(){ 
        return producto; }
    public void     setProducto(Producto p){ 
        this.producto = p; }

    @Override
    public String toString() {
        return String.format(
            "  └─ [Det.ID: %d] Producto: %-20s | Cantidad: %d | Subtotal: $%.2f",
            getId(),
            producto != null ? producto.getNombre() : "?",
            cantidad,
            subtotal
        );
    }
}
