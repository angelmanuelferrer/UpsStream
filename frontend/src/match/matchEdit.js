import { useState } from "react";
import tokenService from "../services/token.service";
import { Link } from "react-router-dom";
import { Button, Form, Input, Label } from "reactstrap";
import getErrorModal from "./../util/getErrorModal";
import getIdFromUrl from "./../util/getIdFromUrl";
import useFetchStateNo from "./../util/useFetchStateNo";
import { useNavigate } from "react-router-dom";
import { FaEye, FaEyeSlash } from "react-icons/fa"; 

const jwt = tokenService.getLocalAccessToken();

export default function MatchEdit() {
  const id = getIdFromUrl(2);
  const emptyMatch = {
    id: id === "new" ? null : id,
    name: "",
    code: "",        
    numberOfPlayers: "" 
  };
  
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [match, setMatch] = useFetchStateNo(
    emptyMatch,
    `/api/v1/matches/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );
  
  const [showCode, setShowCode] = useState(false); // Estado para mostrar/ocultar el código
  const modal = getErrorModal(setVisible, visible, message);
  const navigate = useNavigate();
  
  function handleSubmit(event) {
    event.preventDefault();
    
    fetch(
      "/api/v1/matches" + (match.id ? "/" + match.id : ""),
      {
        method: match.id ? "PUT" : "POST",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(match),
      }
    )
    .then((response) => response.text())
    .then((data) => {
      if (data === "") {
        navigate("/matches");
      } else {
        let json = JSON.parse(data);
        if (json.message) {
          setMessage(json.message);
          setVisible(true);
        } else {
          navigate("/matches");
        }
      }
    })
    .catch((message) => alert(message));
  }
  
  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setMatch({ ...match, [name]: value });
  }
  
  return (
    <div className="auth-page-container">
      <h2 className="text-center">
        {match.id ? "Edit match" : "Add match"}
      </h2>
      <div className="auth-form-container">
        {modal}
        <Form onSubmit={handleSubmit}>
          <div className="custom-form-input">
            <Label for="name" className="custom-form-input-label">Name</Label>
            <Input
              type="text"
              required
              name="name"
              id="name"
              value={match.name || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>

          
          <div className="custom-form-input">
            <Label for="code" className="custom-form-input-label">Code</Label>
            <div style={{ position: "relative" }}>
              <Input
                type={showCode ? "text" : "password"} 
                name="code"
                id="code"
                value={match.code || ""}
                onChange={handleChange}
                className="custom-input"
              />
              <button
                type="button"
                onClick={() => setShowCode(!showCode)} 
                style={{
                  position: "absolute",
                  right: "10px",
                  top: "50%",
                  transform: "translateY(-50%)",
                  background: "none",
                  border: "none",
                  cursor: "pointer",
                }}
              >
                {showCode ? <FaEyeSlash /> : <FaEye />} {/* Toggle icons */}
              </button>
            </div>
          </div>
          
          <div className="custom-form-input">
            <Label for="numberOfPlayers" className="custom-form-input-label">Number of Players</Label>
            <Input
              type="number"
              required
              name="numberOfPlayers"
              id="numberOfPlayers"
              value={match.numberOfPlayers || ""}
              onChange={handleChange}
              className="custom-input"
              min="2" 
              max="5"
            />
          </div>

          <div className="custom-button-row">
            <Button size='lg' style={{ backgroundColor: 'darkblue', borderColor: 'darkblue' }}>Save</Button>
            <Link
              to={`/matches`}
              className="custom-button"
              size='lg' style={{ backgroundColor: 'darkblue', borderColor: 'darkblue' }}
            >
              Cancel
            </Link>
          </div>
        </Form>
      </div>
    </div>
  );
}
