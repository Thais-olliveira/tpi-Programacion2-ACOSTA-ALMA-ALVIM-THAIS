# Food Store — Sistema de Gestión de Pedidos de Comida

Aplicación de consola desarrollada en Java como Trabajo Práctico Integrador de
**Programación 2** (Tecnicatura Universitaria en Programación a Distancia — UTN).

El sistema permite gestionar **categorías, productos, usuarios y pedidos** de un
negocio de comidas, realizando operaciones CRUD completas desde un menú de consola.
Toda la información se almacena **en memoria** mediante colecciones (no utiliza base
de datos).

---

## Requisitos

- **Java 21** (JDK 21 o superior)
- **Apache NetBeans** (IDE en el que se desarrolló el proyecto)

---

## Cómo ejecutar

### Opción A — Desde NetBeans
1. Abrir el proyecto en NetBeans (`File > Open Project`).
2. Ejecutar la clase `main.Main` (botón *Run* o `F6`).

### Opción B — Desde la línea de comandos
Desde la carpeta `src/` del proyecto:

```bash
# Compilar
javac -d ../out $(find . -name "*.java")

# Ejecutar
cd ../out
java main.Main
```

Al iniciar se muestra el menú principal:

```
=== SISTEMA DE PEDIDOS (FOOD STORE) ===
1. Categorías
2. Productos
3. Usuarios
4. Pedidos
0. Salir
Seleccione:
```

---

## Estructura del proyecto

```
src/
├── main/         → Main y menús de consola (interacción con el usuario)
│   ├── Main.java
│   ├── MenuBase.java        (clase base con lectura robusta de entrada)
│   ├── MenuCategoria.java
│   ├── MenuProductos.java
│   ├── UsuarioMenu.java
│   └── PedidoMenu.java
├── service/      → Lógica de negocio y CRUD en memoria
│   ├── CategoriaService.java
│   ├── ProductoService.java
│   ├── UsuarioService.java
│   └── PedidoService.java
├── entities/     → Modelo de dominio (POO)
│   ├── Base.java            (clase abstracta: id, eliminado, createdAt)
│   ├── Calculable.java      (interfaz: calcularTotal())
│   ├── Categoria.java
│   ├── Producto.java
│   ├── Usuario.java
│   ├── Pedido.java          (implementa Calculable)
│   └── DetallePedido.java
├── enums/        → Rol, Estado, FormaPago
└── exception/    → Excepciones propias
    ├── EntidadNoEncontradaException.java
    ├── StockInvalidoException.java
    └── MailDuplicadoException.java
```

La arquitectura separa responsabilidades en tres capas: **entidades** (modelo),
**servicios** (reglas de negocio) y **menús** (interacción por consola).

---

## Funcionalidades

CRUD completo de las cuatro entidades, con baja lógica (*soft delete*) en todas:

- **Categorías** — listar, crear (nombre único), editar, eliminar (con bloqueo si
  tiene productos asociados).
- **Productos** — listar, crear y asociar a una categoría, editar, eliminar.
  Validaciones de precio y stock no negativos.
- **Usuarios** — listar, crear (mail único), editar, eliminar.
- **Pedidos** — listar, listar por usuario, crear con detalles, ver detalles,
  actualizar estado y forma de pago, eliminar. El total se calcula mediante la
  interfaz `Calculable`.

Conceptos aplicados: herencia, polimorfismo, encapsulamiento, interfaces,
colecciones y manejo de excepciones propias.

---

## Enlaces

- **Video demostrativo:** ``
- **Documentación (PDF):** ``
- **Repositorio:** `https://github.com/Thais-olliveira/tpi-Programacion2-ACOSTA-ALMA-ALVIM-THAIS`

---

## Autoras

- Alma Acosta
- Thaís Alvim
