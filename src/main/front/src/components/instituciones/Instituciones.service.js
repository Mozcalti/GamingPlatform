import axios from "axios";

class InstitucionesService {

    lista(texto, fecha,indice){
        return axios
            .get("/institucion/todas", {params: {"texto": texto, "fecha": fecha,"indice": indice}})
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

    validaExcelInstitucion(instituciones){
        return axios.post("institucion/cargarArchivo", instituciones, {headers: {"Content-Type": "multipart/form-data"}})
        .then(respuesta =>{
            this.guardarInstitucion(respuesta.data);
            return respuesta
        });
    }

    guardarInstitucion(guardar){
        return axios.post("/institucion/guardar",guardar)
        .then(response => {
            return response;
        });
    }
}

export default new InstitucionesService();