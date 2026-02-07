# Documento de diseño del sistema

## Introducción

En este proyecto se implementará de forma online el juego Upstream, un juego por turnos de 2 - 5 jugadores. Con una duración aproximada de 20-30 minutos.

El juego se ubica en primavera donde el deshielo llena los cauces de los ríos, abriendo el camino de vuelta para salmones. Cada jugador controlará un banco de salmones que remontará el rio para dejar sus huevos en el lugar donde nacieron, durante el camino deberán evitar ser devorados por varios depredadores naturales como pueden ser osos, garzas o águilas.

Cuando todos los salmones estén en la zona de desove en lo alto del río, o hayan perecido, la partida acaba y el jugador con más puntos será el ganador. 
Para la preparación del juego se seguirán los siguientes pasos: 

1.	Colocar las losetas de mar, donde los salmones empezarán su viaje. 
2.	El resto de las losetas serán barajadas y colocadas formando una pila de robo para posteriormente crear el tablero de juego. 
3.	Cada jugador elige un color y se le asignará el banco de salmones con el color que cada jugador haya escogido, con la finalidad de que se identifiquen a los salmones de los jugadores. 
4.	Se colocará una ficha de salmón en cada loseta de mar (4 en total) por la cara donde aparecen dos salmones. Teniendo como resultado en cada loseta de mar tantas fichas de salmón como jugadores haya. 
5.	Una vez colocadas las fichas se formará el tablero, para ello el jugador que lleve menos tiempo sin mover salmón y en sentido de las agujas del reloj, cogerá una loseta de la pila de robo y la colocará en el tablero (al colocar las fichas se buscará completar la primera columna, no se puede colocar losetas en otras sin haber completado previamente la anterior), se repetirá este paso hasta que tengamos 4 losetas en cada fila que forman las 3 losetas de mar del comienzo.  
6.	Una vez creado el tablero, al siguiente jugador que le correspondería colocar ficha se le otorga la ficha de inicio, la cual cambia en cada ronda para saber quién empieza su turno primero.

Una vez creado el tablero se explicará la mecánica de juego:
Empezando por el jugador inicial, los jugadores toman turnos en sentido horario para mover sus fichas de Salmón. Disponen de 5 puntos de movimientos que deben gastar obligatoriamente. Una vez que todos los jugadores hayan realizado su turno, se habrá completado una ronda. Al final de cada ronda, el jugador inicial entrega la ficha de jugador inicial al siguiente jugador, retira las 3 últimas losetas de río y coloca 3 más de la pila en la parte superior del río, formando una nueva línea. Puede elegir la orientación de las losetas si es relevante (si tienen obstáculos). Si se retira una loseta donde hay una o más fichas de Salmón, se devuelven a la caja

¡IMPORTANTE!
Durante la primera ronda de juego, no se retiran losetas, sólo se colocan 3 nuevas losetas. Al final de la segunda ronda, se retiran las 4 losetas de mar, y se colocan 3 nuevas losetas. Tras esto, cada ronda se irá retirando y colocando losetas de 3 en 3 hasta agotar la pila de losetas.

Se podrá mover de dos formas distintas:
-	Nadar: Moverse de una loseta a otra sin obstáculos en medio se considera nadar. Nadar cuesta 1 punto por loseta a la que se desplace la ficha. Si una loseta está llena (capacidad = tantas fichas como jugadores), las fichas de salmón no pueden cruzarla nadando ni pararse en ella.
-	Saltar: Moverse de una loseta a otra ignorando todos los obstáculos se considera saltar. Cuesta 1 punto de movimiento, más 1 punto por cada loseta saltada. Sólo se puede saltar en línea recta, no se puede cambiar de dirección durante el salto

Cuando se vayan a colocar las últimas dos losetas de río, éstas deben colocarse a los lados, para poner las de desove en el centro, al final del río. Cuando una ficha llegue a la primera loseta de desove debe detenerse y ya no podrán gastarse puntos de movimiento con esa ficha. Al final de cada ronda se sigue eliminando la fila inferior del río, pero no se colocan nuevas losetas. En lugar de eso se mueven todas las fichas en el lago del desove a la casilla siguiente.

En cuanto a los criterios de puntuación:
Cuando todos los salmones estén en las losetas de desove o en la caja, se acaba la partida automáticamente y cada jugador recibe sus puntos: 
-	Por cada salmón (que no ficha de salmón) que tenga, 1 punto. 
-	Por cada ficha de salmón que tenga, 1 punto por cada huevo que haya en la casilla de desove donde se encuentra.
  
El jugador con mayor puntuación será el ganador. En caso de empate ganará el que tenga más salmones. Si persiste el empate ganará quien tenga más fichas o en su defecto, quien tenga sus fichas más adelantadas.

