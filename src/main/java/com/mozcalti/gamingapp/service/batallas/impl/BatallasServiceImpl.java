package com.mozcalti.gamingapp.service.batallas.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.batallas.*;
import com.mozcalti.gamingapp.model.catalogos.EtapasDTO;
import com.mozcalti.gamingapp.model.catalogos.InstitucionDTO;
import com.mozcalti.gamingapp.model.catalogos.ParticipanteDTO;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.model.participantes.EquiposDTO;
import com.mozcalti.gamingapp.model.participantes.InstitucionEquiposDTO;
import com.mozcalti.gamingapp.repository.*;
import com.mozcalti.gamingapp.robocode.BattleRunner;
import com.mozcalti.gamingapp.robocode.Robocode;
import com.mozcalti.gamingapp.service.batallas.BatallasService;
import com.mozcalti.gamingapp.service.correos.SendMailService;
import com.mozcalti.gamingapp.service.correos.impl.SendMailBatallaImpl;
import com.mozcalti.gamingapp.service.dashboard.DashboardService;
import com.mozcalti.gamingapp.service.torneo.TorneosService;
import com.mozcalti.gamingapp.utils.*;
import com.mozcalti.gamingapp.validations.BatallasValidation;
import com.mozcalti.gamingapp.validations.DashboardsGlobalResultadosValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatallasServiceImpl extends GenericServiceImpl<Batallas, Integer> implements BatallasService {

    private static final boolean RECORDER = true;

    private static final String REPLAY_TYPE = "xml";

    @Autowired
    private BatallasRepository batallasRepository;

    @Autowired
    private EtapasRepository etapasRepository;

    @Autowired
    private EquiposRepository equiposRepository;

    @Autowired
    private InstitucionRepository institucionRepository;

    @Autowired
    private ParticipantesRepository participantesRepository;

    @Autowired
    private TorneosService torneosService;

    @Autowired
    private EtapaBatallaRepository etapaBatallaRepository;

    @Autowired
    private BatallaParticipantesRepository batallaParticipantesRepository;

    @Autowired
    private DashboardService dashboardsGlobalResultadosService;

    @Autowired
    private ResultadosRepository resultadosRepository;

    @Autowired
    private SendMailService sendMailService;

    @Override
    public CrudRepository<Batallas, Integer> getDao() {
        return batallasRepository;
    }

    @Value("${robocode.executable}")
    private String pathRobocode;
    @Value("${server.baseUrl}")
    private String baseUrl;
    @Value("${view.htmlViewBattle}")
    private String htmlViewBattle;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void ejecutaBatalla() {

        String horaInicioBatalla;
        String horaFinBatalla ;

        List<Batallas> lstBatallas = new ArrayList<>();
        batallasRepository.findAll().forEach(lstBatallas::add);

        for(Batallas batalla : lstBatallas) {
            horaInicioBatalla = batalla.getFecha() + Constantes.ESPACIO + batalla.getHoraInicio();
            horaFinBatalla = batalla.getFecha() + Constantes.ESPACIO + batalla.getHoraFin();

            try {
                DashboardsGlobalResultadosValidation.validaBatalla(batalla);

                SimpleDateFormat formatter = new SimpleDateFormat(Constantes.FECHA_HORA_PATTERN);
                String fechaSistema = formatter.format(DateUtils.addMinutos(new Date(), Numeros.CINCO.getNumero()));

                if(DateUtils.isHoursRangoValid(horaInicioBatalla, horaFinBatalla,
                    fechaSistema, Constantes.FECHA_HORA_PATTERN)
                        && batalla.getEstatus().equals(EstadosBatalla.PENDIENTE.getEstado())) {

                    log.info("Ejecutando la batalla: " + batalla.getIdBatalla());

                    Etapas etapa = etapasRepository.findById(
                            batalla.getEtapaBatallasByIdBatalla().stream().findFirst().orElseThrow()
                                    .getIdEtapa()).orElseThrow();

                    batalla.setEstatus(EstadosBatalla.EN_PROCESO.getEstado());
                    batallasRepository.save(batalla);

                    RobotParticipanteDTO robotParticipanteDTO = numeroRobotsActivos(
                            batalla.getBatallaParticipantesByIdBatalla().stream().toList());

                     if(robotParticipanteDTO.getNumeroRobots() > Numeros.UNO.getNumero()) {
                         BattleRunner br = new BattleRunner(new Robocode(), batalla.getViewToken(), RECORDER,
                                 etapa.getReglas().getArenaAncho(), etapa.getReglas().getArenaAlto(),
                                 robotParticipanteDTO.getNombreRobotsActivos(),
                                 etapa.getReglas().getNumRondas());

                         br.runBattle(pathRobocode, REPLAY_TYPE);

                         dashboardsGlobalResultadosService.buscaSalidaBatallas(batalla);

                         batalla.setEstatus(EstadosBatalla.TERMINADA.getEstado());
                         batallasRepository.save(batalla);
                     } else if(robotParticipanteDTO.getNumeroRobots() == Numeros.UNO.getNumero()) {

                         Resultados resultados = new Resultados();
                         resultados.setTeamleadername(robotParticipanteDTO.getNombreRobotsActivos());
                         resultados.setRank(Numeros.UNO.getNumero());
                         resultados.setScore(Constantes.DEFAULT);
                         resultados.setSurvival(Constantes.CERO);
                         resultados.setLastsurvivorbonus(Constantes.CERO);
                         resultados.setBulletdamage(Constantes.CERO);
                         resultados.setBulletdamagebonus(Constantes.CERO);
                         resultados.setRamdamage(Constantes.CERO);
                         resultados.setRamdamagebonus(Constantes.CERO);
                         resultados.setFirsts(Numeros.CERO.getNumero());
                         resultados.setSeconds(Numeros.CERO.getNumero());
                         resultados.setThirds(Numeros.CERO.getNumero());
                         resultados.setVer(Numeros.CERO.getNumero());
                         resultados.setIdBatalla(batalla.getIdBatalla());
                         resultadosRepository.save(resultados);

                         batalla.setEstatus(EstadosBatalla.INCOMPLETA.getEstado());
                         batallasRepository.save(batalla);
                     } else {
                         log.info("No existen robots para la batalla: " + batalla.getIdBatalla());
                         batalla.setEstatus(EstadosBatalla.CANCELADA.getEstado());
                         batallasRepository.save(batalla);
                     }

                }

            } catch (ValidacionException | UtilsException e) {
                log.info(e.getMessage());
            }
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public RobotParticipanteDTO numeroRobotsActivos(List<BatallaParticipantes> batallaParticipantes) {

        StringBuilder robotClassName = new StringBuilder();
        String robots = null;
        int numRobot = 0;
        for(BatallaParticipantes batallaParticipante : batallaParticipantes) {
            Equipos equipos = equiposRepository
                    .findById(batallaParticipante.getIdParticipanteEquipo()).orElseThrow();

            Optional<Robots> robot = equipos.getRobotsByIdEquipo().stream()
                    .filter(r -> r.getActivo().equals(Numeros.UNO.getNumero())).findFirst();

            if(robot.isPresent()) {
                numRobot+=1;
                robotClassName.append(robot.orElseThrow().getClassName()).append(Constantes.COMA);
            }
        }

        if(!robotClassName.isEmpty()) {
            robots = robotClassName.substring(Numeros.CERO.getNumero(), robotClassName.length()-Numeros.UNO.getNumero());
        }

        return new RobotParticipanteDTO(numRobot, robots);

    }

    @Override
    public List<EtapasDTO> getEtapas() throws ValidacionException {

        List<Etapas> lstEtapas = new ArrayList<>();
        etapasRepository.findAll().forEach(lstEtapas::add);

        if(lstEtapas.isEmpty()) {
            throw new ValidacionException("No existen etapas a mostrar");
        }

        return lstEtapas.stream().map(EtapasDTO::new).toList();
    }

    @Override
    public List<InstitucionDTO> getInstituciones() throws ValidacionException {

        List<Institucion> lstInstituciones = new ArrayList<>();
        institucionRepository.findAll().forEach(lstInstituciones::add);

        if(lstInstituciones.isEmpty()) {
            throw new ValidacionException("No existen instituciones a mostrar");
        }

        return lstInstituciones.stream().map(InstitucionDTO::new).toList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ParticipanteDTO> getParticipantesByIdInstitucion(Integer idInstitucion) throws ValidacionException {

        List<ParticipanteDTO> participanteDTOS = new ArrayList<>();
        for(Participantes participantes : participantesRepository.findAllByInstitucionId(idInstitucion)
                .stream().filter(p -> !p.getParticipanteEquiposByIdParticipante().isEmpty()).toList()) {
            for(ParticipanteEquipo participanteEquipo : participantes.getParticipanteEquiposByIdParticipante()) {
                Optional<Equipos> equipos = equiposRepository.findById(participanteEquipo.getIdEquipo())
                        .filter(Equipos::isActivo);

                if(equipos.isPresent()) {
                    participanteDTOS.add(new ParticipanteDTO(participanteEquipo.getIdEquipo()));
                }
            }
        }

        for(ParticipanteDTO participanteDTO : participanteDTOS) {
            Equipos equipos = equiposRepository.findById(participanteDTO.getIdParticipante()).orElseThrow();

            StringBuilder nombres = new StringBuilder();
            for(ParticipanteEquipo participanteEquipo : equipos.getParticipanteEquiposByIdEquipo()) {
                Participantes participantes = participantesRepository.findById(participanteEquipo.getIdParticipante()).orElseThrow();
                nombres.append(participantes.getNombre()).append(Constantes.ESPACIO)
                        .append(participantes.getApellidos()).append(Constantes.COMA_ESPACIO);
            }

            participanteDTO.setNombre(nombres.substring(Numeros.CERO.getNumero(), nombres.length()-Numeros.DOS.getNumero()));
        }

        return participanteDTOS;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BatallasDTO generaBatallas(Integer idEtapa, Integer idInstitucion) {

        Etapas etapas = etapasRepository.findById(idEtapa).orElseThrow();
        BatallasDTO batallasDTO = new BatallasDTO();

        BatallasValidation.validaGeneraBatallas(etapas, idEtapa);

        Integer numCompetidores = etapas.getReglas().getNumCompetidores();
        Integer numRondas = etapas.getReglas().getNumRondas();

        EquiposDTO equiposDTO = torneosService.obtieneInstitucionEquipos(idEtapa, idInstitucion);

        List<BatallaFechaHoraInicioDTO> batallaFechaHoraInicioDTOS =
                torneosService.obtieneFechasBatalla(idEtapa, TorneoUtils
                        .calculaTotalBatallas(equiposDTO.getIdEquipos().size(), numCompetidores));

        if(etapas.getReglas().getTrabajo().equals(TipoBatalla.INDIVIDUAL.getTrabajo())) {
            batallaFechaHoraInicioDTOS =
                    torneosService.obtieneFechasBatalla(idEtapa, TorneoUtils
                            .calculaTotalBatallas(equiposDTO.getIdEquipos().size(), numCompetidores));
            for(InstitucionEquiposDTO institucionEquiposDTO : equiposDTO.getEquiposByInstitucion()) {
                if(!institucionEquiposDTO.getIdEquipos().isEmpty()) {
                    List<Integer> randomNumbers = TorneoUtils.armaEquipos(institucionEquiposDTO.getIdEquipos());
                    List<List<BatallaParticipanteDTO>> lists = torneosService.obtieneParticipantes(
                            randomNumbers, numCompetidores);

                    for(int x=Numeros.CERO.getNumero(); x<lists.size(); x++) {
                        BatallaDTO batallaDTO = new BatallaDTO(batallaFechaHoraInicioDTOS.get(x));
                        batallaDTO.setIdEtapa(idEtapa);
                        batallaDTO.setIdInstitucion(institucionEquiposDTO.getIdInstitucion());
                        batallaDTO.setBatallaParticipantes(lists.get(x));
                        batallaDTO.setRondas(numRondas);
                        batallasDTO.getBatallas().add(batallaDTO);
                    }
                }
            }
        } else if(etapas.getReglas().getTrabajo().equals(TipoBatalla.EQUIPO.getTrabajo())
                || etapas.getReglas().getTrabajo().equals(TipoBatalla.MIXTO.getTrabajo())) {

            List<Integer> randomNumbers = TorneoUtils.armaEquipos(equiposDTO.getIdEquipos());
            List<List<BatallaParticipanteDTO>> lists = torneosService.obtieneParticipantes(
                    randomNumbers, numCompetidores);

            BatallasValidation.validaGeneraBatallas(lists.size(), batallaFechaHoraInicioDTOS.size());

            for(int x=Numeros.CERO.getNumero(); x<lists.size(); x++) {
                BatallaDTO batallaDTO = new BatallaDTO(batallaFechaHoraInicioDTOS.get(x));
                batallaDTO.setIdEtapa(idEtapa);
                batallaDTO.setBatallaParticipantes(lists.get(x));
                batallaDTO.setRondas(numRondas);
                batallasDTO.getBatallas().add(batallaDTO);
            }

        }

        return batallasDTO;

    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveBatallas(BatallasDTO batallasDTO) throws ValidacionException {

        BatallasValidation.validaGuardaBatallas(batallasDTO);

        Batallas batallas;
        EtapaBatalla etapaBatalla;
        BatallaParticipantes batallaParticipantes;
        for(BatallaDTO batallaDTO : batallasDTO.getBatallas()) {
            batallas = new Batallas(batallaDTO);

            String token = UUID.randomUUID().toString();
            batallas.setViewToken(token);

            StringBuilder urlView = new StringBuilder(baseUrl).append(Constantes.DIAGONAL)
                    .append(htmlViewBattle).append(Constantes.PARAM_TOKEN).append(token);

            batallas.setViewUrl(urlView.toString());
            batallas = batallasRepository.save(batallas);

            batallaDTO.setIdBatalla(batallas.getIdBatalla());

            etapaBatalla = new EtapaBatalla(batallaDTO.getIdEtapa(), batallas.getIdBatalla());
            etapaBatallaRepository.save(etapaBatalla);

            for(BatallaParticipanteDTO batallaParticipanteDTO : batallaDTO.getBatallaParticipantes()) {
                batallaParticipantes = new BatallaParticipantes(batallaParticipanteDTO, batallas.getIdBatalla());
                batallaParticipantesRepository.save(batallaParticipantes);
            }
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BatallasDTO getBatallas(Integer idEtapa) {

        Etapas etapas = etapasRepository.findById(idEtapa).orElseThrow();

        BatallasDTO batallasDTO = new BatallasDTO();
        BatallaDTO batallaDTO;
        Batallas batallas;
        List<BatallaParticipanteDTO> batallaParticipantesDTO;
        for(EtapaBatalla etapaBatalla : etapas.getEtapaBatallasByIdEtapa()) {
            batallas = batallasRepository.findById(etapaBatalla.getIdBatalla()).orElseThrow();

            batallaDTO = new BatallaDTO();
            batallaDTO.setIdEtapa(idEtapa);
            batallaDTO.setIdBatalla(batallas.getIdBatalla());
            batallaDTO.setFecha(batallas.getFecha());
            batallaDTO.setHoraInicio(batallas.getHoraInicio());
            batallaDTO.setHoraFin(batallas.getHoraFin());

            batallaParticipantesDTO = new ArrayList<>();
            for(BatallaParticipantes batallaParticipantes : batallas.getBatallaParticipantesByIdBatalla()) {
                batallaParticipantesDTO.add(new BatallaParticipanteDTO(batallaParticipantes));
            }

            batallaDTO.setRondas(batallas.getRondas());
            batallaDTO.setBatallaParticipantes(batallaParticipantesDTO);
            batallasDTO.getBatallas().add(batallaDTO);
        }

        return batallasDTO;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void updateBatallas(BatallasDTO batallasDTO) throws ValidacionException {

        BatallasValidation.validaGuardaBatallas(batallasDTO);

        for(BatallaDTO batallaDTO : batallasDTO.getBatallas()) {

            Batallas batallas = batallasRepository.findById(batallaDTO.getIdBatalla()).orElseThrow();
            batallas.setFecha(batallaDTO.getFecha());
            batallas.setHoraInicio(batallaDTO.getHoraInicio());
            batallas.setHoraFin(batallaDTO.getHoraFin());
            batallas.setRondas(batallaDTO.getRondas());

            batallasRepository.save(batallas);

            if(batallaDTO.getBatallaParticipantes().size() != batallas.getBatallaParticipantesByIdBatalla().size()) {
                for(BatallaParticipantes batallaParticipantes : batallas.getBatallaParticipantesByIdBatalla()) {
                    batallaParticipantesRepository.deleteById(batallaParticipantes.getIdBatallaParticipante());
                }

                for(BatallaParticipanteDTO batallaParticipanteDTO : batallaDTO.getBatallaParticipantes()) {
                    BatallaParticipantes batallaParticipantes = new BatallaParticipantes(batallaParticipanteDTO, batallas.getIdBatalla());
                    batallaParticipantesRepository.save(batallaParticipantes);
                }
            }

        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteBatallas(Integer idEtapa) throws ValidacionException {

        Etapas etapas = etapasRepository.findById(idEtapa).orElseThrow();

        BatallasValidation.validaExistenBatallas(etapas);

        Batallas batallas;
        for(EtapaBatalla etapaBatalla : etapas.getEtapaBatallasByIdEtapa()) {
            batallas = batallasRepository.findById(etapaBatalla.getIdBatalla()).orElseThrow();

            for(BatallaParticipantes batallaParticipantes : batallas.getBatallaParticipantesByIdBatalla()) {
                batallaParticipantesRepository.deleteById(batallaParticipantes.getIdBatallaParticipante());
            }

            etapaBatallaRepository.deleteById(etapaBatalla.getIdEtapaBatalla());
            batallasRepository.deleteById(batallas.getIdBatalla());
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void reenvioCorreoBatalla(ParticipantesDTO participantesDTO) throws ValidacionException, UtilsException {

        if(participantesDTO.getIdParticipantes().isEmpty()){
            throw new ValidacionException("Debe enviar por lo menos un participante");
        }

        List<DatosCorreoBatallaDTO> mailsbatallas = new ArrayList<>();
        Integer numeroEtapa =  participantesDTO.getNumeroEtapa();
        participantesDTO.getIdParticipantes().forEach(
                id -> {
                    ParticipanteCorreo participanteCorreo = participantesRepository.findByCorreoReenvio(id, numeroEtapa);

                    if(participanteCorreo == null) {
                        throw new ValidacionException("El participante " + id + " no existe en la etapa especificada");
                    }

                    Batallas batalla = batallasRepository.findById(participanteCorreo.getIdBatalla())
                            .orElseThrow(() -> new ValidacionException("No existe batalla"));

                    DatosCorreoBatallaDTO mailBatallasDTO = new DatosCorreoBatallaDTO(
                            batalla.getFecha(),
                            batalla.getHoraInicio(),
                            batalla.getHoraFin(),
                            batalla.getRondas());

                    List<String> participantes = new ArrayList<>();
                    batallaParticipantesRepository.findAllByIdBatalla(participanteCorreo.getIdBatalla())
                            .forEach(batallaParticipantes ->
                                participantes.add(batallaParticipantes.getNombre())
                            );

                    mailBatallasDTO.setMailToParticipantes(participanteCorreo.getCorreo());
                    mailBatallasDTO.setParticipantes(participantes);
                    mailBatallasDTO.setUrlViewBattle(batalla.getViewUrl());
                    mailBatallasDTO.setBaseUrl(baseUrl);

                    mailsbatallas.add(mailBatallasDTO);

                }
        );

        // Envio Correo
        try {
            for(DatosCorreoBatallaDTO datosCorreoBatallaDTO : mailsbatallas) {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put(SendMailBatallaImpl.ID_PARAMS_TEMPLATE_MAILS, datosCorreoBatallaDTO);
                sendMailService.sendMail(
                        datosCorreoBatallaDTO.getMailToParticipantes(),
                        SendMailBatallaImpl.MAIL_TEMPLATE_KEY,
                        parameters);
                log.info("Correo reenviado correctamente: {}", datosCorreoBatallaDTO.getMailToParticipantes());
            }
        } catch (Exception e) {
            log.error("Error en el reenvioCorreoBatalla(): {}", StackTraceUtils.getCustomStackTrace(e));
        }

    }

}
