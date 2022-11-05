package com.mozcalti.gamingapp.service.torneo.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.batallas.BatallaFechaHoraInicioDTO;
import com.mozcalti.gamingapp.model.batallas.BatallaParticipanteDTO;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.model.participantes.EquiposDTO;
import com.mozcalti.gamingapp.model.participantes.InstitucionEquiposDTO;
import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
import com.mozcalti.gamingapp.model.torneos.HoraHabilDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import com.mozcalti.gamingapp.service.torneo.TorneosService;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.repository.*;
import com.mozcalti.gamingapp.utils.*;
import com.mozcalti.gamingapp.validations.TorneoValidation;
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
    private TorneoHorasHabilesRepository torneoHorasHabilesRepository;
    private EtapaEquipoRepository etapaEquipoRepository;
    private ReglasRepository reglasRepository;

    private ParticipanteEquipoRepository participanteEquipoRepository;

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
                        nombreParticipantes.append(participantes.orElseThrow().getNombre()).append(Constantes.ESPACIO)
                                .append(participantes.orElseThrow().getApellidos())
                                .append(Constantes.COMA_ESPACIO);
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

        if(countCompetidores > Numeros.CERO.getNumero()) {
            listaBatallas.add(batallaParticipanteDTOS);
        }

        return listaBatallas;

    }

    @Override
    public BatallaParticipanteDTO obtieneParticipantes(Integer idEquipo) throws ValidacionException {

            Optional<Equipos> equipos = equiposRepository.findById(idEquipo);
            StringBuilder nombreParticipantes = new StringBuilder();

            if(equipos.isPresent()) {
                for(ParticipanteEquipo participanteEquipo : equipos.orElseThrow().getParticipanteEquiposByIdEquipo()) {
                    Optional<Participantes> participantes = participantesRepository.findById(participanteEquipo.getIdParticipante());
                    nombreParticipantes.append(participantes.orElseThrow().getNombre()).append(Constantes.COMA_ESPACIO);
                }
            }

        return new BatallaParticipanteDTO(
                idEquipo,
                nombreParticipantes.substring(Numeros.CERO.getNumero(), nombreParticipantes.length()-Numeros.DOS.getNumero()));

    }

    @Override
    public List<Torneos> getTorneos() throws ValidacionException {
        List<Torneos> lstTorneos = new ArrayList<>();
        torneosRepository.findAll().forEach(lstTorneos::add);
        return lstTorneos;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void guardaTorneo(TorneoDTO torneoDTO) throws ValidacionException {

        TorneoValidation.validaGuardarTorneo(getTorneos(), torneoDTO, true);
        Torneos torneos = torneosRepository.save(new Torneos(torneoDTO));

        for(HoraHabilDTO horaHabilDTO : torneoDTO.getHorasHabiles()) {
            torneoHorasHabilesRepository.save(new TorneoHorasHabiles(horaHabilDTO, torneos.getIdTorneo()));
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<TorneoDTO> obtieneTorneos() {
        List<Torneos> torneos = getTorneos();
        return torneos.stream().map(TorneoDTO::new).toList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TorneoDTO obtieneTorneos(Integer idTorneo) {
        Optional<Torneos> torneos = torneosRepository.findById(idTorneo);
        return torneos.stream().map(TorneoDTO::new).findFirst().orElseThrow();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void modificaTorneo(TorneoDTO torneoDTO) throws ValidacionException {

        TorneoValidation.validaGuardarTorneo(getTorneos(), torneoDTO, false);

        Optional<Torneos> torneos = torneosRepository.findById(torneoDTO.getIdTorneo());

        TorneoValidation.validaModificaTorneo(torneos);

        if(torneos.isPresent()) {
            torneos.orElseThrow().setFechaInicio(torneoDTO.getFechaInicio());
            torneos.orElseThrow().setFechaFin(torneoDTO.getFechaFin());
            torneos.orElseThrow().setNumEtapas(torneoDTO.getNumEtapas());
            torneosRepository.save(torneos.orElseThrow());

            torneoHorasHabilesRepository.deleteAll(torneos.orElseThrow().getTorneoHorasHabilesByIdTorneo());

            torneoDTO.getHorasHabiles().stream().forEach(t -> torneoHorasHabilesRepository.save(
                    new TorneoHorasHabiles(t, torneos.orElseThrow().getIdTorneo())
            ) );
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void eliminaTorneo(Integer idTorneo) {

        Optional<Torneos> torneos = torneosRepository.findById(idTorneo);

        TorneoValidation.validaEliminaTorneo(torneos);

        for(TorneoHorasHabiles torneoHorasHabiles : torneos.orElseThrow().getTorneoHorasHabilesByIdTorneo()) {
            torneoHorasHabilesRepository.delete(torneoHorasHabiles);
        }

        torneosRepository.delete(torneos.orElseThrow());

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void guardaEtapas(Integer idTorneo, List<EtapaDTO> etapaDTOS) throws ValidacionException {
        Optional<Torneos> torneos = torneosRepository.findById(idTorneo);
        TorneoValidation.validaGuardarEtapas(torneos, etapaDTOS);

        for(EtapaDTO etapaDTO : etapaDTOS) {
            // Etapa
            Etapas etapas = new Etapas(etapaDTO, torneos.orElseThrow().getIdTorneo());
            etapas = etapasRepository.save(etapas);

            // Reglas
            reglasRepository.save(new Reglas(etapaDTO.getReglas(), etapas.getIdEtapa()));

            // Participantes
            if(etapaDTO.getReglas().getTrabajo().equals(TipoBatalla.INDIVIDUAL.getTrabajo())) {
                List<Participantes> lstParticipantes = new ArrayList<>();
                participantesRepository.findAll().forEach(lstParticipantes::add);

                for(Participantes participantes : lstParticipantes) {
                    Equipos equipos = equiposRepository.save(new Equipos(true));

                    etapaEquipoRepository.save(new EtapaEquipo(etapas.getIdEtapa(), equipos.getIdEquipo()));

                    participanteEquipoRepository.save(new ParticipanteEquipo(participantes.getIdParticipante(), equipos.getIdEquipo()));
                }
            }
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<EtapaDTO> obtieneEtapas(Integer idTorneo) throws ValidacionException {
        List<Etapas> etapas = etapasRepository.findAllByIdTorneo(idTorneo);

        List<EtapaDTO> etapaDTOS = new ArrayList<>();
        for(Etapas etapa : etapas) {
            etapaDTOS.add(new EtapaDTO(etapa));
        }

        return etapaDTOS;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void eliminarEtapas(Integer idTorneo) {

        List<Etapas> etapas = etapasRepository.findAllByIdTorneo(idTorneo);

        TorneoValidation.validaEliminaEtapas(etapas);

        for(Etapas etapa : etapas) {
            reglasRepository.deleteById(etapa.getReglas().getIdRegla());

            for(EtapaEquipo etapaEquipo : etapa.getEtapaEquiposByIdEtapa()) {
                etapaEquipoRepository.deleteById(etapaEquipo.getIdEtapaEquipo());
                participanteEquipoRepository.deleteByIdEquipo(etapaEquipo.getIdEquipo());
                equiposRepository.deleteById(etapaEquipo.getIdEquipo());
            }

            etapasRepository.deleteById(etapa.getIdEtapa());
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void modificaEtapas(Integer idTorneo, List<EtapaDTO> etapasDTOS) throws ValidacionException {

        Optional<Torneos> torneos = torneosRepository.findById(idTorneo);
        TorneoValidation.validaGuardarEtapas(torneos, etapasDTOS);

        for(EtapaDTO etapaDTO : etapasDTOS) {
            // Etapa
            Etapas etapa = etapasRepository.findById(etapaDTO.getIdEtapa()).orElseThrow();
            etapa.setNumeroEtapa(etapaDTO.getNumeroEtapa());
            etapa.setFechaInicio(etapaDTO.getFechaInicio());
            etapa.setFechaFin(etapaDTO.getFechaFin());
            etapasRepository.save(etapa);

            // Reglas
            etapa.getReglas().setNumCompetidores(etapaDTO.getReglas().getNumCompetidores());
            etapa.getReglas().setNumRondas(etapaDTO.getReglas().getNumRondas());
            etapa.getReglas().setTiempoBatallaAprox(etapaDTO.getReglas().getTiempoBatallaAprox());
            etapa.getReglas().setTrabajo(etapaDTO.getReglas().getTrabajo());
            etapa.getReglas().setTiempoEspera(etapaDTO.getReglas().getTiempoEspera());
            etapa.getReglas().setArenaAncho(etapaDTO.getReglas().getArenaAncho());
            etapa.getReglas().setArenaAlto(etapaDTO.getReglas().getArenaAlto());

            etapasRepository.save(etapa);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DatosCorreoBatallaDTO> getDatosCorreoBatalla() throws ValidacionException {

        List<DatosCorreoBatallaDTO> mailsbatallas = new ArrayList<>();

        String fechaSistema = DateUtils.getDateFormat(Calendar.getInstance().getTime(), Constantes.FECHA_PATTERN);

        List<Batallas> lstBatallas = new ArrayList<>();
        batallasRepository.findAll().forEach(lstBatallas::add);
        StringBuilder mailToParticipantes = new StringBuilder();

        for(Batallas batalla : lstBatallas) {

            if(fechaSistema.equals(DateUtils.addDias(
                    batalla.getFecha(), Constantes.FECHA_PATTERN, Numeros.UNO_NEGATIVO.getNumero()))
                    && batalla.getBndEnvioCorreo().equals(Numeros.CERO.getNumero())) {

                DatosCorreoBatallaDTO mailBatallasDTO = new DatosCorreoBatallaDTO(batalla.getFecha(),
                        batalla.getHoraInicio(), batalla.getHoraFin(),
                        batalla.getRondas());

                List<String> participante = new ArrayList<>();
                mailToParticipantes.delete(Numeros.CERO.getNumero(), mailToParticipantes.length());
                for(BatallaParticipantes batallaParticipantes : batalla.getBatallaParticipantesByIdBatalla()) {
                    participante.add(batallaParticipantes.getNombre());

                    Equipos equipos = equiposRepository.findById(batallaParticipantes.getIdParticipanteEquipo())
                            .orElseThrow();

                    for(ParticipanteEquipo participanteEquipo : equipos.getParticipanteEquiposByIdEquipo()) {
                        mailToParticipantes.append(participantesRepository.findById(participanteEquipo.getIdParticipante())
                                .orElseThrow().getCorreo()).append(Constantes.COMA);
                    }

                    mailBatallasDTO.setMailToParticipantes(
                            mailToParticipantes.substring(Numeros.CERO.getNumero(),
                                    mailToParticipantes.length()-Numeros.UNO.getNumero()));
                }
                mailBatallasDTO.setParticipantes(participante);
                mailsbatallas.add(mailBatallasDTO);

                batalla.setBndEnvioCorreo(Numeros.UNO.getNumero());
                batallasRepository.save(batalla);
            }

        }

        return mailsbatallas;
    }

}
