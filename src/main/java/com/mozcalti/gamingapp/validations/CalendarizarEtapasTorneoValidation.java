package com.mozcalti.gamingapp.validations;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.EtapaEquipo;
import com.mozcalti.gamingapp.model.Etapas;
import com.mozcalti.gamingapp.model.Torneos;
import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
import com.mozcalti.gamingapp.model.torneos.HoraHabilDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import com.mozcalti.gamingapp.utils.CollectionUtils;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

        if(diaSemanaFIT.contains(Constantes.SABADO) || diaSemanaFIT.contains(Constantes.DOMINGO)) {
            throw new ValidacionException("Fecha de inicio del torneo no puede ser un día inhábil");
        }

        if(diaSemanaFFT.contains(Constantes.SABADO) || diaSemanaFFT.contains(Constantes.DOMINGO)) {
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

        boolean existeEtapas = torneoDTO.getEtapas() != null && !torneoDTO.getEtapas().isEmpty();

        if(existeEtapas && (torneoDTO.getEtapas().size() != torneoDTO.getNumEtapas())) {
            throw new ValidacionException("Deben darse de alta " + torneoDTO.getNumEtapas()
                    + " etapas, de acuerdo a lo configurado en el torneo");
        }

    }

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

    public static List<Integer> armaEquipos(Etapas etapas) throws ValidacionException {

        List<Integer> idEquipos = new ArrayList<>();
        List<Integer> randomNumbers;

        try {
            for(EtapaEquipo etapaEquipo : etapas.getEtapaEquiposByIdEtapa()) {
                idEquipos.add(etapaEquipo.getIdEquipo());
            }

            randomNumbers = CollectionUtils.getRandomNumbers(idEquipos);
        } catch (NoSuchAlgorithmException e) {
            throw new ValidacionException(e);
        }

        return randomNumbers;

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
