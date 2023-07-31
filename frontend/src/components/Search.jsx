import { useState } from "react";
import { useNavigate } from "react-router-dom";
import searchIcon from "../images/search-icon.png";

export default function Search({ onSend, apiKey }) {
    const [searchQuery, setSearchQuery] = useState("");
    const navigate = useNavigate();

    const sendResults = (e) => {
        e.preventDefault();
        fetch(`https://api.themoviedb.org/3/search/movie?query=${encodeURIComponent(searchQuery)}`, {
            headers: {
                accept: "application/json",
                Authorization: "Bearer ".concat(apiKey),
            },
        })
            .then((response) => response.json())
            .then((data) => {
                navigate("/results", { state: { movieResults: data.results, query: searchQuery } });
                onSend();
                setSearchQuery("");
            });
    };

    return (
        <form className="search" onSubmit={sendResults}>
            <input type="text" placeholder="Search for movies..." className="search--input" value={searchQuery} onChange={(e) => setSearchQuery(e.target.value)} />
            <button className="search--button" onClick={sendResults}>
                <img className="search--image" src={searchIcon} />
            </button>
        </form>
    );
}
