import axios from "axios";


class ParticipantesService {
    lista(texto, fecha) {
        return axios
            .get("/participante/todos", {params: {"texto": texto, "fecha":fecha}})
            .then(respuesta => {
                return respuesta
            });
    }

    getParticipante(id) {
        return axios
            .get("/participante/" + id)
            .then(respuesta => {
                return respuesta
            });
    }

    guardarParticipante(participante) {
        return axios
            .post("/participante/guardarParticipante", participante)
            .then(response => {
                return response;
            });

    }

    actualizarParticipante(participante) {
        return axios
            .post("/participante/actualizarParticipante", participante)
            .then(response => {
                return response;
            });

    }

    validaExcel(participante) {
        return axios
            .post("/participante/cargarArchivo", participante, {headers: {"Content-Type": "multipart/form-data"}})
            .then(response => {
                this.guardarListaParticipantes(response.data)
            });
    }

    guardarListaParticipantes(listaParticipantes) {
        return axios
            .post("/participante/guardar", listaParticipantes)
            .then(response => {
                return response;
            });
    }

    obtenerInstitucion(correo) {
        return axios
            .get("/participante/institucion", {params: {"correo": correo}})
            .then(response => {
                return response;
            });
    }
}

export default new ParticipantesService();