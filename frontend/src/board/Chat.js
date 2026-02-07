import '../static/css/board/chat.css';
import { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import tokenService from "../services/token.service";

const jwt = tokenService.getLocalAccessToken();

export default function Chat() {
    const [chatVisible, setChatVisible] = useState(false);
    const [chatMessages, setChatMessages] = useState([]);
    const [newMessage, setNewMessage] = useState(""); 
    const { boardId } = useParams(); 
    const { playerId } = useParams(); 

    
    useEffect(() => {
        if (!chatVisible) return; // Solo realiza el polling si el chat está visible

        const interval = setInterval(() => {
            fetch(`/api/v1/board/${boardId}/chat`, {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error("Failed to fetch chat messages");
                    }
                    return response.json();
                })
                .then((messages) => {
                    setChatMessages(messages);
                })
                .catch((error) => {
                    console.error("Error fetching chat messages:", error);
                });
        }, 1000); // Poll cada 3 segundos

        return () => clearInterval(interval); 
    }, [boardId, chatVisible]);

    // Función para enviar un mensaje al chat
    const sendMessage = (e) => {
        e.preventDefault();
        if (!newMessage.trim()) return;

        fetch(`/api/v1/board/${boardId}/sendChat?message=${newMessage}`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${jwt}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ playerId, message: newMessage }),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to send message");
                }
                return response.json();
            })
            .then((message) => {
                setChatMessages((prevMessages) => [...prevMessages, message]);
                setNewMessage("");
            })
            .catch((error) => {
                console.error("Error sending message:", error);
            });
    };

    return (
        <div>
            {/* Botón para abrir/cerrar el chat */}
            <button
                className="chat-toggle-button"
                onClick={() => setChatVisible(!chatVisible)}
            >
                {chatVisible ? "Close chat" : "Open chat"}
            </button>

            {/* Ventana de chat */}
            {chatVisible && (
                <div className="chat-container">
                    <div className="chat-header">
                        <h4>💬 Chat</h4>
                        <button
                            className="close-chat-button"
                            onClick={() => setChatVisible(false)}
                        >
                            ✖
                        </button>
                    </div>
                    <div className="chat-messages">
                        {chatMessages.length > 0 ? (
                            chatMessages.map((msg, index) => (
                                <div key={index} className="chat-message">
                                    <strong>{msg.player.user.username}: </strong>
                                    <span>{msg.message}</span>
                                </div>
                            ))
                        ) : (
                            <p className="chat-empty">
                                No hay mensajes aún. ¡Sé el primero en escribir!
                            </p>
                        )}
                    </div>
                    <form className="chat-input-form" onSubmit={sendMessage}>
                        <input
                            type="text"
                            value={newMessage}
                            onChange={(e) => setNewMessage(e.target.value)}
                            placeholder="Escribe tu mensaje..."
                            className="chat-input"
                        />
                        <button type="submit" className="chat-send-button">
                            Enviar
                        </button>
                    </form>
                </div>
            )}
        </div>
    );
}
