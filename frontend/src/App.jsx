//import { useContext } from "react";
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

    const verifyToken = () => {
        fetch("/api/auth/verify").then((response) => {
            if (response.status !== 200) {
                setLoggedIn(false);
                console.log(response.text());
            }
        });
    };

    const fetchApiKey = async () => {
        try {
            const response = await fetch("/api/movie/key");
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
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login onLogin={() => setLoggedIn(true)} />} />
                <Route element={<PrivateRoute />}>
                    <Route element={<Home apiKey={apiKey} />} path="/home" />
                    <Route element={<MyLists />} path="/mylists" />
                    <Route element={<MovieResults />} path="/results" />
                    <Route element={<Movie />} path="/movie/:tmdbID" />
                </Route>
            </Routes>
        </>
    );
}
