import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import tokenService from "../services/token.service";
import { Table, Card, CardBody, Button } from "reactstrap";
import getErrorModal from "../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import useFetchStateNo from "../util/useFetchStateNo";
import '../static/css/home/home.css';

const jwt = tokenService.getLocalAccessToken();

export default function PlayerList() {
  const { matchId } = useParams();
  const navigate = useNavigate();
  const [matchName, setMatchName] = useState("");
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const modal = getErrorModal(setVisible, visible, message);
  const [userName, setUserName] = useState('');

  const [players, setPlayers] = useFetchState(
    [],
    `/api/v1/matches/${matchId}/players`,
    tokenService.getLocalAccessToken(),
    setMessage,
    setVisible
  );
  
  const [matches, setMatches] = useFetchStateNo(
    [],
    `/api/v1/matches/${matchId}`,
    tokenService.getLocalAccessToken(),
    setMessage,
    setVisible
  );

  const currentUser = tokenService.getUser();

  useEffect(() => {
    fetch(`/api/v1/matches/${matchId}`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch match details");
        }
        return response.json();
      })
      .then((data) => setMatchName(data.name))
      .catch(() => {
        setMessage("Error fetching match details.");
        setVisible(true);
      });
  }, [matchId]);

  const handlePlay = (event) => {
    event.preventDefault();

    // Crear el board en la base de datos a partir del matchId de la URL (createBoard del BoardController) asociandole un color random a cada player
    fetch(`/api/v1/board/${matchId}`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to create board");
        }
        return response.json();
      })
      .then((data) => {
        const boardId = data.id;
        navigate(`/board/${boardId}`);

        // Crea TileStacks
        fetch(`/api/v1/tileStack/${boardId}`, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${jwt}`,
            "Content-Type": "application/json",
          },
        })
          .then((response) => {
            if (!response.ok) {
              throw new Error("Failed to create tileStack");
            }
            return response.json();
          })
          .then((data) => {
            console.log("TileStack created:", data);
          })
          .catch((error) => {
            console.error("Error creating tileStack:", error);
            setMessage("Error creating tileStack.");
            setVisible(true);
          });

        // Actualizar la fecha de inicio del match
        fetch(`/api/v1/matches/${matchId}/start`, {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${jwt}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            start: new Date().toISOString(),
          }),
        })
          .then((response) => {
            if (!response.ok) {
              throw new Error("Failed to update match");
            }
            return response.json();
          })
          .then((data) => {
            console.log("Match updated:", data);
          })
          .catch((error) => {
            setMessage("Error updating match." + error);
            setVisible(true);
          });
      })
      .catch((error) => {
        setMessage("Error creating board." + error);
        setVisible(true);
      });
  };

  useEffect(() => {
    // Establece un intervalo que se ejecuta cada 1 segundo
    const interval = setInterval(() => {
      // Realiza una solicitud para obtener el estado del partido
      fetch(`/api/v1/matches/${matchId}/status`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
        .then((response) => response.json())
        .then((status) => {
          if (status) {  // Aquí `status` es directamente el valor true o false
            // Si el estado es true, detiene el polling
            clearInterval(interval);
            // Realiza una solicitud para obtener el tablero asociado con el partido
            fetch(`/api/v1/board/match/${matchId}`, {
              headers: {
                Authorization: `Bearer ${jwt}`,
              },
            })
              .then((response) => {
                if (!response.ok) {

                  throw new Error("Failed to fetch board by match ID");
                }
                return response.json();
              })
              .then((board) => {
                const storedBoardId = board.id; // Obtiene el ID del tablero
                navigate(`/board/${storedBoardId}`); // Redirige al tablero de la partida
              })
              .catch((error) => {
                console.error("Error fetching board by match ID:", error);
                setMessage("Error fetching board by match ID.");
                setVisible(true);
              });
          }
        })
        .catch((error) => {
          console.error("Error fetching match status:", error);
          setMessage("Error checking match status.");
          setVisible(true);
        });
    }, 1000); 

    // Limpia el intervalo cuando el componente se desmonte
    return () => clearInterval(interval);
  }, [matchId, navigate]); 

  const handleAddInvitation = async () => {
    if (!userName.trim()) {
        setMessage('Por favor, introduce el nombre del amigo.');
        return;
    }

    try {
        const response = await fetch(`/api/v1/users/matchInvitation/${userName}/${matchId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`,
            },
        });

        if (response.ok) {
            setMessage(`Amigo ${userName} agregado exitosamente.`);
            setUserName(''); 

        } else {
            const errorData = await response.text();
            setMessage(`Error: ${errorData}`);
        }
    } catch (error) {
        setMessage('Error al conectar con el servidor.');
    }
};

  return (
    <div className="matchList-page-container">
      {modal}
      <div className="match-list-box">
        <h2 className="text-center">{matchName}</h2>
        <Card>
          <CardBody>
            <Table aria-label="players" className="mt-4">
              <tbody>
                {players.map((player) => (
                  <tr key={player.id}>
                    <td className="text-center">
                      {player.user ? player.user.username : "No Username"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </CardBody>
        </Card>
        <div className="text-center mt-3">
          <div style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
          {players.length === matches.numberOfPlayers && (
            <Button size='lg' style={{ backgroundColor: 'darkblue', borderColor: 'darkblue' }} onClick={handlePlay}>
              Play
            </Button> )}

            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
            <Button size='lg' style={{ backgroundColor: 'purple', borderColor: 'purple' }} onClick={handleAddInvitation}>
              Invite Friends
            </Button>
            <input
              type="text"
              placeholder="Nombre del amigo"
              onChange={(e) => setUserName(e.target.value)}
              value={userName}
              style={{ width: '100%', borderRadius: '5px', padding: '5px' }}
            />
          </div>
          </div>
        </div>
      </div>
    </div>
  );
}