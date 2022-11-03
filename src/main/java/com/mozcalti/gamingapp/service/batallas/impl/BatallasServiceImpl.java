package com.mozcalti.gamingapp.service.batallas.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.catalogos.EtapasDTO;
import com.mozcalti.gamingapp.model.catalogos.InstitucionDTO;
import com.mozcalti.gamingapp.repository.BatallasRepository;
import com.mozcalti.gamingapp.repository.EquiposRepository;
import com.mozcalti.gamingapp.repository.EtapasRepository;
import com.mozcalti.gamingapp.repository.InstitucionRepository;
import com.mozcalti.gamingapp.robocode.BattleRunner;
import com.mozcalti.gamingapp.robocode.Robocode;
import com.mozcalti.gamingapp.service.batallas.BatallasService;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import com.mozcalti.gamingapp.utils.EstadosBatalla;
import com.mozcalti.gamingapp.utils.Numeros;
import com.mozcalti.gamingapp.validations.DashboardsGlobalResultadosValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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

    @Override
    public CrudRepository<Batallas, Integer> getDao() {
        return batallasRepository;
    }

    @Value("${robocode.executable}")
    private String pathRobocode;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void ejecutaBatalla() {

        String horaInicioBatalla;
        String horaFinBatalla ;

        List<Batallas> lstBatallas = new ArrayList<>();
        batallasRepository.findAll().forEach(lstBatallas::add);

        for(Batallas batallas : lstBatallas) {
            horaInicioBatalla = batallas.getFecha() + Constantes.ESPACIO + batallas.getHoraInicio();
            horaFinBatalla = batallas.getFecha() + Constantes.ESPACIO + batallas.getHoraFin();

            try {
                DashboardsGlobalResultadosValidation.validaBatalla(batallas);

                String fechaSistema = DateUtils.getDateFormat(Calendar.getInstance().getTime(), Constantes.FECHA_HORA_PATTERN);

                if(DateUtils.isHoursRangoValid(horaInicioBatalla, horaFinBatalla,
                    fechaSistema, Constantes.FECHA_HORA_PATTERN)
                        && batallas.getEstatus().equals(EstadosBatalla.PENDIENTE.getEstado())) {

                    log.info("Ejecutando la batalla: " + batallas.getIdBatalla());

                    Etapas etapa = etapasRepository.findById(
                            batallas.getEtapaBatallasByIdBatalla().stream().findFirst().orElseThrow()
                                    .getIdEtapa()).orElseThrow();

                    batallas.setEstatus(EstadosBatalla.EN_PROCESO.getEstado());
                    batallasRepository.save(batallas);

                     if(obtieneRobots(batallas.getBatallaParticipantesByIdBatalla().stream().toList()) != null) {
                         BattleRunner br = new BattleRunner(new Robocode(), String.valueOf(batallas.getIdBatalla()), RECORDER,
                                 etapa.getReglas().getArenaAncho(), etapa.getReglas().getArenaAlto(),
                                 obtieneRobots(batallas.getBatallaParticipantesByIdBatalla().stream().toList()),
                                 etapa.getReglas().getNumRondas());

                         br.runBattle(pathRobocode, REPLAY_TYPE);

                         batallas.setEstatus(EstadosBatalla.TERMINADA.getEstado());
                         batallasRepository.save(batallas);
                     } else {
                         log.info("No existen robots para la batalla: " + batallas.getIdBatalla());
                         batallas.setEstatus(EstadosBatalla.CANCELADA.getEstado());
                         batallasRepository.save(batallas);
                     }

                }

            } catch (ValidacionException | UtilsException e) {
                log.info(e.getMessage());
            }
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String obtieneRobots(List<BatallaParticipantes> batallaParticipantes) {

        StringBuilder robotClassName = new StringBuilder();
        String robots = null;
        for(BatallaParticipantes batallaParticipante : batallaParticipantes) {
            Equipos equipos = equiposRepository
                    .findById(batallaParticipante.getIdParticipanteEquipo()).orElseThrow();

            Optional<Robots> robot = equipos.getRobotsByIdEquipo().stream()
                    .filter(r -> r.getActivo().equals(Numeros.UNO.getNumero())).findFirst();

            if(robot.isPresent()) {
                robotClassName.append(robot.orElseThrow().getClassName()).append(Constantes.COMA);
            }
        }

        if(!robotClassName.isEmpty()) {
            robots = robotClassName.substring(Numeros.CERO.getNumero(), robotClassName.length()-Numeros.UNO.getNumero());
        }

        return robots;
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

}
