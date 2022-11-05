import axios from "axios";

class DashboardService {

    obtenerEtapas(){
        return axios
            .get("/dashboads/obtenerEtapas", {params: {}})
            .then(respuesta => {
                return respuesta
            });
    }

    listaBatallasIndividuales(idEtapa) {
        return axios
            .get("/dashboads/verBatallasIndividuales", {params: {"idEtapa": idEtapa}})
            .then(respuesta => {
                return respuesta
            });
    }
}

export default new DashboardService();