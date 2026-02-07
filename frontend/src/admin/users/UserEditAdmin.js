import React, { useState, useEffect } from 'react';
import { Link } from "react-router-dom";
import { Button, Form, Input, Label } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchData from "../../util/useFetchData";
import useFetchStateNo from "../../util/useFetchStateNo";
import jwt_decode from "jwt-decode";

const jwt = tokenService.getLocalAccessToken();

function sendLogoutRequest() {
  const jwt = window.localStorage.getItem("jwt");
  if (jwt || typeof jwt === "undefined") {
    tokenService.removeUser();
    window.location.href = "/login";
  } else {
    alert("There is no user logged in");
  }
}

export default function UserEditAdmin() {
  const emptyItem = {
    id: null,
    username: "",
    password: "",
    authority: null,
  };

  const idOrUsername = getIdFromUrl(2);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);

  // Function to determine if idOrUsername is an integer
  const isInteger = (value) => {
    return /^\d+$/.test(value);
  };

  const url = isInteger(idOrUsername)
    ? `/api/v1/users/${idOrUsername}`
    : idOrUsername === "new"
    ? null
    : `/api/v1/users/username/${idOrUsername}`;

  const [user, setUser] = useFetchStateNo(
    emptyItem,
    url,
    jwt,
    setMessage,
    setVisible
  );

  const [user1, setUser1] = useFetchStateNo(
    emptyItem,
    url,
    jwt,
    setMessage,
    setVisible
  );

  const auths = useFetchData(`/api/v1/users/authorities`, jwt);

  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    if (name === "authority") {
      const auth = auths.find((a) => a.id === Number(value));
      setUser({ ...user, authority: auth });
    } else setUser({ ...user, [name]: value });
  }

  function handleSubmit(event) {
    event.preventDefault();

    fetch(`/api/v1/users${user.id ? `/${user.id}` : ""}`, {
      method: user.id ? "PUT" : "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.text().then((text) => (text ? JSON.parse(text) : {}));
      })
      .then((json) => {
        if (json.message) {
          setMessage(json.message);
          setVisible(true);
        } else {
          if(jwt_decode(jwt).sub !== user1.username) {
            window.location.href = "/users";
          }else{
            sendLogoutRequest();
          }
        }
      })
      .catch((error) => {
        setMessage(error.message);
        setVisible(true);
      });
  }

  const modal = getErrorModal(setVisible, visible, message);
  const authOptions = auths.map((auth) => (
    <option key={auth.id} value={auth.id}>
      {auth.authority}
    </option>
  ));


  return (
    <div className="auth-page-container">
      {<h2>{user.id ? "Edit User" : "Add User"}</h2>}
      {modal}
      <div className="auth-form-container">
        <Form onSubmit={handleSubmit}>
          <div className="custom-form-input">
            <Label for="username" className="custom-form-input-label">
              Username
            </Label>
            <Input
              type="text"
              required
              name="username"
              id="username"
              value={user.username || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="lastName" className="custom-form-input-label">
              Password
            </Label>
            <Input
              type="password"
              required
              name="password"
              id="password"
              value={user.password || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <Label for="authority" className="custom-form-input-label">
            Authority
          </Label>
          <div className="custom-form-input">
            {user.id ? (
              <Input
                type="select"
                disabled
                name="authority"
                id="authority"
                value={user.authority?.id || ""}
                onChange={handleChange}
                className="custom-input"
              >
                <option value="">None</option>
                {authOptions}
              </Input>
            ) : (
              <Input
                type="select"
                required
                name="authority"
                id="authority"
                value={user.authority?.id || ""}
                onChange={handleChange}
                className="custom-input"
              >
                <option value="">None</option>
                {authOptions}
              </Input>
            )}
          </div>
          <div className="custom-button-row">
            <Button size='lg' style={{ backgroundColor: 'darkblue', borderColor: 'darkblue' }}>Save</Button>
            <Link to={`/matches`}>
              <Button size='lg' style={{ backgroundColor: 'darkblue', borderColor: 'darkblue' }}>Cancel</Button>
            </Link>
          </div>
        </Form>
      </div>
    </div>
  );
}
