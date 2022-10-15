package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.exceptions.ValidacionException;

import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.request.torneo.*;
import com.mozcalti.gamingapp.response.batalla.BatallaResponse;
import com.mozcalti.gamingapp.response.batalla.BatallasResponse;
import com.mozcalti.gamingapp.response.batalla.ParticipanteResponse;
import com.mozcalti.gamingapp.service.*;
import com.mozcalti.gamingapp.utils.CollectionUtils;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import com.mozcalti.gamingapp.validations.CalendarizarEtapasTorneoValidation;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.analysis.function.Cos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CalendarizarEtapasTorneoServiceImpl implements CalendarizarEtapasTorneoService {

    private TorneosService torneosService;

    private TorneoHorasHabilesService torneoHorasHabilesService;

    private EtapasService etapasService;

    private ReglasService reglasService;

    private EquiposService equiposService;

    private EtapaEquipoService etapaEquipoService;

    private ParticipanteEquipoService participanteEquipoService;

    private ParticipantesService participantesService;

    private BatallasService batallasService;

    private BatallaParticipantesService batallaParticipantesService;

    private EtapaBatallaService etapaBatallaService;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveTorneo(TorneoRequest torneoRequest) throws SQLException, ValidacionException {

        CalendarizarEtapasTorneoValidation.validaSaveTorneo(torneoRequest);

        Torneos torneos = new Torneos(torneoRequest);
        torneos = torneosService.save(torneos);

        for(HoraHabilRequest horaHabilRequest : torneoRequest.getHorasHabilesRequest()) {
            torneoHorasHabilesService.save(new TorneoHorasHabiles(horaHabilRequest, torneos.getIdTorneo()));
        }

        Etapas etapas;
        Equipos equipos;
        // Etapas
        for (EtapaRequest etapaRequest : torneoRequest.getEtapasRequest()) {
            CalendarizarEtapasTorneoValidation.validaSaveTorneoEtapas(torneoRequest, etapaRequest);

            etapas = new Etapas(etapaRequest, torneos);
            etapas = etapasService.save(etapas);

            // Reglas
            reglasService.save(new Reglas(etapaRequest.getReglasRequest(), etapas));

            // Participantes
            if(etapaRequest.getReglasRequest().getTrabajo().equals(Constantes.INDIVIDUAL)) {
                for(ParticipanteRequest participanteRequest : etapaRequest.getParticipantesRequest()) {
                    equipos = new Equipos();
                    equipos = equiposService.save(equipos);

                    etapaEquipoService.save(new EtapaEquipo(etapas.getIdEtapa(), equipos.getIdEquipo()));

                    participanteEquipoService.save(new ParticipanteEquipo(participanteRequest.getParticipante(), equipos.getIdEquipo()));
                }
            } else if(etapaRequest.getReglasRequest().getTrabajo().equals(Constantes.EQUIPO)) {
                for(EquipoRequest equipoRequest : etapaRequest.getEquiposRequest()) {
                    equipos = new Equipos(equipoRequest);
                    equipos = equiposService.save(equipos);

                    etapaEquipoService.save(new EtapaEquipo(etapas.getIdEtapa(), equipos.getIdEquipo()));

                    for(Integer idParticipantes : equipoRequest.getParticipantes()) {
                        participanteEquipoService.save(new ParticipanteEquipo(idParticipantes, equipos.getIdEquipo()));
                    }
                }
            }

        }

    }

    @Override
    public BatallasResponse generaBatallas(Integer idEtapa) throws ValidacionException {

        Etapas etapas = etapasService.get(idEtapa);
        BatallasResponse batallas = new BatallasResponse();
        Torneos torneos;

        if(etapas != null) {

            torneos = torneosService.get(etapas.getIdTorneo());

            // Armamos equipos
            List<Integer> idEquipos = new ArrayList<>();
            for(EtapaEquipo etapaEquipo : etapas.getEtapaEquiposByIdEtapa()) {
                idEquipos.add(etapaEquipo.getIdEquipo());
            }

            List<Integer> randomNumbers = CollectionUtils.getRandomNumbers(idEquipos);

            BatallaResponse batallaResponse = null;
            ParticipanteResponse participanteResponse;
            Equipos equiposRandom;
            Participantes participantesEntity;
            int numCompetidores = 0;
            int totalParticipantes = etapas.getReglas().getNumCompetidores();
            String horaIni = torneos.getTorneoHorasHabilesByIdTorneo().stream().toList().get(0).getHoraIniHabil();
            String horaFin;
            List<ParticipanteResponse> participantes = null;
            for(Integer randomNumber : randomNumbers) {
                //System.out.println("----> " + randomNumber);
                equiposRandom = equiposService.get(randomNumber);

                if(etapas.getReglas().getTrabajo().equals(Constantes.INDIVIDUAL)) {
                    for(ParticipanteEquipo participanteEquipo : equiposRandom.getParticipanteEquiposByIdEquipo()) {

                        if(numCompetidores == 0) {
                            batallaResponse = new BatallaResponse();
                            participantes = new ArrayList<>();

                            System.out.println("---> Batalla...");
                            System.out.println("---> " + etapas.getFechaInicio());
                            System.out.println("---> " + horaIni);

                            batallaResponse.setIdEtapa(idEtapa);
                            batallaResponse.setHoraInicio(horaIni);
                            horaFin = DateUtils.addMinutos(horaIni,
                                    Constantes.HORA_PATTERN,
                                    etapas.getReglas().getTiempoBatallaAprox());
                            System.out.println("---> " + horaFin);
                            horaIni = DateUtils.addMinutos(horaFin,
                                    Constantes.HORA_PATTERN,
                                    etapas.getReglas().getTiempoEspera());
                            batallaResponse.setFecha(etapas.getFechaInicio());

                            batallaResponse.setHoraFin(horaFin);
                            batallaResponse.setRondas(etapas.getReglas().getNumRondas());

                            batallas.getBatallasResponse().add(batallaResponse);
                        }

                        if(numCompetidores < totalParticipantes) {
                            participantesEntity = participantesService.get(participanteEquipo.getIdParticipante());
                            System.out.println("-----> nombre: " + participantesEntity.getNombre());

                            participanteResponse = new ParticipanteResponse();
                            //participanteResponse.setIdParticipante(participantesEntity.getIdParticipante());
                            participanteResponse.setIdParticipante(participanteEquipo.getIdEquipo());
                            participanteResponse.setNombre(participantesEntity.getNombre() + " " +
                                    participantesEntity.getApellidos());
                            participantes.add(participanteResponse);

                            numCompetidores+=1;
                        }

                        if(numCompetidores == totalParticipantes) {
                            if(batallaResponse != null) {
                                batallaResponse.setParticipantes(participantes);
                            }

                            numCompetidores = 0;
                        }

                    }

                } else if(etapas.getReglas().getTrabajo().equals(Constantes.EQUIPO)) {

                    if(numCompetidores == 0) {
                        batallaResponse = new BatallaResponse();
                        participantes = new ArrayList<>();

                        System.out.println("---> Batalla...");
                        System.out.println("---> " + etapas.getFechaInicio());
                        System.out.println("---> " + horaIni);
                        batallaResponse.setHoraInicio(horaIni);

                        horaFin = DateUtils.addMinutos(horaIni,
                                Constantes.HORA_PATTERN,
                                etapas.getReglas().getTiempoBatallaAprox());
                        System.out.println("---> " + horaFin);
                        horaIni = DateUtils.addMinutos(horaFin,
                                Constantes.HORA_PATTERN,
                                etapas.getReglas().getTiempoEspera());

                        batallaResponse.setFecha(etapas.getFechaInicio());

                        batallaResponse.setHoraFin(horaFin);
                        batallaResponse.setRondas(etapas.getReglas().getNumRondas());

                        batallas.getBatallasResponse().add(batallaResponse);
                    }

                    if(numCompetidores < totalParticipantes) {
                        System.out.println("------> equipo: " + equiposRandom.getNombre());

                        participanteResponse = new ParticipanteResponse();
                        participanteResponse.setIdParticipante(equiposRandom.getIdEquipo());
                        participanteResponse.setNombre(equiposRandom.getNombre());
                        participantes.add(participanteResponse);

                        numCompetidores+=1;
                    }

                    if(numCompetidores == totalParticipantes) {
                        if(batallaResponse != null) {
                            batallaResponse.setParticipantes(participantes);
                        }

                        numCompetidores = 0;
                    }

                }

            }

            if(batallaResponse != null) {
                batallaResponse.setParticipantes(participantes);
            }

        } else {
            throw new ValidacionException("No existe la etapa con el id indicado: " + idEtapa);
        }

        return batallas;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public BatallasResponse saveBatallas(BatallasResponse batallasResponse) throws ValidacionException {

        if(batallasResponse.getBatallasResponse().isEmpty()) {
            throw new ValidacionException("No existen batallas para guardar");
        }

        Batallas batallas;
        EtapaBatalla etapaBatalla;
        BatallaParticipantes batallaParticipantes;
        for(BatallaResponse batallaResponse : batallasResponse.getBatallasResponse()) {

            if(!etapasService.get(batallaResponse.getIdEtapa()).getEtapaBatallasByIdEtapa().isEmpty()) {
                throw new ValidacionException("Ya existen batallas para el idEtapa: " + batallaResponse.getIdEtapa());
            }

            batallas = new Batallas(batallaResponse);
            batallas = batallasService.save(batallas);

            batallaResponse.setIdBatalla(batallas.getIdBatalla());

            etapaBatalla = new EtapaBatalla(batallaResponse.getIdEtapa(), batallas.getIdBatalla());
            etapaBatallaService.save(etapaBatalla);

            for(ParticipanteResponse participanteResponse : batallaResponse.getParticipantes()) {
                batallaParticipantes = new BatallaParticipantes(participanteResponse, batallas.getIdBatalla());
                batallaParticipantesService.save(batallaParticipantes);
            }

        }

        return batallasResponse;

    }

    @Override
    public List<DatosCorreoBatallaDTO> getDatosCorreoBatalla() throws ValidacionException {

        List<DatosCorreoBatallaDTO> mailsbatallas = new ArrayList<>();
        DatosCorreoBatallaDTO mailBatallasDTO;
        List<String> participante;
        Batallas batallas;
        Equipos equipos;
        StringBuilder mailToParticipantes = new StringBuilder("");

        String fechaSistema = DateUtils.getDateFormat(Calendar.getInstance().getTime(), Constantes.FECHA_PATTERN);

        for(EtapaBatalla etapaBatalla : etapaBatallaService.getAll()) {

            batallas = batallasService.get(etapaBatalla.getIdBatalla());

            if(fechaSistema.equals(batallas.getFecha()) && batallas.getBndEnvioCorreo().equals(0)) {
                mailBatallasDTO = new DatosCorreoBatallaDTO(batallas.getFecha(),
                        batallas.getHoraInicio(), batallas.getHoraFin(),
                        batallas.getRondas());

                participante = new ArrayList<>();
                for(BatallaParticipantes batallaParticipantes : batallas.getBatallaParticipantesByIdBatalla()) {

                    participante.add(batallaParticipantes.getNombre());

                    equipos = equiposService.get(batallaParticipantes.getIdParticipanteEquipo());

                    for(ParticipanteEquipo participanteEquipo : equipos.getParticipanteEquiposByIdEquipo()) {
                        mailToParticipantes.append(participantesService.get(participanteEquipo.getIdParticipante()).getCorreo()).append(",");
                    }
                    mailBatallasDTO.setMailToParticipantes(mailToParticipantes.substring(0, mailToParticipantes.length()-1));
                }
                mailBatallasDTO.setParticipantes(participante);
                mailsbatallas.add(mailBatallasDTO);

                batallas.setBndEnvioCorreo(1);
                batallasService.save(batallas);
            }

        }

        return mailsbatallas;

    }


}
