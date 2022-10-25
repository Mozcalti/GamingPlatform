import axios from "axios";

class InstitucionesService {

    lista(texto, indice){
        return axios
            .get("/institucion/todas", {params: {"texto": texto, "indice": indice}})
            .then(respuesta =>{
                return respuesta
            });
    }

    getInstitucion(id){
        return axios
            .get("/institucion/" + id)
            .then(respuesta =>{
                return respuesta
            });
    }
}

export default new InstitucionesService();