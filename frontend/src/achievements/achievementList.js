import { Table, Button } from "reactstrap";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import { Link } from "react-router-dom";
import deleteFromList from "./../util/deleteFromList";
import { useState } from "react";
import getErrorModal from "./../util/getErrorModal";
import jwt_decode from "jwt-decode";
import '../static/css/achievement/achievement.css';

const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();
let role = []
if (jwt) {
  role = getRolesFromJWT(jwt); 
}

function getRolesFromJWT(jwt) {
  return jwt_decode(jwt).authorities;
}

export default function AchievementList() {
  const [message, setMessage] = useState('');
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);
  const [achievements, setAchievements] = useFetchState([], `/api/v1/achievements/progress`, jwt);

  const achievementList = achievements.map((a) => {
    
    const isCompleted = a.actualValue >= a.threshold;
    
    return (
      <tr key={a.id} className={isCompleted ? "achievement-completed" : ""}>
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
          <table aria-label="achievements" className="mt-4">
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
          </table>

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
