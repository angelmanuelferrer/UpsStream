import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, ButtonGroup, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function SagasListAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [sagas, setSagas] = useFetchState(
    [],
    `/api/v1/saga`,
    jwt,
    setMessage,
    setVisible
  );
  const [alerts, setAlerts] = useState([]);

  const sagaList = sagas.map((saga) => {
    return (
      <tr key={saga.id}>
        <td>{saga.name}</td>
        <td>
          <ButtonGroup>
            <Button
              size="sm"
              color="primary"
              aria-label={"edit-" + saga.id}
              tag={Link}
              to={"/saga/" + saga.id}
            >
              Edit
            </Button>
            <Button
              size="sm"
              color="danger"
              aria-label={"delete-" + sagas.id}
              onClick={() =>
                deleteFromList(
                  `/api/v1/saga/${saga.id}`,
                  saga.id,
                  [sagas, setSagas],
                  [alerts, setAlerts],
                  setMessage,
                  setVisible
                )
              }
            >
              Delete
            </Button>
          </ButtonGroup>
        </td>
      </tr>
    );
  });
  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="admin-page-container">
      <h1 className="text-center">Sagas</h1>
      {alerts.map((a) => a.alert)}
      {modal}
      <Button color="success" tag={Link} to="/saga/new">
        Add Saga
      </Button>
      <div>
        <Table aria-label="sagas" className="mt-4">
          <thead>
            <tr>
              <th>Name</th>
            </tr>
          </thead>
          <tbody>{sagaList}</tbody>
        </Table>
      </div>
    </div>
  );
}
