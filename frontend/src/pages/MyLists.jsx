/* eslint-disable react/no-unescaped-entities */
import { getUsername } from "../utils/authLocalStorage";
import MovieCard from "../components/MovieCard";
import { useEffect, useState } from "react";
import "../styles/movieResults.css";

export default function MyLists() {
    const [movieList, setMovieList] = useState([]);
    const [filteredMovieList, setFilteredMovieList] = useState([]);
    const [genreFilter, setGenreFilter] = useState("");
    const [genreNames, setGenreNames] = useState([]);

    // show Watched list on render
    useEffect(() => {
        // setting genre names for filter dropdown
        const names = getGenreNamesFromLocalStorage();
        setGenreNames(names);

        try {
            fetch("/api/movie/getUserWatchedMovies", {
                method: "get",
            })
                .then((response) => response.json())
                .then((data) => {
                    setMovieList(data);
                });
        } catch (error) {
            console.error("Error fetching API key:", error);
        }
    }, []);

    const getGenreNamesFromLocalStorage = () => {
        const genreMap = localStorage.getItem("genresData");
        if (genreMap) {
            const genreMapString = JSON.parse(genreMap);
            return Object.values(genreMapString).map((genre) => genre.name);
        }
        return [];
    };

    const handleListSelect = (e) => {
        const list = e.target.value;
        try {
            fetch(`/api/movie/getUser${list}Movies`, {
                method: "get",
            })
                .then((response) => response.json())
                .then((data) => {
                    setMovieList(data);
                });
        } catch (error) {
            console.error("Error fetching user's list:", error);
        }
        setGenreFilter("");
    };

    const handleGenreSelect = (e) => {
        const genre = e.target.value;
        setGenreFilter(genre);
        const filteredMovies = movieList.filter((movie) => movie.genres.includes(genre));
        setFilteredMovieList(filteredMovies);
    };

    return (
        <>
            <h1 className="list--header">{`${getUsername()}'s`} Lists</h1>
            <div>
                <select className="select--userList" onChange={handleListSelect}>
                    <option value="Watched">Watched</option>
                    <option value="Watchlist">Watchlist</option>
                </select>

                <select className="select--genre" onChange={handleGenreSelect} value={genreFilter}>
                    <option>Filter by genre</option>

                    {genreNames.map((genreName, index) => (
                        <option key={index} value={genreName}>
                            {genreName}
                        </option>
                    ))}
                </select>
            </div>
            <div className="userList--grid">
                {genreFilter
                    ? filteredMovieList && filteredMovieList.map((movie) => <MovieCard key={movie.tmdbID} {...movie} />)
                    : movieList && movieList.map((movie) => <MovieCard key={movie.tmdbID} {...movie} />)}
            </div>
        </>
    );
}