(https://drive.google.com/file/d/19Ku1wfw4lbaCAq-IUtilbu90doZeNdWA/view?usp=sharing)

## Diagrama(s) UML:

### Diagrama de Dominio/Diseño

![Diagrama de diseño](https://github.com/user-attachments/assets/f9f99e70-6e01-4524-b528-75ecd640954e)


### Diagrama de Capas (incluyendo Controladores, Servicios y Repositorios)

Código que le pasamos a PlantText UML para obtenerl el diagrama de capas al completo.

[DIGRAMA DE CAPAS.txt](https://github.com/user-attachments/files/18397808/DIGRAMA.DE.CAPAS.txt)


Para poder apreciar mejor las asociaciones entre clases lo dividimos en diferentes subdiagramas.


![Diagrama de capas PT1](https://github.com/user-attachments/assets/723129ac-66df-412c-9aef-03f25896974c)


![Diagrama de capas PT2](https://github.com/user-attachments/assets/ce5c8ba0-7a65-4102-b5e7-54bc5058f912)


![Diagrama de capas PT3](https://github.com/user-attachments/assets/06614402-000c-40fa-9006-3cef104e0886)

![MiDiagramaDecapas](https://github.com/user-attachments/assets/b6c066fa-443d-4ece-88a8-2d78536641c8)


## Descomposición del mockups del tablero de juego en componentes

En esta sección procesaremos el mockup del tablero de juego (o los mockups si el tablero cambia en las distintas fases del juego). Etiquetaremos las zonas de cada una de las pantallas para identificar componentes a implementar. Para cada mockup se especificará el árbol de jerarquía de componentes, así como, para cada componente el estado que necesita mantener, las llamadas a la API que debe realizar y los parámetros de configuración global que consideramos que necesita usar cada componente concreto. 

### HOME
![image](https://github.com/user-attachments/assets/ee7debc9-fbf8-4246-9789-61fb1e9fbfd5)
  
- $\color{yellow}{\textsf{NavBar – Barra de navegación lateral}}$
- $\color{orange}{\textsf{Auth-button-intro – Contenedor de los botones de create y join}}$
  - $\color{red}{\textsf{Button – Botón de crear partida}}$
  - $\color{red}{\textsf{Button – Botón de unirse a una partida}}$ 

### CREATE MATCH
![image](https://github.com/user-attachments/assets/83311797-e149-42ff-ae87-791138dffac4)

- $\color{yellow}{\textsf{NavBar – Barra de navegación lateral}}$
- $\color{red}{\textsf{Auth-page-container – Contenedor de la ventana}}$
  - $\color{green}{\textsf{Auth-form-container – Contenedor del formulario}}$
    - $\color{blue}{\textsf{Custom-form-input – Entrada de información del formulario}}$
    - $\color{purple}{\textsf{Custom-button-row – Contenedor de los botones de save y cancel}}$
      - $\color{brown}{\textsf{Button – Botones de save y cancel}}$

### MATCHES
![image](https://github.com/user-attachments/assets/46ac4d47-e4b2-4f9e-ab49-801b1967b179)

- $\color{yellow}{\textsf{NavBar – Barra de navegación lateral}}$
- $\color{red}{\textsf{Match-list-box – Contenedor de las ventana de partidas}}$
  - $\color{orange}{\textsf{Text-center – Contenedor del título MATCHES}}$
  - $\color{green}{\textsf{Button – Boton para crear una partida nueva}}$
  - $\color{blue}{\textsf{Table – Contenedor de la información de las partidas existentes}}$
      - $\color{purple}{\textsf{Thread – Campos de la tabla}}$
      - $\color{brown}{\textsf{TBody – Cuerpo de la tabla}}$
      - $\color{black}{\textsf{Button – Botón de unirse a la partida}}$

### JOIN
![image](https://github.com/user-attachments/assets/14f96007-a41c-42f5-884f-8b7ec3e2e3ef)

- $\color{yellow}{\textsf{NavBar – Barra de navegación lateral}}$
- $\color{red}{\textsf{Auth-page-container – Contenedor del contenido de la ventana}}$
  - $\color{orange}{\textsf{Text-center – Contenedor del título de la partida}}$
  - $\color{purple}{\textsf{Button – Botón de unirse a la lista de jugadores}}$
    
### PLAYER LIST
![image](https://github.com/user-attachments/assets/53c9b869-ad6f-477e-86b0-a0aa42048c1b)

- $\color{yellow}{\textsf{NavBar – Barra de navegación lateral}}$
- $\color{red}{\textsf{Match-list-box – Contenedor de las ventana de jugadores}}$
  - $\color{orange}{\textsf{Text-center – Contenedor del título de la partida}}$
  - $\color{green}{\textsf{Card – Contenedor donde iran cada uno de los jugadores}}$
  - $\color{blue}{\textsf{Card – Contenedor donde iran cada uno de los jugadores}}$
    - $\color{purple}{\textsf{Button – Botón de invitar amigos}}$
    - $\color{pink}{\textsf{Input – Formulario para añadir el nombre del Player que queramos invitar}}$
    - $\color{brown}{\textsf{Button – Botón de play}}$

### BOARD
![image](https://github.com/user-attachments/assets/0b0e7071-a38b-4ef4-a36a-881b25eeeb65)

- $\color{yellow}{\textsf{NavBar – Barra de navegación lateral}}$
- $\color{pink}{\textsf{Movimientos-window – Contenedor del contador de movimiento de los salmones}}$
- $\color{red}{\textsf{Board-grid – Contenedor del conjunto de losetas}}$
  - $\color{orange}{\textsf{Board-grid – Contenedor del conjunto de losetas}}$
    - $\color{gray}{\textsf{Board-row – Fila de losetas}}$
    -  $\color{green}{\textsf{Board-cell – Loseta}}$
  - $\color{blue}{\textsf{Tile-stack – Contenedor de la pila de losetas y sus opciones}}$
    - $\color{lightblue}{\textsf{Tile-stack-button – Botón de rotar las losetas del tileStack}}$
    - $\color{darkblue}{\textsf{Img – Imágen de la loseta que toca colocar en el tablero}}$
  - $\color{brown}{\textsf{Player-card – Carta de cada jugador (indica el color y el turno)}}$
  - $\color{ligthred}{\textsf{Chat – Botón que abre una ventana emergente donde aparece el chat de los jugadores}}$
  - $\color{purple}{\textsf{Rules – Botón que abre una ventana emergente donde aparece el conjunto de reglas del juego}}$

### FINAL DE PARTIDA
![image](https://github.com/user-attachments/assets/606d7482-5f77-4232-9292-9b929b67c61e)

- $\color{yellow}{\textsf{NavBar – Barra de navegación lateral}}$
- $\color{red}{\textsf{Div – Contenedor del contenido del final de partida}}$
  - $\color{orange}{\textsf{Div – Contenedor del anuncio del ganador}}$
    - $\color{bule}{\textsf{H1 – Anuncio del ganador}}$
    - $\color{green}{\textsf{Div – Nombre del ganador}}$
  - $\color{purple}{\textsf{Button – Botón de volver a la lista de partidas}}$   

### PROFILE
![image](https://github.com/user-attachments/assets/53fa566f-0280-42c5-bd76-bb4eb958536d)
![image](https://github.com/user-attachments/assets/a0f932dc-b12a-47b7-889d-0de14572cc69)

- $\color{orange}{\textsf{Img – Imagen del avatar del jugador}}$
- $\color{green}{\textsf{Form – Formulario con los datos del perfil}}$
- $\color{cyan}{\textsf{Toggle – Switch de tipo de jugador}}$
- $\color{purple}{\textsf{Input – Campo Avatar URL}}$
- $\color{purple}{\textsf{Desplegable – Generos}}$
- $\color{red}{\textsf{Desplegable – Plataformas}}$
- $\color{cyan}{\textsf{Desplegable – Sagas}}$
- $\color{green}{\textsf{Div – Tiempo medio en partida}}$
- $\color{orange}{\textsf{Div – Tiempo total en partoda}}$

## Documentación de las APIs
Se considerará parte del documento de diseño del sistema la documentación generada para las APIs, que debe incluir como mínimo, una descripción general de las distintas APIs/tags  proporcionadas. Una descripción de los distintos endpoints y operaciones soportadas. Y la especificación de las políticas de seguridad especificadas para cada endpoint y operación. Por ejemplo: “la operación POST sobre el endpoint /api/v1/game, debe realizarse por parte de un usuario autenticado como Player”.

Si lo desea puede aplicar la aproximación descrita en https://vmaks.github.io/2020/02/09/how-to-export-swagger-specification-as-html-or-word-document/ para generar una versión en formato Word de la especificación de la API generada por OpenAPI, colgarla en el propio repositorio y enlazarla en esta sección del documento.  En caso contrario debe asegurarse de que la interfaz de la documentación open-api de su aplicación está accesible, funciona correctamente, y está especificada conforme a las directrices descritas arriba.

## Patrones de diseño y arquitectónicos aplicados
El proyecto implementa múltiples patrones de diseño, tanto en el backend como en el frontend, que refuerzan su arquitectura modular, escalable y mantenible.

### Patrón: Modelo-Vista-Controlador (MVC)

Tipo: Arquitectónico

Contexto de Aplicación:
El patrón MVC se ha aplicado en toda la arquitectura del backend utilizando Spring Boot. Este patrón organiza la aplicación en tres componentes principales:
-	Modelo: Clases relacionadas con la lógica de negocio y las entidades JPA para la persistencia de datos.
-	Vista: Representada por el frontend en React, aunque este se maneja como una aplicación independiente.
-	Controlador: Clases en el backend anotadas con @RestController, encargadas de gestionar las solicitudes HTTP y actuar como intermediarias entre el modelo y la vista.

Clases o paquetes creados:
-	Controladores: BoardController, PlayerController, MatchController.
-	Servicios: BoardService, PlayerService.
-	Modelos: Clases en el paquete es.us.dp1.l4_04_24_25.Upstream.model.

Ventajas alcanzadas al aplicar el patrón:
-	Separación clara de responsabilidades entre la lógica de negocio, la interfaz de usuario y la interacción con la base de datos.
-	Facilita el mantenimiento y la escalabilidad del sistema.
-	Permite trabajar de forma independiente en el frontend y el backend.

### Patrón: Aplicación de Página Única (SPA)

Tipo: Arquitectónico

Contexto de Aplicación:
El frontend utiliza React para implementar una aplicación SPA. Esto permite que las vistas dinámicas se carguen y actualicen sin recargar toda la página.

Clases o paquetes creados:
-	Componentes React para vistas dinámicas.
-	Configuración del proxy en el archivo package.json para redirigir solicitudes al backend.

Ventajas alcanzadas al aplicar el patrón:
-	Mejora la experiencia del usuario al reducir tiempos de carga.
-	Minimiza la carga en el servidor al evitar el renderizado completo de la página.
-	Facilita la implementación de interacciones complejas en el cliente.

### Patrón: Inyección de Dependencias

Tipo: De Diseño

Contexto de Aplicación:
Spring Boot aplica la inyección de dependencias para gestionar los objetos necesarios en diferentes partes de la aplicación. Esto permite desacoplar la creación de objetos de su uso.

Clases o paquetes creados:
-	Anotaciones como @Service, @Repository, @Autowired en clases como BoardService y PlayerService.
-	Clases de configuración como SecurityConfiguration y JwtUtils.

Ventajas alcanzadas al aplicar el patrón:
-	Mejora la modularidad y facilita las pruebas unitarias.
-	Promueve el principio de inversión de control, permitiendo la gestión centralizada de dependencias.
-	Facilita el mantenimiento al reducir la dependencia explícita entre componentes.

### Patrón: Hook (Frontend - React)

Tipo: De Diseño

Contexto de Aplicación:
En el frontend, se utilizan hooks como useState y useEffect para manejar estados locales y efectos secundarios, respectivamente.

Clases o paquetes creados:
-	Componentes funcionales en React que utilizan hooks para manejar interacciones del usuario y sincronización con el backend.

Ventajas alcanzadas al aplicar el patrón:
-	Simplifica la gestión del estado dentro de los componentes funcionales.
-	Reduce el uso de clases en React, haciendo el código más legible y fácil de mantener.
-	Facilita la integración de operaciones asincrónicas como llamadas API.

### Patrón: Repositorio (Repository)

Tipo: De Diseño

Contexto de Aplicación:
El patrón de repositorio se utiliza para encapsular el acceso a la base de datos mediante interfaces que gestionan operaciones CRUD.

Clases o paquetes creados:
-	Repositorios como PlayerRepository, BoardRepository y MatchRepository, que extienden JpaRepository o CrudRepository.

Ventajas alcanzadas al aplicar el patrón:
-	Centraliza el acceso a los datos, facilitando el mantenimiento.
-	Reduce el código repetitivo gracias a los métodos predefinidos en JpaRepository.
-	Promueve el desacoplamiento entre la lógica de negocio y la capa de persistencia.

### Patrón: Singleton

Tipo: De Diseño

Contexto de Aplicación:
El patrón Singleton se aplica en clases de configuración para garantizar que solo exista una instancia de estas en toda la aplicación.

Clases o paquetes creados:
-	Clases como SecurityConfiguration y WebConfig en el paquete configuration.

Ventajas alcanzadas al aplicar el patrón:
-	Asegura que las configuraciones críticas sean consistentes en toda la aplicación.
-	Evita problemas de inicialización múltiple en configuraciones compartidas.

### Patrón: Proxy

Tipo: De Diseño

Contexto de Aplicación:
El proxy se configura en el archivo package.json del frontend para redirigir automáticamente las solicitudes al backend durante el desarrollo.

Clases o paquetes creados:
-	Configuración en el archivo package.json del proyecto React.

Ventajas alcanzadas al aplicar el patrón:
-	Simplifica la comunicación entre el frontend y el backend en entornos locales.
-	Reduce la complejidad de las URLs en el código del frontend.

### Patrón: Polling

Tipo: De Diseño

Contexto de Aplicación:
El patrón de polling se puede aplicar en el frontend para realizar verificaciones periódicas de cambios en el backend, como actualizaciones en tiempo real o nuevos datos.

Clases o paquetes creados:
-	Métodos en componentes React para realizar solicitudes HTTP periódicas usando bibliotecas como axios.

Ventajas alcanzadas al aplicar el patrón:
-	Permite mantener actualizada la información mostrada al usuario.
-	Es una solución sencilla cuando no se utiliza WebSockets.

### Patrón: Factory/Builder

Tipo: De Diseño

Contexto de Aplicación:
El patrón Factory/Builder se utiliza implícitamente en métodos que gestionan la construcción de objetos complejos, como tokens JWT.

Clases o paquetes creados:
-	Clases como JwtUtils que encapsulan la lógica de creación de tokens.

Ventajas alcanzadas al aplicar el patrón:
-	Facilita la creación de objetos complejos al encapsular su lógica.
-	Mejora la mantenibilidad al centralizar la construcción de objetos.

### Patrón: Observer

Tipo: De Diseño

Contexto de Aplicación:
El patrón Observer puede aplicarse para manejar notificaciones de eventos o cambios en el estado del sistema.

Clases o paquetes creados:
-        Servicios que gestionan eventos del sistema en el backend, como notificaciones entre usuarios.
 
Ventajas alcanzadas al aplicar el patrón:
-	Permite la notificación automática de cambios a múltiples partes interesadas.
-	Mejora la extensibilidad del sistema al soportar nuevas suscripciones.

## Decisiones de diseño
_En esta sección describiremos las decisiones de diseño que se han tomado a lo largo del desarrollo de la aplicación que vayan más allá de la mera aplicación de patrones de diseño o arquitectónicos._

### Decisión 1: Representación del tablero como una matriz de celdas (Tile)
#### Descripción del problema:

Se necesitaba una estructura de datos para representar el tablero del juego y manejar las posiciones de las fichas de salmón.

#### Alternativas de solución evaluadas:
#### Alternativa 1.a: Uso de una lista unidimensional (ArrayList)

Ventajas: Simplicidad en la implementación.

Inconvenientes: Más compleja la gestión de coordenadas en dos dimensiones.

#### Alternativa 1.b: Matriz bidimensional (Tile[][])

Ventajas: Facilita la gestión de las posiciones (x, y) y el acceso directo a cada celda.

Inconvenientes: Requiere inicialización del tamaño fijo del tablero.

#### Justificación de la solución adoptada

Finalmente, Se optó por la matriz bidimensional (alternativa 1.b) para representar el tablero (Tile[][]) debido a la facilidad de acceso y manipulación de coordenadas, facilitando la lógica de movimiento y colocación de las fichas.


### Decisión 2: Generación dinámica de colores para los jugadores al crear un tablero
#### Descripción del problema:

Al crear un nuevo tablero asociado a una partida, era necesario asignar colores únicos a cada jugador de forma automática. Esto aseguraba una experiencia de usuario más fluida, evitando conflictos o repetición de colores entre los jugadores.

#### Alternativas de solución evaluadas:
#### Alternativa 2.a: Permitir que los jugadores seleccionen sus propios colores manualmente antes de iniciar la partida.

Ventajas: Da a los jugadores más control sobre su personalización.

Inconvenientes: Más compleja la gestión de coordenadas en dos dimensiones.

#### Alternativa 2.b: Generar colores de forma aleatoria utilizando una lista predefinida y eliminando los colores asignados para evitar repeticiones.

Ventajas: Simplifica el flujo de creación del tablero al no requerir interacción adicional del usuario.

Inconvenientes: Los jugadores no pueden elegir sus colores, lo que podría ser menos satisfactorio para algunos.

#### Justificación de la solución adoptada

Se optó por la alternativa 2.b, implementando un sistema que asigna colores únicos a los jugadores al azar al momento de crear el tablero. Esta decisión se justificó por su simplicidad y su capacidad para garantizar que los colores sean únicos sin requerir pasos adicionales del usuario. Además, se utilizó la clase Random para seleccionar un color de una lista predefinida y eliminarlo de esta una vez asignado, asegurando la no repetición.


### Decisión 3: Validación de existencia del tablero antes de operaciones CRUD
#### Descripción del problema:

En operaciones de actualización (PUT) y eliminación (DELETE) del tablero, se debe asegurar que el recurso existe en la base de datos antes de realizar cualquier acción. Sin esta validación, podrían ocurrir errores que afectarían la integridad de la aplicación.

#### Alternativas de solución evaluadas:
#### Alternativa 3.a: Realizar las operaciones directamente sin validación previa.

Ventajas: Simplifica la implementación al no necesitar verificaciones adicionales.

Inconvenientes: Puede generar excepciones no controladas si el recurso no existe, lo que impactaría negativamente en la estabilidad de la aplicación.

#### Alternativa 3.b: Implementar una validación manual con bloques condicionales (if-else).

Ventajas: Permite un control más detallado del flujo de validación.

Inconvenientes: Duplica código y requiere lógica adicional en cada método, lo que puede dificultar el mantenimiento.

#### Alternativa 3.c: Utilizar un método de validación centralizada mediante servicios reutilizables (solución adoptada).

Ventajas: Centraliza la lógica de validación, reutilizándola en múltiples endpoints. Esto reduce la duplicación de código y mejora la consistencia en la gestión de recursos.

Inconvenientes: Requiere la creación de métodos adicionales en la capa de servicio, añadiendo complejidad inicial.

#### Justificación de la solución adoptada

Se eligió la alternativa 3.c, implementando una validación a través del servicio boardService.getBoardById(id). Esta solución asegura que cada operación CRUD verifique la existencia del tablero, arrojando una excepción (ResourceNotFoundException) en caso de que no se encuentre. Esta estrategia mejora la robustez del sistema, ya que garantiza que solo se manipulen recursos válidos.



### Decisión 4: Gestión del turno de los jugadores
#### Descripción del problema:

Se necesitaba una manera eficiente de manejar el turno de los jugadores en el juego, asegurando que cada uno pueda realizar su movimiento de forma secuencial y ordenada.

#### Alternativas de solución evaluadas:
#### Alternativa 4.a: Uso de un entero (int) para representar el turno

Ventajas: Implementación sencilla y directa. Además, facilita la alternancia entre jugadores (por ejemplo,0 para el primer jugador, 1 para el segundo, etc).

Inconvenientes: Menor flexibilidad si se agregan más jugadores o reglas específicas. Tampoco ofrece una identificación clara en contextos donde los jugadores tienen características como colores asignados.

#### Alternativa 4.b: Uso de una lista de colores (color[]) para gestionar los turnos

Ventajas: Cada jugador está identificado por un color, lo que facilita la diferenciación visual y lógica. Mayor flexibilidad al agregar o eliminar jugadores.La asignación aleatoria de colores mejora la dinámica del juego y ofrece variedad en cada partida.

Inconvenientes: La implementación es ligeramente más compleja debido a la necesidad de gestionar la lista de colores y su rotación.

#### Alternativa 4.c: Uso de una cola (Queue) para gestionar los turnos

Ventajas: Permite una gestión ordenada y eficiente del turno de los jugadores, utilizando una estructura de tipo FIFO (First In, First Out). Facilita la implementación de reglas avanzadas, como saltos de turno o penalizaciones. Flexible, por lo tanto es ideal si se agregan nuevos jugadores o modifican las reglas.

Inconvenientes: Requiere un manejo adicional de la estructura de datos para gestionar correctamente el cambio de turnos.

#### Justificación de la solución adoptada

Se ha optado por implementar la alternativa 4.b, usando la lista de colores y la propiedad color para la gestión de los turnos debido a que cada jugador tiene asignado un color específico, lo que facilita su identificación tanto en la interfaz como en la lógica del juego, la lista permite gestionar el cambio de turno de manera cíclica, garantizando que cada jugador tenga su oportunidad de jugar de forma ordenada y esta selección aleatoria de los colores al inicio del juego asegura que ningún jugador tenga una ventaja predefinida.



### Decisión 5: Gestión de la colocación de las 3 losetas al final de cada ronda
#### Descripción del problema:

Se requería un sistema eficiente para manejar la colocación de 3 losetas al final de cada ronda, asegurando que el jugador correspondiente realizara esta acción después de que todos los jugadores hubieran gastado sus puntos de movimiento. El objetivo era garantizar un flujo ordenado y estratégico, respetando las mecánicas del juego y añadiendo una fase clara al cierre de cada ronda.

#### Alternativas de solución evaluadas
#### Alternativa 5.a: Colocación automática por el sistema

Ventajas: Totalmente automatizada, reduce el tiempo de juego y elimina la necesidad de que los jugadores intervengan.

Inconvenientes: Limita la estrategia, ya que los jugadores no tienen control sobre dónde se colocan las losetas. Puede ser percibida como menos interactiva y menos satisfactoria para los jugadores.

#### Alternativa 5.b: Colocación por parte del jugador con turno al final de la ronda

Ventajas: Aumenta la implicación del jugador en el cierre de la ronda, añadiendo un elemento estratégico. Es intuitiva, ya que después de los movimientos, el turno de colocación pasa al jugador correspondiente. Permite una planificación estratégica sobre dónde colocar las losetas para afectar la próxima ronda de forma ventajosa.

Inconvenientes: Puede ralentizar el cierre de la ronda si el jugador no está seguro de dónde colocar las losetas.

#### Alternativa 5.c: Colocación consensuada entre todos los jugadores

Ventajas: Fomenta la interacción y el trabajo en equipo entre los jugadores. Puede generar dinámicas interesantes al negociar las posiciones de las losetas.

Inconvenientes: Incrementa significativamente el tiempo de la fase de cierre de ronda, especialmente si hay desacuerdos.

#### Justificación de la solución adoptada

Se ha optado por implementar la Alternativa 5.b: Colocación por parte del jugador con turno al final de la ronda, ya que ofrece un equilibrio ideal entre dinamismo, interacción y estrategia. Esta solución asegura que al final de cada ronda, un jugador tenga la responsabilidad de colocar las 3 losetas, lo que añade un elemento táctico al cierre de la ronda y prepara el tablero para la próxima fase del juego.



### Decisión 6: Implementación de un chat para la comunicación entre jugadores
#### Descripción del problema:

Se identificó la necesidad de habilitar un sistema que permitiera a los jugadores de una partida comunicarse entre ellos en tiempo real. Esto era crucial para fomentar la interacción social y mejorar la experiencia del juego.

#### Alternativas de solución evaluadas
#### Alternativa 6.a: Chat de texto libre integrado en la interfaz del juego

Ventajas: Permite una comunicación completamente personalizada, adecuada para cualquier tipo de interacción entre los jugadores. Mejora la experiencia del usuario al proporcionar un medio de comunicación directo y en tiempo real.

Inconvenientes: Puede distraer a los jugadores si no se diseña de manera que sea discreto y no interfiera con la jugabilidad.

#### Alternativa 6.b: Uso de mensajes predefinidos

Ventajas: Implementación sencilla y rápida, con una lista fija de mensajes que los jugadores pueden seleccionar: por ejemplo, "¡Buen movimiento!", "Tu turno", "¿Qué opinas?"). Evita problemas relacionados con lenguaje inapropiado o moderación y facilita la comunicación rápida sin distraer a los jugadores.

Inconvenientes: Limita la capacidad de expresión, ya que los jugadores no pueden escribir mensajes personalizados.

#### Alternativa 6.c: Integración con plataformas externas de chat

Ventajas: Evita la necesidad de desarrollar e integrar un sistema de chat desde cero, reduciendo la carga de desarrollo. Permite a los jugadores usar herramientas familiares, como Discord, Slack o WhatsApp, para comunicarse para juegos en los que los jugadores ya suelen usar plataformas externas para coordinarse.

Inconvenientes: Puede romper la inmersión del juego al obligar a los jugadores a salir de la interfaz del mismo y no proporciona control directo sobre el sistema de chat.

#### Justificación de la solución adoptada
Se ha optado por implementar la Alternativa 6.a: Chat de texto libre integrado en la interfaz del juego, ya que ofrece una solución equilibrada entre flexibilidad, personalización y experiencia de usuario. Esta decisión asegura que los jugadores puedan comunicarse de manera efectiva y sin limitaciones, manteniéndose dentro del entorno del juego para garantizar una experiencia inmersiva.



### Decisión 7: Ubicación del botón para invitar amigos y recuadro para ingresar el nombre del usuario
#### Descripción del problema:

Se identificó la necesidad de permitir a los jugadores invitar a sus amigos a unirse a una partida ya creada. Esto fomentaría la participación de más jugadores y mejoraría la experiencia social del juego. Además, se decidió incluir un campo donde los jugadores puedan especificar el nombre del usuario a invitar, simplificando el proceso de invitación.

#### Alternativas de solución evaluadas:
#### Alternativa 7.a: Ubicación en la pantalla de espera de los jugadores, con un recuadro para el nombre del usuario:

Ventajas: Aprovecha un momento lógico para invitar a otros, ya que los jugadores suelen esperar antes de comenzar. El recuadro para ingresar el nombre permite que la invitación sea más específica y dirigida, y facilita el acceso a esta funcionalidad sin interrumpir la jugabilidad.

Inconvenientes: Puede generar confusión si el diseño no deja claro que la invitación es para la partida en curso o si no se explica adecuadamente cómo usar el recuadro.

#### Alternativa 7.b: Ubicación en un menú principal o en la interfaz general, con un campo de búsqueda:

Ventajas: Permite invitar amigos en cualquier momento, incluso antes de entrar a una partida.

Inconvenientes: No dejaba claro a qué partida específica se unirán los amigos invitados.

#### Alternativa 7.c: Ubicación en una notificación emergente durante el juego:

Ventajas: Permite invitar amigos incluso después de inicir la partida.

Inconvenientes: Puede distraer a los jugadores durante la partida y el proceso de ingreso del nombre podría ser confuso en un contexto emergente.

#### Justificación de la solución adoptada:
Se eligió la Alternativa 7.a: Ubicación en la pantalla de espera de los jugadores, con un recuadro para el nombre del usuario, ya que los jugadores pueden invitar a amigos de manera clara y dirigida, aprovechando el tiempo de espera antes de comenzar la partida. Esta solución asegura una experiencia fluida y mejora la interacción social.


### Decisión 8: Implementación del perfil avanzado de jugador

#### Descripción del problema:
Era necesario implementar una página pública de perfil para cada jugador que incluyera información personal, preferencias y estadísticas. Además, se debía permitir a los usuarios elegir un tipo de perfil que limite o no su actividad, y gestionar una imagen de perfil.

#### Solución implementada:
Se desarrollaron diferentes funcionalidades para cumplir con los requisitos de las historias de usuario del módulo "Perfil Avanzado":

- Se creó un modelo `PlayerProfile` asociado a `Player` para almacenar información editable: biografía, ubicación (obligatoria), fecha de nacimiento, géneros favoritos, plataformas y sagas favoritas.
- Se añadió una relación entre jugadores y sus preferencias (`genres`, `platforms`, `sagas`) mediante entidades relacionadas y sus respectivos repositorios y servicios.
- Se desarrollaron endpoints REST para actualizar y consultar estos datos desde el frontend, incluyendo validaciones de campos obligatorios y tipos de dato.
- Se implementó lógica en el backend para calcular y mostrar estadísticas (tiempo total y medio de juego por jugador) reutilizando datos de `Match` y movimientos de juego.
- Se añadió un campo `profileImageUrl` para que el jugador pueda asignar una imagen mediante una URL, la cual se renderiza en la vista pública del perfil.
- Se añadió la propiedad `profileType` que puede tomar los valores `HARDCORE` o `CASUAL`. Para jugadores `CASUAL`, se implementó una restricción que:
  - Limita a 2 partidas diarias mediante comprobación en el backend al crear una partida.
  - Impone un tiempo máximo de partida y movimiento, arrojando una excepción si se supera, finalizando la partida con mensaje de causa.
- Se modificaron los controladores y servicios de `Player`, `Match` y `Board` para incluir esta lógica de validación.
- Se realizaron pruebas unitarias y de integración para garantizar el correcto funcionamiento de cada funcionalidad descrita, incluyendo control de flujo y restricciones.

#### Ventajas alcanzadas:
- Perfil personalizable que mejora la experiencia social y competitiva.
- Control de uso del sistema para fomentar un juego equilibrado entre perfiles.
- Carga eficiente de datos y restricciones integradas en la lógica del backend.

## Refactorizaciones

### Refactorización 1: 
En esta refactorización añadimos un mapa de parámetros a la partida para ayudar a personalizar la información precalculada de la que partimos en cada fase del juego.
#### Estado inicial del código
``` 
import { Table, Button } from "reactstrap";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import { Link } from "react-router-dom";
import deleteFromList from "./../util/deleteFromList";
import { useState } from "react";
import getErrorModal from "./../util/getErrorModal";
const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();
/*const achievements = [
  {
    id: 1,
    name: "Experiencia básica",
    description: "Si juegas 10 partidas o más",
    badgeImage: "https://cdn-icons-png.flaticon.com/512/5243/5243423.png",
    threshold: "10",
    metric: "GAMES_PLAYED"
  },
  {
    id: 2,
    name: "Explorador",
    description: "Si juegas 25 partidas o más",
    badgeImage: "https://cdn-icons-png.flaticon.com/512/603/603855.png",
    threshold: "25",
    metric: "GAMES_PLAYED"
  },
  {
    id: 3,
    name: "Experto",
    description: "Si ganas 20 partidas o más",
    badgeImage: "https://cdn-icons-png.flaticon.com/512/4737/4737471.png",
    threshold: "20",
    metric: "VICTORIES"
  }
];*/
export default function AchievementList() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);
  const [achievements, setAchievements] = useFetchState([], `/api/v1/achievements`, jwt);
  const achievementList = achievements.map((a) => {
    return (
      <tr key={a.id}>
        <td className="text-center">{a.name}</td>
        <td className="text-center">{a.description}</td>
        <td className="text-center">
          <img
            src={a.badgeImage ? a.badgeImage : imgnotfound}
            alt={a.name}
            width="50px"
          />
        </td>
        <td className="text-center">{a.threshold}</td>
        <td className="text-center">{a.metric}</td>
        <td className="text-center">
          <Button outline color="warning" >
            <Link
              to={`/achievements/`+a.id} className="btn sm"
              style={{ textDecoration: "none" }}>Edit</Link>
          </Button>  
          <Button outline color="danger"
            onClick={() => deleteFromList(`/api/v1/achievements/${a.id}`, a.id, [achievements, setAchievements], [alerts, setAlerts], setMessage, setVisible)}>
              Delete
          </Button>
        </td>
      </tr>
    );
  });
  const modal = getErrorModal(setVisible, visible, message);
  return (
    <div>
      <div className="admin-page-container">
        <h1 className="text-center">Achievements</h1>
        <div>
          <Table aria-label="achievements" className="mt-4">
            <thead>
              <tr>
                <th className="text-center">Name</th>
                <th className="text-center">Description</th>
                <th className="text-center">Image</th>
                <th className="text-center">Threshold</th>
                <th className="text-center">Metric</th>
                <th className="text-center">Actions</th>
              </tr>
            </thead>
            <tbody>{achievementList}</tbody>
          </Table>
          <Button outline color="success" >
            <Link
              to={`/achievements/new`} className="btn sm"
              style={{ textDecoration: "none" }}>Create achievement</Link>
          </Button>
        </div>
      </div>
    </div>
  );
}
```

```
import { Table } from "reactstrap";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import { useState } from "react";
import getErrorModal from "./../util/getErrorModal";
const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();
const achievements = [
    {
      id: 1,
      name: "Experiencia básica",
      description: "Si juegas 10 partidas o más",
      badgeImage: "https://cdn-icons-png.flaticon.com/512/5243/5243423.png",
      threshold: "10",
      metric: "GAMES_PLAYED"
    },
    {
      id: 2,
      name: "Explorador",
      description: "Si juegas 25 partidas o más",
      badgeImage: "https://cdn-icons-png.flaticon.com/512/603/603855.png",
      threshold: "25",
      metric: "GAMES_PLAYED"
    },
    {
      id: 3,
      name: "Experto",
      description: "Si ganas 20 partidas o más",
      badgeImage: "https://cdn-icons-png.flaticon.com/512/4737/4737471.png",
      threshold: "20",
      metric: "VICTORIES"
    }
  ];
export default function AchievementListPlayer() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [achievements, setAchievements] = useFetchState([], `/api/v1/achievements`, jwt);
  const achievementList = achievements.map((a) => {
    return (
      <tr key={a.id}>
        <td className="text-center">{a.name}</td>
        <td className="text-center">{a.description}</td>
        <td className="text-center">
          <img
            src={a.badgeImage ? a.badgeImage : imgnotfound}
            alt={a.name}
            width="50px"
          />
        </td>
        <td className="text-center">{a.threshold}</td>
        <td className="text-center">{a.metric}</td>
      </tr>
    );
  });
  const modal = getErrorModal(setVisible, visible, message);
  return (
    <div>
      <div className="admin-page-container">
        <h1 className="text-center">Achievements</h1>
        <div>
          <Table aria-label="achievements" className="mt-4">
            <thead>
              <tr>
                <th className="text-center">Name</th>
                <th className="text-center">Description</th>
                <th className="text-center">Image</th>
                <th className="text-center">Threshold</th>
                <th className="text-center">Metric</th>
              </tr>
            </thead>
            <tbody>{achievementList}</tbody>
          </Table>
        </div>
      </div>
    </div>
  );
}
```

#### Estado del código refactorizado

```
import { Table, Button } from "reactstrap";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import { Link } from "react-router-dom";
import deleteFromList from "./../util/deleteFromList";
import { useState } from "react";
import getErrorModal from "./../util/getErrorModal";
import jwt_decode from "jwt-decode";

const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();
let role = []
if (jwt) {
  role = getRolesFromJWT(jwt); /*Cuidado que .authorities devuelve un array*/
}

function getRolesFromJWT(jwt) {
  return jwt_decode(jwt).authorities;
}


/*const achievements = [
  {
    id: 1,
    name: "Experiencia básica",
    description: "Si juegas 10 partidas o más",
    badgeImage: "https://cdn-icons-png.flaticon.com/512/5243/5243423.png",
    threshold: "10",
    metric: "GAMES_PLAYED"
  },
  {
    id: 2,
    name: "Explorador",
    description: "Si juegas 25 partidas o más",
    badgeImage: "https://cdn-icons-png.flaticon.com/512/603/603855.png",
    threshold: "25",
    metric: "GAMES_PLAYED"
  },
  {
    id: 3,
    name: "Experto",
    description: "Si ganas 20 partidas o más",
    badgeImage: "https://cdn-icons-png.flaticon.com/512/4737/4737471.png",
    threshold: "20",
    metric: "VICTORIES"
  }
];*/

export default function AchievementList() {
  const [message, setMessage] = useState('');
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);
  const [achievements, setAchievements] = useFetchState([], `/api/v1/achievements`, jwt);

  const achievementList = achievements.map((a) => {
    return (
      <tr key={a.id}>
        <td className="text-center">{a.name}</td>
        <td className="text-center">{a.description}</td>
        <td className="text-center">
          <img
            src={a.badgeImage ? a.badgeImage : imgnotfound}
            alt={a.name}
            width="50px"
          />
        </td>
        <td className="text-center">{a.threshold}</td>
        <td className="text-center">{a.metric}</td>
        <td className="text-center">

          {/*Mostrar los botones de editar y eliminar solo si el usuario es ADMIN*/}
          {role[0] === "ADMIN" && (
            <>
              <Button outline color="warning" >
                <Link
                  to={`/achievements/`+a.id} className="btn sm"
                  style={{ textDecoration: "none" }}>Edit</Link>
              </Button>  
              <Button outline color="danger"
                onClick={() => deleteFromList(`/api/v1/achievements/${a.id}`, a.id, [achievements, setAchievements], [alerts, setAlerts], setMessage, setVisible)}>
                  Delete
              </Button>
            </>
          )}

        </td>
      </tr>
    );
  });

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div>
      <div className="admin-page-container">
        <h1 className="text-center">Achievements</h1>
        {modal}
        <div>
          <Table aria-label="achievements" className="mt-4">
            <thead>
              <tr>
                <th className="text-center">Name</th>
                <th className="text-center">Description</th>
                <th className="text-center">Image</th>
                <th className="text-center">Threshold</th>
                <th className="text-center">Metric</th>

                {/*Mostrar Actions solo si el usuario es ADMIN*/}
                {role[0] === "ADMIN" && (
                  <th className="text-center">Actions</th>
                )}
                
              </tr>
            </thead>
            <tbody>{achievementList}</tbody>
          </Table>

          {/*Mostrar el botón de crear solo si el usuario es ADMIN*/}      
          {role[0] === "ADMIN" && (
            <Button outline color="success" >
              <Link
                to={`/achievements/new`} className="btn sm"
                style={{ textDecoration: "none" }}>Create achievement</Link>
            </Button>
          )}
          
        </div>
      </div>
    </div>
  );
}
```
#### Problema que nos hizo realizar la refactorización
Se necesitaban dos clases para una solo función, donde dependiendo si eras administrador o jugador te mostraba la opción de crear un logro, editarlo o eliminarlo o no te mostraba nada.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Ahora solo se necesita una clase para ello y lo hace más legible. 

### Refactorización 2: 
En esta refactorización el metodo cambia la ficha salmon de una loseta Tile a otra, para ello tenía que buscar si ya estaba en alguna loseta para quitarlo, la busqueda al principio se hacía en el metodo directamente y se ha cambiado a hacerla atraves del service sin cargar todas las losetas.

#### Estado inicial del código
```
@Transactional
    public void moveSalmonToTile(Integer salmonId, Integer tileId){
        Salmon s = salmonRepository.getSalmonById(salmonId);
        Tile t = tileRepository.getTileById(tileId);
        List<Tile> tiles = tileRepository.findAll();
        Optional<Tile> tileWithSalmon = tiles.stream()
                                     .filter(x -> x.getSalmon() != null && x.getSalmon().getId() == salmonId)
                                     .findFirst();
        if(tileWithSalmon.isPresent()){
            List<Salmon> ls = tileWithSalmon.get().getSalmon(); 
            ls.removeIf(x -> x.getId() == salmonId); 
            tileWithSalmon.get().setSalmon(ls);
            save(tileWithSalmon.get());
        }
        List<Salmon> sal = t.getSalmon();
        sal.add(s);
        t.setSalmon(sal);
        save(t);
    }
```
#### Estado del código refactorizado
##### TileRepository
```
@Query("SELECT t FROM Tile t JOIN t.salmons s WHERE s.id = :id")
    Optional<Tile> findTileBySalmonId(@Param("id") int id) throws DataAccessException;
```
##### TileService
```
 @Transactional
    public void moveSalmonToTile(Integer salmonId, Integer tileId){
        Optional<Salmon> so = salmonRepository.getSalmonById(salmonId);
        Optional<Tile> to = tileRepository.getTileById(tileId);
        if (so.isEmpty() || to.isEmpty()) {
            throw new IllegalArgumentException("Salmón o loseta no encontrados");
        }
        if(to.get().getCapacity() > to.get().getSalmons().size()){
            Tile t = to.get();
            Salmon s = so.get();
            Optional<Tile> tileWithSalmon = tileRepository.findTileBySalmonId(salmonId);
            if(tileWithSalmon.isPresent()){
                Tile tWS = tileWithSalmon.get();
                List<Salmon> ls = tWS.getSalmons(); 
                ls.removeIf(x -> x.getId().equals(salmonId)); 
                tWS.setSalmons(ls);
                save(tWS);
            }
            List<Salmon> sal = t.getSalmons();
            sal.add(s); 
            t.setSalmons(sal);
            save(t);
        }else{
            throw new IllegalArgumentException("La loseta no tiene espacio suficiente");
        }
    }
```
#### Problema que nos hizo realizar la refactorización
Al hacer la busqueda cargando todas las Tile en memoria con tileRepository.findAll() para luego filtrar era muy ineficiente y más complejo.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Al crear un método en el repositorio que busque directamente el Tile con el salmonId, evita cargar y filtrar toda la lista de Tile innecesariamente, mejorando el rendimiento.

### Refactorización 3: 
En esta refactorización se simplifican las validaciones al eliminar condiciones redundantes de la función, que comprueba si la loseta destino es adyacente a la que actualmente se encuentra el salmón. También afecta a las refactorizaciones 4 y 5.
#### Estado inicial del código
```  
@Transactional
    public boolean adyacente(Salmon salmon, Tile destinationTile) {
        // Comprobación de si la loseta de destino es adyacente

        boolean isAdjacent = false;
        int targetX = destinationTile.getX();
        int targetY = destinationTile.getY();
        int count = salmon.getBoard().getTileCount();

        if (count < 12) {
            throw new IllegalStateException("El board debe tener al menos 12 losetas para poder mover salmones.");
        }

        if (destinationTile.getCapacity() <= 0) {
            throw new IllegalStateException("No pueden haber más salmones en la loseta.");
        }
        if (salmon.getBoard().isSalmonMoved() && salmon.getBoard().getTurno().equals(Arrays.stream(Colour.values())
                .filter(c -> c.ordinal() == 12 % salmon.getBoard().getMatch().getNumberOfPlayers()).findFirst().get())
                && count < 29) {
            throw new IllegalStateException("Tienes que colocar las 3 losetas antes de mover un salmón.");
        }
        // Los movimientos válidos son aquellos en los que las coordenadas
        // (x, y) de la loseta de destino son adyacentes a las del salmón.
        if (salmon.getY() == 1 && salmon.getX() != 11 && salmon.getX() != 12) {
            isAdjacent = ((salmon.getX() - targetX == -1 && salmon.getY() == targetY) // Movimiento horizontal
                    || (Math.abs(salmon.getY() - targetY) == 1 && salmon.getX() == targetX)) // Movimiento vertical
                    && count >= 12
                    && destinationTile.getCapacity() > 0;
            if (destinationTile.getType() == TileType.ROCK) {
                isAdjacent = isAdjacent
                        && destinationTile.getCapacity() > (salmon.getBoard().getMatch().getPlayers().size()) -
                                (salmon.getBoard().getMatch().getPlayers().size() - 1);

                if (destinationTile.getCapacity() <= (salmon.getBoard().getMatch().getPlayers().size()) -
                        (salmon.getBoard().getMatch().getPlayers().size() - 1)) {
                    throw new IllegalStateException(
                            "En una loseta de tipo ROCK el número de salmones no puede ser mayor o igual que el de jugadores.");
                }
            }
            ;
        } else if ((salmon.getY() == 0 || salmon.getY() == 2) && salmon.getX() == 10) {
            isAdjacent = (targetX == 11 && targetY == 1) && count >= 12 && destinationTile.getCapacity() > 0;
            if (destinationTile.getType() == TileType.ROCK) {
                isAdjacent = isAdjacent
                        && destinationTile.getCapacity() > (salmon.getBoard().getMatch().getPlayers().size()) -
                                (salmon.getBoard().getMatch().getPlayers().size() - 1);

                if (destinationTile.getCapacity() <= (salmon.getBoard().getMatch().getPlayers().size()) -
                        (salmon.getBoard().getMatch().getPlayers().size() - 1)) {
                    throw new IllegalStateException(
                            "En una loseta de tipo ROCK el número de salmones no puede ser mayor o igual que el de jugadores.");
                }
            }
            ;
        } else if (salmon.getY() == 1 && salmon.getX() == 11) {
            isAdjacent = (targetX == 11 && targetY == 0) && count >= 12 && destinationTile.getCapacity() > 0;
            if (destinationTile.getType() == TileType.ROCK) {
                isAdjacent = isAdjacent
                        && destinationTile.getCapacity() > (salmon.getBoard().getMatch().getPlayers().size()) -
                                (salmon.getBoard().getMatch().getPlayers().size() - 1);

                if (destinationTile.getCapacity() <= (salmon.getBoard().getMatch().getPlayers().size()) -
                        (salmon.getBoard().getMatch().getPlayers().size() - 1)) {
                    throw new IllegalStateException(
                            "En una loseta de tipo ROCK el número de salmones no puede ser mayor o igual que el de jugadores.");
                }
            }
            ;
        } else if (salmon.getY() == 0 && salmon.getX() == 11) {
            isAdjacent = (targetX == 12 && targetY == 1) && count >= 12 && destinationTile.getCapacity() > 0;
            if (destinationTile.getType() == TileType.ROCK) {
                isAdjacent = isAdjacent
                        && destinationTile.getCapacity() > (salmon.getBoard().getMatch().getPlayers().size()) -
                                (salmon.getBoard().getMatch().getPlayers().size() - 1);

                if (destinationTile.getCapacity() <= (salmon.getBoard().getMatch().getPlayers().size()) -
                        (salmon.getBoard().getMatch().getPlayers().size() - 1)) {
                    throw new IllegalStateException(
                            "En una loseta de tipo ROCK el número de salmones no puede ser mayor o igual que el de jugadores.");
                }
            }
            ;
        } else if (salmon.getY() == 1 && salmon.getX() == 12) {
            isAdjacent = (targetX == 11 && targetY == 2) && count >= 12 && destinationTile.getCapacity() > 0;
            if (destinationTile.getType() == TileType.ROCK) {
                isAdjacent = isAdjacent
                        && destinationTile.getCapacity() > (salmon.getBoard().getMatch().getPlayers().size()) -
                                (salmon.getBoard().getMatch().getPlayers().size() - 1);
                if (destinationTile.getCapacity() <= (salmon.getBoard().getMatch().getPlayers().size()) -
                        (salmon.getBoard().getMatch().getPlayers().size() - 1)) {
                    throw new IllegalStateException(
                            "En una loseta de tipo ROCK el número de salmones no puede ser mayor o igual que el de jugadores.");
                }
            }

        } else {
            isAdjacent = ((salmon.getX() - targetX == -1 && salmon.getY() == targetY) // Movimiento horizontal
                    || (Math.abs(salmon.getY() - targetY) == 1 && salmon.getX() - targetX == -1)) // Movimiento vertical
                    && count >= 12
                    && destinationTile.getCapacity() > 0;
            if (destinationTile.getType() == TileType.ROCK) {
                isAdjacent = isAdjacent
                        && destinationTile.getCapacity() > (salmon.getBoard().getMatch().getPlayers().size()) -
                                (salmon.getBoard().getMatch().getPlayers().size() - 1);

                if (destinationTile.getCapacity() <= (salmon.getBoard().getMatch().getPlayers().size()) -
                        (salmon.getBoard().getMatch().getPlayers().size() - 1)) {
                    throw new IllegalStateException(
                            "En una loseta de tipo ROCK el número de salmones no puede ser mayor o igual que el de jugadores.");
                }
            }
            
        }
        return isAdjacent;
    }
```
#### Estado del código refactorizado
##### SalmonService
```
 @Transactional
    public boolean adyacente(Salmon salmon, Tile destinationTile) {
        // Comprobación de si la loseta de destino es adyacente

        boolean isAdjacent = false;
        int targetX = destinationTile.getX();
        int targetY = destinationTile.getY();
        // Los movimientos válidos son aquellos en los que las coordenadas
        // (x, y) de la loseta de destino son adyacentes a las del salmón.
        if (salmon.getY() == 1 && salmon.getX() != 11 && salmon.getX() != 12) {
            isAdjacent = ((salmon.getX() - targetX == -1 && salmon.getY() == targetY) // Movimiento horizontal
                    || (Math.abs(salmon.getY() - targetY) == 1 && salmon.getX() == targetX)); // Movimiento vertical
        } else if ((salmon.getY() == 0 || salmon.getY() == 2) && salmon.getX() == 10) {
            isAdjacent = (targetX == 11 && targetY == 1);
        } else if (salmon.getY() == 1 && salmon.getX() == 11) {
            isAdjacent = (targetX == 11 && targetY == 0);
        } else if (salmon.getY() == 0 && salmon.getX() == 11) {
            isAdjacent = (targetX == 12 && targetY == 1);
        } else if (salmon.getY() == 1 && salmon.getX() == 12) {
            isAdjacent = (targetX == 11 && targetY == 2);

        } else {
            isAdjacent = ((salmon.getX() - targetX == -1 && salmon.getY() == targetY) // Movimiento horizontal
                    || (Math.abs(salmon.getY() - targetY) == 1 && salmon.getX() - targetX == -1)); // Movimiento
                                                                                                   // vertical

        }
        return isAdjacent;
    }
```
#### Problema que nos hizo realizar la refactorización
El principal problema que llevó a la refactorización de la función fue su complejidad innecesaria y redundancia en las condiciones, lo que dificultaba la comprensión y legibilidad del código.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Mayor claridad y legibilidad.
### Refactorización 4: 
La refactorización separó responsabilidades en métodos específicos (isCapacitable, losetasSinColocar, conteoMasQueDoce) para validar condiciones, mejorando la claridad y reutilización del código. Esto simplificó la función canSwimToTile, la cual cumpruba si una ficha de salmón puede nadar a una loseta. También afecta a la función "adyacente" mencionada en la refactorización 3 y a la función canJumpToTile mencionada en la refactorización 5
#### Estado inicial del código
```
@Transactional
    public boolean canSwimToTile(Salmon salmon, Tile destinationTile) {
        Integer count = salmon.getBoard().getTileCount();
        // Comprobación de si la loseta de destino es adyacente
        boolean isAdjacent = adyacente(salmon, destinationTile);
        boolean isBlocked = false;
        Tile originTile = tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId());
        if ((destinationTile.getType() == TileType.BEAR && isAdjacent) || destinationTile.getType() == TileType.WATERFALL) {
            isBlocked = isBlockedByTrunk(destinationTile, salmon.getX(), salmon.getY());
        }
        if (Tile.getPuntos(destinationTile.getX(), destinationTile.getY()) > 1) {
            throw new IllegalStateException("No puedes nadar a una loseta del desove.");
        }
        if (originTile != null && !isBlocked && isAdjacent) { // Si el movimiento ya no es válido no compruebo si está
                                                              // bloqueado
            if (originTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL) {
                isBlocked = isBlockedInsedeTrunk(originTile, destinationTile);
            }
        }
        if (isBlocked) {
            isAdjacent = false;
        }
        if ((salmon.getBoard().isSalmonMoved() && salmon.getBoard().getTurno().equals(Arrays.stream(Colour.values())
                .filter(c -> c.ordinal() == 12 % salmon.getBoard().getMatch().getNumberOfPlayers()).findFirst().get() )|| salmon.getBoard().getRoundPass())
                && count < 29) {
            throw new IllegalStateException("Tienes que colocar las 3 losetas antes de mover un salmón.");
        }
        
        return isAdjacent;
    }
```
#### Estado del código refactorizado
##### SalmonService
```
 @Transactional
    public boolean isCapacitable(Tile destinationTile, Salmon salmon) {
        int capacity = destinationTile.getCapacity();
        boolean res = true;
        if (capacity <= 0) {
            throw new IllegalStateException("No pueden haber más salmones en la loseta.");
        }
        if (destinationTile.getType() == TileType.ROCK) {
            res = capacity > (salmon.getBoard().getMatch().getPlayers().size()) -
                    (salmon.getBoard().getMatch().getPlayers().size() - 1);

            if (capacity <= (salmon.getBoard().getMatch().getPlayers().size()) -
                    (salmon.getBoard().getMatch().getPlayers().size() - 1)) {
                throw new IllegalStateException(
                        "En una loseta de tipo ROCK el número de salmones no puede ser mayor o igual que el de jugadores.");
            }
        }
        return res;
    }

    @Transactional
    public void losetasSinColocar(Salmon salmon) {
        Integer count = salmon.getBoard().getTileCount();
        if ((salmon.getBoard().isSalmonMoved() && salmon.getBoard().getTurno().equals(Arrays.stream(Colour.values())
                .filter(c -> c.ordinal() == 12 % salmon.getBoard().getMatch().getNumberOfPlayers()).findFirst().get())
                || salmon.getBoard().getRoundPass())
                && count < 29) {
            throw new IllegalStateException("Tienes que colocar las 3 losetas antes de mover un salmón.");
        }
    }

    @Transactional
    public void conteoMasQueDoce(int count) {
        if (count < 12) {
            throw new IllegalStateException("El board debe tener al menos 12 losetas para poder mover salmones.");
        }
    }

    @Transactional
    public boolean canSwimToTile(Salmon salmon, Tile destinationTile) {
        Integer count = salmon.getBoard().getTileCount();
        // Comprobación de si la loseta de destino es adyacente
        boolean isAdjacent = adyacente(salmon, destinationTile);
        boolean isBlocked = false;
        losetasSinColocar(salmon);// Compruebo si se han colocado las losetas
        conteoMasQueDoce(count);// Compruebo si hay más de 12 losetas
        if (!isCapacitable(destinationTile, salmon)) { // Si la capacidad no es válida no puede nadar
            isAdjacent = false;
        }

        Tile originTile = tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId());
        if ((destinationTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL)
                && isAdjacent) {
            isBlocked = isBlockedByTrunk(destinationTile, salmon.getX(), salmon.getY());
        }
        if (Tile.getPuntos(destinationTile.getX(), destinationTile.getY()) > 1) {
            throw new IllegalStateException("No puedes nadar a una loseta del desove.");
        }
        if (originTile != null && !isBlocked && isAdjacent) { // Si el movimiento ya no es válido no compruebo si está
                                                              // bloqueado
            if (originTile.getType() == TileType.BEAR || originTile.getType() == TileType.WATERFALL) {
                isBlocked = isBlockedInsedeTrunk(originTile, destinationTile);
            }
        }
        if (isBlocked) {
            isAdjacent = false;
        }

        return isAdjacent;
    }
```
#### Problema que nos hizo realizar la refactorización
canSwimToTile tenía una lógica compleja y extensa que combinaba múltiples responsabilidades, como verificar adyacencia, capacidad de la loseta, colocación de losetas y bloqueos, lo que dificultaba la comprensión y reutilización del código.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Dividir el código en métodos más pequeños para que puedan ser reutilizados.

### Refactorización 5: 
La refactorización divide la lógica de la función canJumpToTile en funciones más pequeñas y específicas como isCapacitable, losetasSinColocar, y conteoMasQueDoce, para mejorar la legibilidad y reutilización. Estas últimas funciones también aparecen en la refactorización 4.
#### Estado inicial del código
```
@Transactional
public boolean canJumpToTile(Salmon salmon, Tile destinationTile) {
        // Comprobación de si la loseta de destino es adyacente
        int targetX = destinationTile.getX();
        int targetY = destinationTile.getY();
        int movement = salmon.getPlayer().getMovement();
        int count = salmon.getBoard().getTileCount();
        boolean isBlocked = false;
        Tile originTile = tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId());
        Tile intermediateTile = getIntermediateTile(originTile, destinationTile);

        if (count < 12) {
            throw new IllegalStateException("El board debe tener al menos 12 losetas para poder mover ficha.");
        }
        if (Tile.getPuntos(destinationTile.getX(), destinationTile.getY()) > 1) {
            throw new IllegalStateException("No puedes nadar a una loseta del desove.");
        }
        if (destinationTile.getCapacity() <= 0) {
            throw new IllegalStateException("No pueden haber más salmones en la loseta.");
        }
        if ((salmon.getBoard().isSalmonMoved() && salmon.getBoard().getTurno().equals(Arrays.stream(Colour.values())
                .filter(c -> c.ordinal() == 12 % salmon.getBoard().getMatch().getNumberOfPlayers()).findFirst().get() )|| salmon.getBoard().getRoundPass())
                && count < 29) {
            throw new IllegalStateException("Tienes que colocar las 3 losetas antes de mover un salmón.");
        }
        // Los movimientos válidos son aquellos en los que las coordenadas
        // (x, y) de la loseta de destino son adyacentes a las del salmón.
        boolean canJump = false;
        if ((salmon.getY() == 0 || salmon.getY() == 2) && salmon.getX() == 9) {
            canJump = false;
        } else if (salmon.getY() == 1 && salmon.getX() == 10) {
            canJump = false;
        } else {
            canJump = (((salmon.getX() - targetX == -2 && salmon.getY() == targetY) // Movimiento horizontal
                    || (Math.abs(salmon.getY() - targetY) == 2 && salmon.getX() - targetX == -1)) // Movimiento vertical
                    && movement % 5 != 4)
                    && count >= 12
                    && destinationTile.getCapacity() > 0;
            if (destinationTile.getType() == TileType.ROCK) {
                canJump = canJump
                        && destinationTile.getCapacity() > (salmon.getBoard().getMatch().getPlayers().size()) -
                                (salmon.getBoard().getMatch().getPlayers().size() - 1);

                if (destinationTile.getCapacity() <= (salmon.getBoard().getMatch().getPlayers().size()) -
                        (salmon.getBoard().getMatch().getPlayers().size() - 1)) {
                    throw new IllegalStateException(
                            "En una loseta de tipo ROCK el número de salmones no puede ser mayor o igual que el de jugadores.");
                }
            }
            ;
        }
        if (!canJump) {// Comprobamos si no estas usando el salto para desplazar dos, si no si lo estas
                       // usando para saltar un tronco, en ese caso tienes que poder saltar
            if (destinationTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL) {
                isBlocked = isBlockedByTrunk(destinationTile, salmon.getX(), salmon.getY());
                if (originTile != null && !isBlocked) { // Si el movimiento ya no es válido no compruebo si
                                                        // está bloqueado
                    if (originTile.getType() == TileType.BEAR) {
                        isBlocked = isBlockedInsedeTrunk(originTile, destinationTile);
                    }
                }
            }
            if (isBlocked) {
                canJump = true;
            }
        } else if (intermediateTile != null) { // Compruebo que en el salto no haya una loseta intermedia en la que sus
                                               // troncos me impidan el paso
            if (destinationTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL) {
                isBlocked = isBlockedByTrunk(destinationTile, intermediateTile.getX(), intermediateTile.getY());
                if (originTile != null && !isBlocked) { // Si el movimiento ya no es válido no compruebo si
                                                        // está bloqueado
                    if (originTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL) {
                        isBlocked = isBlockedInsedeTrunk(originTile, intermediateTile);
                    }
                }
            }
            if ((intermediateTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL)
                    && !isBlocked) {// si ya esta bloqueado no lo compruebo
                if (isBlockedByTrunk(intermediateTile, salmon.getX(), salmon.getY())) {
                    canJump = false;
                } else if (isBlockedInsedeTrunk(intermediateTile, destinationTile)) {
                    canJump = false;
                }
            }
            if (isBlocked) {
                canJump = false;
            }
        }

        return canJump;
    }
```
#### Estado del código refactorizado
##### SalmonService
```
 @Transactional
    public boolean canJumpToTile(Salmon salmon, Tile destinationTile) {
        // Comprobación de si la loseta de destino es adyacente
        int targetX = destinationTile.getX();
        int targetY = destinationTile.getY();
        int movement = salmon.getPlayer().getMovement();
        int count = salmon.getBoard().getTileCount();
        boolean isBlockedDestination = false;
        boolean isBlockedOrigin = false;
        Tile originTile = tileService.getTileByCoordenadas(salmon.getX(), salmon.getY(), salmon.getBoard().getId());
        Tile intermediateTile = getIntermediateTile(originTile, destinationTile);
        boolean distanciaValida = false;
        boolean canJump = true;
        boolean isAdjacent;
        if (!isCapacitable(destinationTile, salmon)) { // Si la capacidad no es válida no puede saltar
            canJump = false;
        }
        losetasSinColocar(salmon);// Compruebo si se han colocado las losetas
        conteoMasQueDoce(count);// Compruebo si hay más de 12 losetas
        isAdjacent = adyacente(salmon, destinationTile);
        if (movement % 5 == 4) {
            throw new IllegalStateException("No tienes suficientes movimientos para saltar");
        }

        if (Tile.getPuntos(destinationTile.getX(), destinationTile.getY()) > 1) {
            throw new IllegalStateException("No puedes nadar a una loseta del desove.");
        }

        // Los movimientos válidos son aquellos en los que las coordenadas
        // (x, y) de la loseta de destino son adyacentes a las del salmón.
        // Movimiento vertical

        if ((salmon.getY() == 0 || salmon.getY() == 2) && salmon.getX() == 9) {
            canJump = false;
        } else if (salmon.getY() == 1 && salmon.getX() == 10) {
            canJump = false;
        } else {
            distanciaValida = ((salmon.getX() - targetX == -2 && salmon.getY() == targetY) // Movimiento horizontal
                    || (Math.abs(salmon.getY() - targetY) == 2 && salmon.getX() - targetX == -1)); // Movimiento
                                                                                                   // vertical
        }

        if (isAdjacent) {// Comprobamos si no estas usando el salto para desplazar dos, si no si lo estas
                         // usando para saltar un tronco, en ese caso tienes que poder saltar
            if (destinationTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL) {
                isBlockedDestination = isBlockedByTrunk(destinationTile, salmon.getX(), salmon.getY());
            }
            if (originTile != null) { // Si el movimiento ya no es válido no compruebo si
                                                    // está bloqueado
                if (originTile.getType() == TileType.BEAR || originTile.getType() == TileType.WATERFALL) {
                    isBlockedOrigin = isBlockedInsedeTrunk(originTile, destinationTile);
                }
            }

            if (!(isBlockedDestination && isBlockedOrigin) && (isBlockedDestination || isBlockedOrigin)) {
                distanciaValida = true;
            }
        } else if (intermediateTile != null && canJump) { // Compruebo que en el salto no haya una loseta intermedia en
                                                          // la que sus
            // troncos me impidan el paso
            if (destinationTile.getType() == TileType.BEAR || destinationTile.getType() == TileType.WATERFALL) {
                if (isBlockedByTrunk(destinationTile, intermediateTile.getX(), intermediateTile.getY())) {
                    canJump = false;
                }
                if (originTile != null && !canJump) { // Si el movimiento ya no es válido no compruebo si
                                                        // está bloqueado
                    if (originTile.getType() == TileType.BEAR || originTile.getType() == TileType.WATERFALL) {
                        if (isBlockedInsedeTrunk(originTile, intermediateTile)) {
                            canJump = false;
                        }
                    }
                }
            }
            if ((intermediateTile.getType() == TileType.BEAR || intermediateTile.getType() == TileType.WATERFALL)
                    && canJump) {// si ya esta bloqueado no lo compruebo
                if (isBlockedByTrunk(intermediateTile, salmon.getX(), salmon.getY())) {// Compruebo si la loseta
                                                                                       // intermedia me bloquea
                    canJump = false;
                } else if (isBlockedInsedeTrunk(intermediateTile, destinationTile)) {
                    canJump = false;
                }
            }
        }
        return canJump && distanciaValida; // canJump comprueba que no haya ninguna regla de negocio que te impida el
                                           // salto y distanciaValida que la distancia sea la correcta
    }
```
#### Problema que nos hizo realizar la refactorización
El problema que motivó la refactorización fue la complejidad y falta de claridad en el código original
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Mayor facilidad a la hora de identificar errores y legibilidad
