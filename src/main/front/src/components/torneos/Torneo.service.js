import axios from "axios";

class TorneoService {
    guardarTorneo(torneo) {
        return axios
            .post("/torneo/guardar", torneo)
            .then(response => {
                return response;
            });
    }

    consultarTorneos() {
        return axios
            .get("torneo/consultar")
            .then(response => {
                return response;
            })
    }

    elimiarTorneo(idTorneo) {
        return axios
            .delete("torneo/eliminar/" + idTorneo)
    }

    gettorneo(id) {
        return axios
            .get("/torneo/consultar/" + id)
            .then(respuesta => {
                return respuesta
            });
    }

}

export default new TorneoService();