import { useState, useEffect } from "react";
import tokenService from "../services/token.service";
import { useNavigate } from "react-router-dom";
import "./FriendList.css";

const jwt = tokenService.getLocalAccessToken();

export default function FriendList() {
  const [userName, setUserName] = useState("");
  const [friends, setFriends] = useState([]);
  const [invitations, setInvitations] = useState([]);
  const [matchInvitations, setMatchInvitations] = useState([]);
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const interval = setInterval(() => {
      const fetchFriends = async () => {
        try {
          const response = await fetch(`/api/v1/users/friends`, {
            method: "GET",
            headers: {
              Authorization: `Bearer ${jwt}`,
            },
          });

          if (response.ok) {
            const data = await response.json();
            setFriends(data);
          } else if (response.status === 404) {
            setMessage("Usuario no encontrado.");
          } else {
            setMessage("Error al cargar la lista de amigos.");
          }
        } catch (error) {
          setMessage("Error al conectar con el servidor.");
        }
      };

      const fetchInvitations = async () => {
        try {
          const response = await fetch(`/api/v1/users/friendsInvitation`, {
            method: "GET",
            headers: {
              Authorization: `Bearer ${jwt}`,
            },
          });

          if (response.ok) {
            const data = await response.json();
            setInvitations(data);
          } else if (response.status === 404) {
            setMessage("Usuario no encontrado.");
          } else {
            setMessage("Error al cargar las invitaciones.");
          }
        } catch (error) {
          setMessage("Error al conectar con el servidor.");
        }
      };

      const fetchMatchInvitations = async () => {
        try {
          const response = await fetch(`/api/v1/users/matchInvitation`, {
            method: "GET",
            headers: {
              Authorization: `Bearer ${jwt}`,
            },
          });

          if (response.ok) {
            const data = await response.json();
            setMatchInvitations(data);
          } else if (response.status === 404) {
            setMessage("Usuario no encontrado.");
          } else {
            setMessage("Error al cargar las invitaciones.");
          }
        } catch (error) {
          setMessage("Error al conectar con el servidor.");
        }
      };

      fetchFriends();
      fetchInvitations();
      fetchMatchInvitations();
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  const handleAddInvitation = async () => {
    if (!userName.trim()) {
      setMessage("Por favor, introduce el nombre del amigo.");
      return;
    }

    try {
      const response = await fetch(`/api/v1/users/friends/${userName}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
      });

      if (response.ok) {
        setMessage(`Amigo ${userName} agregado exitosamente.`);
        setUserName("");
      } else {
        const errorData = await response.text();
        setMessage(`Error: ${errorData}`);
      }
    } catch (error) {
      setMessage("Error al conectar con el servidor.");
    }
  };

  const handleRemoveInvitation = async (invitationId) => {
    try {
      const response = await fetch(
        `/api/v1/users/friendsInvitation/${invitationId}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      );

      if (response.ok) {
        setMessage("Invitacion eliminada exitosamente.");
        setInvitations((prev) =>
          prev.filter((inv) => inv.id !== invitationId)
        );
      } else {
        const errorData = await response.text();
        setMessage(`Error: ${errorData}`);
      }
    } catch (error) {
      setMessage("Error al conectar con el servidor.");
    }
  };

  const handleRemoveFriend = async (friendId) => {
    try {
      const response = await fetch(`/api/v1/users/friends/${friendId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });

      if (response.ok) {
        setMessage("Amigo eliminado exitosamente.");
        setFriends((prev) => prev.filter((f) => f.id !== friendId));
      } else {
        const errorData = await response.text();
        setMessage(`Error: ${errorData}`);
      }
    } catch (error) {
      setMessage("Error al conectar con el servidor.");
    }
  };

  const handleAcceptFriend = async (invitation) => {
    try {
      const response = await fetch(
        `/api/v1/users/friendsInvitation/${invitation.id}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      );

      if (response.ok) {
        setMessage("Amigo aceptado exitosamente.");
        setInvitations((prev) =>
          prev.filter((inv) => inv.id !== invitation.id)
        );
        setFriends((prev) => [...prev, invitation.sendUser]);
      } else {
        const errorData = await response.text();
        setMessage(`Error: ${errorData}`);
      }
    } catch (error) {
      setMessage("Error al conectar con el servidor.");
    }
  };

  const handleAcceptMatchInvitation = async (invitation) => {
    try {
      const response = await fetch(
        `/api/v1/users/matchInvitation/${invitation.id}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${jwt}`,
            "Content-Type": "application/json",
          },
        }
      );

      if (response.ok) {
        navigate(`/matches/${invitation.match.id}/players`);
      } else {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to join the match");
      }
    } catch (error) {
      setMessage(`Error: ${error.message}`);
    }
  };

  const handleRemoveMatchInvitation = async (invitationId) => {
    try {
      const response = await fetch(
        `/api/v1/users/matchInvitation/${invitationId}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      );

      if (response.ok) {
        setMessage("Invitacion eliminada exitosamente.");
        setMatchInvitations((prev) =>
          prev.filter((inv) => inv.id !== invitationId)
        );
      } else {
        const errorData = await response.text();
        setMessage(`Error: ${errorData}`);
      }
    } catch (error) {
      setMessage("Error al conectar con el servidor.");
    }
  };

  return (
    <div className="friend-list-container">
      <h1>Gestión de Amigos</h1>

      <input
        type="text"
        placeholder="Nombre del amigo"
        value={userName}
        onChange={(e) => setUserName(e.target.value)}
        className="friend-list-input"
      />

      <button onClick={handleAddInvitation} className="friend-list-button">
        Enviar solicitud de amistad
      </button>

      {message && <p className="friend-list-message">{message}</p>}

      <div className="friend-list-section">
        <h2>Lista de Amigos</h2>
        <ul className="friend-list-list">
          {friends.length > 0 ? (
            friends.map((friend) => (
              <li className="friend-list-item" key={friend.id}>
                <button
                  onClick={() => navigate(`/users/${friend.username}`)}
                  className="friend-list-btn username"
                >
                  {friend.username}
                </button>
                <div className="friend-list-action">
                  <button
                    onClick={() => handleRemoveFriend(friend.id)}
                    className="friend-list-btn remove"
                  >
                    Eliminar
                  </button>
                </div>
              </li>
            ))
          ) : (
            <p>No tienes amigos en tu lista.</p>
          )}
        </ul>
      </div>

      <div className="friend-list-section">
        <h2>Solicitudes de Amistad</h2>
        <ul className="friend-list-list">
          {invitations.length > 0 ? (
            invitations.map((inv) => (
              <li className="friend-list-item" key={inv.id}>
                <span className="friend-list-username">
                  {inv.sendUser.username}
                </span>
                <div className="friend-list-action">
                  <button
                    onClick={() => handleRemoveInvitation(inv.id)}
                    className="friend-list-btn remove"
                  >
                    Eliminar
                  </button>
                  <button
                    onClick={() => handleAcceptFriend(inv)}
                    className="friend-list-btn accept"
                  >
                    Aceptar
                  </button>
                </div>
              </li>
            ))
          ) : (
            <p>No tienes invitaciones.</p>
          )}
        </ul>
      </div>

      <div className="friend-list-section">
        <h2>Invitaciones de partida</h2>
        <ul className="friend-list-list">
          {matchInvitations.length > 0 ? (
            matchInvitations.map((inv) => (
              <li className="friend-list-item" key={inv.id}>
                <span className="friend-list-username">
                  {inv.sendUser.username}
                </span>
                <div className="friend-list-action">
                  <button
                    onClick={() => handleRemoveMatchInvitation(inv.id)}
                    className="friend-list-btn remove"
                  >
                    Eliminar
                  </button>
                  <button
                    onClick={() => handleAcceptMatchInvitation(inv)}
                    className="friend-list-btn accept"
                  >
                    Aceptar
                  </button>
                </div>
              </li>
            ))
          ) : (
            <p>No tienes invitaciones de partida.</p>
          )}
        </ul>
      </div>
    </div>
  );
}