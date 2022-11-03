package com.mozcalti.gamingapp.service.torneo.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosInstitucionGpoDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosParticipantesGpoDTO;
import com.mozcalti.gamingapp.repository.EquiposRepository;
import com.mozcalti.gamingapp.repository.EtapaEquipoRepository;
import com.mozcalti.gamingapp.repository.ParticipanteEquipoRepository;
import com.mozcalti.gamingapp.service.torneo.EtapasService;
import com.mozcalti.gamingapp.repository.EtapasRepository;
import com.mozcalti.gamingapp.service.dashboard.DashboardService;
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

    @Override
    public CrudRepository<Etapas, Integer> getDao() {
        return etapasRepository;
    }

    @Override
    public List<ResultadosInstitucionGpoDTO> obtieneParticipantes(Integer idEtapa) throws ValidacionException {

        if(!etapasRepository.findById(idEtapa).isPresent()) {
            throw new ValidacionException("No existe la estapa indicada");
        }

        return dashboardService.gruopResultadosParticipantesBatalla(idEtapa);
    }

    @Override
    public Etapas obtieneSiguieteEtapa(Integer idEtapa) {

        List<Etapas> lstEtapas = new ArrayList<>();
        etapasRepository.findAll().forEach(lstEtapas::add);

        boolean encontrado = false;
        int posEtapa = 0;
        for(Etapas etapa : lstEtapas.stream().sorted(Comparator.comparing(Etapas::getIdEtapa)).toList()) {
            if(etapa.getIdEtapa() == idEtapa && !encontrado) {
                encontrado = true;
            } else if(!encontrado) {
                posEtapa+=1;
            }
        }

        return lstEtapas.get(posEtapa+1);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void finEtapaInstitucion(Integer idEtapa, Integer numParticipantes) {
        List<ResultadosInstitucionGpoDTO> resultadosInstitucionGpoDTOS =
                TorneoUtils.obtieneParticipantesInstitucion(obtieneParticipantes(idEtapa), numParticipantes);

        Etapas etapas = obtieneSiguieteEtapa(idEtapa);

        List<Equipos> lstEquiposUpdate = new ArrayList<>();
        equiposRepository.findAll().forEach(lstEquiposUpdate::add);

        for(Equipos equipo : lstEquiposUpdate) {
            equipo.setActivo(false);
            equiposRepository.save(equipo);
        }

        for(ResultadosInstitucionGpoDTO resultado : resultadosInstitucionGpoDTOS) {
            log.info("----> " + resultado.getNombreInstitucion());

            Equipos equipos = equiposRepository.save(new Equipos(true));
            etapaEquipoRepository.save(new EtapaEquipo(etapas.getIdEtapa(), equipos.getIdEquipo()));

            for(ResultadosParticipantesGpoDTO participantesGpoDTO : resultado.getParticipantes()) {
                log.info("--> " + participantesGpoDTO.getNombreParticipantes());
                participanteEquipoRepository.save(new ParticipanteEquipo(participantesGpoDTO.getIdParticipante(), equipos.getIdEquipo()));
            }
        }

    }

    @Override
    public void finEtapaGlobal(Integer numParticipantes) {

    }

}
