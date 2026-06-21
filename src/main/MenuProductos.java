package main;

import entities.Categoria;
import entities.Producto;
import exception.EntidadNoEncontradaException;
import exception.StockInvalidoException;
import service.CategoriaService;
import service.ProductoService;
import java.util.List;
import java.util.Scanner;

public class MenuProductos extends MenuBase {

    private ProductoService service;
    private CategoriaService categoriaService;

    public MenuProductos(Scanner sc, ProductoService service, CategoriaService categoriaService) {
        super(sc);
        this.service = service;
        this.categoriaService = categoriaService;
    }

    @Override
    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ PRODUCTOS ---");
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
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }

    private void listar() {
        mostrarProductos();
    }

    private void crear() {
        Categoria categoria = seleccionarCategoria();
        if (categoria == null) {
            return;
        }

        String nombre = leerTextoObligatorio("Nombre: ");
        double precio = leerDouble("Precio: ");
        String descripcion = leerTexto("Descripción: ");
        int stock = leerEntero("Stock: ");
        String imagen = leerTexto("Imagen: ");
        boolean disponible = leerTexto("¿Disponible? (S/N): ").equalsIgnoreCase("S");

        try {
            Producto p = service.crear(nombre, precio, descripcion, stock, imagen, disponible, categoria);
            System.out.println("Producto creado con id: " + p.getId());
        } catch (IllegalArgumentException | StockInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editar() {
        if (mostrarProductos().isEmpty()) {
            return;
        }
        Long id = leerLong("Id del producto a editar: ");
        try {
            Producto actual = service.buscarPorId(id);

            String precioStr = leerTexto("Nuevo precio (Enter para mantener " + actual.getPrecio() + "): ");
            double precio = precioStr.isEmpty() ? actual.getPrecio() : Double.parseDouble(precioStr.replace(",", "."));

            String stockStr = leerTexto("Nuevo stock (Enter para mantener " + actual.getStock() + "): ");
            int stock = stockStr.isEmpty() ? actual.getStock() : Integer.parseInt(stockStr);

            Categoria categoria = actual.getCategoria();
            String cambiar = leerTexto("¿Cambiar categoría? (S/N): ");
            if (cambiar.equalsIgnoreCase("S")) {
                Categoria nueva = seleccionarCategoria();
                if (nueva != null) {
                    categoria = nueva;
                }
            }

            service.editar(id, precio, stock, categoria);
            System.out.println("Producto actualizado");
        } catch (EntidadNoEncontradaException | StockInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: valor numérico inválido");
        }
    }

    private void eliminar() {
        if (mostrarProductos().isEmpty()) {
            return;
        }
        Long id = leerLong("Id del producto a eliminar: ");
        String confirma = leerTexto("¿Confirma la eliminación? (S/N): ");
        if (!confirma.equalsIgnoreCase("S")) {
            System.out.println("Eliminación cancelada");
            return;
        }
        try {
            service.eliminar(id);
            System.out.println("Producto eliminado.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private List<Producto> mostrarProductos() {
        List<Producto> productos = service.listar();
        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados");
        } else {
            System.out.println("\nProductos:");
            for (Producto p : productos) {
                System.out.println(p);
            }
        }
        return productos;
    }

    private Categoria seleccionarCategoria() {
        List<Categoria> categorias = categoriaService.listar();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorías cargadas. Cree una categoría primero.");
            return null;
        }
        System.out.println("\nCategorías disponibles:");
        for (Categoria c : categorias) {
            System.out.println(c);
        }
        Long idCat = leerLong("Id de la categoría: ");
        try {
            return categoriaService.buscarPorId(idCat);
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}