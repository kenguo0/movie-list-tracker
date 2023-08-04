import { useEffect, useState } from "react";
import MovieCard from "../components/MovieCard";
import "../styles/movieResults.css";

export default function Home({ apiURL }) {
    const [popularMovies, setPopularMovies] = useState([]);
    const [apiKey, setApiKey] = useState("");
    const fetchApiKey = async () => {
        try {
            const response = await fetch(`${apiURL}/api/movie/key`, {
                headers: {
                    "Content-Type": "application/json",
                },
                method: "get",
                credentials: "include",
            });
            if (response.headers.get("isAuthenticatedHeader") === "true") {
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
        fetchApiKey();
        if (apiKey !== "") {
            fetch("https://api.themoviedb.org/3/trending/movie/week", {
                headers: {
                    accept: "application/json",
                    Authorization: "Bearer ".concat(apiKey),
                },
            })
                .then((response) => response.json())
                .then((data) => {
                    setPopularMovies(data.results);
                });

            if (localStorage.getItem("genresData") === null) {
                fetch("https://api.themoviedb.org/3/genre/movie/list", {
                    headers: {
                        accept: "application/json",
                        Authorization: "Bearer ".concat(apiKey),
                    },
                })
                    .then((response) => response.json())
                    .then((data) => {
                        localStorage.setItem("genresData", JSON.stringify(data.genres));
                    });
            }
        }
    }, [apiKey]);

    return (
        <>
            <h1 className="results--header">Popular Movies</h1>
            <div className="grid">
                {popularMovies.map((movie) => (
                    <MovieCard key={movie.id} {...movie} />
                ))}
            </div>
        </>
    );
}
