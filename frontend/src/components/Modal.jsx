import "../styles/modal.css";
import { useNavigate } from "react-router-dom";
import checkmark from "../images/checkmark.png";

export default function Modal({ toggleModal }) {
    const navigate = useNavigate();
    return (
        <div>
            <div className="modal">
                <div className="modal-content">
                    <img src={checkmark} className="checkmark" />
                    <div className="close">
                        <span className="close--button" onClick={() => toggleModal(false)}>
                            &times;
                        </span>
                    </div>
                    <h1 className="register--message">Registration Successful</h1>

                    <button
                        className="continue--button"
                        onClick={() => {
                            toggleModal(false);
                            navigate("/login");
                        }}>
                        Continue to Sign In
                    </button>
                </div>
            </div>
        </div>
    );
}
