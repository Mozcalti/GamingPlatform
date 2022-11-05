import axios from "axios";

class AuthService {
    login(username, password) {
        return axios
            .post("/api/login", {
                username,
                password
            })
            .then(response => {

                if (response.headers["authorization"]) {
                    sessionStorage.setItem("token", response.headers["authorization"]);
                    sessionStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    logout() {
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("user");
    }

    register(username, email, password) {
        return axios.post("/api/signup", {
            username,
            email,
            password
        });
    }

    getCurrentUser() {
        return sessionStorage.getItem('token');
    }
}

export default new AuthService();
