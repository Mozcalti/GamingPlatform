package com.mozcalti.gamingapp.validations;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Etapas;
import com.mozcalti.gamingapp.model.Torneos;
import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CalendarizarEtapasTorneoValidation {

    public static void validaSaveTorneoEtapas(EtapaDTO etapaDTO) throws ValidacionException {
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

    }

    public static void validaGetTorneo(Torneos torneos, int idTorneo) throws ValidacionException {

        if(torneos == null) {
            throw new ValidacionException("No existe en torneo con el id indicado: " + idTorneo);
        }

    }

    public static void validaGeneraBatallas(Etapas etapas, Integer idEtapa) throws ValidacionException {
        if(etapas == null) {
            throw new ValidacionException("No existe la etapa con el id indicado: " + idEtapa);
        }
    }

    public static void validaExistenBatallas(Etapas etapas) throws ValidacionException {
        if(etapas.getEtapaBatallasByIdEtapa().isEmpty()) {
            throw new ValidacionException("No hay batallas para la estapa indicada");
        }
    }

}
