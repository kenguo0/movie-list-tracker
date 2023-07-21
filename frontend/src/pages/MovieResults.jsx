import { useLocation } from "react-router-dom";
import MovieCard from "../components/MovieCard";
import "../styles/movieResults.css";

export default function MovieResults() {
    const location = useLocation();

    return (
        <>
            <h2 className="results--header">{`Results for '${location.state && location.state.query}' : `}</h2>
            <div className="grid">{location.state && location.state.movieResults.map((movie) => <MovieCard key={movie.id} {...movie} />)}</div>
        </>
    );
}
