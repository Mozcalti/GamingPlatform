package com.mozcalti.gamingapp.validations;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.torneos.HoraHabilDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TorneoValidation {

    public static void validaGuardarTorneo(TorneoDTO torneoDTO) throws ValidacionException {

        DateUtils.isValidDate(torneoDTO.getFechaInicio(),
                Constantes.FECHA_PATTERN,
                "Fecha de inicio del torneo no válido");

        DateUtils.isValidDate(torneoDTO.getFechaFin(),
                Constantes.FECHA_PATTERN,
                "Fecha de fin del torneo no válido");

        DateUtils.isDatesRangoValid(torneoDTO.getFechaInicio(),
                torneoDTO.getFechaFin(),
                Constantes.FECHA_PATTERN,
                "Fecha inicio del torneo no puede ser mayor a la fecha fin");

        String diaSemanaFIT = DateUtils.getDateFormat(
                DateUtils.getDateFormat(torneoDTO.getFechaInicio(), Constantes.FECHA_PATTERN).getTime(),
                Constantes.DIA_PATTERN);
        String diaSemanaFFT = DateUtils.getDateFormat(
                DateUtils.getDateFormat(torneoDTO.getFechaFin(), Constantes.FECHA_PATTERN).getTime(),
                Constantes.DIA_PATTERN);

        if(diaSemanaFIT.contains(Constantes.SABADO) || diaSemanaFIT.contains(Constantes.DOMINGO)) {
            throw new ValidacionException("Fecha de inicio del torneo no puede ser un día inhábil");
        }

        if(diaSemanaFFT.contains(Constantes.SABADO) || diaSemanaFFT.contains(Constantes.DOMINGO)) {
            throw new ValidacionException("Fecha de fin del torneo no puede ser un día inhábil");
        }

        if(torneoDTO.getHorasHabiles() == null) {
            throw new ValidacionException("Debe seleccionar por lo menos un horario para el torneo");
        }

        for(HoraHabilDTO horaHabilDTO : torneoDTO.getHorasHabiles()) {
            DateUtils.isDatesRangoValid(horaHabilDTO.getHoraIniHabil(),
                    horaHabilDTO.getHoraFinHabil(),
                    Constantes.HORA_PATTERN,
                    "Horario del torneo no válido");
        }

    }

}
