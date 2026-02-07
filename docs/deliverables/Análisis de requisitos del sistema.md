# Documento de análisis de requisitos del sistema


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
-	Saltar: Moverse de una loseta a otra ignorando todos los obstáculos se considera saltar. Cuesta 1 punto de movimiento, más 1 punto por cada loseta saltada. Sólo se puede saltar en línea recta, no se puede cambiar de dirección durante el salto.

Cuando se vayan a colocar las últimas dos losetas de río, éstas deben colocarse a los lados, para poner las de desove en el centro, al final del río. Cuando una ficha llegue a la primera loseta de desove debe detenerse y ya no podrán gastarse puntos de movimiento con esa ficha. Al final de cada ronda se sigue eliminando la fila inferior del río, pero no se colocan nuevas losetas. En lugar de eso se mueven todas las fichas en el lago del desove a la casilla siguiente.

En cuanto a los criterios de puntuación:
Cuando todos los salmones estén en las losetas de desove o en la caja, se acaba la partida automáticamente y cada jugador recibe sus puntos: 
-	Por cada salmón (que no ficha de salmón) que tenga, 1 punto. 
-	Por cada ficha de salmón que tenga, 1 punto por cada huevo que haya en la casilla de desove donde se encuentra.
  
El jugador con mayor puntuación será el ganador. En caso de empate ganará el que tenga más salmones. Si persiste el empate ganará quien tenga más fichas o en su defecto, quien tenga sus fichas más adelantadas.

