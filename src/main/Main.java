package main;

import service.CategoriaService;
import service.ProductoService;
import java.util.Scanner;
import service.PedidoService;
import service.UsuarioService;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        CategoriaService categoriaService = new CategoriaService();
        ProductoService productoService = new ProductoService();
        PedidoService pedidoService = new PedidoService();
        UsuarioService usuarioService = new UsuarioService();
        
       // PedidoMenu pedidoMenu = new PedidoMenu();
       // UsuarioMenu usuarioMenu = new UsuarioMenu();

        int opcion;
        do {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorías");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");

            opcion = leerEntero(sc);

            switch (opcion) {
                case 1 -> new MenuCategoria(sc, categoriaService).mostrar();
                case 2 -> new MenuProductos(sc, productoService, categoriaService).mostrar();
                case 3 -> new UsuarioMenu(usuarioService, sc).mostrar();
                case 4 -> new PedidoMenu(pedidoService, usuarioService, productoService, sc).mostrar();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 0);

        sc.close();
    }

    private static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }
}