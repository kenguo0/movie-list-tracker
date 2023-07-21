import { useLocation } from "react-router-dom";
import "../styles/moviePage.css";
import ListActions from "../components/ListActions";

export default function Movie() {
    const location = useLocation();
    const movieInfo = location.state;

    return (
        <div className="movie--page" style={{ backgroundImage: `url(https://www.themoviedb.org/t/p/w1920_and_h1080_multi_faces/${movieInfo.backdrop_path})` }}>
            <div className="movie--container">
                <img className="movie--poster" src={`https://image.tmdb.org/t/p/w500/${movieInfo.poster_path}`} />
                <div className="movie--details">
                    <h1 className="movie--title">{`${movieInfo.title} (${movieInfo.release_year})`}</h1>
                    <h3 className="summary--header">Summary</h3>
                    <p className="summary">{movieInfo.summary}</p>
                    <p className="genres">Genres: </p>
                    <ListActions />
                </div>
            </div>
        </div>
    );
}
