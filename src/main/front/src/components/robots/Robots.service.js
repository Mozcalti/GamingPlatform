import axios from "axios";

class RobotsService {

    lista(idParticipante) {
        return axios
            .get("/robot/verRobots", {params: {"idParticipante": idParticipante}})
            .then(respuesta => {
                return respuesta
            });
    }

    cargarRobot(robot) {
        return axios
            .post("robot/cargarRobot", robot, {headers: {"Content-Type": "multipart/form-data"}})
            .then(response => {
                return response;
            });
    }

    guardarRobot(robot) {
        return axios
            .post("robot/guardarRobot", robot)
            .then(response => {
                return response;
            });
    }


    eliminarRobot(idRobot) {
        return axios
            .delete("robot/eliminarRobot",{params: {"idRobot": idRobot}})
            .then(response => {
                return response;
            });
    }

    seleccionarRobot(robot) {
        return axios
            .put("robot/seleccionarRobot",robot)
            .then(response => {
                return response;
            });
    }

    getPartiticpanteByCorreo(correo) {
        return axios
            .get("/participante/correo", {params: {"correo": correo}})
            .then(respuesta => {
                return respuesta
            });
    }

}

export default new RobotsService();