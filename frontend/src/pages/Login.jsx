/* eslint-disable react/no-unescaped-entities */
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { saveUsername, setAuthenticationStatus } from "../utils/authLocalStorage";
import "../styles/userForm.css";

export default function Login({ onLogin, apiURL }) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState();
    const navigate = useNavigate();

    function sendLoginRequest() {
        const reqBody = {
            username: username,
            password: password,
        };

        if (username === "") {
            setErrorMessage("Username is required");
            return;
        } else if (password === "") {
            setErrorMessage("Password is required");
            return;
        }

        fetch(`${apiURL}/api/auth/login`, {
            headers: {
                "Content-Type": "application/json",
            },
            method: "post",
            credentials: "include",
            body: JSON.stringify(reqBody),
        }).then((response) => {
            if (response.status === 200) {
                setAuthenticationStatus(true);
                saveUsername(response.headers.get("Username"));
                onLogin();
                navigate("/home");
            } else {
                setErrorMessage("Invalid credentials");
            }
        });
    }

    return (
        <div className="user--div">
            <form className="user--form">
                <span>Login</span>
                <div>
                    <label className="input">Username</label>
                    <input type="text" className="login--username" placeholder="Enter username" value={username} onChange={(e) => setUsername(e.target.value)} />
                </div>

                <div>
                    <label className="input">Password</label>
                    <input type="password" className="form--password" placeholder="Enter password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </div>
                {errorMessage && <div className="error"> {errorMessage} </div>}
                <button type="button" className="login--button" onClick={sendLoginRequest}>
                    Login
                </button>
                <div>
                    <p className="register--message">
                        Don't have an account? &nbsp;<Link to={"/register"}> Sign Up </Link>
                    </p>
                </div>
            </form>
        </div>
    );
}
