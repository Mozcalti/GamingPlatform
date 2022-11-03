package com.mozcalti.gamingapp.utils;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.TorneoHorasHabiles;
import com.mozcalti.gamingapp.model.Torneos;
import com.mozcalti.gamingapp.model.batallas.BatallaFechaHoraInicioDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosInstitucionGpoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TorneoUtils {

    public static Map<Integer, List<BatallaFechaHoraInicioDTO>> obtieneMapHorarios(Torneos torneos, String fechaInicioParam) throws UtilsException {

        List<BatallaFechaHoraInicioDTO> horariosPermitidos = new ArrayList<>();

        String fechaInicio;
        for(TorneoHorasHabiles torneoHorasHabiles : torneos.getTorneoHorasHabilesByIdTorneo()) {
            if(fechaInicioParam == null) {
                fechaInicio = torneos.getFechaInicio();
            } else {
                fechaInicio = fechaInicioParam;
            }
            int id = 1;
            horariosPermitidos.add(new BatallaFechaHoraInicioDTO(id++, fechaInicio,
                    torneoHorasHabiles.getHoraIniHabil(),
                    torneoHorasHabiles.getHoraFinHabil()));
            do {
                fechaInicio = DateUtils.addDias(fechaInicio, Constantes.FECHA_PATTERN, 1);

                String diaSemana = DateUtils.getDateFormat(
                        DateUtils.getDateFormat(fechaInicio, Constantes.FECHA_PATTERN).getTime(),
                        Constantes.DIA_PATTERN);

                boolean diaPermitido = !diaSemana.contains(Constantes.SABADO)
                        && !diaSemana.contains(Constantes.DOMINGO)
                        && DateUtils.isDatesRangoValid(fechaInicio, torneos.getFechaFin(), Constantes.FECHA_PATTERN);

                if(diaPermitido) {
                    horariosPermitidos.add(new BatallaFechaHoraInicioDTO(id++, fechaInicio,
                            torneoHorasHabiles.getHoraIniHabil(),
                            torneoHorasHabiles.getHoraFinHabil()));
                }

            } while(DateUtils.isDatesRangoValid(fechaInicio, torneos.getFechaFin(), Constantes.FECHA_PATTERN));

        }

        return horariosPermitidos.stream()
                .sorted(Comparator.comparing(BatallaFechaHoraInicioDTO::getId))
                .collect(Collectors.groupingBy(BatallaFechaHoraInicioDTO::getId));

    }

    public static boolean horaValida(Torneos torneos, String fecha, String horaValid) {

        Map<Integer, List<BatallaFechaHoraInicioDTO>> mapHorarios = obtieneMapHorarios(torneos, null);
        boolean resultado = false;

        for(Map.Entry<Integer, List<BatallaFechaHoraInicioDTO>> entry : mapHorarios.entrySet()) {
            for(BatallaFechaHoraInicioDTO batallaFechaHoraInicioDTO : entry.getValue()) {
                String horaInicio = batallaFechaHoraInicioDTO.getFecha() + " " + batallaFechaHoraInicioDTO.getHoraInicio();
                String horaFin = batallaFechaHoraInicioDTO.getFecha() + " " + batallaFechaHoraInicioDTO.getHoraFin();
                String horaValida = fecha + " " + horaValid;

                if(!resultado) {
                    resultado = DateUtils.isHoursRangoValid(horaInicio, horaFin, horaValida, Constantes.FECHA_HORA_PATTERN);
                }
            }
        }

        return resultado;

    }

    public static Integer calculaTotalBatallas(Integer dividendo, Integer divisor) {

        float div = (float) dividendo/divisor;
        int resDiv = Math.round(div);

        if(resDiv == 0) {
            resDiv+=1;
        }

        return resDiv;
    }

    public static List<Integer> armaEquipos(Set<Integer> idEquipos) throws ValidacionException {

        List<Integer> randomNumbers;

        try {
            randomNumbers = CollectionUtils.getRandomNumbers(idEquipos);
        } catch (NoSuchAlgorithmException e) {
            throw new ValidacionException(e);
        }

        return randomNumbers;

    }

    public static List<ResultadosInstitucionGpoDTO>
    obtieneParticipantesInstitucion(List<ResultadosInstitucionGpoDTO> resultadosInstitucionGpoDTOS, Integer numParticipantes)
            throws ValidacionException {

        List<ResultadosInstitucionGpoDTO> salida = new ArrayList<>();

        for(ResultadosInstitucionGpoDTO resultado : resultadosInstitucionGpoDTOS) {

            ResultadosInstitucionGpoDTO resultadosInstitucionGpoDTO = new ResultadosInstitucionGpoDTO();
            resultadosInstitucionGpoDTO.setNombreInstitucion(resultado.getNombreInstitucion());

            for(int x=0; x<numParticipantes; x++) {
                if(resultado.getParticipantes().size()>x) {
                    resultadosInstitucionGpoDTO.getParticipantes().add(resultado.getParticipantes().get(x));
                }
            }
            salida.add(resultadosInstitucionGpoDTO);
        }

        return salida;

    }


}
