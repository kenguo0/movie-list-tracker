/* eslint-disable react/no-unescaped-entities */
import { getUsername } from "../utils/authLocalStorage";
import "../styles/dropdown.css";
import MovieCard from "../components/MovieCard";
import { useEffect, useState } from "react";

export default function MyLists() {
    const [movieList, setMovieList] = useState([]);

    // TO-DO: get list of movies belonging to user and render MovieCards based on response
    useEffect(() => {
        try {
            fetch("/api/movie/getAllUserMovies", {
                headers: {
                    "Content-Type": "application/json",
                },
                method: "get",
            });
        } catch (error) {
            console.error("Error fetching API key:", error);
        }
    }, []);

    return (
        <>
            <h1>{`${getUsername()}'s`} Lists</h1>
            {/* <MovieResults /> */}
            <div>
                <select className="choose--list">
                    <option value="watched">Watched</option>
                    <option value="watch-list">Watchlist</option>
                </select>
            </div>
            <div className="grid">{movieList && movieList.map((movie) => <MovieCard key={movie.id} {...movie} />)}</div>
        </>
    );
}
