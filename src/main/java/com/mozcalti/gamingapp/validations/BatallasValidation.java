package com.mozcalti.gamingapp.validations;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Batallas;
import com.mozcalti.gamingapp.model.Etapas;
import com.mozcalti.gamingapp.model.batallas.BatallaDTO;
import com.mozcalti.gamingapp.model.batallas.BatallasDTO;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import com.mozcalti.gamingapp.utils.Numeros;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatallasValidation {

    public static void validaGuardaBatallas(BatallasDTO batallasDTO) {

        if(batallasDTO.getBatallas().isEmpty()) {
            throw new ValidacionException("No existen batallas");
        }

        for(BatallaDTO batallaDTO : batallasDTO.getBatallas()) {
            DateUtils.isValidDate(batallaDTO.getFecha(),
                    Constantes.FECHA_PATTERN,
                    "Fecha de la batalla que inicia a las " + batallaDTO.getHoraInicio() + " no es válido");

            DateUtils.isDatesRangoValid(batallaDTO.getHoraInicio(),
                    batallaDTO.getHoraFin(),
                    Constantes.HORA_PATTERN,
                    "Rango de horas no válido en la batlla que inicia a las " + batallaDTO.getHoraInicio());

            if(batallaDTO.getBatallaParticipantes().size() == Numeros.UNO.getNumero()) {
                throw new ValidacionException("Existen batallas con 1 sólo participante, favor de verificar");
            }

            if(batallaDTO.getBatallaParticipantes().isEmpty()) {
                throw new ValidacionException("Debe haber por lo menos 2 participantes para la batalla");
            }

            if(batallaDTO.getRondas() == Numeros.CERO.getNumero()) {
                throw new ValidacionException("El número de rondas debe ser mayos a 0, en la batlla que inicia a las "
                        + batallaDTO.getHoraInicio());
            }
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
