import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, ButtonGroup, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function GendersListAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [genders, setGenders] = useFetchState(
    [],
    `/api/v1/gender`,
    jwt,
    setMessage,
    setVisible
  );
  const [alerts, setAlerts] = useState([]);

  const genderList = genders.map((gender) => {
    return (
      <tr key={gender.id}>
        <td>{gender.name}</td>
        <td>
          <ButtonGroup>
            <Button
              size="sm"
              color="primary"
              aria-label={"edit-" + gender.id}
              tag={Link}
              to={"/gender/" + gender.id}
            >
              Edit
            </Button>
            <Button
              size="sm"
              color="danger"
              aria-label={"delete-" + gender.id}
              onClick={() =>
                deleteFromList(
                  `/api/v1/gender/${gender.id}`,
                  gender.id,
                  [genders, setGenders],
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
      <h1 className="text-center">Genders</h1>
      {alerts.map((a) => a.alert)}
      {modal}
      <Button color="success" tag={Link} to="/gender/new">
        Add Genders
      </Button>
      <div>
        <Table aria-label="genders" className="mt-4">
          <thead>
            <tr>
              <th>Name</th>
            </tr>
          </thead>
          <tbody>{genderList}</tbody>
        </Table>
      </div>
    </div>
  );
}
