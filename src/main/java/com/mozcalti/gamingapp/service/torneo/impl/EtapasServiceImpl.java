package com.mozcalti.gamingapp.service.torneo.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosInstitucionGpoDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosParticipantesDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosParticipantesGpoDTO;
import com.mozcalti.gamingapp.repository.*;
import com.mozcalti.gamingapp.service.torneo.EtapasService;
import com.mozcalti.gamingapp.service.dashboard.DashboardService;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.Numeros;
import com.mozcalti.gamingapp.utils.TorneoUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class EtapasServiceImpl extends GenericServiceImpl<Etapas, Integer> implements EtapasService {

    private EtapasRepository etapasRepository;

    private DashboardService dashboardService;

    private EquiposRepository equiposRepository;

    private EtapaEquipoRepository etapaEquipoRepository;

    private ParticipanteEquipoRepository participanteEquipoRepository;

    private RobotsRepository robotsRepository;

    @Override
    public CrudRepository<Etapas, Integer> getDao() {
        return etapasRepository;
    }

    @Override
    public List<ResultadosInstitucionGpoDTO> obtieneParticipantesByInstitucion(Integer idEtapa)
            throws ValidacionException {

        if(!etapasRepository.findById(idEtapa).isPresent()) {
            throw new ValidacionException("No existe la estapa indicada");
        }

        return dashboardService.gruopResultadosParticipantesBatalla(idEtapa);

    }

    @Override
    public Etapas obtieneSiguienteEtapa(Integer idEtapa) {

        List<Etapas> lstEtapas = new ArrayList<>();
        etapasRepository.findAll().forEach(lstEtapas::add);

        boolean encontrado = Boolean.FALSE;
        int posEtapa = Numeros.CERO.getNumero();
        for(Etapas etapa : lstEtapas.stream().sorted(Comparator.comparing(Etapas::getIdEtapa)).toList()) {
            if(etapa.getIdEtapa() == idEtapa && !encontrado) {
                encontrado = Boolean.TRUE;
            } else if(!encontrado) {
                posEtapa+=Numeros.UNO.getNumero();
            }
        }

        return lstEtapas.get(posEtapa+Numeros.UNO.getNumero());
    }

    @Override
    public void desactivaEquipos() {

        List<Equipos> lstEquiposUpdate = new ArrayList<>();
        equiposRepository.findAll().forEach(lstEquiposUpdate::add);

        for(Equipos equipo : lstEquiposUpdate) {
            equipo.setActivo(Boolean.FALSE);
            equiposRepository.save(equipo);
        }

        List<Robots> lstRobots = new ArrayList<>();
        robotsRepository.findAll().forEach(lstRobots::add);

        for(Robots robot : lstRobots) {
            robot.setActivo(Numeros.CERO.getNumero());
            robotsRepository.save(robot);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void finEtapaInstitucion(Integer idEtapa, Integer numParticipantes) {

        List<ResultadosInstitucionGpoDTO> resultadosInstitucion =
                TorneoUtils.obtieneParticipantesInstitucion(obtieneParticipantesByInstitucion(idEtapa), numParticipantes);

        Etapas etapas = obtieneSiguienteEtapa(idEtapa);

        desactivaEquipos();

        for(ResultadosInstitucionGpoDTO resultado : resultadosInstitucion) {
            Equipos equipos = equiposRepository.save(new Equipos(Boolean.TRUE));
            etapaEquipoRepository.save(new EtapaEquipo(etapas.getIdEtapa(), equipos.getIdEquipo()));

            for(ResultadosParticipantesGpoDTO participantesGpoDTO : resultado.getParticipantes()) {
                participanteEquipoRepository.save(new ParticipanteEquipo(participantesGpoDTO.getIdParticipante(), equipos.getIdEquipo()));
            }
        }

    }

    @Override
    public List<ResultadosParticipantesDTO> obtieneParticipantes(Integer idEtapa) {

        if(!etapasRepository.findById(idEtapa).isPresent()) {
            throw new ValidacionException("No existe la estapa indicada");
        }

        return dashboardService.listaResultadosParticipantesBatalla(idEtapa, Constantes.TODOS);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void finEtapaGlobal(Integer idEtapa, Integer numParticipantes) {

        List<ResultadosParticipantesDTO> resultadosParticipantesDTOS = obtieneParticipantes(idEtapa);

        Etapas etapas = obtieneSiguienteEtapa(idEtapa);

        desactivaEquipos();

        for(ResultadosParticipantesDTO resultado : resultadosParticipantesDTOS.subList(Numeros.CERO.getNumero(), numParticipantes)) {
            Equipos equipos = equiposRepository.save(new Equipos(Boolean.TRUE));
            etapaEquipoRepository.save(new EtapaEquipo(etapas.getIdEtapa(), equipos.getIdEquipo()));
            participanteEquipoRepository.save(new ParticipanteEquipo(resultado.getIdParticipante(), equipos.getIdEquipo()));
        }

    }

}
