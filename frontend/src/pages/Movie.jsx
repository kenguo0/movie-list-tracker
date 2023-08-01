import { useLocation } from "react-router-dom";
import "../styles/moviePage.css";
import ListActions from "../components/ListActions";
import { useEffect, useState } from "react";

export default function Movie({ apiURL }) {
    const location = useLocation();
    const movieInfo = location.state;
    const [movieWatchStatus, setMovieWatchStatus] = useState(null);
    const [movieRating, setMovieRating] = useState();

    useEffect(() => {
        if (movieInfo.watchStatus == null) {
            try {
                fetch(`${apiURL}/api/movie/getUserMovieDetails/${movieInfo.tmdbID}`, {
                    headers: {
                        Accept: "application/json",
                    },
                    method: "get",
                    credentials: "include",
                })
                    .then((response) => {
                        if (response.status === 404) {
                            return response.text().then((errorMessage) => {
                                setMovieWatchStatus(undefined);
                                setMovieRating("");
                                throw new Error(errorMessage);
                            });
                        }
                        return response.json();
                    })
                    .then((data) => {
                        setMovieWatchStatus(data.watchStatus);
                        setMovieRating(data.rating);
                    })
                    .catch(() => {
                        // don't need to do anything here
                        // error is only thrown to break out of fetch
                    });
            } catch (error) {
                console.error("Error getting user movie:", error);
            }
        } else {
            setMovieWatchStatus(movieInfo.watchStatus);
            setMovieRating(movieInfo.rating);
        }
    }, []);

    return (
        <div className="movie--page" style={{ backgroundImage: `url(https://www.themoviedb.org/t/p/w1920_and_h1080_multi_faces/${movieInfo.backdropPath})` }}>
            <div className="movie--container">
                <img className="movie--poster" src={`https://image.tmdb.org/t/p/w500/${movieInfo.posterPath}`} />
                <div className="movie--details">
                    <h1 className="movie--title">{`${movieInfo.title} (${movieInfo.releaseYear})`}</h1>
                    <h3 className="summary--header">Summary</h3>
                    <p className="summary">{movieInfo.summary}</p>
                    <p className="genres">
                        <span style={{ color: "#cea9ff" }}>Genres: </span>
                        {movieInfo.genres.join(", ")}
                    </p>
                    {movieWatchStatus !== null ? <ListActions movieDetails={movieInfo} watchStatus={movieWatchStatus} rating={movieRating} apiURL={apiURL} /> : null}
                </div>
            </div>
        </div>
    );
}
