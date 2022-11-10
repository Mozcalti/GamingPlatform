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

        if(!lstTorneos.isEmpty() && esAlta) {
            Torneos torneos = lstTorneos.get(lstTorneos.size()-Numeros.UNO.getNumero());
            DateUtils.isDatesRangoValid(
                    DateUtils.addDias(torneos.getFechaFin(), Constantes.FECHA_PATTERN, Numeros.UNO.getNumero()),
                    torneoDTO.getFechaInicio(), Constantes.FECHA_PATTERN,
                    "Las fechas del nuevo torneo debe continuar después del último registrado");
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

    public static void validaModificaTorneo(@NonNull Optional<Torneos> torneos) throws ValidacionException {

        if (!torneos.isPresent()) {
            throw new ValidacionException("No existe torneo a modificar");
        }

        if (!torneos.orElseThrow().getEtapasByIdTorneo().isEmpty()) {
            throw new ValidacionException("No es posible modificar el Torneo ya que existen etapas configuradas");
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

    public static void validaGuardarEtapas(@NonNull Optional<Torneos> torneos, List<EtapaDTO> etapasDTOS) {

        if (!torneos.isPresent()) {
            throw new ValidacionException("No existe torneo al cual agregar las etapas");
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

            if(etapaDTO.getReglas() == null) {
                throw new ValidacionException("Debe mandar las reglas para la etapa");
            }
        }

    }

    public static void validaEliminaEtapas(List<Etapas> etapas) {
        for(Etapas etapa : etapas) {
            if(!etapa.getEtapaBatallasByIdEtapa().isEmpty()) {
                throw new ValidacionException("No es posible eliminar las etapas debido a que tiene batallas programadas");
            }
        }
    }

}
