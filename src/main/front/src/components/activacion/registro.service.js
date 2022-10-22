import axios from "axios";

class RegistroService {
    activate(token, password) {
        return axios
            .post("/users/registrationConfirm", {
                token,
                password
            })
            .then(response => {
                return response;
            });
    }

    verify(token) {
        return axios
            .post(`/users/verifyToken?token=${token}`)
            .then(response => {
                return response;
            })
    }
}

export default new RegistroService();
