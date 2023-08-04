import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import MyLists from "./pages/MyLists";
import "./App.css";
import Login from "./pages/Login";
import PrivateRoute from "./utils/PrivateRoute";
import Navbar from "./components/Navbar";
import { useState, useEffect } from "react";
import { getAuthenticationStatus } from "./utils/authLocalStorage";
import Register from "./pages/Register";
import MovieResults from "./pages/MovieResults";
import Cover from "./pages/Cover";
import Movie from "./pages/Movie";

export default function App() {
    const [loggedIn, setLoggedIn] = useState(getAuthenticationStatus());
    const [apiKey, setApiKey] = useState("");
    const API_URL = "https://movie-list-tracker.up.railway.app";

    const verifyToken = () => {
        fetch(`${API_URL}/api/auth/verify`, {
            headers: {
                "Content-Type": "application/json",
            },
            method: "get",
            credentials: "include",
        }).then((response) => {
            if (response.status !== 200) {
                setLoggedIn(false);
                console.log(response.text());
            }
        });
    };

    const fetchApiKey = async () => {
        try {
            const response = await fetch(`${API_URL}/api/auth/verify`);
            if (response.ok) {
                const data = await response.text();
                setApiKey(data);
            } else {
                console.log("Could not get API key", response.text());
            }
        } catch (error) {
            console.error("Error fetching API key:", error);
        }
    };

    useEffect(() => {
        verifyToken();
        fetchApiKey();
    }, []);

    return (
        <>
            <Navbar isLoggedIn={loggedIn} onLogout={() => setLoggedIn(false)} apiKey={apiKey} />
            <Routes>
                <Route path="/" element={<Cover />} />
                <Route path="/register" element={<Register apiURL={API_URL} />} />
                <Route path="/login" element={<Login onLogin={() => setLoggedIn(true)} apiURL={API_URL} />} />
                <Route element={<PrivateRoute />}>
                    <Route element={<Home apiURL={API_URL} />} path="/home" />
                    <Route element={<MyLists apiURL={API_URL} />} path="/mylists" />
                    <Route element={<MovieResults />} path="/results" />
                    <Route element={<Movie apiURL={API_URL} />} path="/movie/:tmdbID" />
                </Route>
            </Routes>
        </>
    );
}
