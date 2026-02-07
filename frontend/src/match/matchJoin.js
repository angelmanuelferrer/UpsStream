import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import tokenService from "../services/token.service";
import { Button, Form, Input, Label } from "reactstrap";
import getErrorModal from "./../util/getErrorModal";
import { FaEye, FaEyeSlash } from "react-icons/fa";

const jwt = tokenService.getLocalAccessToken();

export default function JoinMatch() {
  const { matchId } = useParams();
  const [match, setMatch] = useState({ code: "" });
  const [enteredCode, setEnteredCode] = useState("");
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const modal = getErrorModal(setVisible, visible, message);
  const navigate = useNavigate();

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
      .then((data) => {
        setMatch(data);
      })
      .catch((error) => {
        console.error("Error fetching match details:", error);
        setMessage("There was an error fetching match details.");
        setVisible(true);
      });
  }, [matchId]);

  const handleSubmit = (event) => {
    event.preventDefault();
    if (match.code && enteredCode !== match.code) {
      setMessage("Invalid code! Please check and try again.");
      setVisible(true);
      return;
    }
    fetch(`/api/v1/matches/${matchId}/join`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ code: match.code ? enteredCode : null }),
    })
      .then((response) => {
        if (response.ok) {
          navigate(`/matches/${matchId}/players`);
        } else {
          return response.json().then((error) => {
            throw new Error(error.message || "Failed to Join");
          });
        }
      })
      .catch((error) => {
        console.error("Error joining match:", error);
        setMessage(error.message);
        setVisible(true);
      });
  };

  return (
    <div className="auth-page-container">
      <h2 className="text-center">{match.name || "Join Match"}</h2>
      <div className="auth-form-container">
        {modal}
        <Form onSubmit={handleSubmit}>
          {match.code && (
            <div className="custom-form-input">
              <Label for="code" className="custom-form-input-label">Enter Match Code</Label>
              <div style={{ position: "relative" }}>
                <Input
                  type={showPassword ? "text" : "password"}
                  required
                  name="code"
                  id="code"
                  value={enteredCode}
                  onChange={(e) => setEnteredCode(e.target.value)}
                  className="custom-input"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
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
                  {showPassword ? <FaEyeSlash /> : <FaEye />}
                </button>
              </div>
            </div>
          )}
          <div className="custom-button-row">
            <Button size= 'lg' style={{ backgroundColor: 'darkblue', borderColor: 'darkblue' }}>Join</Button>
          </div>
        </Form>
      </div>
    </div>
  );
}
