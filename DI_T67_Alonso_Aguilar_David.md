# Parte 1 · Evaluación Heurística - GymFit Manager

A continuación se detalla la evaluación heurística de la pantalla principal de la aplicación, identificando 8 problemas
críticos de usabilidad basados en los principios de diseño de interfaces.

| #     | Problema detectado                                                                                                                                                                                                     | Dónde                                             | Pauta o principio que incumple                                                                                    | Gravedad (0-4) | Propuesta de mejora                                                                                                                                        |
|:------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------|:------------------------------------------------------------------------------------------------------------------|:--------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **1** | **Menú desordenado, ambiguo y profundo:** El menú superior no sigue un estándar (empieza por "Ayuda" y termina en "Otros"), tiene nombres muy vagos ("COSAS", "Opciones2") y cascadas de hasta 6 niveles.              | Barra de menús superior.                          | **Menús Intuitivos / Regla de los 3 clics** (Tema 7).                                                             |     **3**      | Reorganizar en un orden estándar (Archivo, Editar, Socios, Cuotas, Ayuda), usar nombres descriptivos y limitar las acciones a un máximo de 3 clics.        |
| **2** | **Menú contextual saturado y con submenús:** Contiene 17 opciones, superando con creces el máximo recomendado, e incluye 6 opciones con cascadas (▶), dificultando la selección rápida.                                | Lista de socios (panel derecho).                  | **Menús Intuitivos (Contextuales)** (Tema 7): Máximo 7-10 elementos y evitar cascadas.                            |     **3**      | Reducir el menú a las 6 o 7 acciones más frecuentes (Ver ficha, Editar, Renovar, Congelar, Exportar, Dar de baja) sin submenús.                            |
| **3** | **Controles inadecuados que exigen memorización:** El campo "Sexo" pide escribir "H, M u O", la "Provincia" pide un código INE y el "Tipo cuota" usa checkboxes en lugar de opciones excluyentes.                      | Formulario de alta (panel izquierdo).             | **El control adecuado para cada dato / Minimiza la memoria** (Tema 6 y 7).                                        |     **3**      | Usar `RadioButton` para Sexo y Tipo de cuota (opciones excluyentes), y un `ComboBox` (desplegable) con los nombres reales de las provincias.               |
| **4** | **Color estridente y tipografía ilegible:** Fondo amarillo chillón, banner publicitario fucsia que distrae y un uso excesivo de mayúsculas, negritas, subrayados y exclamaciones rojas ("ALTA DE SOCIO NUEVO!!!").     | Toda la pantalla (especialmente fondo y títulos). | **Colores y Diseño / Tipografía Legible** (Tema 7): Texto oscuro sobre claro, no abusar del color ni de negritas. |     **3**      | Aplicar una paleta coherente de 3-4 colores (fondo claro neutro, texto oscuro, azul para acentos) y usar tamaños de fuente legibles sin exceso de énfasis. |
| **5** | **Mensaje de error técnico, culposo y confuso:** Muestra un código "0x45", no explica el problema, culpa al usuario ("abortada por el usuario"), usa doble negación ("¿Desea no cancelar?") y botones "Sí/No".         | Cuadro de diálogo modal (inferior derecho).       | **Mensajes Claros, positivos y con salida / Control del usuario** (Tema 7).                                       |     **4**      | Reescribir el mensaje explicando qué dato falló y cómo solucionarlo, en un tono positivo, usando botones con verbos claros (ej. "Corregir datos").         |
| **6** | **Falta de ayudas y formatos en campos de texto:** El campo "F. nac" obliga al usuario a escribir la fecha a mano con un formato ambiguo, y el campo "IBAN" no ofrece ningún ejemplo visual de lo que se espera.       | Formulario de alta (campos "F. nac" e "IBAN").    | **Evita y explica errores / Control apropiado** (Tema 6 y 7).                                                     |     **3**      | Sustituir la caja de fecha por un selector de fecha (`DatePicker`) y añadir texto de ejemplo (`promptText`) en el IBAN.                                    |
| **7** | **Botón destructivo gigante y botones inconsistentes:** El botón "BORRAR TODO" es enorme, rojo y está pegado a los de aceptar. Además, se mezclan idiomas y redundancias en los botones "OK", "Cancel" y "Aceptar".    | Botonera inferior del formulario.                 | **Control del usuario / Elementos Interactivos** (Tema 7): Acciones destructivas separadas y confirmación.        |     **4**      | Destacar la acción principal ("Guardar socio"), alejar el botón de limpiar, renombrarlos claramente y añadir un diálogo de confirmación antes de borrar.   |
| **8** | **Tabla sin contexto ni legibilidad:** La lista de socios no tiene título que explique qué es, las columnas carecen de encabezados obligando a adivinar qué significa cada número o letra, y mezcla formatos de fecha. | Tabla de datos (panel derecho).                   | **Estructura Clara / Tablas y gráficos** (Tema 7): Encabezados de columna claros y título.                        |     **3**      | Añadir un título ("Listado de socios"), incorporar cabeceras a la tabla (Nombre, Fecha, Cuota, Provincia, Activo) y unificar los formatos visualizados.    |

