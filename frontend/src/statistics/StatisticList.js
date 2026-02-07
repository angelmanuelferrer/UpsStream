import React, { useState } from "react";
import { Table } from "reactstrap";
import useFetchState from "../util/useFetchState";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";

const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();
const currentUserId = currentUser ? currentUser.id : null; 

export default function StatisticsList() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [statistics] = useFetchState(
    [],
    `/api/v1/statistics`,
    jwt,
    setMessage,
    setVisible
  );

  // Filtrar estadísticas por usuario actual
  const filteredStatistics = statistics.filter(
    (s) => s.user?.id === currentUserId
  ); 

  // Renderizar las estadísticas filtradas
  const statisticsList = filteredStatistics.map((s) => (
    <tr key={s.id}>
      <td className="text-center">{s.matchesPlayed || 0}</td>
      <td className="text-center">{s.matchesWon || 0}</td>
      <td className="text-center">{s.matchesLost || 0}</td>
      <td className="text-center">{s.points || 0}</td>
    </tr>
  ));

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div>
      <div className="admin-page-container">
        <h1 className="text-center">My Statistics</h1>
        {modal}

        <div>
          <Table aria-label="statistics" className="mt-4">
            <thead>
              <tr>
                <th className="text-center">Games Played</th>
                <th className="text-center">Games Won</th>
                <th className="text-center">Games Lost</th>
                <th className="text-center">Points</th>
              </tr>
            </thead>
            <tbody>
              {statisticsList.length > 0 ? (
                statisticsList
              ) : (
                <tr>
                  <td className="text-center" colSpan="4">
                    No statistics available.
                  </td>
                </tr>
              )}
            </tbody>
          </Table>
        </div>
      </div>
    </div>
  );
}
