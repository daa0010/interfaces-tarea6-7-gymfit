# GymFit Manager

**GymFit Manager** es una aplicación de escritorio desarrollada en JavaFX para la gestión integral de los socios de un gimnasio. 

Este proyecto ha sido diseñado como práctica para el módulo de **Desarrollo de Interfaces**, poniendo un foco especial en la **usabilidad**, la evaluación heurística y la mejora de la experiencia de usuario (UX) frente a interfaces deficientes.

## Características principales

- **Gestión completa (CRUD):** Creación, visualización, edición y borrado de socios.
- **Persistencia de datos:** Los datos se guardan de forma local y segura mediante una base de datos **SQLite** (`gymfit_socios.db`) que se autogenera en la carpeta del usuario. Transacciones seguras para evitar pérdida de datos.
- **Interfaz mejorada y usable:**
  - Resolución de escritorio adaptada (1280x800).
  - Formularios intuitivos con controles nativos (ComboBox, DatePicker, RadioButtons).
  - Barra de menús estándar con **atajos de teclado**.
  - Barra de herramientas con iconos y **tooltips** descriptivos.
  - **Menú contextual** ágil en la tabla de registros.
- **Gestión de errores amigable:** Implementación de un modal superpuesto (StackPane) que bloquea la interfaz para mostrar mensajes de error claros, positivos y con opciones de solución directa (ej. validación avanzada de IBAN).
- **Acciones seguras:** Diálogos de confirmación nativos antes de ejecutar acciones destructivas (borrado).

## Tecnologías utilizadas

- **Lenguaje:** Java
- **Interfaz Gráfica:** JavaFX 21.0.6 (FXML + CSS personalizado)
- **Gestor de dependencias:** Maven
- **Base de datos:** SQLite (driver `sqlite-jdbc` 3.45.2.0)
- **IDE recomendado:** IntelliJ IDEA

## Estructura del Proyecto

El proyecto sigue el estándar de directorios de Maven:
- `src/main/java/.../model/`: Lógica de negocio, objetos y conexión a base de datos (SocioRepository).
- `src/main/java/.../util/`: Herramientas auxiliares de formato y validación.
- `src/main/resources/.../css/`: Hojas de estilo para la interfaz.
- `src/main/resources/.../main.fxml`: Diseño de la vista principal.

## Cómo ejecutar el proyecto

1. Clona o extrae este repositorio en tu equipo.
2. Abre el proyecto utilizando IntelliJ IDEA (u otro IDE compatible con Maven).
3. Asegúrate de recargar los cambios de Maven para que se descarguen las dependencias (especialmente JavaFX y el driver de SQLite).
4. Ejecuta la clase principal: `GymFitManager.java` (o `Launcher.java`).

> **Nota:** La base de datos se generará automáticamente en tu directorio de usuario (`user.home`) la primera vez que arranques la aplicación, poblándose con datos de ejemplo si es necesario.

## Autor

Desarrollado por **David Alonso Aguilar** para la Tarea 6-7 de Desarrollo de Interfaces.