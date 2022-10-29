package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.batallas.BatallaFechaHoraInicioDTO;
import com.mozcalti.gamingapp.model.batallas.BatallaParticipanteDTO;
import com.mozcalti.gamingapp.model.participantes.EquiposDTO;
import com.mozcalti.gamingapp.model.participantes.InstitucionEquiposDTO;
import com.mozcalti.gamingapp.service.TorneosService;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.repository.*;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import com.mozcalti.gamingapp.utils.Numeros;
import com.mozcalti.gamingapp.utils.TorneoUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class TorneosServiceImpl extends GenericServiceImpl<Torneos, Integer> implements TorneosService {

    private TorneosRepository torneosRepository;
    private EtapasRepository etapasRepository;
    private BatallasRepository batallasRepository;
    private InstitucionRepository institucionRepository;

    private EquiposRepository equiposRepository;
    private ParticipantesRepository participantesRepository;

    @Override
    public CrudRepository<Torneos, Integer> getDao() {
        return torneosRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<BatallaFechaHoraInicioDTO> obtieneFechasBatalla(Integer idEtapa, Integer numeroFechas)
            throws ValidacionException {

        Etapas etapas = etapasRepository.findById(idEtapa).orElseThrow();

        Torneos torneos = torneosRepository.findById(etapas.getIdTorneo()).orElseThrow();

        int tiempoBatalla = etapas.getReglas().getTiempoBatallaAprox();
        int tiempoEspera = etapas.getReglas().getTiempoEspera();
        List<BatallaFechaHoraInicioDTO> batallaFechaHoraInicioDTOS = new ArrayList<>();
        String fecha;
        String horaInicioBatalla;
        String horaFinBatalla;
        if(!etapas.getEtapaBatallasByIdEtapa().isEmpty()) {
            List<Batallas> batallas;
            batallas = etapas.getEtapaBatallasByIdEtapa().stream()
                    .map(o -> batallasRepository.findById(o.getIdBatalla()).orElseThrow()).toList();

            Optional<Batallas> batallaFinal = batallas.stream()
                    .sorted(Comparator.comparing(Batallas::getFecha).reversed())
                    .sorted(Comparator.comparing(Batallas::getHoraFin).reversed()).findFirst();

            fecha = batallaFinal.orElseThrow().getFecha();
            horaInicioBatalla = DateUtils.addMinutos(batallaFinal.orElseThrow().getHoraFin(), Constantes.HORA_PATTERN, tiempoEspera);
            horaFinBatalla = DateUtils.addMinutos(horaInicioBatalla, Constantes.HORA_PATTERN, tiempoBatalla);
        } else {
            Optional<TorneoHorasHabiles> torneoHorasHabiles = torneos.getTorneoHorasHabilesByIdTorneo().stream()
                    .sorted(Comparator.comparing(TorneoHorasHabiles::getIdHoraHabil)).findFirst();

            fecha = torneos.getFechaInicio();
            horaInicioBatalla = torneoHorasHabiles.orElseThrow().getHoraIniHabil();
            horaFinBatalla = DateUtils.addMinutos(horaInicioBatalla, Constantes.HORA_PATTERN, tiempoBatalla);
        }

        Optional<TorneoHorasHabiles> torneoHoraInicioTope = torneos.getTorneoHorasHabilesByIdTorneo().stream()
                .sorted(Comparator.comparing(TorneoHorasHabiles::getIdHoraHabil)).findFirst();

        Optional<TorneoHorasHabiles> torneoHoraFinTope = torneos.getTorneoHorasHabilesByIdTorneo().stream()
                .sorted(Comparator.comparing(TorneoHorasHabiles::getIdHoraHabil).reversed()).findFirst();

        Map<Integer, List<BatallaFechaHoraInicioDTO>> mapHorarios = TorneoUtils.obtieneMapHorarios(torneos, fecha);
        batallaFechaHoraInicioDTOS.add(new BatallaFechaHoraInicioDTO(Numeros.CERO.getNumero(), fecha, horaInicioBatalla, horaFinBatalla));
        int contadorFechas = Numeros.UNO.getNumero();
        for(Map.Entry<Integer, List<BatallaFechaHoraInicioDTO>> entry : mapHorarios.entrySet()) {
            for(BatallaFechaHoraInicioDTO batallaFechaHoraInicioDTO1 : entry.getValue()) {
                if(contadorFechas < numeroFechas) {
                    horaInicioBatalla = DateUtils.addMinutos(horaFinBatalla, Constantes.HORA_PATTERN, tiempoEspera);
                    horaFinBatalla = DateUtils.addMinutos(horaInicioBatalla, Constantes.HORA_PATTERN, tiempoBatalla);

                    if(TorneoUtils.horaValida(torneos, fecha, horaInicioBatalla) && TorneoUtils.horaValida(torneos, fecha, horaFinBatalla)) {
                        batallaFechaHoraInicioDTOS.add(
                                new BatallaFechaHoraInicioDTO(batallaFechaHoraInicioDTO1.getId(),
                                        fecha, horaInicioBatalla, horaFinBatalla));
                        contadorFechas+=Numeros.UNO.getNumero();
                    } else if(!DateUtils.isDatesRangoValid(horaInicioBatalla,
                            torneoHoraFinTope.orElseThrow().getHoraFinHabil(), Constantes.HORA_PATTERN)) {
                        fecha = DateUtils.addDias(fecha, Constantes.FECHA_PATTERN, Numeros.UNO.getNumero());
                        horaFinBatalla = DateUtils.addMinutos(
                                torneoHoraInicioTope.orElseThrow().getHoraIniHabil(), Constantes.HORA_PATTERN, Numeros.DOS_NEGATIVO.getNumero());
                    }
                }
            }
        }

        return batallaFechaHoraInicioDTOS;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public EquiposDTO obtieneInstitucionEquipos() throws ValidacionException {

        EquiposDTO equiposDTOS = new EquiposDTO();
        List<InstitucionEquiposDTO> institucionEquiposDTOS = new ArrayList<>();

        List<Institucion> instituciones = new ArrayList<>();
        institucionRepository.findAll().forEach(instituciones::add);
        for(Institucion institucion : instituciones) {
            Set<Integer> idEquiposActivos = new HashSet<>();
            boolean bndInsVacio = true;
            for(Participantes participantes : participantesRepository.findAllByInstitucionId(institucion.getId())
                    .stream().filter(p -> !p.getParticipanteEquiposByIdParticipante().isEmpty()).toList()) {
                bndInsVacio = false;
                for(ParticipanteEquipo participanteEquipo : participantes.getParticipanteEquiposByIdParticipante()) {
                    Optional<Equipos> equipos = equiposRepository.findById(participanteEquipo.getIdEquipo())
                            .filter(Equipos::isActivo);

                    if(equipos.isPresent()) {
                        idEquiposActivos.add(equipos.orElseThrow().getIdEquipo());
                        equiposDTOS.getIdEquipos().add(equipos.orElseThrow().getIdEquipo());
                    }
                }
            }

            if(!bndInsVacio) {
                institucionEquiposDTOS.add(new InstitucionEquiposDTO(institucion.getId(),
                        idEquiposActivos));
            }

        }

        equiposDTOS.setEquiposByInstitucion(institucionEquiposDTOS);

        return equiposDTOS;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<List<BatallaParticipanteDTO>> obtieneParticipantes(List<Integer> idEquipos, Integer totalParticipantes)
            throws ValidacionException {

        List<List<BatallaParticipanteDTO>> listaBatallas = new ArrayList<>();
        List<BatallaParticipanteDTO> batallaParticipanteDTOS = new ArrayList<>();
        int countCompetidores = Numeros.CERO.getNumero();

        for(Integer idEquipo : idEquipos) {

            if(countCompetidores == Numeros.CERO.getNumero()) {
                batallaParticipanteDTOS = new ArrayList<>();
            }

            if(countCompetidores < totalParticipantes) {
                Optional<Equipos> equipos = equiposRepository.findById(idEquipo);
                StringBuilder nombreParticipantes = new StringBuilder();

                if(equipos.isPresent()) {
                    for(ParticipanteEquipo participanteEquipo : equipos.orElseThrow().getParticipanteEquiposByIdEquipo()) {
                        Optional<Participantes> participantes = participantesRepository.findById(participanteEquipo.getIdParticipante());
                        nombreParticipantes.append(participantes.orElseThrow().getNombre()).append(Constantes.SEPARA_NOM_PARTICIPANTES);
                    }
                }

                batallaParticipanteDTOS.add(new BatallaParticipanteDTO(
                        idEquipo,
                        nombreParticipantes.substring(Numeros.CERO.getNumero(), nombreParticipantes.length()-Numeros.DOS.getNumero())));

                countCompetidores+=Numeros.UNO.getNumero();
            }

            if(countCompetidores == totalParticipantes) {
                listaBatallas.add(batallaParticipanteDTOS);
                countCompetidores = Numeros.CERO.getNumero();
            }

        }

        listaBatallas.add(batallaParticipanteDTOS);

        return listaBatallas;

    }

    @Override
    public BatallaParticipanteDTO obtieneParticipantes(Integer idEquipo) throws ValidacionException {

            Optional<Equipos> equipos = equiposRepository.findById(idEquipo);
            StringBuilder nombreParticipantes = new StringBuilder();

            if(equipos.isPresent()) {
                for(ParticipanteEquipo participanteEquipo : equipos.orElseThrow().getParticipanteEquiposByIdEquipo()) {
                    Optional<Participantes> participantes = participantesRepository.findById(participanteEquipo.getIdParticipante());
                    nombreParticipantes.append(participantes.orElseThrow().getNombre()).append(Constantes.SEPARA_NOM_PARTICIPANTES);
                }
            }

        return new BatallaParticipanteDTO(
                idEquipo,
                nombreParticipantes.substring(Numeros.CERO.getNumero(), nombreParticipantes.length()-Numeros.DOS.getNumero()));

    }

}
