import no_Poster from "../images/no-poster.png";
import { Link } from "react-router-dom";

export default function MovieCard({ title, poster_path, release_date, overview, id, backdrop_path }) {
    const tmdbImgPath = "https://image.tmdb.org/t/p/w500/";
    const release_year = release_date.substring(0, 4);
    const movieInfo = {
        title: title,
        summary: overview,
        release_year: release_year,
        backdrop_path: backdrop_path,
        poster_path: poster_path,
    };

    return (
        <Link to={`/movie/${id}`} state={movieInfo}>
            <section className="card">
                <img src={poster_path ? tmdbImgPath + poster_path : no_Poster} />
                <div className="title">
                    <h3>{title}</h3>
                </div>
            </section>
        </Link>
    );
}
