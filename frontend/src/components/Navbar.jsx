import { Link, useNavigate } from "react-router-dom";
import "../styles/navBar.css";
import movie_logo from "../images/movie-logo.png";
import { useState, useEffect } from "react";
import { setAuthenticationStatus } from "../utils/authLocalStorage";
import Search from "./Search";

export default function Navbar({ isLoggedIn, onLogout, apiKey }) {
    const [isIconClicked, setIsIconClicked] = useState(false);
    const navigate = useNavigate();

    const handleClick = () => {
        setIsIconClicked(!isIconClicked);
    };

    const handleLogin = () => {
        navigate("/login"); // moved to own function (causes infinite render when put directly in onClick)
    };

    const handleLogout = () => {
        navigate("/");
        onLogout();
        setAuthenticationStatus(false);
    };

    const closeNav = () => {
        setIsIconClicked(false);
    };

    useEffect(() => {
        if (isIconClicked) {
            document.body.style.overflow = "hidden";
        } else {
            document.body.style.overflow = "visible";
        }
    }, [isIconClicked]);

    return (
        <nav className="nav">
            <div className="logo--div">
                <img src={movie_logo} className="movie--logo" />
                <Link to={"/home"}> WatchVerse </Link>
            </div>
            {isLoggedIn ? (
                <div>
                    <div className="navigation--div">
                        <ul className={isIconClicked ? "nav--links active" : "nav--links"}>
                            <li>
                                <Search onSend={closeNav} apiKey={apiKey} />
                            </li>
                            <li>
                                <Link to={"/mylists"}>My Lists</Link>
                            </li>
                            <li>
                                <button className="auth--button" onClick={handleLogout}>
                                    LOG OUT
                                </button>
                            </li>
                        </ul>
                    </div>
                    <button className="mobile--button" onClick={handleClick}>
                        {isIconClicked ? <i className="fas fa-times fa-3x" /> : <i className="fas fa-bars fa-3x" />}
                    </button>
                </div>
            ) : (
                <div>
                    <button className="auth--button" onClick={handleLogin}>
                        SIGN IN
                    </button>
                </div>
            )}
        </nav>
    );
}
