package service;

import entities.Categoria;
import exception.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;
import entities.Producto;

public class CategoriaService {

    private List<Categoria> categorias;
    private Long contadorId;

    public CategoriaService() {
        this.categorias = new ArrayList<>();
        this.contadorId = 1L;
    }

    // HU-CAT-02: Crear Categoria
    public Categoria crear(String nombre, String descripcion) {
        for (Categoria c : categorias) {
            if (!c.isEliminado() && c.getNombre().equalsIgnoreCase(nombre)) {
                throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + nombre);
            }
        }
        Categoria categoria = new Categoria(nombre, descripcion);
        categoria.setId(contadorId);
        contadorId++;
        categorias.add(categoria);
        return categoria;
    }

    // HU-CAT-01: Listar Categorias
    public List<Categoria> listar() {
        List<Categoria> activas = new ArrayList<>();
        for (Categoria c : categorias) {
            if (!c.isEliminado()) {
                activas.add(c);
            }
        }
        return activas;
    }

    // Buscar por id
    public Categoria buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Categoria c : categorias) {
            if (c.getId().equals(id) && !c.isEliminado()) {
                return c;
            }
        }
        throw new EntidadNoEncontradaException("No se encontró la categoría con id: " + id);
    }

    // HU-CAT-03: Editar categoria
    public void editar(Long id, String nombre, String descripcion) throws EntidadNoEncontradaException {
        Categoria categoria = buscarPorId(id);
        for (Categoria c : categorias) {
            if (!c.isEliminado() && !c.getId().equals(id) && c.getNombre().equalsIgnoreCase(nombre)) {
                throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + nombre);
            }
        }
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
    }

    // HU-CAT-04: Eliminar categoria (baja lógica)
    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Categoria categoria = buscarPorId(id);

        // Verificar si tiene productos asociados
        for (Producto p : categoria.getProductos()) {
            if (!p.isEliminado()) {
                throw new IllegalStateException(
                    "No se puede eliminar la categoría porque tiene productos asociados");
            }
        }

        categoria.setEliminado(true);
    }
}