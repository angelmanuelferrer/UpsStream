import { useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import getErrorModal from "../util/getErrorModal";
import { Button, ButtonGroup, Table } from "reactstrap";
import { Link } from "react-router-dom";
import '../static/css/home/home.css';

export default function MatchList() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [matches, setMatches] = useFetchState(
    [],
    `/api/v1/matches`,
    tokenService.getLocalAccessToken(),
    setMessage,
    setVisible
  );

  const [friendBoards, setFriendBoards] = useFetchState(
    [],
    `/api/v1/board/friendsBoard`,
    tokenService.getLocalAccessToken(),
    setMessage,
    setVisible
  );

  const currentUser = tokenService.getUser();
  const currentUserId = currentUser ? currentUser.id : null; // Verifica si currentUser no es null
  const isAdmin = currentUser ? currentUser.roles.find(r => r==='ADMIN') : null; // Verifica si el usuario es admin

  const matchesList = matches.map((match) => {
  const currentPlayersCount = match.players ? match.players.length : 0; // Asegura que players no sea null


    return (
      <tr key={match.id}>
        <td className="text-center">{match.name}</td>
        <td className="text-center">{match.code ? "PRIVATE" : "PUBLIC"}</td>
        <td className="text-center">{match.start ? match.start : "WAITING"}</td>
        <td className="text-center">{match.finish ? match.finish : "---"}</td>
        <td className="text-center">{`${currentPlayersCount}/${match.numberOfPlayers}`}</td> 
        <td className="text-center">
          <ButtonGroup>
            <>
              {/* Asegura que match.user no sea null y tenga un id */}
              {((match.user && match.user.id === currentUserId) || isAdmin) &&(
                <>
                  <Button
                    size="sm"
                    color="primary"
                    aria-label={"editar-" + match.name}
                    tag={Link}
                    to={"/matches/" + match.id}
                  >
                    Edit
                  </Button>
                  {<Button
                    size="sm"
                    color="danger"
                    aria-label={"eliminar-" + match.name}
                    onClick={() => {
                      let confirmMessage = window.confirm(
                        "¿Estás seguro de que deseas eliminarlo?"
                      );
                      if (!confirmMessage) return;
                      fetch(`/api/v1/matches/${match.id}`, {
                        method: "DELETE",
                        headers: {
                          "Content-Type": "application/json",
                          Authorization: `Bearer ${tokenService.getLocalAccessToken()}`,
                        },
                      })
                        .then((res) => {
                          if (res.status === 204) {
                            setMessage("Eliminado con éxito");
                            setVisible(true);
                            setMatches(matches.filter((m) => m.id !== match.id));
                          }
                        })
                        .catch((err) => {
                          setMessage(err.message);
                          setVisible(true);
                        });
                    }}
                  >
                    Delete
                  </Button>}
                </>
              )}
               <Button
                size="sm"
                color="warning"
                tag={Link}
                to={`/matches/${match.id}/join`}
              >
                Join
              </Button>
              {friendBoards.find(b => b.match.id === match.id) && <Button
                size="sm"
                color="info"
                tag={Link}
                to={`/board/${friendBoards.find(b => b.match.id === match.id).id}?spectator=true`} 
              >
                Spectate
              </Button>}
            </>
          </ButtonGroup>
        </td>
      </tr>
    );
  });

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="matchList-page-container">
      <div className="match-list-box">
        <h1 className="text-center">Matches</h1>
        {modal}
        <div className="float-right">
          <Button color="success" tag={Link} to="/matches/new">
            Add match
          </Button>
        </div>
        <div>
          <Table aria-label="matches" className="mt-4">
            <thead>
              <tr>
                <th width="15%" className="text-center">Name</th>
                <th width="15%" className="text-center">Type</th>
                <th width="15%" className="text-center">Start</th>
                <th width="15%" className="text-center">End</th>
                <th width="15%" className="text-center">Players</th> 
                <th width="30%" className="text-center">Actions</th>
              </tr>
            </thead>
            <tbody>{matchesList}</tbody>
          </Table>
        </div>
      </div>
    </div>
  );
}