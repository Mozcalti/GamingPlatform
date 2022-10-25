import axios from "axios";


class ArchivoService{
    lista(texto){
        return axios
            .get("/participante/todos", {params: {"texto": texto}})
            .then(respuesta =>{
               return respuesta
            });
    }

    getParticipante(id){
        return axios
            .get("/participante/" + id)
            .then(respuesta =>{
                return respuesta
            });
    }

    guardarParticipante(participante){
        return axios
            .post("/participante/guardarParticipante", participante)
            .then(response => {
                return response;
            });

    }

    actualizarParticipante(participante){
        return axios
            .post("/participante/actualizarParticipante", participante)
            .then(response => {
                return response;
            });

    }


}

export default new ArchivoService();