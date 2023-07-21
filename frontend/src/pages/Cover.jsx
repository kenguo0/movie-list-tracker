import cover_background from "../images/cover-background.jpg";
import "../styles/coverPage.css";

export default function Cover() {
    return (
        <div className="cover--page" style={{ backgroundImage: `url(${cover_background}` }}>
            <p className="cover--message">Your personal movie universe journey awaits!</p>
        </div>
    );
}
