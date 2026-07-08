# 📦 Sistema de Gestión de Inventario (POO - Java)

Proyecto final del curso de **Programación Orientada a Objetos**.
Este sistema permite administrar productos, categorías y usuarios utilizando Java puro, aplicando principios de POO y almacenamiento en archivos CSV.

---

## ✨ Características

* 🔹 **Gestión de productos**

  * Crear, editar, listar y eliminar productos
  * Cada producto tiene: precio, stock y categoría
  * Cálculo automático del valor total (precio × stock)

* 🔹 **Gestión de categorías**

  * Crear y administrar categorías
  * Atributos: ID, nombre y descripción

* 🔹 **Autenticación de usuarios**

  * Inicio de sesión para acceso al sistema
  * Control básico de usuarios

* 🔹 **Persistencia en CSV**

  * Almacenamiento simple sin base de datos
  * Cada modelo implementa:

    * `toCSV()` → serialización
    * `fromCSV()` → deserialización

* 🔹 **Registro de logs**

  * Seguimiento de operaciones importantes
  * Incluye: fecha, hora, usuario y acción

* 🔹 **Validaciones**

  * Precios positivos
  * IDs únicos
  * Verificación de existencia de categorías
  * Control de datos inválidos

---

## 🧱 Tecnologías utilizadas

* ☕ **Java**
* 📄 **Archivos CSV** (persistencia de datos)
* 🧩 **Programación Orientada a Objetos**

  * Encapsulamiento
  * Herencia
  * Polimorfismo
  * Abstracción
  * Interfaces (`Persistible`, `Autenticable`)

---

## 📁 Estructura del proyecto

```
src/
├── controller/
│   ├── CategoriaController.java
│   ├── ProductoController.java
│   ├── UsuarioController.java
│   └── Resultado.java
│
├── model/
│   ├── Autenticable.java
│   ├── Persistible.java
│   ├── Persona.java
│   ├── Categoria.java
│   ├── CategoriaModel.java
│   ├── ProductoBase.java
│   ├── Producto.java
│   ├── ProductoModel.java
│   ├── LogEntry.java
│   └── LogModel.java
│
└── util/
    ├── CSVUtil.java
    ├── Rutas.java
    └── Validador.java
```

---

## 🧩 Modelo de datos

### 📦 Producto

* Hereda de `ProductoBase`
* Atributos: id, nombre, precio, stock, categoría
* Métodos:

  * `calcularValorTotal()`
  * `getTipoProducto()` → `"ESTANDAR"`

### 🏷️ Categoría

* Atributos:

  * `id`
  * `nombre`
  * `descripcion`

### 👤 Usuario

* Basado en la clase `Persona`
* Implementa autenticación mediante la interfaz `Autenticable`

---

## 🚀 Cómo ejecutar el proyecto

1. Clona el repositorio:

   ```bash
   git clone https://github.com/OrellanaJheremiasT/POO-Proyecto.git
   ```

2. Abre el proyecto en tu IDE:

   * IntelliJ IDEA
   * Eclipse
   * NetBeans

3. Ubica la clase con el método `main` y ejecútala.

4. Asegúrate de tener los archivos CSV en las rutas correctas:

   * `categorias.csv`
   * `productos.csv`
   * `usuarios.csv`
   * `logs.csv`

   👉 Si no existen, créalos vacíos (con o sin cabeceras según tu implementación).

---

## 📌 Notas

* El proyecto sigue el patrón **MVC (Modelo - Vista - Controlador)**.
* No utiliza base de datos, lo cual simplifica la implementación.
* Ideal para demostrar conceptos de POO en un entorno académico.

---

## 📝 Licencia

Este proyecto está bajo la licencia **MIT**.