---

# Parte 2 · Mini Test con Usuarios y Conclusión

Para evaluar la usabilidad del rediseño de **GymFit Manager**, se ha llevado a cabo una prueba de usabilidad con dos
usuarios de distinto perfil tecnológico.

## 1. Definición del Test

* **Tarea asignada:** Rellenar el formulario de alta con un IBAN erróneo a propósito para provocar el mensaje de error,
  corregir el dato, guardar el socio, seleccionarlo en la tabla derecha y eliminarlo usando el botón con el diálogo de
  confirmación.
* **Entorno:** Aplicación ejecutada en escritorio a resolución completa (1280x800).

## 2. Resultados y Métricas

| Métrica                | Usuario 1 (Perfil joven / Informático)                                                                                        | Usuario 2 (Perfil adulto / Usuario básico)                                                                                               |
|:-----------------------|:------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------|
| **Completada**         | Sí                                                                                                                            | Sí                                                                                                                                       |
| **Tiempo empleado**    | 1 min 10 seg                                                                                                                  | 2 min 45 seg                                                                                                                             |
| **Errores cometidos**  | 0 errores.                                                                                                                    | 1 error leve (intentó escribir la fecha a mano en lugar de usar el icono del calendario).                                                |
| **Satisfacción (1-5)** | 5 / 5                                                                                                                         | 4 / 5                                                                                                                                    |
| **Observaciones**      | El usuario utilizó el menú contextual (clic derecho en la tabla) de forma instintiva. Le pareció muy claro el modal de error. | Agradeció que los botones destructivos estuvieran lejos de los principales. El mensaje de error le ayudó a saber qué fallaba en el IBAN. |

## 3. Conclusión y Mejoras Concretas

El test confirma que el rediseño ha resuelto los bloqueos de la interfaz original. El flujo de lectura de arriba a abajo
y el uso de controles nativos (`DatePicker`, `RadioButton`) guían al usuario de forma natural, logrando que perfiles con
baja competencia digital completen la tarea sin frustración.

A partir de la observación de los usuarios, se han detectado **dos mejoras concretas** para implementar en futuras
versiones:

1. **Ordenación del desplegable:** El `ComboBox` de Provincias debe cargarse ordenado alfabéticamente. El Usuario 2
   tardó unos segundos de más buscando su provincia al estar ordenadas por código interno en lugar de por la inicial.
2. **Feedback visual de éxito:** Al pulsar "Guardar socio" y ejecutarse todo correctamente, el formulario simplemente se
   limpia. Se propone añadir un pequeño aviso visual (tipo *Toast* o un texto verde temporal) que diga *"Socio guardado
   correctamente"*, para evitar que el usuario dude de si el clic ha funcionado antes de mirar la tabla.