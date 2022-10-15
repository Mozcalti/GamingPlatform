package com.mozcalti.gamingapp.validations;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.request.torneo.EtapaRequest;
import com.mozcalti.gamingapp.request.torneo.HoraHabilRequest;
import com.mozcalti.gamingapp.request.torneo.TorneoRequest;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;

public final class CalendarizarEtapasTorneoValidation {

    public static void validaSaveTorneo(TorneoRequest torneoRequest) throws ValidacionException {
        // Datos torneo
        DateUtils.isValidDate(torneoRequest.getFechaInicio(),
                Constantes.FECHA_PATTERN,
                "Fecha de inicio del torneo no válido");

        DateUtils.isValidDate(torneoRequest.getFechaFin(),
                Constantes.FECHA_PATTERN,
                "Fecha de fin del torneo no válido");

        DateUtils.isDatesRangoValid(torneoRequest.getFechaInicio(),
                torneoRequest.getFechaFin(),
                Constantes.FECHA_PATTERN,
                "Fecha inicio del torneo no puede ser mayor a la fecha fin");

        String diaSemanaFIT = DateUtils.getDateFormat(DateUtils.getDateFormat(torneoRequest.getFechaInicio(), Constantes.FECHA_PATTERN).getTime(),"E");
        String diaSemanaFFT = DateUtils.getDateFormat(DateUtils.getDateFormat(torneoRequest.getFechaFin(), Constantes.FECHA_PATTERN).getTime(),"E");

        if(diaSemanaFIT.contains("SÁB") || diaSemanaFIT.contains("DOM")) {
            throw new ValidacionException("Fecha de inicio del torneo no puede ser un día inhábil");
        }

        if(diaSemanaFFT.contains("SÁB") || diaSemanaFFT.contains("DOM")) {
            throw new ValidacionException("Fecha de fin del torneo no puede ser un día inhábil");
        }

        for(HoraHabilRequest horaHabilRequest : torneoRequest.getHorasHabilesRequest()) {
            DateUtils.isDatesRangoValid(horaHabilRequest.getHoraIniHabil(),
                    horaHabilRequest.getHoraFinHabil(),
                    Constantes.HORA_PATTERN,
                    "Horario del torneo no válido");
        }

    }

    public static void validaSaveTorneoEtapas(TorneoRequest torneoRequest, EtapaRequest etapaRequest) throws ValidacionException {
        // Datos torneo etapas
        DateUtils.isValidDate(etapaRequest.getFechaInicio(),
                Constantes.FECHA_PATTERN,
                "Fecha de inicio de la etapa " + etapaRequest.getNumeroEtapa() + " no válido");

        DateUtils.isValidDate(etapaRequest.getFechaFin(),
                Constantes.FECHA_PATTERN,
                "Fecha de fin de la etapa " + etapaRequest.getNumeroEtapa() + " no válido");

        DateUtils.isDatesRangoValid(etapaRequest.getFechaInicio(),
                etapaRequest.getFechaFin(),
                Constantes.FECHA_PATTERN,
                "Fecha inicio de la etapa " + etapaRequest.getNumeroEtapa() + " no puede ser mayor a la fecha fin");

        String diaSemanaFIT = DateUtils.getDateFormat(DateUtils.getDateFormat(etapaRequest.getFechaInicio(), Constantes.FECHA_PATTERN).getTime(),"E");
        String diaSemanaFFT = DateUtils.getDateFormat(DateUtils.getDateFormat(etapaRequest.getFechaFin(), Constantes.FECHA_PATTERN).getTime(),"E");

        if(diaSemanaFIT.contains("SÁB") || diaSemanaFIT.contains("DOM")) {
            throw new ValidacionException("Fecha de inicio de la etapa " + etapaRequest.getNumeroEtapa() + " no puede ser un día inhábil");
        }

        if(diaSemanaFFT.contains("SÁB") || diaSemanaFFT.contains("DOM")) {
            throw new ValidacionException("Fecha de fin de la etapa " + etapaRequest.getNumeroEtapa() + " no puede ser un día inhábil");
        }

        /*int bndHoraValida = 0;
        for(HoraHabilRequest horaHabilRequest : torneoRequest.getHorasHabilesRequest()) {
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
