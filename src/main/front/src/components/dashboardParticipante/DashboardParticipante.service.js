import axios from "axios";

class DashboardParticipanteService {

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

    listaResultadosGlobal(idEtapa, institucion) {
        return axios
            .get("/dashboads/consulta", {params: {"idEtapas": idEtapa, "idInstitucion": institucion}})
            .then(respuesta => {
                return respuesta
            });
    }
}

export default new DashboardParticipanteService();