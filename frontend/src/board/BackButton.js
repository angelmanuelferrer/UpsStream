import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../static/css/board/backButton.css';

export default function BackButton() {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate('/matches');
    };

    return (
        <button className ="back-button" onClick={handleClick}>
            Back to the lobby
        </button>
    );
}