import axios from "axios";

class BattleService {
    list() {
        return axios
            .get("/torneo/prueba")
            .then(response => {
                return response;
            });
    }
}

export default new BattleService();
