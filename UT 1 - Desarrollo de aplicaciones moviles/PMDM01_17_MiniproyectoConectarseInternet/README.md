# Actividades de Cálculo para Niños (3-5 años)

Este proyecto es una aplicación educativa diseñada para facilitar el aprendizaje del cálculo mediante la metodología **ABN**. La app ofrece acceso a una biblioteca dinámica de juegos interactivos organizados por categorías de conteo, numeración y operaciones.

## 📺 Vídeo de Demostración
Puedes ver el funcionamiento de la aplicación, la carga asíncrona de datos y la navegación aquí:

![Vídeo del Proyecto](funcionamiento_miniproyecto.webm)

*(Si el vídeo no se reproduce directamente, puedes encontrarlo en la raíz del repositorio como `Video.webm`)*

---

## Requisitos del Proyecto

### 1. Obtención de datos de Internet (Get data from the internet)
- Implementación de **Retrofit** para descargar de forma dinámica el listado de juegos en formato JSON desde un repositorio de GitHub.
- Uso de **GsonConverterFactory** para el mapeo automático de los datos JSON a objetos Kotlin (`JuegoAbn`).

### 2. Carga de imágenes de Internet (Load and display images)
- Integración de la librería **Coil** mediante el componente `AsyncImage`.
- Visualización de miniaturas de cada actividad descargadas directamente desde URLs externas.

---

## Implementación Técnica de Corrutinas
La gestión de la asincronía se basa en los siguientes principios:

- **Puntos de Suspensión:** Se han definido funciones `suspend` para la comunicación con la API. El código incluye puntos de suspensión donde la ejecución puede pausarse y reanudarse más tarde sin detener la aplicación.
- **No bloqueo del Hilo Principal (Main Thread):** Gracias al uso de `viewModelScope.launch`, el hilo principal de la interfaz de usuario no se bloquea mientras la corrutina espera completar tareas de red o retardos.
- **Ejecución Simultánea:** Se utiliza el constructor `launch` para iniciar corrutinas que se ejecutan de forma simultánea, optimizando el tiempo de carga.
- **Sincronización con CoroutineScope:** Para garantizar que las funciones de carga completen su ejecución antes de actualizar estados críticos de la IU, se han agrupado tareas mediante bloques `coroutineScope`.
- **Gestión de Cancelación y Errores:** - Implementación de bloques `try-catch` para el manejo de excepciones de red.
    - Captura específica de `CancellationException`, asegurando que la excepción se vuelva a lanzar (`throw e`) para permitir una cancelación limpia de la corrutina según las pautas de clase.

---

## Arquitectura
La aplicación sigue el patrón **MVVM** (Model-View-ViewModel) y utiliza un estado de interfaz reactivo (`AbnUiState`) con tres estados definidos:
1. **Loading:** Se muestra un indicador de carga mientras la corrutina está activa.
2. **Success:** Se muestra la lista de juegos una vez recibidos los datos.
3. **Error:** Permite al usuario reintentar la carga si falla la conexión.
