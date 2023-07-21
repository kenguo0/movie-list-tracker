export default function ListActions() {
    // make call to database to check if movie belongs in list, then decide to show highlighter switch accordingly

    return (
        <div>
            <div className="user--inputs">
                <div className="toggle--button">
                    <input id="watched" type="radio" name="switch" />
                    <input id="watchlist" type="radio" name="switch" />
                    <label htmlFor="watched">Watched &#9989;</label>
                    <label htmlFor="watchlist">Watchlist &#128083;</label>
                    <span className="highlighter"></span>
                </div>

                <p className="rating">Rating:</p>
            </div>
        </div>
    );
}
