package com.mozcalti.gamingapp.validations;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Etapas;
import com.mozcalti.gamingapp.model.Torneos;
import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
import com.mozcalti.gamingapp.model.torneos.HoraHabilDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import com.mozcalti.gamingapp.utils.Numeros;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TorneoValidation {

    public static void validaGuardarTorneo(@NonNull List<Torneos> lstTorneos, TorneoDTO torneoDTO, boolean esAlta)
            throws ValidacionException {

        if(!lstTorneos.isEmpty() && esAlta) {
            throw new ValidacionException("Por el momento no es posible agregar más de 1 torneo");
        }

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

        if(torneoDTO.getNumEtapas() > Numeros.DOS.getNumero()) {
            throw new ValidacionException("Por el momento sólo se pueden dar 2 etapas en el torneo");
        }

    }

    public static void validaConsultarTorneo(@NonNull List<Torneos> torneos) {
        if (torneos.isEmpty()) {
            throw new ValidacionException("No existe torneo");
        }
    }
    public static void validaEliminaTorneo(@NonNull Optional<Torneos> torneos) throws ValidacionException {

        if (!torneos.isPresent()) {
            throw new ValidacionException("No existe torneo a eliminar");
        }

        if (!torneos.orElseThrow().getEtapasByIdTorneo().isEmpty()) {
            throw new ValidacionException("No es posible eliminar el Torneo ya que existen etapas configuradas");
        }

    }

    public static void validaGuardarEtapas(@NonNull List<Torneos> torneos, List<EtapaDTO> etapasDTOS,
                                           List<Etapas> lstEtapas, boolean esAlta) {

        if (torneos.isEmpty()) {
            throw new ValidacionException("No existe torneo al cual agregar las etapas");
        }

        if(!lstEtapas.isEmpty() && esAlta) {
            throw new ValidacionException("Ya existen etapas guardadas");
        }

        if (etapasDTOS.isEmpty()) {
            throw new ValidacionException("Debe por lo menos agregar una etapa");
        }

        if (etapasDTOS.size() > Numeros.DOS.getNumero()) {
            throw new ValidacionException("No es posible agregas más de 2 etapas por el momento");
        }

        for(EtapaDTO etapaDTO : etapasDTOS) {
            DateUtils.isValidDate(etapaDTO.getFechaInicio(),
                    Constantes.FECHA_PATTERN,
                    "Fecha de inicio de la etapa " + etapaDTO.getNumeroEtapa() + " no válido");

            DateUtils.isValidDate(etapaDTO.getFechaFin(),
                    Constantes.FECHA_PATTERN,
                    "Fecha de fin de la etapa " + etapaDTO.getNumeroEtapa() + " no válido");

            DateUtils.isDatesRangoValid(etapaDTO.getFechaInicio(),
                    etapaDTO.getFechaFin(),
                    Constantes.FECHA_PATTERN,
                    "Fecha inicio de la etapa " + etapaDTO.getNumeroEtapa()
                            + " no puede ser mayor a la fecha fin");

            String diaSemanaFIT = DateUtils.getDateFormat(
                    DateUtils.getDateFormat(etapaDTO.getFechaInicio(), Constantes.FECHA_PATTERN).getTime(),
                    Constantes.DIA_PATTERN);
            String diaSemanaFFT = DateUtils.getDateFormat(
                    DateUtils.getDateFormat(etapaDTO.getFechaFin(), Constantes.FECHA_PATTERN).getTime(),
                    Constantes.DIA_PATTERN);

            if(diaSemanaFIT.contains(Constantes.SABADO) || diaSemanaFIT.contains(Constantes.DOMINGO)) {
                throw new ValidacionException("Fecha de inicio de la etapa "
                        +  etapaDTO.getNumeroEtapa() + " no puede ser un día inhábil");
            }

            if(diaSemanaFFT.contains(Constantes.SABADO) || diaSemanaFFT.contains(Constantes.DOMINGO)) {
                throw new ValidacionException("Fecha de fin de la etapa "
                        +  etapaDTO.getNumeroEtapa() + " no puede ser un día inhábil");
            }

            if(etapaDTO.getReglas() == null) {
                throw new ValidacionException("Debe mandar las reglas para la etapa");
            }
        }

    }

}