(https://drive.google.com/file/d/19Ku1wfw4lbaCAq-IUtilbu90doZeNdWA/view?usp=sharing)

## Tipos de Usuarios / Roles

Jugador: Usuario del juego con capacidad de crear partidas, crear y eliminar su cuenta, iniciar y cerrar sesión, editar su perfil, añadir, invitar y eliminar amigos. Además de jugar el propio juego.

Además, puede configurar un perfil público con información editable y estadísticas personales, así como elegir entre modos de juego avanzados (Hard Core o Casual Gamer).

Administrador:  Usuario encargado de supervisar y controlar el funcionamiento de la aplicación, asegurando su eficiencia y seguridad. Este usuario tiene privilegios respecto a los jugadores, pudiendo hacer lo mismo que ellos sin tener que ser el propio jugador para modificar sus datos y todo lo referido al funcionamento del juego y el sitio web. 


## Historias de Usuario

A continuación se definen  todas las historias de usuario a implementar:


### H1 Registrar una cuenta
Como jugador o administrador quiero que el sistema me permita registrar una nueva cuenta para poder guardar mi progreso y ser identificado con mis datos asociados a la cuenta por los demás jugadores.

*- Mockup de la interfaz de usuario H1*

![image](https://github.com/user-attachments/assets/22daf726-93fb-44c1-8aa2-27fefee75a3a)


### H2 Eliminar una cuenta
Como administrador quiero que el sistema me permita eliminar cuentas que estén inactivas para borrar sus datos asociados de la base de datos.  


*- Mockup de la interfaz de usuario H2*

![image](https://github.com/user-attachments/assets/21953364-2c90-4091-92c2-631f6bd5585f)



### H3 Editar una cuenta
Como jugador o administrador quiero que el sistema me permita editar la cuenta para poder cambiar mi nombre o credenciales.  


*- Mockup de la  interfaz de usuario H3*

![image](https://github.com/user-attachments/assets/f72e4614-abe3-45a8-bf6d-b74be73bd2d5)



### H4 Inicio de sesión
Como jugador o administrador quiero que el sistema me permita iniciar sesión con mis credenciales asociadas a una cuenta para poder jugar. 

*- Mockup de la interfaz de usuario H4*

![image](https://github.com/user-attachments/assets/27a1c3fa-5eea-411c-882c-97329921125a)



### H5 Cerrar sesión
Como jugador o administrador quiero que el sistema me permita cerrar mi sesión para poder iniciar sesión con otras cuentas.  

*- Mockup de la interfaz de usuario H5*

![image](https://github.com/user-attachments/assets/c348bd05-aad6-4f58-bb94-1933272b9b6c)


### H6 Unirse a una partida 
Como jugador o administrador quiero que el sistema me permita unirme a una partida en la cual esté invitado, sepa el código de unión o dicha partida sea abierta. 


### H7 Editar partida 
Como jugador o administrador quiero que el sistema me permita editar una partida que he creado para cambiarle el nombre, ampliar la capacidad de la misma o hacerla privada.


### H8 Borrar partida
Como jugador o administrador quiero que el sistema me permita borrar una partida que he creado para quitar aquellas que tengan el estado FINISHED, o borrarlas en el caso que me arrepienta de crearla.


### H9 Crear partida  
Como jugador o administrador quiero que el sistema me permita crear una partida para jugar.  

*- Mockup de la historia de usuario H9 (1ª forma)*

![image](https://github.com/user-attachments/assets/aa903243-9ddd-41bc-814f-6d10bc87e709)


### H10 Ver la lista de partidas disponibles
Como jugador o administrador quiero que el sistema me permita ver las partidas creadas para unirme a ellas.


### H11 Ver número de jugadores en la partida 
Como jugador o administrador quiero ver el número de jugadores actuales en la PlayerList esperando a comenzar la partida para saber si puedo unirme o esta llena la sala, así como su capacidad.

*- Mockup de la historia de usuario H6, H7, H8  H9 (2ª forma), H10, H11*

![image](https://github.com/user-attachments/assets/dc0099ee-a12f-4ef7-bb2c-05567d2dee2b)


### H12 Ver la lista de jugadores que participarán en la partida 
Como jugador o administrador quiero ver el nombre de los jugadores, una vez metidos en la PlayerList, con los que jugaré la partida para identificar a las personas con las que voy a jugar (para posteriormente añadirlas como amigos por ejemplo).


### H13 Invitar amigos a mi partida
Como jugador o administrador quiero que el sistema me deje invitar a los amigos que desee (un máximo de 5) a mi partida para poder jugar con ellos.  


### H14 Visualización del botón play
Como jugador o administrador de la partida quiero que una vez unidas todas las personas a la partida me aparezca un botón de play para iniciar la partida.

*- Mockup de las historias de usuario H12, H13, H14*

![image](https://github.com/user-attachments/assets/ec80cf9f-ae6c-41aa-b51e-a45804952523)


### H15 Modo espectador
Como jugador o administrador quiero poder unirme a partidas, en las cuales participen jugadores de mi lista de amigos, para ver el transcurso de la partida de forma espectante.

*-Mockup de la historia de usuario H15*

![image](https://github.com/user-attachments/assets/6104331b-10a3-4239-9d81-e18fd017e305)


### H16 Lista de amigos 
Como jugador o administrador quiero que el sistema me permita tener una lista con los amigos para poder visualizar todas mis amistades.


### H17 Lista de invitaciones a partida
Como jugador o administrador quiero que el sistema me permita tener una lista con todas las invitaciones a partidas que reciba para poder unirme a la que quiera.


### H18 Añadir amigos  
Como jugador o administrador quiero que el sistema me permita añadir personas a mi lista de amigos para que aparezcan en mi lista de amigos. 


### H19 Eliminar amigos 
Como jugador quiero que el sistema me permita eliminar a uno o varios amigos de mi lista de amigos para poder modificar mi lista de amigos, o rechazar una solicitud de amistad. 

*-Mockup de las hitorias de usuario H16, H17, H18, H19*

![image](https://github.com/user-attachments/assets/0a934e38-4685-489c-a1c8-978150ac068f)


### H20 Consultar datos de los desarrolladores del proyecto 
Como jugador quiero que el sistema me permita ver los desarrolladores del proyecto para saber quien lo ha desarrollado.

*-Mockup de la historia de usuario H20*

![image](https://github.com/user-attachments/assets/d51f0bdc-1998-4ca7-b03c-aa35af5bdd05)


### H21 Gestión de logros 
Como jugador o administrador quiero poder ver los logros disponibles y como se obtienen para poder conseguirlos.

*-Mockup de la historia de usuario H21*
Para administradores:

![image](https://github.com/user-attachments/assets/20145ec3-fc46-4141-b71b-1a4a71dd6944)


Para jugadores:

![image](https://github.com/user-attachments/assets/5fbeaea3-8e99-47fc-8a33-6aa77563a5f3)


### H22 Crear logros
Como administrador quiero que el sistema me permita crear logros para crear nuevas metas a los jugadores.

*-Mockup de la historia de usuario H22*

![image](https://github.com/user-attachments/assets/e632f74a-5317-44c4-9377-2f56ec2d26fa)


### H23 Mofificar logros
Como administrador quiero que el sistema me permita modificar logros con la finalidad de cambiar el nombre, descripción, método de obtención o imágen.

*-Mockup de la historia de usuario H23*

![image](https://github.com/user-attachments/assets/162a7acd-ab53-41d2-aa7b-061e560be4c7)


### H24 Mover ficha 
Como jugador o administrador quiero que el sistema me permita mover una ficha durante una partida para seguir el transcurso del juego. Podemos mover ficha de dos formas distintas: 

- Moverse de una loseta a otra sin obstáculos entre medio se considera nadar. Nadar cuesta 1 punto por loseta a la que se desplace la ficha. 
- Moverse de una loseta a otra ignorando todos los obstáculos se considera saltar. Cuesta 1 punto de movimiento, más 1 punto por cada loseta saltada. Sólo se puede saltar en línea recta (no se puede cambiar de dirección durante el salto).
  

### H25 Visualización de player en el tablero
Como jugador o administrador quiero ver un icono de cada participante de la partida para poder identificarlos con sus respectivas fichas.


### H26 Visualización de las fichas en el tablero 
Como jugador o administrador quiero ver mis fichas en el tablero para poder planear una estrategia dependiendo del desarrollo de la partida.


### H27 Establecimiento de color de jugador 
Como jugador o administrador quiero ver el color que se me otorga en cada partida para distinguir mis fichas de los demás.


### H28 Establecimiento de turno de jugador 
Como jugador o administrador quiero ver cuando me toca mover mis salmones o colocar losetas para que la partida lleve un orden lógico de turnos.


### H29 Colocar loseta
Como jugador o administrador quiero colocar una loseta cada vez que sea mi turno a la hora de montar el tablero para empezar a jugar, además de colocarlas en el transcurso del juego (de 3 en 3) una vez se esté jugando la partida.


### H30 Rotar loseta
Como jugador o administrador quiero poder rotar la loseta del TileStack que me corresponda para montar el rio con la orientación que elija dependiendo de mi estrategia.


### H31 Ver movimientos
Como jugador o administrador quiero ver los movimientos de salmon que me quedan para decidir mis estrategias. 

*-Mockups de las historias de usuario H24, H25, H26, H27, H28, H29, H30, H31*

![image](https://github.com/user-attachments/assets/be180b7a-768d-4433-b0a1-1e782843b4be)


### H32 Abrir chat
Como jugador o administrador quiero poder abrir el chat para comunicarme con el resto de jugadores.

*-Mockup de la historia de usuario H32*

![image](https://github.com/user-attachments/assets/1388b3ea-a6a7-4915-9ee0-4dc426108d4c)


### H33 Mirar reglas del juego
Como jugador o administrador quiero poder mirar las reglas del juego dentro de una partida para recordarlas o informarme sobre como jugar.

*-Mockup de la historia de usuario H33*

![image](https://github.com/user-attachments/assets/6c362a89-a83a-4bc6-947d-12baeea58e72)


### H34 Gestión de perfil avanzado  
Como jugador quiero tener un perfil público donde pueda mostrar información personal como biografía, ubicación, fecha de nacimiento, géneros, plataformas y sagas favoritas, además de estadísticas personales y una imagen de perfil personalizable, para que otros jugadores me conozcan mejor.

### H35 Tipos de perfil: Hard Core Gamer vs Casual Gamer  
Como jugador quiero poder escoger entre un perfil “Hard Core Gamer” (sin restricciones) y “Casual Gamer” (con límite de 2 partidas diarias y tiempo máximo de turno), para que el sistema se adapte a mis hábitos de juego.


## Diagrama conceptual del sistema

![image](https://github.com/user-attachments/assets/c43f9ed4-c95a-4ee1-a240-b12d7cb061c4)


## Reglas de Negocio


### R1- Inicio de partida

Cuando todos los jugadores estén preparados para jugar, se podrá iniciar la partida. 

### R2- Fin de partida 

Cuando todos los salmones estén en la zona de desove la partida acabará automáticamente. 

### R3- Puntuación 

El sistema calculará los puntos de manera que por cada salmón que tenga en la zona de desove obtendrá un punto, y por cada ficha de salmón que tenga, 1 punto por cada huevo que haya en la casilla de desove en la que se encuentra. 

### R4- Número de jugadores 

De 2-5 jugadores. Además, no se podrá participar en una partida donde ya estén jugando 5 jugadores.

### R5- Reparto de las fichas iniciales 

Una vez montado el tablero de juego se le otorga la ficha negra al jugador que le correspondería poner la siguiente loseta en el tablero, y cuando entre todos los jugadores se ponga las 12 primeras fichas del tablero, esta ficha indicará el orden de juego de los jugadores. Aparte de esta, se asocia otra ficha de turno de color blanco que se tendrá en cuenta una vez colocadas las 12 primeras fichas y que indicará los turnos de colocación de las 3 nuevas losetas una vez acabada la ronda. 

### R6- Colocar loseta en lugar incorrecto 

Las losetas siempre se deberán colocar completando primero la columna correspondiente (teniendo en cuenta que jugamos en horizontal). 

### R7- No gastar más puntos de movimientos de los disponibles 

En cada turno cada jugador tendrá 5 puntos de movimiento para repartir entre sus salmones, una vez agotados comenzará el turno del siguiente jugador. 

### R8- Losetas 

Dependiendo los tipos de losetas adyacentes, el jugador podrá mantener o perder salmones. Identificamos los siguientes tipos de losetas: 

  - Agua: Es una loseta sin normas especiales. 

  - Mar: Son las losetas de inicio de la partida, donde ponemos las fichas de salmón inicialmente. 

  - Desove: Son las losetas finales del juego, estas losetas indicarán el ganador de la ronda.  

  - Oso: Si una ficha de salmón salta hacia aquí, pierdes un salmón. Estas losetas contienen también salto de agua.
    
  - Águila: Si una ficha de salmón nada en esta loseta, pierdes un salmón y se voltea la loseta, pasando a ser de tipo agua. 

  - Garza: Al final del turno, pierdes un salmón de una de las fichas que tengas en esta loseta. Si tienes 2 fichas de salmón en losetas de garza solo perderás un salmón de una de esas 2 fichas.

  - Roca: En esta loseta cabe una ficha de salmón menos que en el resto de losetas. 

  - Salto de agua: Los salmones tienen que saltar estos obstáculos para superarlos.
    
### R9- Fichas 

Vamos a encontrar distintos tipos de fichas con distintos de uso dentro del juego: 

  - Fichas de loseta: Aquellas "fichas" que forman el tablero donde jugaremos.

  - Fichas de Salmón: Por un lado muestran 2 salmones y por el otro un único salmón. Cada jugador contará con 4 fichas. 

  - Fichas de Color: Cada jugador tendrá una ficha de color diferente (siendo en nuestro caso el rectángulo donde se encuentra cada jugador) correspondiente con el color de las fichas de salmón para que no se les olvide su color. 

  - Fichas de Movimiento: Respecto a la ficha de turno negra, cada vez que un jugador gaste los puntos de movimiento o coloce una loseta de las 12 primeras se entrega esta ficha al siguiente jugador, y respecto a la ficha de turno blanco cada vez que un jugador coloque las 3 nuevas losetas se entregará al siguiente jugador para ser entonces aquel que ponga las 3 nuevas losetas al final de la siguiente ronda.

### R10- Movimiento de fichas de salmón en turnos ajenos 

Un jugador que no esté en su turno no puede mover ninguna de sus fichas de salmón, tendrá que esperar a tener la ficha de movimiento negra en posesión para moverla.  

### R11- Movimiento de fichas de loseta en turnos ajenos

Un jugador que no esté en su turno no puede mover ninguna de las fichas de loseta del montón, tendrá que esperar a tener la ficha de movimiento blanca o negra en posesión para moverla y que sea el final de la ronda. 

### R12- Movimiento de fichas de salmón ajenas 

Un jugador no podrá mover las fichas de salmón de otros jugadores, solo podrá mover las fichas de salmón correspondientes al color de su ficha de color. 


### R13 - Tipos de perfil y límites  
El sistema distinguirá entre perfiles “Hard Core Gamer” y “Casual Gamer”. Este último tendrá un límite de 2 partidas diarias y un tiempo máximo por partida. Si excede el tiempo, será expulsado de la partida con una notificación indicando el motivo.


## Decisiones de diseño de la interfaz
_En esta sección describiremos las decisiones de diseño que se han tomado a lo largo del desarrollo de la aplicación que vayan más allá de la mera aplicación de patrones de diseño o arquitectónicos._

### Decisión 1: Implementación de la visibilidad de la contraseña mediante el icono de ojo
#### Descripción del problema:

Uno de los requerimientos de usabilidad identificados durante el desarrollo de la aplicación era facilitar al usuario la posibilidad de ver su contraseña mientras la ingresa al crear o editar una partida. Esto permitiría reducir los errores al introducir la contraseña y mejorar la experiencia del usuario.

#### Alternativas de solución evaluadas:
#### Alternativa 1.a: No mostrar ninguna opción de visibilidad de contraseña, dejando la entrada oculta por defecto.

Ventajas: Se mantiene la entrada de la contraseña completamente privada y evita posibles exposiciones de la misma en situaciones públicas.

Inconvenientes: Podría generar frustración en los usuarios debido a errores al escribir la contraseña sin poder visualizarla.

#### Alternativa 1.b: Implementar un icono de “ojo” para permitir al usuario alternar entre ver y ocultar la contraseña.

Ventajas: Mejora la experiencia del usuario al permitirle verificar visualmente la contraseña si lo desea. Esto ayuda a reducir errores de tecleo y posibles fricciones en el proceso de inicio de sesión.

Inconvenientes: Existe un pequeño riesgo de que otros puedan ver la contraseña si el usuario elige mostrarla en un entorno inseguro.

#### Alternativa 1.c: Activar la visibilidad de la contraseña de manera automática durante un periodo breve después de que el usuario la escribe, desactivándose automáticamente luego de unos segundos.

Ventajas: Proporciona un equilibrio entre facilidad de uso y seguridad, al mostrar la contraseña solo brevemente.

Inconvenientes: Puede confundir a los usuarios si no comprenden cuándo o por qué la contraseña se oculta automáticamente. Además, requiere implementar una lógica de temporización adicional, lo que añade complejidad.

#### Justificación de la solución adoptada

Finalmente, se optó por la alternativa 1.b (icono de “ojo” que permite alternar la visibilidad de la contraseña) debido l control total por parte del usuario sobre cuándo mostrar u ocultar su contraseña. Esta solución fue la preferida dado que reduce la frustración del usuario sin introducir complejidad innecesaria y proporciona una experiencia intuitiva y fácilmente adaptable. Además, este enfoque minimiza el riesgo de exposición de la contraseña al no mostrarla automáticamente, sino solo bajo solicitud explícita del usuario, maximizando así tanto la seguridad como la facilidad de uso.

### Decisión 2: Restricción de edición de partidas solo para el creador
#### Descripción del problema:

Durante el desarrollo de creación de partidas en la aplicación, se planteó la necesidad de controlar quién puede editar los detalles de una partida una vez creada. En el diseño original, cualquier jugador participante en la partida podía visualizar la opción de edición. Sin embargo, esto generaba aumentaba el riesgo de conflictos si varios usuarios intentaban editar la partida simultáneamente. Era fundamental que el creador de la partida fuera el único con permiso para modificarla, asegurando así que los detalles de la partida permanecieran controlados por un único usuario.

#### Alternativas de solución evaluadas:
#### Alternativa 2.a: Permitir que todos los participantes puedan editar la partida.

Ventajas: Da flexibilidad a los usuarios, especialmente en partidas de equipo, permitiendo a cualquiera hacer ajustes rápidamente.

Inconvenientes: Puede dar lugar a inconsistencias si varios jugadores realizan ediciones sin coordinación. También existe el riesgo de que un usuario no autorizado modifique detalles críticos de la partida.

#### Alternativa 2.b: Solo el creador de la partida tiene acceso a la opción de edición, y los demás jugadores no pueden ver esta funcionalidad en su interfaz.

Ventajas: Garantiza que los cambios sean realizados solo por la persona responsable de la partida, manteniendo la consistencia de los datos. Además, oculta la opción de edición para los demás usuarios, evitando confusiones y reduciendo la interfaz a las opciones necesarias para cada rol.

Inconvenientes: Limita la capacidad de otros jugadores de realizar ajustes si el creador no está disponible, lo que podría afectar en partidas a largo plazo.

#### Alternativa 2.c: Permitir la edición solo al creador, pero agregar una función de "solicitud de edición" para que los demás participantes puedan proponer cambios, que el creador podrá aprobar o rechazar.

Ventajas: Proporciona una opción intermedia en la que solo el creador edita, pero los demás jugadores pueden sugerir modificaciones, lo que mejora la colaboración en la partida.

Inconvenientes: Agrega complejidad al diseño de la aplicación y podría resultar confuso para los usuarios. Además, requeriría notificaciones y manejo adicional de solicitudes, aumentando la carga de trabajo para el creador.

#### Justificación de la solución adoptada: 
Se seleccionó la alternativa 2.b en la cual solo el creador de la partida puede editarla y los demás usuarios no visualizan la opción de edición. Esta solución fue elegida por su simplicidad y su enfoque en la claridad y la consistencia de los datos, al evitar la posibilidad de cambios accidentales por parte de otros jugadores. Además, elimina confusiones en la interfaz al no mostrar funciones innecesarias para los usuarios que no tienen permisos de edición. Esta decisión asegura que la experiencia del usuario sea intuitiva y que la gestión de la partida permanezca bajo el control de quien la creó, manteniendo la organización del sistema y la estabilidad de los datos de las partidas.

### Decisión 3: Configuración de privacidad de partidas mediante contraseña
#### Descripción del problema:

Para gestionar el acceso a las partidas en la aplicación, surgió la necesidad de establecer un mecanismo que permitiera distinguir entre partidas públicas y privadas. Esto daría a los usuarios control sobre quién puede unirse a sus partidas. La opción más sencilla y accesible para los usuarios era la de configurar una partida como privada al añadir una contraseña durante el proceso de creación. Sin embargo, esto requería definir un flujo claro: en caso de que el usuario dejara el campo de contraseña vacío, la partida se crearía como pública y estaría abierta a cualquier jugador.

#### Alternativas de solución evaluadas:
#### Alternativa 3.a: Hacer que todas las partidas sean privadas y siempre requieran una contraseña.

Ventajas: Mayor control sobre quién puede unirse a las partidas, lo que aumenta la privacidad para todos los usuarios y elimina la posibilidad de que alguien acceda sin permiso.

Inconvenientes: Obliga a los usuarios a establecer una contraseña cada vez, lo cual puede ser inconveniente y añadir pasos innecesarios en el caso de partidas que quieren ser públicas.

#### Alternativa 3.b: Implementar una opción de "Privacidad" en el formulario de creación de partida, donde el usuario elige explícitamente entre "pública" o "privada", y solo en el caso de ser privada se solicita una contraseña.

Ventajas: Da claridad al usuario al permitirle decidir directamente si la partida es pública o privada, y solo requiere una contraseña en el segundo caso.

Inconvenientes: Añade un paso adicional en el formulario y se puede alargar el proceso de creación para los usuarios que solo quieren jugar rápidamente.

#### Alternativa 3.c: Crear partidas públicas por defecto si no se introduce una contraseña en el campo correspondiente. Si el usuario introduce una contraseña, la partida se configura automáticamente como privada.

Ventajas: Simplifica el flujo de creación de partidas al no requerir pasos adicionales. Al mismo tiempo, permite al usuario controlar la privacidad de la partida con solo añadir o no una contraseña, haciendo que el proceso sea más rápido e intuitivo.

Inconvenientes: Podría generar confusión en usuarios que no estén seguros de si una partida es pública o privada hasta después de crearla, aunque este riesgo se minimiza con mensajes claros en la interfaz.

#### Justificación de la solución adoptada: 
Se seleccionó la alternativa 3.c en la que la partida se configura como pública si el usuario no introduce una contraseña y como privada si la incluye. Esta solución fue preferida por su simplicidad y efectividad, permitiendo a los usuarios elegir el nivel de privacidad de una partida de manera rápida y natural. La decisión también evita añadir pasos adicionales en el proceso de creación, haciendo que la aplicación sea más accesible y rápida de usar. Además, esta configuración intuitiva disminuye la probabilidad de errores, pues el usuario decide la privacidad simplemente dejando el campo vacío o llenándolo con una contraseña.


### Decisión 4: Pantallas de Acceso Diferenciadas para Partidas Públicas y Privadas
#### Descripción del problema:

Para mejorar la experiencia de usuario al unirse a partidas, era necesario implementar un flujo diferenciado de acceso según el tipo de partida. Si una partida es pública, cualquier usuario debería poder unirse fácilmente desde una pantalla con un botón de "Join". En el caso de una partida privada, también debería mostrarse el botón de "Join", pero con un campo adicional para ingresar la contraseña.

#### Alternativas de solución evaluadas:

#### Alternativa 4.a: Crear pantallas diferenciadas para partidas públicas y privadas; la primera solo con un botón de "Join" y la segunda con el botón y un campo para ingresar la contraseña.

Ventajas: Mejora la experiencia de usuario al hacer el flujo de acceso claro y adaptado a cada tipo de partida, reduciendo errores y confusión.

Inconvenientes: Requiere el desarrollo de dos pantallas, lo que implica un esfuerzo adicional de diseño y programación.

#### Alternativa 4.b: Crear una pantalla genérica de acceso a partidas, donde el usuario ingrese una contraseña opcionalmente (sin diferenciar entre pública o privada).

Ventajas: Simplifica el desarrollo al usar una sola pantalla de acceso para todas las partidas.

Inconvenientes: Podría generar confusión en el usuario al no dejar claro cuándo la contraseña es necesaria y cuándo no. 

#### Justificación de la solución adoptada: 
Se seleccionó la alternativa 4.a, creando pantallas diferenciadas para partidas públicas y privadas. Esta opción ofrece una experiencia de usuario más intuitiva al presentar un flujo específico para cada tipo de partida. Los usuarios de partidas públicas pueden unirse con un solo clic, mientras que los de partidas privadas deben ingresar la contraseña, lo que refuerza la seguridad y claridad del acceso.


### Decisión 5: Control del Comienzo de la Partida Basado en el Número de Jugadores
#### Descripción del problema:

Para garantizar que las partidas comiencen únicamente cuando haya un número suficiente de jugadores preparados, se estableció un mecanismo que verifica este requisito antes de permitir que se inicie el juego. Además, se decidió dar la posibilidad a cualquier jugador de iniciar la partida una vez que se cumpla esta condición, reduciendo la dependencia de un único jugador (el creador).

#### Alternativas de solución evaluadas:
#### Alternativa 5.a: Restringir el botón de "Play" exclusivamente al creador de la partida, como se hacía previamente.

Ventajas: Mejora la organización, ya que el creador tiene el control exclusivo para coordinar el inicio.

Inconvenientes: Depende únicamente de la disponibilidad del creador, lo que podría retrasar el inicio si este no está preparado.

#### Alternativa 5.b: Permitir que cualquier jugador pueda presionar el botón de "Play" una vez que se cumpla el número mínimo de jugadores.

Ventajas: Ofrece mayor flexibilidad, ya que no depende exclusivamente del creador para iniciar la partida. Promueve la colaboración y participación de todos los jugadores.

Inconvenientes: Podría haber algún desajuste si varios jugadores intentan iniciar la partida al mismo tiempo, aunque esto se puede mitigar con un control adecuado.

#### Justificación de la solución adoptada:
Se optó por la alternativa 5.b, en la que el botón de "Play" se habilita únicamente cuando el número de jugadores es el requerido y puede ser presionado por cualquier participante. Esta solución garantiza que la partida solo comience cuando las condiciones sean las adecuadas. Además, elimina la dependencia exclusiva del creador y reduce posibles retrasos, mejorando la experiencia de los jugadores.


### Decisión 6: Implementación del chat mediante una ventana emergente
#### Descripción del problema:

Se requería integrar un sistema de chat en la partida para permitir la comunicación entre jugadores, sin que este ocupase espacio en la interfaz principal o interfiriera en la jugabilidad. La solución debía ser accesible y discreta, de forma que los jugadores pudieran interactuar con el chat solo cuando lo necesitaran.

#### Alternativas de solución evaluadas
#### Alternativa 6.a: Chat integrado en la interfaz principal del juego

Ventajas: Siempre visible, lo que facilita el acceso inmediato a los mensajes. Puede fomentar una comunicación más activa y constante entre los jugadores.

Inconvenientes: Ocupa espacio en la pantalla, lo que puede interferir con la jugabilidad, especialmente en dispositivos con pantallas pequeñas. Puede resultar molesto o innecesario para los jugadores que prefieran concentrarse exclusivamente en el juego.

#### Alternativa 6.b: Chat mediante una ventana emergente

Ventajas: No ocupa espacio en la interfaz principal, permitiendo que los jugadores se concentren en la partida. Es accesible en cualquier momento, pero solo cuando el jugador decida interactuar con él. Facilita la integración de funcionalidades adicionales, como minimizar o cerrar el chat sin perder mensajes.

Inconvenientes: Requiere que los jugadores abran la ventana para acceder al chat. Puede ser menos visible en situaciones en las que los mensajes sean urgentes o requieran una respuesta inmediata.

#### Justificación de la solución adoptada
Se optó por implementar la Alternativa 6.b: Chat mediante una ventana emergente, ya que proporciona una solución equilibrada entre discreción y funcionalidad. Esta decisión asegura que el chat no interfiera con la jugabilidad, mientras sigue siendo accesible en cualquier momento según las necesidades del jugador.



### Decisión 7: Implementación de un botón "How to Play" para mostrar las reglas del juego
#### Descripción del problema:

Se identificó la necesidad de proporcionar un acceso rápido y sencillo a las reglas del juego dentro de la interfaz. Esto era crucial para garantizar que los jugadores, especialmente los nuevos, pudieran consultar las normas en cualquier momento de la partida sin interrumpir la experiencia de juego.

#### Alternativas de solución evaluadas
#### Alternativa 7.a: Botón dedicado "How to Play" en la interfaz principal

Ventajas: Proporciona un acceso claro y constante a las reglas durante toda la partida. No ocupa mucho espacio y es ideal para consultas rápidas, permitiendo a los jugadores resolver dudas sin pausar el juego.

Inconvenientes: Los jugadores podrían no notar el botón si no está bien ubicado o resaltado.

#### Alternativa 7.b: Mostrar las reglas al inicio de la partida

Ventajas: Garantiza que todos los jugadores vean las reglas antes de comenzar a jugar. Evita la necesidad de buscar las reglas durante la partida.

Inconvenientes: Puede ser molesto para jugadores experimentados que no necesitan ver las reglas nuevamente. No permite consultas rápidas durante el juego, lo que puede generar confusión en situaciones específicas.

#### Alternativa 7.c: Mostrar las reglas en un documento externo o enlace

Ventajas: Ideal para reglas extensas o con gráficos detallados.

Inconvenientes: Obliga a los jugadores a salir de la interfaz del juego, lo que interrumpe la experiencia inmersiva.

#### Justificación de la solución adoptada
Se optó por implementar la Alternativa 7.a: Botón dedicado "How to Play" en la interfaz principal, ya que proporciona un acceso rápido, intuitivo y constante a las reglas del juego sin interferir en la jugabilidad. Esta solución permite a los jugadores consultar las normas en cualquier momento de forma discreta, asegurando una experiencia fluida y accesible para todos.


