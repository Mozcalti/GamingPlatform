package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.exceptions.ValidacionException;

import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.model.torneos.*;
import com.mozcalti.gamingapp.model.batallas.BatallaDTO;
import com.mozcalti.gamingapp.model.batallas.BatallasDTO;
import com.mozcalti.gamingapp.service.*;
import com.mozcalti.gamingapp.utils.CollectionUtils;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import com.mozcalti.gamingapp.validations.CalendarizarEtapasTorneoValidation;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
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
    public void saveTorneo(TorneoDTO torneoDTO) throws SQLException, ValidacionException {

        CalendarizarEtapasTorneoValidation.validaSaveTorneo(torneoDTO);

        Torneos torneos = new Torneos(torneoDTO);
        torneos = torneosService.save(torneos);

        for(HoraHabilDTO horaHabilDTO : torneoDTO.getHorasHabiles()) {
            torneoHorasHabilesService.save(new TorneoHorasHabiles(horaHabilDTO, torneos.getIdTorneo()));
        }

        Etapas etapas;
        Equipos equipos;
        // Etapas
        for (EtapaDTO etapaDTO : torneoDTO.getEtapas()) {
            CalendarizarEtapasTorneoValidation.validaSaveTorneoEtapas(torneoDTO, etapaDTO);

            etapas = new Etapas(etapaDTO, torneos);
            etapas = etapasService.save(etapas);

            // Reglas
            reglasService.save(new Reglas(etapaDTO.getReglas(), etapas.getIdEtapa()));

            // Participantes
            if(etapaDTO.getReglas().getTrabajo().equals(Constantes.INDIVIDUAL)) {
                for(ParticipanteDTO participanteDTO : etapaDTO.getParticipantes()) {
                    equipos = new Equipos();
                    equipos = equiposService.save(equipos);

                    etapaEquipoService.save(new EtapaEquipo(etapas.getIdEtapa(), equipos.getIdEquipo()));

                    participanteEquipoService.save(new ParticipanteEquipo(participanteDTO.getParticipante(), equipos.getIdEquipo()));
                }
            } else if(etapaDTO.getReglas().getTrabajo().equals(Constantes.EQUIPO)) {
                for(EquipoDTO equipoDTO : etapaDTO.getEquipos()) {
                    equipos = new Equipos(equipoDTO);
                    equipos = equiposService.save(equipos);

                    etapaEquipoService.save(new EtapaEquipo(etapas.getIdEtapa(), equipos.getIdEquipo()));

                    for(Integer idParticipantes : equipoDTO.getParticipantes()) {
                        participanteEquipoService.save(new ParticipanteEquipo(idParticipantes, equipos.getIdEquipo()));
                    }
                }
            }

        }

    }

    @Override
    public TorneoDTO getTorneo(int idTorneo) throws ValidacionException {

        Torneos torneos = torneosService.get(idTorneo);

        if(torneos == null) {
            throw new ValidacionException("No existe en torneo con el id indicado: " + idTorneo);
        }

        TorneoDTO torneoDTO = new TorneoDTO(torneos, idTorneo);
        List<HoraHabilDTO> horasHabiles = new ArrayList<>();

        for(TorneoHorasHabiles torneoHorasHabiles : torneos.getTorneoHorasHabilesByIdTorneo()) {
            horasHabiles.add(new HoraHabilDTO(torneoHorasHabiles));
        }

        torneoDTO.setHorasHabiles(horasHabiles);

        List<EtapaDTO> etapasDTO = new ArrayList<>();
        EtapaDTO etapaDTO;
        Equipos equipos;
        Participantes participantes;
        List<ParticipanteDTO> participantesDTO = new ArrayList<>();
        EquipoDTO equipoDTO;
        List<EquipoDTO> equiposDTO = new ArrayList<>();
        List<Integer> equipoParticipantes;
        for(Etapas etapas : torneos.getEtapasByIdTorneo()
                .stream().sorted(Comparator.comparing(Etapas::getNumeroEtapa)).toList()) {
            etapaDTO = new EtapaDTO(etapas);

            for(EtapaEquipo etapaEquipo : etapas.getEtapaEquiposByIdEtapa()) {
                equipos = equiposService.get(etapaEquipo.getIdEquipo());

                if(etapas.getReglas().getTrabajo().equals(Constantes.INDIVIDUAL)) {
                    for(ParticipanteEquipo participanteEquipo : equipos.getParticipanteEquiposByIdEquipo()){
                        participantes = participantesService.get(participanteEquipo.getIdParticipante());
                        participantesDTO.add(new ParticipanteDTO(participantes.getIdParticipante()));
                    }

                    etapaDTO.setParticipantes(participantesDTO);
                } else if(etapas.getReglas().getTrabajo().equals(Constantes.EQUIPO)) {
                    equipoDTO = new EquipoDTO();
                    equipoDTO.setNombreEquipo(equipos.getNombre());

                    equipoParticipantes = new ArrayList<>();
                    for(ParticipanteEquipo participanteEquipo : equipos.getParticipanteEquiposByIdEquipo()){
                        equipoParticipantes.add(participanteEquipo.getIdParticipante());
                        equipoDTO.setParticipantes(equipoParticipantes);
                    }
                    equiposDTO.add(equipoDTO);
                    etapaDTO.setEquipos(equiposDTO);
                }
            }

            etapasDTO.add(etapaDTO);

        }

        torneoDTO.setEtapas(etapasDTO);

        return torneoDTO;

    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateTorneo(TorneoDTO torneoDTO) throws ValidacionException {

        CalendarizarEtapasTorneoValidation.validaSaveTorneo(torneoDTO);

        Torneos torneos = new Torneos(torneoDTO);
        torneos = torneosService.save(torneos);

        for(HoraHabilDTO horaHabilDTO : torneoDTO.getHorasHabiles()) {
            torneoHorasHabilesService.save(new TorneoHorasHabiles(horaHabilDTO, torneos.getIdTorneo()));
        }

        // Etapas
        for (EtapaDTO etapaDTO : torneoDTO.getEtapas()) {
            CalendarizarEtapasTorneoValidation.validaSaveTorneoEtapas(torneoDTO, etapaDTO);

            Etapas etapasUpdate = etapasService.get(etapaDTO.getIdEtapa());

            etapasUpdate.setNumeroEtapa(etapaDTO.getNumeroEtapa());
            etapasUpdate.setFechaInicio(etapaDTO.getFechaInicio());
            etapasUpdate.setFechaFin(etapaDTO.getFechaFin());
            etapasService.save(etapasUpdate);

            // Reglas
            reglasService.save(new Reglas(etapaDTO.getReglas(), etapasUpdate.getIdEtapa()));

            // Participantes
            if((etapaDTO.getParticipantes() != null && etapaDTO.getParticipantes().size() != etapasUpdate.getEtapaEquiposByIdEtapa().size())
                    || (etapaDTO.getEquipos() != null)) {
                // Eliminando Participantes
                Equipos equipos = null;
                for(EtapaEquipo etapaEquipo : etapasUpdate.getEtapaEquiposByIdEtapa()) {
                    equipos = equiposService.get(etapaEquipo.getIdEquipo());

                    if(etapaDTO.getReglas().getTrabajo().equals(Constantes.INDIVIDUAL)) {
                        for(ParticipanteEquipo participanteEquipo : equipos.getParticipanteEquiposByIdEquipo()) {
                            participanteEquipoService.delete(participanteEquipo.getIdParticipanteEquipo());
                        }

                        etapaEquipoService.delete(etapaEquipo.getIdEtapaEquipo());
                        equiposService.delete(etapaEquipo.getIdEquipo());
                    } else if(etapaDTO.getReglas().getTrabajo().equals(Constantes.EQUIPO)) {

                        for(EquipoDTO equipoDTO : etapaDTO.getEquipos()) {
                            if(equipoDTO.getNombreEquipo().equals(equipos.getNombre())) {
                                if(equipoDTO.getParticipantes().size() != equipos.getParticipanteEquiposByIdEquipo().size()) {
                                    equipoDTO.setBndCambioEquipo(1);

                                    for(ParticipanteEquipo participanteEquipo : equipos.getParticipanteEquiposByIdEquipo()) {
                                        participanteEquipoService.delete(participanteEquipo.getIdParticipanteEquipo());
                                    }

                                    etapaEquipoService.delete(etapaEquipo.getIdEtapaEquipo());
                                    equiposService.delete(etapaEquipo.getIdEquipo());
                                } else {
                                    equipoDTO.setBndCambioEquipo(0);
                                }
                            }
                        }
                    }
                }

                // Alta Participantes
                if(etapaDTO.getReglas().getTrabajo().equals(Constantes.INDIVIDUAL)) {
                    for(ParticipanteDTO participanteDTO : etapaDTO.getParticipantes()) {
                        equipos = new Equipos();
                        equipos = equiposService.save(equipos);

                        etapaEquipoService.save(new EtapaEquipo(etapasUpdate.getIdEtapa(), equipos.getIdEquipo()));

                        participanteEquipoService.save(new ParticipanteEquipo(participanteDTO.getParticipante(), equipos.getIdEquipo()));
                    }
                } else if(etapaDTO.getReglas().getTrabajo().equals(Constantes.EQUIPO)) {
                    for(EquipoDTO equipoDTO : etapaDTO.getEquipos()) {
                        if(equipoDTO.getBndCambioEquipo() == 1) {
                            equipos = new Equipos(equipoDTO);
                            equipos = equiposService.save(equipos);

                            etapaEquipoService.save(new EtapaEquipo(etapasUpdate.getIdEtapa(), equipos.getIdEquipo()));

                            for(Integer idParticipantes : equipoDTO.getParticipantes()) {
                                participanteEquipoService.save(new ParticipanteEquipo(idParticipantes, equipos.getIdEquipo()));
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteTorneo(int idTorneo) throws ValidacionException {

        Torneos torneosDelete = torneosService.get(idTorneo);

        if(torneosDelete == null) {
            throw new ValidacionException("No existe el torneo a eliminar");
        }

        for(TorneoHorasHabiles torneoHorasHabiles : torneosDelete.getTorneoHorasHabilesByIdTorneo()) {
            torneoHorasHabilesService.delete(torneoHorasHabiles.getIdHoraHabil());
        }

        for(Etapas etapas : torneosDelete.getEtapasByIdTorneo()) {

            if(etapas.getEtapaBatallasByIdEtapa() != null && etapas.getEtapaBatallasByIdEtapa().size() > 0) {
                throw new ValidacionException("No es posible eliminar un torneo con batallas relacionadas");
            }

            reglasService.delete(etapas.getReglas().getIdRegla());

            Equipos equipos;
            for(EtapaEquipo etapaEquipo : etapas.getEtapaEquiposByIdEtapa()) {
                equipos = equiposService.get(etapaEquipo.getIdEquipo());

                for(ParticipanteEquipo participanteEquipo : equipos.getParticipanteEquiposByIdEquipo()) {
                    participanteEquipoService.delete(participanteEquipo.getIdParticipanteEquipo());
                }

                etapaEquipoService.delete(etapaEquipo.getIdEtapaEquipo());
                equiposService.delete(equipos.getIdEquipo());
            }

            etapasService.delete(etapas.getIdEtapa());

        }

        torneosService.delete(idTorneo);

    }

    @Override
    public BatallasDTO generaBatallas(Integer idEtapa) throws ValidacionException {

        Etapas etapas = etapasService.get(idEtapa);
        BatallasDTO batallas = new BatallasDTO();
        Torneos torneos;

        if(etapas != null) {

            torneos = torneosService.get(etapas.getIdTorneo());

            // Armamos equipos
            List<Integer> idEquipos = new ArrayList<>();
            for(EtapaEquipo etapaEquipo : etapas.getEtapaEquiposByIdEtapa()) {
                idEquipos.add(etapaEquipo.getIdEquipo());
            }

            List<Integer> randomNumbers = CollectionUtils.getRandomNumbers(idEquipos);

            BatallaDTO batallaDTO = null;
            com.mozcalti.gamingapp.model.batallas.ParticipanteDTO participanteDTO;
            Equipos equiposRandom;
            Participantes participantesEntity;
            int numCompetidores = 0;
            int totalParticipantes = etapas.getReglas().getNumCompetidores();
            String horaIni = torneos.getTorneoHorasHabilesByIdTorneo().stream().toList().get(0).getHoraIniHabil();
            String horaFin;
            List<com.mozcalti.gamingapp.model.batallas.ParticipanteDTO> participantes = null;
            for(Integer randomNumber : randomNumbers) {
                //System.out.println("----> " + randomNumber);
                equiposRandom = equiposService.get(randomNumber);

                if(etapas.getReglas().getTrabajo().equals(Constantes.INDIVIDUAL)) {
                    for(ParticipanteEquipo participanteEquipo : equiposRandom.getParticipanteEquiposByIdEquipo()) {

                        if(numCompetidores == 0) {
                            batallaDTO = new BatallaDTO();
                            participantes = new ArrayList<>();

                            System.out.println("---> Batalla...");
                            System.out.println("---> " + etapas.getFechaInicio());
                            System.out.println("---> " + horaIni);

                            batallaDTO.setIdEtapa(idEtapa);
                            batallaDTO.setHoraInicio(horaIni);
                            horaFin = DateUtils.addMinutos(horaIni,
                                    Constantes.HORA_PATTERN,
                                    etapas.getReglas().getTiempoBatallaAprox());
                            System.out.println("---> " + horaFin);
                            horaIni = DateUtils.addMinutos(horaFin,
                                    Constantes.HORA_PATTERN,
                                    etapas.getReglas().getTiempoEspera());
                            batallaDTO.setFecha(etapas.getFechaInicio());

                            batallaDTO.setHoraFin(horaFin);
                            batallaDTO.setRondas(etapas.getReglas().getNumRondas());

                            batallas.getBatallasResponse().add(batallaDTO);
                        }

                        if(numCompetidores < totalParticipantes) {
                            participantesEntity = participantesService.get(participanteEquipo.getIdParticipante());
                            System.out.println("-----> nombre: " + participantesEntity.getNombre());

                            participanteDTO = new com.mozcalti.gamingapp.model.batallas.ParticipanteDTO();
                            //participanteResponse.setIdParticipante(participantesEntity.getIdParticipante());
                            participanteDTO.setIdParticipante(participanteEquipo.getIdEquipo());
                            participanteDTO.setNombre(participantesEntity.getNombre() + " " +
                                    participantesEntity.getApellidos());
                            participantes.add(participanteDTO);

                            numCompetidores+=1;
                        }

                        if(numCompetidores == totalParticipantes) {
                            if(batallaDTO != null) {
                                batallaDTO.setParticipantes(participantes);
                            }

                            numCompetidores = 0;
                        }

                    }

                } else if(etapas.getReglas().getTrabajo().equals(Constantes.EQUIPO)) {

                    if(numCompetidores == 0) {
                        batallaDTO = new BatallaDTO();
                        participantes = new ArrayList<>();

                        System.out.println("---> Batalla...");
                        System.out.println("---> " + etapas.getFechaInicio());
                        System.out.println("---> " + horaIni);
                        batallaDTO.setHoraInicio(horaIni);

                        horaFin = DateUtils.addMinutos(horaIni,
                                Constantes.HORA_PATTERN,
                                etapas.getReglas().getTiempoBatallaAprox());
                        System.out.println("---> " + horaFin);
                        horaIni = DateUtils.addMinutos(horaFin,
                                Constantes.HORA_PATTERN,
                                etapas.getReglas().getTiempoEspera());

                        batallaDTO.setFecha(etapas.getFechaInicio());

                        batallaDTO.setHoraFin(horaFin);
                        batallaDTO.setRondas(etapas.getReglas().getNumRondas());

                        batallas.getBatallasResponse().add(batallaDTO);
                    }

                    if(numCompetidores < totalParticipantes) {
                        System.out.println("------> equipo: " + equiposRandom.getNombre());

                        participanteDTO = new com.mozcalti.gamingapp.model.batallas.ParticipanteDTO();
                        participanteDTO.setIdParticipante(equiposRandom.getIdEquipo());
                        participanteDTO.setNombre(equiposRandom.getNombre());
                        participantes.add(participanteDTO);

                        numCompetidores+=1;
                    }

                    if(numCompetidores == totalParticipantes) {
                        if(batallaDTO != null) {
                            batallaDTO.setParticipantes(participantes);
                        }

                        numCompetidores = 0;
                    }

                }

            }

            if(batallaDTO != null) {
                batallaDTO.setParticipantes(participantes);
            }

        } else {
            throw new ValidacionException("No existe la etapa con el id indicado: " + idEtapa);
        }

        return batallas;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public BatallasDTO saveBatallas(BatallasDTO batallasDTO) throws ValidacionException {

        if(batallasDTO.getBatallasResponse().isEmpty()) {
            throw new ValidacionException("No existen batallas para guardar");
        }

        Batallas batallas;
        EtapaBatalla etapaBatalla;
        BatallaParticipantes batallaParticipantes;
        for(BatallaDTO batallaDTO : batallasDTO.getBatallasResponse()) {

            if(!etapasService.get(batallaDTO.getIdEtapa()).getEtapaBatallasByIdEtapa().isEmpty()) {
                throw new ValidacionException("Ya existen batallas para el idEtapa: " + batallaDTO.getIdEtapa());
            }

            batallas = new Batallas(batallaDTO);
            batallas = batallasService.save(batallas);

            batallaDTO.setIdBatalla(batallas.getIdBatalla());

            etapaBatalla = new EtapaBatalla(batallaDTO.getIdEtapa(), batallas.getIdBatalla());
            etapaBatallaService.save(etapaBatalla);

            for(com.mozcalti.gamingapp.model.batallas.ParticipanteDTO participanteDTO : batallaDTO.getParticipantes()) {
                batallaParticipantes = new BatallaParticipantes(participanteDTO, batallas.getIdBatalla());
                batallaParticipantesService.save(batallaParticipantes);
            }

        }

        return batallasDTO;

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
