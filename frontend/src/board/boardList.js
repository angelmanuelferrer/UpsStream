import React, { useEffect, useRef, useState } from "react";
import { useParams, useSearchParams } from "react-router-dom";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";
import '../static/css/board/board.css';
import '../static/css/board/rules.css';
import useFetchState from "../util/useFetchState";
import Chat from "./Chat";
import Rules from "./Rules";
import EndGame from "./endGame";
import {useNavigate} from "react-router-dom"
const jwt = tokenService.getLocalAccessToken();

const getPlayerColor = (colour) => {
  switch (colour) {
    case "GREEN":
      return "#90ee90"; // green
    case "RED":
      return "#ffcccb"; // red
    case "PURPLE":
      return "#d8bfd8"; // blue
    case "YELLOW":
      return "#ffffe0"; // yellow
    case "ORANGE":
      return "#ffdab9"; // orange
    default:
      return "black";
  }
}

export default function BoardList({ Spectator }) {
  const navigate=useNavigate()
  const { boardId } = useParams();
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const modal = getErrorModal(setVisible, visible, message);
  const [numberOfPlayers, setNumberOfPlayers] = useState([]);
  const [players, setPlayers] = useState([]);
  const [points, setPoints] = useState([]);
  const [board, setBoard] = useState([]);
  const [angulo, setAngulo] = useState(0);
  const ref = useRef(null);

  const [searchParams] = useSearchParams();

  const spectator = searchParams.get("spectator") === "true"

  const [currentTurn, setCurrentTurn] = useFetchState(
    [],
    `/api/v1/board/${boardId}/turno`,
    tokenService.getLocalAccessToken(),
    setMessage,
    setVisible
  );

  const player = players.find(player => player.user.username === tokenService.getUser().username);

  const [movimientos, setMovimientos] = useFetchState(
    [],
    player ? `/api/v1/players/${player.id}/movement` : null,
    tokenService.getLocalAccessToken(),
    setMessage,
    setVisible
  );

  const [currentTurnTile, setCurrentTurnTile] = useFetchState(
    [],
    `/api/v1/board/${boardId}/turnoTile`,
    tokenService.getLocalAccessToken(),
    setMessage,
    setVisible
  );

  const [winner, setWinner] = useState(false);
  const [salmons, setSalmons] = useState([]);
  const [tileStack, setTileStack] = useState([]);
  const [rotatedSrc, setRotatedSrc] = useState(tileStack?.image);

  const [currentTileCount, setCurrentTileCount] = useFetchState(
    [],
    `/api/v1/board/${boardId}/tileCount`,
    tokenService.getLocalAccessToken(),
    setMessage,
    setVisible
  );

  // Obtiene el tablero
  useEffect(() => {
    const initialBoard = [
      Array(12).fill(null),
      Array(13).fill(null),
      Array(12).fill(null),
    ];
    setBoard(initialBoard);
  }, [boardId]);

  // Obtiene la tileStack
  useEffect(() => {
    fetch(`/api/v1/tileStack/${boardId}/board`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to search tileStack");
        }
        return response.text(); 
      })
      .then((text) => {
        if (text) {
          const data = JSON.parse(text);
          setTileStack(data);
        } else {
          console.warn("Respuesta vacía al buscar tileStack");
          setTileStack([]); 
        }
      })
      .catch((error) => {
        console.error("Error searching tileStack:", error);
        setMessage("Error searching tileStack.");
        setVisible(true);
      });
  }, [currentTileCount, boardId]);

  // Obtiene los jugadores
  useEffect(() => {
    fetch(`/api/v1/board/${boardId}`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setPlayers(data.match.players);
        setNumberOfPlayers(data.match.players.length);
      })
      .catch((error) => {
        setMessage("Error fetching players." + error);
        setVisible(true);
      });
  }, [boardId, salmons, board]);

  // Función para colocar la tile en el tablero y crearla
  const handleCellDrop = (e, rowIndex, colIndex) => {
    const tileImage = e.dataTransfer.getData("tileImage");
    const newBoard = [...board];
    newBoard[rowIndex][colIndex] = { image: tileImage };
    setBoard(newBoard);

    const salmon = e.dataTransfer.getData("salmon");
    console.log(salmon)
    if (!salmon) {
      const tileCount = currentTileCount;
      let setIndex;
      if (rowIndex !== 1) {
        setIndex = Math.floor(tileCount / 3) + 1; 

        // Verifica si la posición x es válida

      } else {
        setIndex = Math.floor(tileCount / 3) + 2; 

      }

      if (colIndex !== setIndex) {
        setMessage("Quedan zonas por rellenar con losetas");
        setVisible(true);
        setTimeout(() => {
          window.location.reload(); 
        }, 2000); 
        return;
      }

      // Llamada a la API para crear la tile
      fetch(`/api/v1/tile/${boardId}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${jwt}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          tileStack: {
            id: tileStack.id,
            type: tileStack.type,
            image: tileStack.image,
            used: tileStack.used,
            board: tileStack.board,
          },
          rotation: angulo,
          capacity: players.length,
          image: tileStack.image,
          type: tileStack.image === "/loseta_agua.png" ? "WATER" : tileStack.image === "/aguila.png" ? "EAGLE" :
            tileStack.image === "/roca.png" ? "ROCK" : tileStack.image === "/garza.png" ? "HERON" : tileStack.image === "/salto_agua.png" ? "WATERFALL" :
              "BEAR",
          x: colIndex,
          y: rowIndex,
        }),
      })
        .then((response) => {
          if (!response.ok) {
            return response.json().then((error) => {
              throw new Error(error.message || "Failed to create tile");
            });
          }
        })
        .then((data) => {
          console.log("Tile created:", data);
        })
        .catch((error) => {
          console.error("Error creating tile:", error);
          setMessage(error.message);
          setVisible(true);
          setTimeout(() => {
            window.location.reload(); 
          }, 3000); 
        });

      // Actualiza la tileStack como usada
      fetch(`/api/v1/tileStack/${tileStack.id}`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${jwt}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          used: true,
        }),
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to update tileStack");
          }
          return response.text().then((text) => {
            return text ? JSON.parse(text) : {};
          });
        })
        .then((data) => {
          console.log("TileStack updated:", data);
        })
        .catch((error) => {
          setMessage("Error updating match." + error);
          setVisible(true);
        });

    } else {
      const salmon = JSON.parse(e.dataTransfer.getData("salmon"));

      // Llamada a la API para mover el salmón
      fetch(`/api/v1/salmon/${salmon.id}/move`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${jwt}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          x: colIndex,
          y: rowIndex,
        }),
      })
        .then((response) => {
          if (!response.ok) {
            return response.json().then((error) => {
              throw new Error(error.message || "Failed to move salmon");
            });
          }
        })
        .catch((error) => {
          console.error("Error moving salmon:", error);
          setMessage(error.message);
          setVisible(true);
        });
    }
  };

  useEffect(() => {
    const canvas = ref.current;
    if (!canvas) return; 
    const context = canvas.getContext("2d");
    const image = new Image();

    image.onload = () => {
      const radians = Math.PI / 3; // Rotación de 180 grados

      
      const sin = Math.abs(Math.sin(radians));
      const cos = Math.abs(Math.cos(radians));
      const newWidth = image.width * cos + image.height * sin;
      const newHeight = image.width * sin + image.height * cos;

      canvas.width = newWidth;
      canvas.height = newHeight;

      // Limpiar y rotar
      context.clearRect(0, 0, canvas.width, canvas.height);
      context.translate(newWidth / 2, newHeight / 2);
      context.rotate(radians);

      // Dibujar imagen rotada
      context.drawImage(image, -image.width / 2, -image.height / 2);

      setRotatedSrc(canvas.toDataURL());
    };
    image.src = tileStack.image;
  }, [angulo, tileStack.image]);


  // Polling para obtener las tiles del board
  useEffect(() => {
    const interval = setInterval(() => {
      fetch(`/api/v1/tile/board/${boardId}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to fetch tiles");
          }
          return response.json();
        })
        .then((tiles) => {
          const newBoard = [...board];
          newBoard.forEach((row, y) => {
            row.forEach((_, x) => {
              newBoard[y][x] = null;
            });
          });
          tiles.forEach((tile) => {
            newBoard[tile.y][tile.x] = tile;
          });
          setBoard(newBoard);
        })
        .catch((error) => {
          console.error("Error fetching tiles:", error);
        });
    }, 1000); 

    return () => clearInterval(interval); 
  }, [boardId, board]);


  useEffect(() => {
    const interval = setInterval(() => {
      fetch(`/api/v1/board/${boardId}/winner`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to fetch winner");
          }
          return response.json();
        })
        .then((winner2) => {
          setWinner(winner2);
        })
        .catch((error) => {
          console.error("Error fetching winner:", error);
        });
    }, 1000); 

    return () => clearInterval(interval); 
  }, [boardId, board]);
  useEffect(() => {
    const interval = setInterval(() => {
      fetch(`/api/v1/board/${boardId}/userTimeExceeded`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to fetch user time exceeded");
          }
          return response.json();
        })
        .then((userTimeExceeded) => {
            if(userTimeExceeded){
              window.alert(`The game finishes because player ${userTimeExceeded.username} has reached his time limit`)
              navigate("/")
            }
        })
        .catch((error) => {
          console.error("Error fetching winner:", error);
        });
    }, 1000); 

    return () => clearInterval(interval); 
  }, [boardId, board]);

  // Polling para obtener los salmones del board
  useEffect(() => {
    const interval = setInterval(() => {
      fetch(`/api/v1/salmon/${boardId}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to fetch tiles");
          }
          return response.json();
        })
        .then((tiles) => {
          setSalmons(tiles);
        })
        .catch((error) => {
          console.error("Error fetching tiles:", error);
        });
    }, 1000); 

    return () => clearInterval(interval); 
  }, [boardId, board]);

  // Función para permitir el drag de la tile (desde la tileStack)
  const handleDragStart = (e, tile, columnIndex) => {
    const player = players.find(player => player.user.username === tokenService.getUser().username);
    console.log(tokenService.getUser().username);
    if (currentTileCount <= 12 && player?.colour !== currentTurn) {
      setMessage("No es tu turno");
      setVisible(true);
      return;
    } else if (currentTileCount > 12 && player?.colour !== currentTurnTile) {
      setMessage("No es tu turno");
      setVisible(true);
      return;
    }
    e.dataTransfer.setData("tileImage", tile.image);
    e.dataTransfer.setData("sourceColumn", columnIndex);
  };

  // Función para permitir el drop de la tile
  const handleDragOver = (e) => {
    e.preventDefault();

  };

  function arrastrarSalmon(e, salmon) {
    // Obtén el jugador actual (basado en el turno y el token del usuario)
    const player = players.find(player => player.user.username === tokenService.getUser().username);

    // Verifica si es el turno del jugador actual
    if (player?.colour !== currentTurn) {
      setMessage("No es tu turno");
      setVisible(true);
      return;
    }
    // Verifica si el salmón pertenece al jugador actual (por color)
    if (salmon.player.colour !== player?.colour) {
      setMessage("No puedes mover un salmón que no te pertenece");
      setVisible(true);
      return;
    }
    // Si pasa las validaciones, permite arrastrar
    e.dataTransfer.setData("salmon", JSON.stringify(salmon));
  }


  return (
    !winner ? (
    <div className="board-page-container">
      {modal}

        <Rules />
        <Chat />
      
      <div className="movimientos-window">
        <ul className="movimientos-list">Movements: {movimientos}</ul>
      </div>

      <div className="board-grid">
        {board.map((row, rowIndex) => (
          <div key={rowIndex} className="board-row">
            {row.map((cell, colIndex) => (
              <div
                key={colIndex}
                className={`board-cell ${(colIndex === 0) || (colIndex === 1 && rowIndex === 1) ? 'water-tile' : ''}
                ${(colIndex === 11 && rowIndex === 1) ? 'desove-abajo' :
                      (colIndex === 11 && rowIndex === 0) ? 'desove-izquierda' :
                        (colIndex === 11 && rowIndex === 2) ? 'desove-derecha' :
                          (colIndex === 12 && rowIndex === 1) ? 'desove-arriba' : ``}`}

                onDrop={(e) => handleCellDrop(e, rowIndex, colIndex)}
                onDragOver={handleDragOver}
              >
                {cell && <img src={cell.image} alt={`Tile ${cell.id}`} className="tile-image" 
                style={{ transform: `rotate(${cell.rotation || 0}deg)` }} />}
                <div className="salmon-container">
                  {salmons.map((salmon) => (
                    salmon.x === colIndex && salmon.y === rowIndex && (
                      <img
                        key={salmon.id}
                        src={salmon.image}
                        alt={`Salmon of ${salmon.player.colour}`}
                        className="small-salmon"
                        onDragStart={(e) => arrastrarSalmon(e, salmon)}
                        draggable = {!spectator}
                      />
                    )
                  ))}
                </div>
              </div>
            ))}
          </div>
        ))}
      </div>

      <div className="tile-stack">
        <canvas ref={ref} style={{ display: "none" }}></canvas>
        <img
          key={tileStack.id}
          src={tileStack.image}
          alt="TileStack"
          draggable = {!spectator}
          onDragStart={(e) => handleDragStart(e, tileStack)}
          className="tile-stack-image"
        style={{ rotate: `${angulo}deg` }}
        />
        <button className="tile-stack-button" onClick={() => setAngulo((angulo + 60) % 360)}> ROTATE</button>

          {/* Renderizamos los jugadores con los colores*/}
          <div className="player-list">
            {Array.from({ length: numberOfPlayers }).map((_, index) => (
              <div
                key={index}
                className={`player-card ${players[index].colour === currentTurn ? 'current-turn' : ''}`}
                style={{ backgroundColor: getPlayerColor(players[index].colour) }}
              >
                {players[index].user ? players[index].user.username : "No Username"}
                <p>
                  points: {players[index].points}
                </p>
                {players[index].colour === currentTurn && <div className="turn-indicator"></div>}
                {currentTileCount >= 12 && (
                  players[index].colour === currentTurnTile && <div className="turn-indicator2"></div>
                )}
              </div>

          ))}
        </div>
      </div>
    </div>
  ): <EndGame winner={winner} />
    );
}