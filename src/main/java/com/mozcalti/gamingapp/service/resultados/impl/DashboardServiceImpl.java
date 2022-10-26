package com.mozcalti.gamingapp.service.resultados.impl;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.batallas.resultado.*;
import com.mozcalti.gamingapp.model.dto.PaginadoDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.repository.*;
import com.mozcalti.gamingapp.service.BatallasService;
import com.mozcalti.gamingapp.service.resultados.DashboardService;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.FileUtils;
import com.mozcalti.gamingapp.validations.DashboardsGlobalResultadosValidation;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private BatallasService batallasService;

    @Autowired
    private EquiposRepository equiposRepository;

    @Autowired
    private RobotsRepository robotsRepository;

    @Autowired
    private ResultadosRepository resultadosRepository;

    @Autowired
    private EtapasRepository etapasRepository;

    @Autowired
    private ParticipantesRepository participantesRepository;

    @Autowired
    private EtapaEquipoRepository etapaEquipoRepository;

    @Autowired
    private InstitucionRepository institucionRepository;

    @Autowired
    private BatallasRepository batallasRepository;

    @Value("${resources.static.resultados-batalla}")
    private String pathResultadosBatalla;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void buscaSalidaBatallas() {

        StringBuilder horaInicioBatalla = new StringBuilder();
        StringBuilder horaFinBatalla = new StringBuilder();
        StringBuilder fileResultadoBatallas = new StringBuilder();

        for(Batallas batallas : batallasService.getAll()) {

            horaInicioBatalla.delete(0, horaInicioBatalla.length());
            horaInicioBatalla.append(batallas.getFecha()).append(" ").append(batallas.getHoraInicio());

            horaFinBatalla.delete(0, horaFinBatalla.length());
            horaFinBatalla.append(batallas.getFecha()).append(" ").append(batallas.getHoraFin());

            try {
                DashboardsGlobalResultadosValidation.validaBatalla(batallas);
                DashboardsGlobalResultadosValidation.validaFechaHoraBatalla(horaInicioBatalla.toString(), horaFinBatalla.toString());

                for(BatallaParticipantes batallaParticipantes : batallas.getBatallaParticipantesByIdBatalla()) {
                    Optional<Equipos> equipos = equiposRepository.findById(batallaParticipantes.getIdParticipanteEquipo());

                    Optional<Robots> robot = robotsRepository.findAllByIdEquipo(equipos.orElseThrow().getIdEquipo()).stream()
                            .filter(r -> r.getActivo().equals(1))
                            .findFirst();

                    if(robot.isPresent()) {
                        fileResultadoBatallas.delete(0, fileResultadoBatallas.length());
                        fileResultadoBatallas.append(pathResultadosBatalla).append("/").append(robot.get().getIdRobot()).append(".xml");

                        DashboardsGlobalResultadosValidation.validaExisteArchivoXML(fileResultadoBatallas.toString());

                        StringBuilder lineas = FileUtils.getRecordInfo(fileResultadoBatallas.toString(),
                                Constantes.RECORD_INFO_INICIO,
                                Constantes.RECORD_INFO_FIN);

                        XStream xStream = new XStream();
                        xStream.addPermission(NoTypePermission.NONE);
                        xStream.addPermission(NullPermission.NULL);
                        xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);

                        xStream.allowTypesByWildcard(new String[] {
                                "com.mozcalti.gamingapp.model.batallas.resultado.**"
                        });

                        xStream.processAnnotations(RecordInfo.class);

                        RecordInfo recordInfo = (RecordInfo) xStream.fromXML(lineas.toString());

                        for(Result result : recordInfo.getResults()) {
                            resultadosRepository.save(new Resultados(result, batallas.getIdBatalla()));
                        }

                        batallas.setBndTermina(1);
                    }
                }
                batallasService.save(batallas);
            } catch (ValidacionException | UtilsException e) {
                log.info(e.getMessage());
            }
        }

    }

    @Override
    public TablaDTO<ResultadosDTO> listaResultadosBatalla(Integer indice) {
        Page<Resultados> resultadosPages = resultadosRepository.findAll(PageRequest.of(indice, 50));
        PaginadoDTO paginadoDTO = new PaginadoDTO(resultadosPages.getTotalPages(), resultadosPages.getNumber());
        List<Resultados> resultadosParte = resultadosPages.toList();

        List<ResultadosDTO> resultadosDTO = resultadosParte.stream()
                .map(i -> new ResultadosDTO(
                        i.getTeamleadername(),
                        i.getRank(),
                        i.getScore(),
                        i.getSurvival(),
                        i.getLastsurvivorbonus(),
                        i.getBulletdamage(),
                        i.getBulletdamagebonus(),
                        i.getRamdamage(),
                        i.getRamdamagebonus(),
                        i.getFirsts(),
                        i.getSeconds(),
                        i.getThirds(),
                        i.getVer())
                ).toList();

        TablaDTO<ResultadosDTO> tablaDTO = new TablaDTO<>();
        tablaDTO.setLista(resultadosDTO);
        tablaDTO.setPaginadoDTO(paginadoDTO);

        return tablaDTO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ResultadosParticipantesDTO> listaResultadosParticipantesBatalla(Integer idEtapa, String nombreInstitucion) {

        Optional<Etapas> etapas = etapasRepository.findById(idEtapa);

        List<ResultadosParticipantesDTO> resultadosParticipantesDTOS = new ArrayList<>();

        for(EtapaBatalla etapaBatalla : etapas.orElseThrow().getEtapaBatallasByIdEtapa()) {
            Optional<Batallas> batallas = batallasRepository.findById(etapaBatalla.getIdBatalla());

            for(Resultados resultados : batallas.orElseThrow().getResultadosByIdBatalla().stream()
                        .sorted(Comparator.comparing(Resultados::getScore).reversed()).toList()) {

                Optional<Robots> robot = robotsRepository.findAllByNombre(resultados.getTeamleadername())
                        .stream()
                        .findFirst();

                if(robot.isPresent()) {
                    Optional<Equipos> equipos = equiposRepository.findById(robot.orElseThrow().getIdEquipo());

                    StringBuilder participantes = new StringBuilder();
                    Optional<Institucion> institucion = Optional.empty();
                    for(ParticipanteEquipo participanteEquipo : equipos.orElseThrow().getParticipanteEquiposByIdEquipo()) {
                        Optional<Participantes> participante = participantesRepository.findById(participanteEquipo.getIdParticipante());

                        participantes.append(participante.orElseThrow().getNombre()).append(" ").append(participante.orElseThrow().getApellidos()).append(",");

                        if(!institucion.isPresent()) {
                            institucion = Optional.of(participante.get().getInstitucion());
                        }
                    }

                    resultadosParticipantesDTOS.add(new ResultadosParticipantesDTO(
                            participantes.substring(0 , participantes.length()-1),
                            institucion.orElseThrow().getNombre(),
                            robot.orElseThrow().getNombre(),
                            resultados.getScore()
                    ));

                }
            }
        }

        resultadosParticipantesDTOS = filtraInstituciones(resultadosParticipantesDTOS, nombreInstitucion);

        return resultadosParticipantesDTOS;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ResultadosInstitucionGpoDTO> gruopResultadosParticipantesBatalla(Integer idEtapa) {

        List<ResultadosInstitucionGpoDTO> resultadosInstitucionGpoDTOS;

        List<ResultadosParticipantesDTO> resultadosParticipantesDTOS = listaResultadosParticipantesBatalla(idEtapa, Constantes.TODOS);

        resultadosInstitucionGpoDTOS = groupInstituciones(resultadosParticipantesDTOS);

        return resultadosInstitucionGpoDTOS;

    }

    private List<ResultadosParticipantesDTO> filtraInstituciones(List<ResultadosParticipantesDTO> resultadosParticipantesDTOS,
                                                                 String nombreInstitucion) {
        if(!nombreInstitucion.equals(Constantes.TODOS)) {
            return resultadosParticipantesDTOS.stream().filter(o -> o.getNombreInstitucion().equals(nombreInstitucion)).toList();
        } else {
            return resultadosParticipantesDTOS;
        }
    }

    private List<ResultadosInstitucionGpoDTO> groupInstituciones(List<ResultadosParticipantesDTO> resultadosParticipantesDTOS) {

        List<ResultadosInstitucionGpoDTO> resultadosInstitucionGpoDTOS = new ArrayList<>();
        Map<String, List<ResultadosParticipantesGpoDTO>> instituciones = new HashMap<>();
        List<ResultadosParticipantesGpoDTO> participantes = new ArrayList<>();
        String nombreInstitucion = null;

        for(ResultadosParticipantesDTO resultadosParticipantesDTO : resultadosParticipantesDTOS.stream()
                .sorted(Comparator.comparing(ResultadosParticipantesDTO::getNombreInstitucion)).toList()) {

            if(nombreInstitucion != null && !nombreInstitucion.equals(resultadosParticipantesDTO.getNombreInstitucion())) {
                participantes = new ArrayList<>();
            }

            participantes.add(new ResultadosParticipantesGpoDTO(
                    resultadosParticipantesDTO.getNombreParticipantes(),
                    resultadosParticipantesDTO.getNombreRobot(),
                    resultadosParticipantesDTO.getScore()
            ));

            instituciones.put(resultadosParticipantesDTO.getNombreInstitucion(), participantes);

            nombreInstitucion = resultadosParticipantesDTO.getNombreInstitucion();
        }

        for(Map.Entry<String, List<ResultadosParticipantesGpoDTO>> entry : instituciones.entrySet()) {
            resultadosInstitucionGpoDTOS.add(new ResultadosInstitucionGpoDTO(entry.getKey(), entry.getValue()));
        }

        return resultadosInstitucionGpoDTOS;
    }

}
