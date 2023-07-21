/* eslint-disable react/no-unescaped-entities */
import { useState } from "react";
import "../styles/userForm.css";
import Modal from "../components/Modal";

export default function Register() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [passwordRe, setPasswordRe] = useState("");
    const [errorMessage, setErrorMessage] = useState();
    const [showModal, setShowModal] = useState(false);

    function sendRegisterRequest() {
        const reqBody = {
            username: username,
            password: password,
            passwordRe: passwordRe,
        };

        if (username === "") {
            setErrorMessage("Username is required");
            return;
        } else if (username.length < 3 || username.length > 20) {
            setErrorMessage("Username must be between 3 and 20 characters.");
            return;
        } else if (password === "") {
            setErrorMessage("Password is required");
            return;
        } else if (password !== passwordRe) {
            setErrorMessage("Passwords do not match");
            return;
        }
        fetch("/api/auth/register", {
            headers: {
                "Content-Type": "application/json",
            },
            method: "post",
            body: JSON.stringify(reqBody),
        })
            .then((response) => {
                if (response.status === 200) {
                    console.log(response.text());
                    setShowModal(true);
                } else {
                    return response.text();
                }
            })
            .then((data) => {
                setErrorMessage(data);
            });
    }

    return (
        <>
            <div className="user--div">
                <form className="user--form">
                    <span>Register</span>
                    <div>
                        <label>Username</label>
                        <input type="text" className="register--username" placeholder="Enter username" value={username} onChange={(e) => setUsername(e.target.value)} />
                    </div>

                    <div>
                        <label>Password</label>
                        <input type="password" placeholder="Enter password" value={password} onChange={(e) => setPassword(e.target.value)} />
                    </div>

                    <div>
                        <label>Re-Enter Password</label>
                        <input type="password" className="form--password" placeholder="Re-Enter Password" value={passwordRe} onChange={(e) => setPasswordRe(e.target.value)} />
                    </div>
                    {errorMessage && <div className="error"> {errorMessage} </div>}
                    <button type="button" className="login--button" onClick={sendRegisterRequest}>
                        Register
                    </button>
                </form>
            </div>
            {showModal ? <Modal className="fade-up" toggleModal={setShowModal} /> : null}
        </>
    );
}
