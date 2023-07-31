import "../styles/registerModal.css";
import { useNavigate } from "react-router-dom";
import checkmark from "../images/checkmark.png";

export default function RegisterModal({ toggleModal }) {
    const navigate = useNavigate();
    return (
        <div>
            <div className="modal">
                <div className="modal-content">
                    <div className="close">
                        <span className="close--button" onClick={() => toggleModal(false)}>
                            &times;
                        </span>
                    </div>
                    <img src={checkmark} className="checkmark" />

                    <h1 className="register--successMsg">Registration Successful</h1>

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
