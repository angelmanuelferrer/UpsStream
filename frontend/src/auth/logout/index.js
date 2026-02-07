import React from "react";
import { Link } from "react-router-dom";
import "../../static/css/auth/authButton.css";
import "../../static/css/auth/authPage.css";
import tokenService from "../../services/token.service";
import { Button } from "reactstrap";

const Logout = () => {
  function sendLogoutRequest() {
    const jwt = window.localStorage.getItem("jwt");
    if (jwt || typeof jwt === "undefined") {
      tokenService.removeUser();
      window.location.href = "/";
    } else {
      alert("There is no user logged in");
    }
  }

  return (
    <div className="auth-page-container">
      <div className="auth-form-container">
        <h2 className="text-center text-md">
          Are you sure you want to log out?
        </h2>
        <div className="options-row">
          <Link to="/">
            <Button size='lg' style={{ backgroundColor: 'darkblue', borderColor: 'darkblue' }}>No</Button>
          </Link>
          <Button size='lg' style={{ backgroundColor: 'darkblue', borderColor: 'darkblue' }} onClick={() => sendLogoutRequest()}>
          
            Yes
          </Button>
        </div>
      </div>
    </div>
  );
};

export default Logout;
