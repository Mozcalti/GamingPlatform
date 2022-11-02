package com.mozcalti.gamingapp.service.batallas.impl;

import com.mozcalti.gamingapp.exceptions.ValidacionException;

import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.batallas.BatallaFechaHoraInicioDTO;
import com.mozcalti.gamingapp.model.batallas.BatallaParticipanteDTO;
import com.mozcalti.gamingapp.model.participantes.EquiposDTO;
import com.mozcalti.gamingapp.model.participantes.InstitucionEquiposDTO;
import com.mozcalti.gamingapp.model.batallas.BatallaDTO;
import com.mozcalti.gamingapp.model.batallas.BatallasDTO;
import com.mozcalti.gamingapp.service.batallas.BatallaParticipantesService;
import com.mozcalti.gamingapp.service.batallas.BatallasService;
import com.mozcalti.gamingapp.service.batallas.CalendarizarEtapasTorneoService;
import com.mozcalti.gamingapp.service.batallas.EtapaBatallaService;
import com.mozcalti.gamingapp.service.torneo.EtapasService;
import com.mozcalti.gamingapp.service.torneo.TorneosService;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.Numeros;
import com.mozcalti.gamingapp.utils.TipoBatalla;
import com.mozcalti.gamingapp.utils.TorneoUtils;
import com.mozcalti.gamingapp.validations.CalendarizarEtapasTorneoValidation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CalendarizarEtapasTorneoServiceImpl implements CalendarizarEtapasTorneoService {

    private TorneosService torneosService;

    private EtapasService etapasService;

    private BatallasService batallasService;

    private BatallaParticipantesService batallaParticipantesService;

    private EtapaBatallaService etapaBatallaService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BatallasDTO generaBatallas(Integer idEtapa) {

        Etapas etapas = etapasService.get(idEtapa);
        BatallasDTO batallasDTO = new BatallasDTO();

        CalendarizarEtapasTorneoValidation.validaGeneraBatallas(etapas, idEtapa);

        Integer numCompetidores = etapas.getReglas().getNumCompetidores();
        Integer numRondas = etapas.getReglas().getNumRondas();

        EquiposDTO equiposDTO = torneosService.obtieneInstitucionEquipos();

        List<BatallaFechaHoraInicioDTO> batallaFechaHoraInicioDTOS =
                torneosService.obtieneFechasBatalla(idEtapa, TorneoUtils
                        .calculaTotalBatallas(equiposDTO.getIdEquipos().size(), numCompetidores));

        if(etapas.getReglas().getTrabajo().equals(TipoBatalla.INDIVIDUAL.getTrabajo())) {
            for(InstitucionEquiposDTO institucionEquiposDTO : equiposDTO.getEquiposByInstitucion()) {
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
        } else if(etapas.getReglas().getTrabajo().equals(TipoBatalla.EQUIPO.getTrabajo())) {
            List<Integer> randomNumbers = TorneoUtils.armaEquipos(equiposDTO.getIdEquipos());
            List<List<BatallaParticipanteDTO>> lists = torneosService.obtieneParticipantes(
                    randomNumbers, numCompetidores);

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

        if(batallasDTO.getBatallas().isEmpty()) {
            throw new ValidacionException("No existen batallas para guardar");
        }

        Batallas batallas;
        EtapaBatalla etapaBatalla;
        BatallaParticipantes batallaParticipantes;
        for(BatallaDTO batallaDTO : batallasDTO.getBatallas()) {

            if(!etapasService.get(batallaDTO.getIdEtapa()).getEtapaBatallasByIdEtapa().isEmpty()) {
                throw new ValidacionException("Ya existen batallas para el idEtapa: " + batallaDTO.getIdEtapa());
            }

            batallas = new Batallas(batallaDTO);
            batallas = batallasService.save(batallas);

            batallaDTO.setIdBatalla(batallas.getIdBatalla());

            etapaBatalla = new EtapaBatalla(batallaDTO.getIdEtapa(), batallas.getIdBatalla());
            etapaBatallaService.save(etapaBatalla);

            for(BatallaParticipanteDTO batallaParticipanteDTO : batallaDTO.getBatallaParticipantes()) {
                batallaParticipantes = new BatallaParticipantes(batallaParticipanteDTO, batallas.getIdBatalla());
                batallaParticipantesService.save(batallaParticipantes);
            }

        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BatallasDTO getBatallas(Integer idEtapa) {

        Etapas etapas = etapasService.get(idEtapa);

        CalendarizarEtapasTorneoValidation.validaExistenBatallas(etapas);

        BatallasDTO batallasDTO = new BatallasDTO();
        BatallaDTO batallaDTO;
        Batallas batallas;
        List<BatallaParticipanteDTO> batallaParticipantesDTO;
        for(EtapaBatalla etapaBatalla : etapas.getEtapaBatallasByIdEtapa()) {
            batallas = batallasService.get(etapaBatalla.getIdBatalla());

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

        if(batallasDTO.getBatallas().isEmpty()) {
            throw new ValidacionException("No existen batallas para modificar");
        }

        for(BatallaDTO batallaDTO : batallasDTO.getBatallas()) {

            Batallas batallas = batallasService.get(batallaDTO.getIdBatalla());
            batallas.setFecha(batallaDTO.getFecha());
            batallas.setHoraInicio(batallaDTO.getHoraInicio());
            batallas.setHoraFin(batallaDTO.getHoraFin());
            batallas.setRondas(batallaDTO.getRondas());

            batallasService.save(batallas);

            if(batallaDTO.getBatallaParticipantes().size() != batallas.getBatallaParticipantesByIdBatalla().size()) {
                for(BatallaParticipantes batallaParticipantes : batallas.getBatallaParticipantesByIdBatalla()) {
                    batallaParticipantesService.delete(batallaParticipantes.getIdBatallaParticipante());
                }

                for(BatallaParticipanteDTO batallaParticipanteDTO : batallaDTO.getBatallaParticipantes()) {
                    BatallaParticipantes batallaParticipantes = new BatallaParticipantes(batallaParticipanteDTO, batallas.getIdBatalla());
                    batallaParticipantesService.save(batallaParticipantes);
                }
            }

        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteBatallas(Integer idEtapa) throws ValidacionException {

        Etapas etapas = etapasService.get(idEtapa);

        CalendarizarEtapasTorneoValidation.validaExistenBatallas(etapas);

        Batallas batallas;
        for(EtapaBatalla etapaBatalla : etapas.getEtapaBatallasByIdEtapa()) {
            batallas = batallasService.get(etapaBatalla.getIdBatalla());

            for(BatallaParticipantes batallaParticipantes : batallas.getBatallaParticipantesByIdBatalla()) {
                batallaParticipantesService.delete(batallaParticipantes.getIdBatallaParticipante());
            }

            etapaBatallaService.delete(etapaBatalla.getIdEtapaBatalla());
            batallasService.delete(batallas.getIdBatalla());
        }

    }

}
