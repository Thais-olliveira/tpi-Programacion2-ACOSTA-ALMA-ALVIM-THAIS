package main;

import entities.Categoria;
import exception.EntidadNoEncontradaException;
import service.CategoriaService;
import java.util.List;
import java.util.Scanner;

public class MenuCategoria extends MenuBase {

    private CategoriaService service;

    public MenuCategoria(Scanner sc, CategoriaService service) {
        super(sc);
        this.service = service;
    }

    @Override
    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ CATEGORÍAS ---");
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
        mostrarCategorias();
    }

    private void crear() {
        String nombre = leerTextoObligatorio("Nombre: ");
        String descripcion = leerTexto("Descripción: ");
        try {
            Categoria c = service.crear(nombre, descripcion);
            System.out.println("Categoría creada con id: " + c.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editar() {
        if (mostrarCategorias().isEmpty()) {
            return;
        }
        Long id = leerLong("Id de la categoría a editar: ");
        try {
            Categoria actual = service.buscarPorId(id);

            String nombre = leerTexto("Nuevo nombre (Enter para mantener \"" + actual.getNombre() + "\"): ");
            if (nombre.isEmpty()) {
                nombre = actual.getNombre();
            }

            String descripcion = leerTexto("Nueva descripción (Enter para mantener \"" + actual.getDescripcion() + "\"): ");
            if (descripcion.isEmpty()) {
                descripcion = actual.getDescripcion();
            }

            service.editar(id, nombre, descripcion);
            System.out.println("Categoría actualizada");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        if (mostrarCategorias().isEmpty()) {
            return;
        }
        Long id = leerLong("Id de la categoría a eliminar: ");
        String confirma = leerTexto("¿Confirma la eliminación? (S/N): ");
        if (!confirma.equalsIgnoreCase("S")) {
            System.out.println("Eliminación cancelada");
            return;
        }
        try {
            service.eliminar(id);
            System.out.println("Categoría eliminada");
        } catch (EntidadNoEncontradaException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Listar las categorías
    private List<Categoria> mostrarCategorias() {
        List<Categoria> categorias = service.listar();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorías cargadas");
        } else {
            System.out.println("\nCategorías:");
            for (Categoria c : categorias) {
                System.out.println(c);
            }
        }
        return categorias;
    }
}