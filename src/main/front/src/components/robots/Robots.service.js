import axios from "axios";

class RobotsService {

    lista(idEquipo) {
        return axios
            .get("/robot/verRobots", {params: {"idEquipo": idEquipo}})
            .then(respuesta => {
                return respuesta
            });
    }

    cargarRobot(robot) {
        return axios
            .post("robot/cargarRobot", robot, {headers: {"Content-Type": "multipart/form-data"}})
            .then(response => {
                this.guardarRobot(response.data)
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
}

export default new RobotsService();