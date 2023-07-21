export function setAuthenticationStatus(isAuthenticated) {
    localStorage.setItem("isAuthenticated", JSON.stringify(isAuthenticated));
}

export function getAuthenticationStatus() {
    const isAuthenticated = localStorage.getItem("isAuthenticated");
    return isAuthenticated ? JSON.parse(isAuthenticated) : false;
}

export function saveUsername(username) {
    localStorage.setItem("username", username);
}

export function getUsername() {
    const username = localStorage.getItem("username");
    return username ? username : false;
}
