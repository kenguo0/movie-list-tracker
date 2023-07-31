/* eslint-disable react/no-unescaped-entities */
import cover_background from "../images/cover-background.jpg";
import "../styles/coverPage.css";

export default function Cover() {
    return (
        <div className="cover--page" style={{ backgroundImage: `url(${cover_background}` }}>
            <h1 className="cover--title">
                Build your personal <span>WatchVerse</span>, one movie at a time
            </h1>
            <p className="cover--message"> Easily keep track of the movies you've watched and those you can't wait to see!</p>
        </div>
    );
}
