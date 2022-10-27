package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.exceptions.ValidacionException;

import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.batallas.BatallaFechaHoraInicioDTO;
import com.mozcalti.gamingapp.model.batallas.BatallaParticipanteDTO;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.model.participantes.EquiposDTO;
import com.mozcalti.gamingapp.model.participantes.InstitucionEquiposDTO;
import com.mozcalti.gamingapp.model.torneos.*;
import com.mozcalti.gamingapp.model.batallas.BatallaDTO;
import com.mozcalti.gamingapp.model.batallas.BatallasDTO;
import com.mozcalti.gamingapp.service.*;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
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
    public void saveTorneo(TorneoDTO torneoDTO) throws ValidacionException {

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
            CalendarizarEtapasTorneoValidation.validaSaveTorneoEtapas(etapaDTO);

            etapas = new Etapas(etapaDTO, torneos.getIdTorneo());
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public TorneoDTO getTorneo(int idTorneo) {

        Torneos torneos = torneosService.get(idTorneo);

        CalendarizarEtapasTorneoValidation.validaGetTorneo(torneos, idTorneo);

        TorneoDTO torneoDTO = new TorneoDTO(torneos, idTorneo);

        torneoDTO.setHorasHabiles(torneos.getTorneoHorasHabilesByIdTorneo().stream().map(HoraHabilDTO::new).toList());

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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void updateTorneo(TorneoDTO torneoDTO) throws ValidacionException {

        CalendarizarEtapasTorneoValidation.validaSaveTorneo(torneoDTO);

        Torneos torneos = new Torneos(torneoDTO);
        torneos = torneosService.save(torneos);

        for(HoraHabilDTO horaHabilDTO : torneoDTO.getHorasHabiles()) {
            torneoHorasHabilesService.save(new TorneoHorasHabiles(horaHabilDTO, torneos.getIdTorneo()));
        }

        // Etapas
        for (EtapaDTO etapaDTO : torneoDTO.getEtapas()) {
            CalendarizarEtapasTorneoValidation.validaSaveTorneoEtapas(etapaDTO);

            Etapas etapasUpdate = etapasService.get(etapaDTO.getIdEtapa());

            etapasUpdate.setNumeroEtapa(etapaDTO.getNumeroEtapa());
            etapasUpdate.setFechaInicio(etapaDTO.getFechaInicio());
            etapasUpdate.setFechaFin(etapaDTO.getFechaFin());
            etapasService.save(etapasUpdate);

            // Reglas
            reglasService.save(new Reglas(etapaDTO.getReglas(), etapasUpdate.getIdEtapa()));

            // Participantes
            Equipos equipos;
            for(EtapaEquipo etapaEquipo : etapasUpdate.getEtapaEquiposByIdEtapa()) {
                equipos = equiposService.get(etapaEquipo.getIdEquipo());

                for(ParticipanteEquipo participanteEquipo : equipos.getParticipanteEquiposByIdEquipo()) {
                    participanteEquipoService.delete(participanteEquipo.getIdParticipanteEquipo());
                }

                etapaEquipoService.delete(etapaEquipo.getIdEtapaEquipo());
                equiposService.delete(etapaEquipo.getIdEquipo());
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

            if(!etapas.getEtapaBatallasByIdEtapa().isEmpty()) {
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

        if(etapas.getReglas().getTrabajo().equals(Constantes.INDIVIDUAL)) {
            for(InstitucionEquiposDTO institucionEquiposDTO : equiposDTO.getEquiposByInstitucion()) {

                List<Integer> randomNumbers = CalendarizarEtapasTorneoValidation
                        .armaEquipos(institucionEquiposDTO.getIdEquipos());
                List<List<BatallaParticipanteDTO>> lists = torneosService.obtieneParticipantes(
                        randomNumbers, numCompetidores);

                for(int x=0; x<lists.size(); x++) {
                    BatallaDTO batallaDTO = new BatallaDTO();

                    batallaDTO.setIdEtapa(idEtapa);

                    batallaDTO.setIdInstitucion(institucionEquiposDTO.getIdInstitucion());

                    BatallaFechaHoraInicioDTO batallaFechaHoraInicioDTO = batallaFechaHoraInicioDTOS.get(x);
                    batallaDTO.setFecha(batallaFechaHoraInicioDTO.getFecha());
                    batallaDTO.setHoraInicio(batallaFechaHoraInicioDTO.getHoraInicio());
                    batallaDTO.setHoraFin(batallaFechaHoraInicioDTO.getHoraFin());
                    batallaDTO.setBatallaParticipantes(lists.get(x));
                    batallaDTO.setRondas(numRondas);

                    batallasDTO.getBatallas().add(batallaDTO);
                }

            }
        } else if(etapas.getReglas().getTrabajo().equals(Constantes.EQUIPO)) {
            List<Integer> randomNumbers = CalendarizarEtapasTorneoValidation
                    .armaEquipos(equiposDTO.getIdEquipos());
            List<List<BatallaParticipanteDTO>> lists = torneosService.obtieneParticipantes(
                    randomNumbers, numCompetidores);

            for(int x=0; x<lists.size(); x++) {
                BatallaDTO batallaDTO = new BatallaDTO();

                batallaDTO.setIdEtapa(idEtapa);

                BatallaFechaHoraInicioDTO batallaFechaHoraInicioDTO = batallaFechaHoraInicioDTOS.get(x);
                batallaDTO.setFecha(batallaFechaHoraInicioDTO.getFecha());
                batallaDTO.setHoraInicio(batallaFechaHoraInicioDTO.getHoraInicio());
                batallaDTO.setHoraFin(batallaFechaHoraInicioDTO.getHoraFin());
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
    public List<DatosCorreoBatallaDTO> getDatosCorreoBatalla() throws ValidacionException {

        List<DatosCorreoBatallaDTO> mailsbatallas = new ArrayList<>();
        DatosCorreoBatallaDTO mailBatallasDTO;
        List<String> participante;
        Batallas batallas;
        Equipos equipos;
        StringBuilder mailToParticipantes = new StringBuilder();

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
