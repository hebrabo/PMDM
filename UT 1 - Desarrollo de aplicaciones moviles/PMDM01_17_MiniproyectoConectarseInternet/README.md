# ABN Juegos de Cálculo (3-5 años)

Miniproyecto desarrollado para la asignatura de PMDM, enfocado en el aprendizaje del método ABN mediante juegos interactivos.

## Características Técnicas

### Conectividad y Red (REST)
* **Servicio Web RESTful**: La aplicación realiza solicitudes estandarizadas mediante URIs para recuperar un catálogo de 100 juegos.
* **Retrofit**: Implementación de un cliente de red para la comunicación con el servidor.
* **Inicialización Diferida**: Uso de `by lazy` para delegar la creación del objeto API al momento de su primer uso, optimizando el rendimiento.

### Gestión de Datos y Serialización
* **Kotlinx Serialization**: Se utiliza la biblioteca oficial de Kotlin para convertir cadenas JSON en objetos de datos (`data class`).
* **Anotaciones de Serialización**: Uso de `@Serializable` y `@SerialName` para mapear claves de pares clave-valor del JSON a propiedades de Kotlin.
* **Análisis de Arrays JSON**: La respuesta del servicio web se procesa como una colección de objetos estructurados.

### Programación Asíncrona (Corrutinas)
* **No bloqueo del Main Thread**: Las operaciones de red se ejecutan en segundo plano utilizando `viewModelScope.launch` para mantener la interfaz fluida.
* **Puntos de Suspensión**: Uso de funciones `suspend` para pausar y reanudar tareas de larga duración sin detener el hilo principal.
* **Gestión de Errores y Cancelación**: Bloques `try-catch` que capturan y relanzan la `CancellationException` para garantizar una limpieza de recursos adecuada.

## Herramientas utilizadas
* Kotlin & Jetpack Compose
* Retrofit 2
* Kotlinx Serialization
* Coil (Carga asíncrona de imágenes)