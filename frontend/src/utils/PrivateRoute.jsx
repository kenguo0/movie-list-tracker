import { Navigate, Outlet } from "react-router-dom";
import { getAuthenticationStatus } from "./authLocalStorage";
const PrivateRoute = () => {
    const isAuthenticated = getAuthenticationStatus();

    // check isAuthenticated state from AuthContext and redirect accordingly
    return isAuthenticated ? <Outlet /> : <Navigate to="/login" />;
};

export default PrivateRoute;
