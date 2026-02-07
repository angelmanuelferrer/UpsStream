import { useState } from 'react';
import { Button } from 'reactstrap';
import '../static/css/board/rules.css';

export default function Rules() {
    const [isRulesVisible, setIsRulesVisible] = useState(false);

    const toggleRules = () => {
        setIsRulesVisible(!isRulesVisible);
    };

    return (
        <div>
            <Button className="rules-button" onClick={toggleRules}>¿How to play?</Button>
            {isRulesVisible && (
                <div className="rules-tab">
                    <h2>Game rules</h2>
                    <div className="rules-content">
                        <p>1. A la hora de montar el tablero, aparecerá un punto negro encima del jugador al que le toque colocar una loseta en el río.</p>
                        <p>2. Una vez colocadas las 12 primeras losetas, sin contar las de inicio, todos los jugadores deberán mover sus salmones, gastando un total de 5 puntos de movimiento, en su turno.</p>
                        <p>3. Cuando todos los jugadores hayan movido sus salmones, el punto blanco indicará al jugador que le toque colocar una columna de losetas en el tablero. Una vez colocadas las 3 losetas
                            se volverá a repetir los pasos anteriores, el punto negro indicará el turno para el movimiento de los salmones y el blanco cambiará al siguiente jugador cuando todos los jugadores hayan gastado sus 
                            puntos de movimiento en sus salmones.</p>
                        <p>4. Cuando algun salmon entre en la zona de desove, cada turno se moverá automatica mente una loseta más (dentro del desove)</p>
                        <p>5. Una vez acabada la partida se determinará un ganador según la puntuación</p>
                        <p>PUNTUACIÓN: </p>
                            <p>Por cada salmón (que no ficha de salmón) que tenga, 1 punto. </p>
                            <p>Por cada ficha de salmón que tenga, 1 punto por cada huevo que haya en la casilla de desove donde se encuentra.</p>
                        <p>TIPOS DE MOVIMIENTO:</p>
                            <p>Nadar: Moverse de una loseta a otra sin obstáculos en medio se considera nadar. Nadar cuesta 1 punto por loseta a la que se desplace la ficha. Si una loseta está llena (capacidad = tantas fichas como jugadores), las fichas de salmón no pueden cruzarla nadando ni pararse en ella.</p>
                            <p>Saltar: Moverse de una loseta a otra ignorando todos los obstáculos se considera saltar. Cuesta 1 punto de movimiento, más 1 punto por cada loseta saltada. Sólo se puede saltar en línea recta, no se puede cambiar de dirección durante el salto</p>
                        <p>LOSETAS:</p>
                            <p>Agua: Es una loseta sin normas especiales. </p>
                            <p>Mar: Son las losetas de inicio de la partida, donde ponemos las fichas de salmón inicialmente. </p>
                            <p>Desove: Son las losetas finales del juego, estas losetas indicarán el ganador de la ronda.</p>
                            <p>Oso: Si una ficha de salmón salta hacia aquí, pierdes un salmón. Estas losetas contienen también salto de agua.</p>
                            <p>Águila: Si una ficha de salmón nada en esta loseta, pierdes un salmón y se voltea la loseta. </p>
                            <p>Garza: Al final del turno, pierdes un salmón de una de las fichas que tenas en esta loseta. Si tienes 2 fichas de salmón en una loseta de garza solo perderás   un salmón de una de esas 2 fichas. </p>
                            <p>Roca: En esta loseta cabe una ficha de salmón menos que en el resto de losetas, estas losetas no se usan en partidas de 2 jugadores. </p>
                            <p>Salto de agua: Los salmones tienen que saltar estos obstáculos para superarlos.</p>
                    </div>
                    <Button onClick={toggleRules}>Close</Button>
                </div>
            )}
        </div>
    );
}