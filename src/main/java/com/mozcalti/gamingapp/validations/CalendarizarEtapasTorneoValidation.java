package com.mozcalti.gamingapp.validations;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
import com.mozcalti.gamingapp.model.torneos.HoraHabilDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;

public final class CalendarizarEtapasTorneoValidation {

    public static void validaSaveTorneo(TorneoDTO torneoDTO) throws ValidacionException {
        // Datos torneo
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

        if(diaSemanaFIT.contains("SÁB") || diaSemanaFIT.contains("DOM")) {
            throw new ValidacionException("Fecha de inicio del torneo no puede ser un día inhábil");
        }

        if(diaSemanaFFT.contains("SÁB") || diaSemanaFFT.contains("DOM")) {
            throw new ValidacionException("Fecha de fin del torneo no puede ser un día inhábil");
        }

        if(torneoDTO.getHorasHabiles().isEmpty()) {
            throw new ValidacionException("Debe seleccionar por lo menos un horario para el torneo");
        }

        for(HoraHabilDTO horaHabilDTO : torneoDTO.getHorasHabiles()) {
            DateUtils.isDatesRangoValid(horaHabilDTO.getHoraIniHabil(),
                    horaHabilDTO.getHoraFinHabil(),
                    Constantes.HORA_PATTERN,
                    "Horario del torneo no válido");
        }

        if(torneoDTO.getEtapas() != null && !torneoDTO.getEtapas().isEmpty()) {
            if(torneoDTO.getEtapas().size() != torneoDTO.getNumEtapas()) {
                throw new ValidacionException("Deben darse de alta " + torneoDTO.getNumEtapas()
                        + " etapas, de acuerdo a lo configurado en el torneo");
            }
        }

    }

    public static void validaSaveTorneoEtapas(TorneoDTO torneoDTO, EtapaDTO etapaDTO) throws ValidacionException {
        // Datos torneo etapas
        DateUtils.isValidDate(etapaDTO.getFechaInicio(),
                Constantes.FECHA_PATTERN,
                "Fecha de inicio de la etapa " + etapaDTO.getNumeroEtapa() + " no válido");

        DateUtils.isValidDate(etapaDTO.getFechaFin(),
                Constantes.FECHA_PATTERN,
                "Fecha de fin de la etapa " + etapaDTO.getNumeroEtapa() + " no válido");

        DateUtils.isDatesRangoValid(etapaDTO.getFechaInicio(),
                etapaDTO.getFechaFin(),
                Constantes.FECHA_PATTERN,
                "Fecha inicio de la etapa " + etapaDTO.getNumeroEtapa() + " no puede ser mayor a la fecha fin");

        String diaSemanaFIT = DateUtils.getDateFormat(
                DateUtils.getDateFormat(etapaDTO.getFechaInicio(), Constantes.FECHA_PATTERN).getTime(),
                Constantes.DIA_PATTERN);
        String diaSemanaFFT = DateUtils.getDateFormat(
                DateUtils.getDateFormat(etapaDTO.getFechaFin(), Constantes.FECHA_PATTERN).getTime(),
                Constantes.DIA_PATTERN);

        if(diaSemanaFIT.contains("SÁB") || diaSemanaFIT.contains("DOM")) {
            throw new ValidacionException("Fecha de inicio de la etapa " + etapaDTO.getNumeroEtapa() + " no puede ser un día inhábil");
        }

        if(diaSemanaFFT.contains("SÁB") || diaSemanaFFT.contains("DOM")) {
            throw new ValidacionException("Fecha de fin de la etapa " + etapaDTO.getNumeroEtapa() + " no puede ser un día inhábil");
        }

        /*int bndHoraValida = 0;
        for(HoraHabilRequest horaHabilRequest : torneoRequest.getHorasHabiles()) {
            if(DateUtils.isHoursRangoValid(etapaRequest.getFechaInicio(), etapaRequest.getFechaFin(),
                    horaHabilRequest.getHoraIniHabil(), horaHabilRequest.getHoraFinHabil(), Constantes.FECHA_PATTERN)
                    && bndHoraValida == 0) {
                bndHoraValida = 1;
            }
        }

        if(bndHoraValida == 0) {
            throw new ValidacionException("Horario inhábil en la etapa " + etapaRequest.getNumeroEtapa());
        }*/

    }

}
