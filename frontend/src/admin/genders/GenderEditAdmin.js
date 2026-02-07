import React from 'react';
import { useEffect, useState } from 'react';
import { Button, Form, FormGroup, Label, Input } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authButton.css";
import { useNavigate, useParams } from 'react-router-dom';

const jwt = tokenService.getLocalAccessToken();

export default function GenderEditAdmin() {
    const { id } = useParams();
    const [gender, setGender] = useState({ name:""});
    const [message, setMessage] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        if(id=="new"){
            return ;
        }
        fetch(`/api/v1/gender/${id}`, {
            headers: {
                Authorization: `Bearer ${jwt}`,
            },
        })
            .then((res) => res.json())
            .then((data) => setGender(data))
            .catch((err) => { 
                console.error('Error fetching data', err ); 
                setMessage('Error fetching data');
            }); 
    }, [id]);


    const handleChange = (e) => {
        const { name, value } = e.target;
        setGender({ ...gender, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch(`/api/v1/gender${id==='new'?'':`/${id}`}`, {
            method: id==='new'?'POST':'PUT',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${jwt}`,
            },
            body: JSON.stringify(gender),
        })
            .then((res) => res.json())
            .then((data) => {
                setMessage(`gender ${id==='new'?'created':'updated'} successfully`);
                navigate('/gender');
            })
            .catch((err) => {
                setMessage(err.message);
            });
    };

    return (
        <div>
            <h2>Edit Gender</h2>
            <Form onSubmit={handleSubmit}>
                <Label for ="Name">Name</Label>
                <Input
                    type="text"
                    name="name"
                    id="name"
                    value={gender.name}
                    onChange={handleChange}
                    required
                    className='custom-input'
                />
                <Button className='auth-button' type="submit" color="primary">Save</Button>
                <Button className='auth-button' color="button" onClick={() => navigate('/gender')}>Cancel</Button>
            </Form>    
        </div>
    );
};

