import React, { useState, useEffect } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";
import logo from '../src/static/images/upstream.png';

function AppNavbar() {
    const [roles, setRoles] = useState([]);
    const [username, setUsername] = useState("");
    const jwt = tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);

    const toggleNavbar = () => setCollapsed(!collapsed);

    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
            setUsername(jwt_decode(jwt).sub);
        }
    }, [jwt])

    let adminLinks = <></>;
    let ownerLinks = <></>;
    let userLinks = <></>;
    let userLogout = <></>;
    let publicLinks = <></>;

    roles.forEach((role) => {
        if (role === "ADMIN") {
            adminLinks = (
                <>                    
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/users">Users</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/saga">Sagas</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/platform">Platforms</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/gender">Genders</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/achievements">Achievements</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/developers">Developers</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/statistics">Statistics</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/friends"> Social</NavLink>
                    </NavItem>

                </>
            )
        }  
        if (role === "PLAYER") {
            ownerLinks = (
                <>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/achievements">Achievements</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/developers">Developers</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/statistics">Statistics</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "black" }} tag={Link} to="/friends"> Social</NavLink>
                    </NavItem>
                </>
           )
        }      
    })

    

    if (!jwt) {
        publicLinks = (
            <>
                <NavItem>
                    <NavLink style={{ color: "black" }} id="docs" tag={Link} to="/docs">Docs</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "black" }} id="register" tag={Link} to="/register">Register</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "black" }} id="login" tag={Link} to="/login">Login</NavLink>
                </NavItem>
            </>
        )
    } else {
        userLinks = (
            <>
                
            </>
        )
        userLogout = (
            <>
                <NavItem>
                    <NavLink style={{ color: "black" }} id="docs" tag={Link} to="/docs">Docs</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "black" }} id="users" tag={Link} to={`/users/${username}`}>{username}</NavLink>
                </NavItem>
                <NavItem className="d-flex">
                    <NavLink style={{ color: "black" }} id="logout" tag={Link} to="/logout">Logout</NavLink>
                </NavItem>
                <NavItem>
                    
                </NavItem>
            </>
        )

    }

    return (
        <div>
            <Navbar expand="md" style={{ backgroundColor: "white" }}>
                <NavbarBrand href="/">
                    <img src= {logo} style={{ height: 40, width: 220 }} />
                </NavbarBrand>
                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="me-auto mb-2 mb-lg-0" navbar>
                        {userLinks}
                        {adminLinks}
                        {ownerLinks}
                    </Nav>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {publicLinks}
                        {userLogout}
                    </Nav>
                </Collapse>
            </Navbar>
        </div>
    );
}

export default AppNavbar;