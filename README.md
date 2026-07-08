# POO-Proyecto

**Proyecto final del curso de Programación Orientada a Objetos**

Sistema de gestión de inventario desarrollado en Java. Permite administrar productos, categorías y usuarios, con persistencia en archivos CSV y un sistema básico de autenticación y registro de operaciones (logs).

---

## ✨ Características principales

- **Gestión de productos**: Crear, editar, listar y eliminar productos. Cada producto tiene precio, stock y una categoría asociada.
- **Gestión de categorías**: Administrar categorías con ID, nombre y descripción.
- **Autenticación de usuarios**: Inicio de sesión para controlar el acceso al sistema.
- **Persistencia en CSV**: Los datos se guardan en archivos CSV. Cada clase del modelo implementa `toCSV()` y `fromCSV()` para serializar/deserializar.
- **Registro de logs**: Cada operación importante (altas, bajas, modificaciones) se registra con fecha, hora y usuario.
- **Validaciones**: Precios positivos, IDs únicos, existencia de categorías antes de asignar a un producto, etc.

---

## 🧱 Tecnologías

- **Java** (100% del código)
- **CSV** como almacenamiento persistente
- **Programación orientada a objetos**: herencia, encapsulamiento, interfaces (`Persistible`, `Autenticable`)

---

## 📁 Estructura del proyecto
src/
├── controller/
│ ├── CategoriaController.java
│ ├── ProductoController.java
│ ├── Resultado.java # Envuelve resultados de operaciones
│ └── UsuarioController.java
├── model/
│ ├── Autenticable.java # Interface para login
│ ├── Categoria.java
│ ├── CategoriaModel.java # Lógica de negocio para categorías
│ ├── LogEntry.java
│ ├── LogModel.java
│ ├── Persistible.java # Interface para CSV
│ ├── Persona.java # Clase base para Usuario
│ ├── Producto.java # Extiende ProductoBase
│ ├── ProductoBase.java
│ └── ProductoModel.java # Lógica de negocio para productos
└── util/
├── CSVUtil.java # Lectura/escritura genérica de CSV
├── Rutas.java # Constantes con rutas de archivos
└── Validador.java # Validaciones comunes

---

## 🧩 Modelo de datos

- **Producto**: hereda de `ProductoBase` y añade el atributo `stock`. Implementa:
  - `calcularValorTotal()` → precio × stock
  - `getTipoProducto()` → retorna `"ESTANDAR"`
- **Categoría**: atributos `id`, `nombre`, `descripción`.
- **Usuario** (implícito en `Persona`): usado para autenticación.

---

## 🚀 Cómo ejecutar

1. Clona el repositorio:
   ```bash
   git clone https://github.com/OrellanaJheremiasT/POO-Proyecto.git
Abre el proyecto en tu IDE (Eclipse, IntelliJ, NetBeans).

Compila y ejecuta la clase principal.
Nota: La clase principal aún no está definida en este README; busca el archivo que contiene el método main (posiblemente en controller o en la raíz de src).

Asegúrate de que los archivos CSV (categorias.csv, productos.csv, usuarios.csv, logs.csv) existan en las rutas indicadas en Rutas.java (o créalos vacíos con cabeceras).

📝 Licencia
Este proyecto se distribuye bajo la licencia MIT.
