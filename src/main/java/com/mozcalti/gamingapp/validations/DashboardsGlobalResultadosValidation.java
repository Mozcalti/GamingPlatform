package com.mozcalti.gamingapp.validations;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Batallas;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import com.mozcalti.gamingapp.utils.FileUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DashboardsGlobalResultadosValidation {

    public static void validaBatalla(Batallas batallas) throws ValidacionException {
         if(batallas == null) {
             throw new ValidacionException("No existe batalla");
         } else if(batallas.getBndTermina().equals(1)) {
             throw new ValidacionException("La batalla ha terminado, cheque los resultados de la misma");
         }
    }
    public static void validaFechaHoraBatalla(String horaInicioBatalla, String horaFinBatalla) throws ValidacionException {

        String fechaSistema = DateUtils.getDateFormat(Calendar.getInstance().getTime(), Constantes.FECHA_HORA_PATTERN);

        if(!DateUtils.isHoursRangoValid(horaInicioBatalla, horaFinBatalla, fechaSistema, Constantes.FECHA_HORA_PATTERN)) {
            throw new ValidacionException("No existe batalla para la fecha y hora del día");
        }
    }

    public static void validaExisteArchivoXML(String fileResultadoBatallas) throws ValidacionException {

        try {
            if(!FileUtils.isFileValid(fileResultadoBatallas)) {
                throw new ValidacionException("No existe archivo XML de termino de batalla");
            }
        } catch (UtilsException e) {
            throw new ValidacionException("Error al checar archivo XML Resultados", e);
        }

    }

}
