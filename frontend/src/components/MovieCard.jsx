import no_Poster from "../images/no-poster.png";
import { Link } from "react-router-dom";

export default function MovieCard({ ...movieInfo }) {
    const tmdbImgPath = "https://image.tmdb.org/t/p/w500/";
    const genreMap = localStorage.getItem("genresData");
    const genresData = JSON.parse(genreMap);
    let movie;

    // Function to get genre names from genre IDs
    function getGenreNames() {
        const genreNames = [];
        movieInfo.genre_ids.forEach((id) => {
            const genre = genresData.find((genre) => genre.id === id);
            if (genre) {
                genreNames.push(genre.name);
            }
        });

        return genreNames;
    }

    // added logic to handle different variable names from TMDB and backend api
    if (movieInfo.watchStatus == null) {
        movie = {
            tmdbID: movieInfo.id,
            title: movieInfo.title,
            summary: movieInfo.overview,
            posterPath: movieInfo.poster_path,
            backdropPath: movieInfo.backdrop_path,
            releaseYear: movieInfo.release_date.substring(0, 4),
            genres: getGenreNames(),
        };
    } else {
        movie = {
            tmdbID: movieInfo.tmdbID,
            title: movieInfo.title,
            summary: movieInfo.summary,
            posterPath: movieInfo.posterPath,
            backdropPath: movieInfo.backdropPath,
            releaseYear: movieInfo.releaseYear,
            genres: movieInfo.genres,
            watchStatus: movieInfo.watchStatus,
            rating: movieInfo.rating,
        };
    }

    const setDefaultPosterSrc = (e) => {
        e.target.src = no_Poster;
    };

    return (
        <Link to={`/movie/${movie.tmdbID}`} state={movie}>
            <section className="card" style={movieInfo.watchStatus != null ? { maxWidth: "300px" } : null}>
                <img src={movie.posterPath ? tmdbImgPath + movie.posterPath : no_Poster} onError={setDefaultPosterSrc} />
                <div className="title">
                    <h3>{movieInfo.title}</h3>
                </div>
            </section>
        </Link>
    );
}
