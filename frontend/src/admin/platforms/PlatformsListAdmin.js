import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, ButtonGroup, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function PlatformsListAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [platforms, setPlatforms] = useFetchState(
    [],
    `/api/v1/platform`,
    jwt,
    setMessage,
    setVisible
  );
  const [alerts, setAlerts] = useState([]);

  const platformList = platforms.map((platform) => {
    return (
      <tr key={platform.id}>
        <td>{platform.name}</td>
        <td>
          <ButtonGroup>
            <Button
              size="sm"
              color="primary"
              aria-label={"edit-" + platform.id}
              tag={Link}
              to={"/platform/" + platform.id}
            >
              Edit
            </Button>
            <Button
              size="sm"
              color="danger"
              aria-label={"delete-" + platforms.id}
              onClick={() =>
                deleteFromList(
                  `/api/v1/platform/${platform.id}`,
                  platform.id,
                  [platforms, setPlatforms],
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
      <h1 className="text-center">platforms</h1>
      {alerts.map((a) => a.alert)}
      {modal}
      <Button color="success" tag={Link} to="/platform/new">
        Add platform
      </Button>
      <div>
        <Table aria-label="platforms" className="mt-4">
          <thead>
            <tr>
              <th>Name</th>
            </tr>
          </thead>
          <tbody>{platformList}</tbody>
        </Table>
      </div>
    </div>
  );
}
