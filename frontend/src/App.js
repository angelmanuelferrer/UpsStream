import React from "react";
import { Route, Routes } from "react-router-dom";
import jwt_decode from "jwt-decode";
import { ErrorBoundary } from "react-error-boundary";
import AppNavbar from "./AppNavbar";
import Home from "./home";
import PrivateRoute from "./privateRoute";
import Register from "./auth/register";
import Login from "./auth/login";
import Logout from "./auth/logout";
import PlanList from "./public/plan";
import tokenService from "./services/token.service";
import UserListAdmin from "./admin/users/UserListAdmin";
import UserEditAdmin from "./admin/users/UserEditAdmin";
import SwaggerDocs from "./public/swagger";
import DeveloperList from "./developers";
import AchievementList from "./achievements/achievementList";
import AchievementEdit from "./achievements/achievementEdit";
import MatchList from "./match/matchList";
import MatchEdit from "./match/matchEdit";
import MatchJoin from "./match/matchJoin"; // Importa el componente
import FriendsList from "./friends/FriendList";
import PlayerList from "./player/playerList";
import BoardList from "./board/boardList";
import StatisticsList from "./statistics/StatisticList";
import ProfilePage from './profile/profile'
import SagaEditAdmin from "./admin/sagas/SagaEditAdmin";
import SagasListAdmin from "./admin/sagas/SagasListAdmin";
import PlatformEditAdmin from "./admin/platforms/PlatformEditAdmin";
import PlatformsListAdmin from "./admin/platforms/PlatformsListAdmin";
import GenderEditAdmin from "./admin/genders/GenderEditAdmin";
import GendersListAdmin from "./admin/genders/GendersListAdmin";
function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert">
      <p>Something went wrong:</p>
      <pre>{error.message}</pre>
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  );
}

function App() {
  const jwt = tokenService.getLocalAccessToken();
  let roles = [];
  if (jwt) {
    roles = getRolesFromJWT(jwt);
  }

  function getRolesFromJWT(jwt) {
    return jwt_decode(jwt).authorities;
  }

  let adminRoutes = <></>;
  let ownerRoutes = <></>;
  let userRoutes = <></>;
  let publicRoutes = <></>;
  let vetRoutes = <></>;

  roles.forEach((role) => {
    if (role === "ADMIN") {
      adminRoutes = (
        <>
          <Route path="/users" exact={true} element={<PrivateRoute><UserListAdmin /></PrivateRoute>} />
          <Route path="/users/:username" exact={true} element={<PrivateRoute><UserEditAdmin /></PrivateRoute>} />
          <Route path="/saga" exact={true} element={<PrivateRoute><SagasListAdmin /></PrivateRoute>} />
          <Route path="/saga/:id" exact={true} element={<PrivateRoute><SagaEditAdmin /></PrivateRoute>} />
          <Route path="/platform" exact={true} element={<PrivateRoute><PlatformsListAdmin /></PrivateRoute>} />
          <Route path="/platform/:id" exact={true} element={<PrivateRoute><PlatformEditAdmin /></PrivateRoute>} />
          <Route path="/gender" exact={true} element={<PrivateRoute><GendersListAdmin /></PrivateRoute>} />
          <Route path="/gender/:id" exact={true} element={<PrivateRoute><GenderEditAdmin /></PrivateRoute>} />
          <Route path="/gender" exact={true} element={<PrivateRoute><GendersListAdmin /></PrivateRoute>} />
          <Route path="/gender/:id" exact={true} element={<PrivateRoute><GenderEditAdmin /></PrivateRoute>} />
          <Route path="/achievements/" exact={true} element={<PrivateRoute><AchievementList /></PrivateRoute>} />
          <Route path="/achievements/:achievementId" exact={true} element={<PrivateRoute><AchievementEdit /></PrivateRoute>} />
          <Route path="/statistics" element={<StatisticsList/>} />

        </>
      );
    }
    if (role === "PLAYER") {
      ownerRoutes = (
        <>
          <Route path="/users/:username" exact={true} element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
          <Route path="/achievements/" exact={true} element={<PrivateRoute><AchievementList /></PrivateRoute>} />
        </>
      );
    }
  });

  if (!jwt) {
    publicRoutes = (
      <>
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
      </>
    );
  } else {
    userRoutes = (
      <>
        <Route path="/developers" element={<DeveloperList />} />
        <Route path="/matches" element={<PrivateRoute><MatchList /></PrivateRoute>} />
        <Route path="/matches/:matchId" exact={true} element={<PrivateRoute><MatchEdit /></PrivateRoute>} />
        <Route path="/matches/:matchId/join" element={<PrivateRoute><MatchJoin /></PrivateRoute>} />
        <Route path="/matches/:matchId/players" element={<PrivateRoute><PlayerList /></PrivateRoute>} />
        <Route path="/board/:boardId" element={<PrivateRoute><BoardList /></PrivateRoute>} />
        <Route path="/logout" element={<Logout />} />
        <Route path="/login" element={<Login />} />
        <Route path="/friends" element={<FriendsList/>} />
        <Route path="/statistics" element={<StatisticsList/>} />
      </>
    );
  }

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback}>
        <AppNavbar />
        <Routes>
          <Route path="/" exact={true} element={<Home />} />
          <Route path="/plans" element={<PlanList />} />
          <Route path="/docs" element={<SwaggerDocs />} />
          {publicRoutes}
          {userRoutes}
          {adminRoutes}
          {ownerRoutes}
          {vetRoutes}
        </Routes>
      </ErrorBoundary>
    </div>
  );
}

export default App;



