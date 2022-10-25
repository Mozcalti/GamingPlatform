import axios from "axios";

class InstitucionService{

    getInstituciones(){
        return axios
            .get("/institucion/")
            .then(respuesta =>{
                return respuesta
            });
    }
}

export default new InstitucionService();