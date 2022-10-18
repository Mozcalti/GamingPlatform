package com.mozcalti.gamingapp.model.torneos;

import com.mozcalti.gamingapp.model.TorneoHorasHabiles;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HoraHabilDTO {

    private Integer idHoraHabil;
    private String horaIniHabil;
    private String horaFinHabil;

    public HoraHabilDTO(TorneoHorasHabiles torneoHorasHabiles) {
        this.idHoraHabil = torneoHorasHabiles.getIdHoraHabil();
        this.horaIniHabil = torneoHorasHabiles.getHoraIniHabil();
        this.horaFinHabil = torneoHorasHabiles.getHoraFinHabil();
    }

}
