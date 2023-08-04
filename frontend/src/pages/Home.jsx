import { useEffect, useState } from "react";
import MovieCard from "../components/MovieCard";
import "../styles/movieResults.css";

export default function Home({ apiKey }) {
    const [popularMovies, setPopularMovies] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        if (apiKey) {
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
            setIsLoading(false);
        }
    }, [apiKey]);

    return (
        <>
            {isLoading ? (
                <p>Loading...</p>
            ) : (
                <>
                    {" "}
                    <h1 className="results--header">Popular Movies</h1>
                    <div className="grid">
                        {popularMovies.map((movie) => (
                            <MovieCard key={movie.id} {...movie} />
                        ))}
                    </div>
                </>
            )}
        </>
    );
}
