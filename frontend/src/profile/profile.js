import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import "./profile.css";

function EditableList({ isEditing, options, property, profile, setProfile }) {
  const [selectedOption, setSelectedOption] = useState("");
  const selected = profile[property];

  const handleAdd = () => {
    if (selectedOption && selectedOption !== "" && !selected.includes(selectedOption)) {
      const copyProfile = { ...profile };
      const copySelected = [...selected];
      copySelected.push(selectedOption);
      copyProfile[property] = copySelected;
      setProfile(copyProfile);
      setSelectedOption("");
    }
  };

  return (
    <div className="editable-list">
      <ul className="editable-list__items">
        {selected.map((item, idx) => (
          <li key={idx} className="editable-list__item">{item.name}</li>
        ))}
      </ul>

      {isEditing && (
        <div className="editable-list__controls" style={{ marginTop: "8px" }}>
          <select
            className="editable-list__select"
            value={selectedOption.id || ""}
            onChange={(e) => {
              const option = e.target.value;
              setSelectedOption(options.find(o => `${o.id}` === option));
            }}
          >
            <option value="">-- Choose an option --</option>
            {options
              .filter((opt) => !selected.some(sel => sel.id === opt.id))
              .map((opt) => (
                <option key={opt.id} value={opt.id}>
                  {opt.name}
                </option>
              ))}
          </select>
          <button className="editable-list__button" type="button" onClick={handleAdd}>
            Add
          </button>
        </div>
      )}
    </div>
  );
}

export default function ProfilePage() {
  const { username } = useParams();
  const [profile, setProfile] = useState({
    favoriteGenres: [],
    favoritePlatforms: [],
    favoriteSagas: [],
    occasionalPlayer: false,
  });
  const [error, setError] = useState(null);
  const jwt = tokenService.getLocalAccessToken();
  const isEditing = username === tokenService.getUser().username;
  const [genres, setGenres] = useFetchState([], '/api/v1/gender', jwt, null, null);
  const [sagas, setSagas] = useFetchState([], '/api/v1/saga', jwt, null, null);
  const [platform, setPlatform] = useFetchState([], '/api/v1/platform', jwt, null, null);
  const [statistics, setStatistics] = useFetchState([], `/api/profiles/by-username/statistics/${username}`, jwt, null, null);
  useEffect(() => {
    fetch(`/api/profiles/by-username/${username}`, { headers: { Authorization: `Bearer ${jwt}` } })
      .then(res => res.json())
      .then((data) => {
        setProfile(data);
      })
      .catch((e) => setError(e.message));
  }, [username]);

  function handleChange(e) {
    const { name, value } = e.target;
    setProfile((f) => ({ ...f, [name]: value }));
  }

  async function handleSave() {
    setError(null);
    const updated = await fetch(`/api/profiles`, {
      method: "PUT",
      headers: { "Content-Type": "application/json", Authorization: `Bearer ${jwt}` },
      body: JSON.stringify(profile),
    })
      .then(res => res.json())
      .then(json => {
        if (json.message) {
          window.alert(json.message);
        } else {
          setProfile(json);
        }
      })
      .catch(err => window.alert(err));
  }

  if (error) {
    return <p className="profile-page__error">Error: {error}</p>;
  }
  if (!profile) {
    return <p className="profile-page__loading">Loading profile…</p>;
  }

  return (
    <div className="profile-page">
      <h1 className="profile-page__title">Player Profile</h1>

      {/* Avatar */}
      {profile.avatarUrl && (
        <img
          src={profile.avatarUrl}
          alt="avatar"
          className="profile-page__avatar"
        />
      )}

      {/* Bio */}
      <section className="profile-page__section profile-page__bio">
        <h3 className="profile-page__section-title">Bio</h3>
        {isEditing ? (
          <textarea
            className="profile-page__textarea"
            name="bio"
            value={profile.bio}
            onChange={handleChange}
            rows={4}
          />
        ) : (
          <p className="profile-page__text">{profile.bio || ""}</p>
        )}
      </section>

      {/* Location */}
      <section className="profile-page__section profile-page__location">
        <h3 className="profile-page__section-title">Location</h3>
        {isEditing ? (
          <input
            className="profile-page__input"
            type="text"
            name="location"
            value={profile.location}
            onChange={handleChange}
            required
          />
        ) : (
          <p className="profile-page__text">{profile.location || ""}</p>
        )}
      </section>

      {/* Birth date */}
      <section className="profile-page__section profile-page__birthdate">
        <h3 className="profile-page__section-title">Birth date</h3>
        {isEditing ? (
          <input
            className="profile-page__input"
            type="date"
            name="birthDate"
            value={profile.birthDate}
            onChange={handleChange}
          />
        ) : (
          <p className="profile-page__text">{profile.birthDate || "yyyy-MM-dd"}</p>
        )}
      </section>

      {/* Player type */}
      <section className="profile-page__section profile-page__player-type">
        <h3 className="profile-page__section-title">Player type</h3>
        {isEditing ? (
          <div className="toggle-switch">
            <label className="switch">
              <input
                type="checkbox"
                checked={profile.occasionalPlayer || false}
                onChange={(e) =>
                  setProfile((prev) => ({
                    ...prev,
                    occasionalPlayer: e.target.checked,
                  }))
                }
              />
              <span className="slider"></span>
            </label>
            <span className="toggle-label">
              {profile.occasionalPlayer ? "Casual player" : "Hardcore player"}
            </span>
          </div>
        ) : (
          <p className="profile-page__text">
            {profile.occasionalPlayer ? "Casual player" : "Hardcore player"}
          </p>
        )}
      </section>

      {isEditing && (
        <section className="profile-page__section profile-page__avatar-url">
          <h3 className="profile-page__section-title">Avatar URL</h3>
          <input
            className="profile-page__input"
            type="text"
            name="avatarUrl"
            value={profile.avatarUrl}
            onChange={handleChange}
          />
        </section>
      )}

      {/* Favorites */}
      <section className="profile-page__section profile-page__favorites">
        <h3 className="profile-page__section-title">Favorite genres</h3>
        <EditableList
          isEditing={isEditing}
          property={"favoriteGenres"}
          profile={profile}
          setProfile={setProfile}
          options={genres}
        />
      </section>

      <section className="profile-page__section profile-page__favorites">
        <h3 className="profile-page__section-title">Favorite platforms</h3>
        <EditableList
          isEditing={isEditing}
          property={"favoritePlatforms"}
          profile={profile}
          setProfile={setProfile}
          options={platform}
        />
      </section>

      <section className="profile-page__section profile-page__favorites">
        <h3 className="profile-page__section-title">Favorite sagas</h3>
        <EditableList
          isEditing={isEditing}
          property={"favoriteSagas"}
          profile={profile}
          setProfile={setProfile}
          options={sagas}
        />
      </section>
      <section className="profile-page__section profile-page__avatar-url">
          <h3 className="profile-page__section-title">Average Time (min)</h3>
          <input
            className="profile-page__input"
            type="number"
            name="avgTime (s)"
            value={statistics.avg/60}
            readOnly={true}
          />
        </section>
        <section className="profile-page__section profile-page__avatar-url">
          <h3 className="profile-page__section-title">Sum Time (min)</h3>
          <input
            className="profile-page__input"
            type="number"
            name="sumTime"
            value={statistics.sum/60}
            readOnly={true}
          />
        </section>

      {/* Buttons */}
      {isEditing && (
        <div className="profile-page__actions" style={{ marginTop: 20 }}>
          <button className="profile-page__button" onClick={handleSave}>
            Save
          </button>
        </div>
      )}
    </div>
  );
}