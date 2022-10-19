import axios from "axios";

class UsuariosService {
    list() {
        return axios
            .get("/api/usuario")
            .then(response => {
                return response;
            });
    }

    add(usuario) {
        return axios
            .post("/api/usuario", usuario)
            .then(response => {
                return response;
            });
    }
}

export default new UsuariosService();
