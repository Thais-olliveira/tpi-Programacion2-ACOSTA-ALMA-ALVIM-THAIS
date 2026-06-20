package service;

import entities.Producto;
import entities.Categoria;
import exception.EntidadNoEncontradaException;
import exception.StockInvalidoException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    private List<Producto> productos;
    private Long contadorId;

    public ProductoService() {
        this.productos = new ArrayList<>();
        this.contadorId = 1L;
    }

    // HU-PROD-02: Crear producto
    public Producto crear(String nombre, Double precio, String descripcion, int stock,
                          String imagen, boolean disponible, Categoria categoria) throws StockInvalidoException {
        if (precio < 0) {
            throw new StockInvalidoException("El precio no puede ser negativo");
        }
        if (stock < 0) {
            throw new StockInvalidoException("El stock no puede ser negativo");
        }
        Producto producto = new Producto(nombre, precio, descripcion, stock, imagen, disponible, categoria);
        producto.setId(contadorId);
        contadorId++;
        productos.add(producto);
        categoria.agregarProducto(producto);
        return producto;
    }

    // HU-PROD-01: Listar producto
    public List<Producto> listar() {
        List<Producto> activos = new ArrayList<>();
        for (Producto p : productos) {
            if (!p.isEliminado()) {
                activos.add(p);
            }
        }
        return activos;
    }

    // buscar por id
    public Producto buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Producto p : productos) {
            if (p.getId().equals(id) && !p.isEliminado()) {
                return p;
            }
        }
        throw new EntidadNoEncontradaException("No se encontró el producto con id: " + id);
    }

    // HU-PROD-03: Editar producto
    public void editar(Long id, Double precio, int stock, Categoria categoria)
            throws EntidadNoEncontradaException, StockInvalidoException {
        Producto producto = buscarPorId(id);
        if (precio < 0) {
            throw new StockInvalidoException("El precio no puede ser negativo");
        }
        if (stock < 0) {
            throw new StockInvalidoException("El stock no puede ser negativo");
        }
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCategoria(categoria);
    }

    // HU-PROD-04: Eliminar producto (baja lógica)
    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Producto producto = buscarPorId(id);
        producto.setEliminado(true);
    }
}