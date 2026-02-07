import React from 'react';
import '../App.css';
import '../static/css/home/home.css';
import { Link } from "react-router-dom";
import tokenService from '../services/token.service';
import { Button } from 'reactstrap';

export default function Home() {
    const jwt = tokenService.getLocalAccessToken();

    return (
        <div className="home-page-container">
            {jwt && (
            <div className="hero-div">
                <div className="auth-button-intro">
                    <Link to="matches/new">
                        <Button size='lg' style={{ backgroundColor: 'darkblue', borderColor: 'darkblue' }}>CREATE GAME</Button>
                    </Link>          
                    <Link to="matches">
                        <Button size='lg' style={{ backgroundColor: 'darkblue', borderColor: 'darkblue' }}>JOIN GAME</Button>
                    </Link>
                </div>
            </div>
            )}
        </div>
    );
}