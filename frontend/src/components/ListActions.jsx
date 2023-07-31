import { useState } from "react";

export default function ListActions({ movieDetails, watchStatus, rating }) {
    const [listType, setlistType] = useState(watchStatus);
    const [userRating, setUserRating] = useState(rating);
    const [addedToList, setAddedToList] = useState(watchStatus != null);
    const [showAddedMsg, setShowAddedMsg] = useState(false);

    const updateWatchedStatus = (e) => {
        // only call api to update watch status if the movie is not already added to list
        if (listType == null) {
            const userList = e.target.value;
            movieDetails.watchStatus = userList;
            setlistType(userList);
        } else {
            // call api to change watch status in database
            const movieWatchStatus = e.target.value;
            try {
                fetch(`/api/movie/updateWatchStatus/${movieDetails.tmdbID}`, {
                    headers: {
                        "Content-Type": "application/json",
                    },
                    method: "put",
                    body: movieWatchStatus,
                }).then((response) => {
                    if (response.status === 200) {
                        setlistType(movieWatchStatus);
                        console.log("changed movie to: ", movieWatchStatus);
                    } else {
                        console.log("error:", response.text);
                    }
                });
            } catch (error) {
                console.error("Error updating user movie watch status:", error);
            }
        }
    };

    const addMovieToList = () => {
        try {
            fetch(`/api/movie/addToList`, {
                headers: {
                    "Content-Type": "application/json",
                },
                method: "post",
                body: JSON.stringify(movieDetails),
            }).then((response) => {
                if (response.status === 200) {
                    console.log("success:", response.text);
                } else {
                    console.log("error:", response.text);
                }
            });
        } catch (error) {
            console.error("Error adding movie to user's list:", error);
        }
    };

    const removeFromList = () => {
        try {
            fetch(`/api/movie/removeFromList/${movieDetails.tmdbID}`, {
                headers: {
                    "Content-Type": "application/json",
                },
                method: "delete",
            }).then((response) => {
                if (response.status === 200) {
                    console.log("success:", response.text);
                } else {
                    console.log("error:", response.text);
                }
            });
        } catch (error) {
            console.error("Error removing movie from user's list:", error);
        }
    };

    const handleAdd = () => {
        setAddedToList(true);
        addMovieToList();
        setShowAddedMsg(true);
    };

    const handleRemove = () => {
        setlistType(null);
        setAddedToList(false);
        removeFromList();
    };

    const handleRatingSelect = (e) => {
        const rating = e.target.value;
        if (addedToList) {
            try {
                fetch(`/api/movie/updateRating/${movieDetails.tmdbID}`, {
                    headers: {
                        "Content-Type": "application/json",
                    },
                    method: "put",
                    body: e.target.value,
                }).then((response) => {
                    if (response.status === 200) {
                        console.log("success:", response.text);
                    } else {
                        console.log("error:", response.text);
                    }
                });
            } catch (error) {
                console.error("Error updating movie's rating:", error);
            }
        }
        movieDetails.rating = rating;
        setUserRating(e.target.value);
    };

    return (
        <div className="list--actions">
            <h6 style={{ visibility: listType == null ? "visible" : "hidden" }}>Choose a list to add to:</h6>
            <div className="user--inputs">
                <div className="user--buttons">
                    <div className="toggle--button">
                        <input id="watched" type="radio" name="switch" value="watched" checked={listType === "watched"} onChange={updateWatchedStatus} />
                        <input id="watchlist" type="radio" name="switch" value="watchlist" checked={listType === "watchlist"} onChange={updateWatchedStatus} />
                        <label htmlFor="watched">Watched &#9989;</label>
                        <label htmlFor="watchlist">Watchlist &#128083;</label>
                        {listType != null ? <span className="highlighter"></span> : null}
                    </div>
                    {addedToList ? (
                        <button className="remove--button" onClick={handleRemove}>
                            <label>
                                Remove
                                <br />
                                from list
                            </label>
                        </button>
                    ) : null}
                </div>
                {listType === "watched" ? (
                    <div className="rating--div">
                        <p className="rating--text">Your Rating:</p>
                        <select className="select--rating" onChange={handleRatingSelect} value={userRating}>
                            {userRating == "" ? (
                                <option hidden="true" value="select">
                                    Select Rating
                                </option>
                            ) : null}

                            {[...Array(10)]
                                // map element to value 1-10
                                .map((_, i) => i + 1)
                                // map value to option element
                                .map((i) => (
                                    <option className="rating--options" key={i} value={i}>
                                        {i}
                                    </option>
                                ))}
                        </select>
                    </div>
                ) : null}

                {listType != null && !addedToList ? (
                    <button className="add--button" onClick={handleAdd}>
                        <label>Add to list</label>
                    </button>
                ) : null}

                {showAddedMsg ? <p className="added--message">Added to list!</p> : null}
            </div>
        </div>
    );
}
