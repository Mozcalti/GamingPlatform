package com.mozcalti.gamingapp.service.batallas.impl;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.batallas.view.BatallaViewDTO;
import com.mozcalti.gamingapp.repository.*;
import com.mozcalti.gamingapp.service.batallas.BatallasService;
import com.mozcalti.gamingapp.service.batallas.ViewBattleService;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import com.mozcalti.gamingapp.utils.Numeros;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ViewBattleServiceImpl implements ViewBattleService {

    @Autowired
    private BatallasRepository batallasRepository;

    @Autowired
    private BatallasService batallasService;

    @Autowired
    private EquiposRepository equiposRepository;

    @Autowired
    private ParticipanteEquipoRepository participanteEquipoRepository;

    @Autowired
    private ParticipantesRepository participantesRepository;

    @Value("${robocode.battles}")
    private String pathRobocodeBattles;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BatallaViewDTO obtieneDatosViewBattle(String idBatalla) throws ValidacionException {

        log.info("Obteniendo Json Visualización de la batalla: {}", idBatalla);

        Batallas batallas = batallasRepository.findByViewToken(idBatalla);

        BatallaViewDTO batallaViewDTO = new BatallaViewDTO();

        if(batallas == null) {
            throw new ValidacionException("No hay batallas para el id solicitado");
        }

        List<String> participantes = new ArrayList<>();
        batallas.getBatallaParticipantesByIdBatalla().stream().forEach(
                b -> participantes.add(b.getNombre())
        );
        batallaViewDTO.setBattleParticipantes(participantes);

        StringBuilder battleFechaHora = new StringBuilder(batallas.getFecha()).append(Constantes.ESPACIO)
                .append(batallas.getHoraInicio());

        String battleFecha = DateUtils.getDateFormat(
                DateUtils.getDateFormat(battleFechaHora.toString(), Constantes.FECHA_HORA_PATTERN).getTime(),
                Constantes.FECHA_HORA_PATTERN_VIEW
        );

        batallaViewDTO.setBattleFecha(battleFecha);

        File file = new File(pathRobocodeBattles + File.separator + batallas.getViewToken() + ".xml");

        if (file.exists()) {
            DocumentBuilder documentBuilder;
            try {
                documentBuilder = getDocumentBuilder();
                batallaViewDTO.setBattleXml(documentBuilder.parse(file));
            } catch (ParserConfigurationException | SAXException | IOException e) {
                log.error(e.getMessage());
            }
        }

        batallaViewDTO.setEstatus(batallas.getEstatus());
        batallaViewDTO.setNombreParticipanteDefault(nombreParticipanteRobotActivo(
                batallas.getBatallaParticipantesByIdBatalla().stream().toList()));

        return batallaViewDTO;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String nombreParticipanteRobotActivo(List<BatallaParticipantes> batallaParticipantes) {

        String nombreParticipanteActivo = null;
        for(BatallaParticipantes batallaParticipante : batallaParticipantes) {
            Equipos equipos = equiposRepository
                    .findById(batallaParticipante.getIdParticipanteEquipo()).orElseThrow();

            Optional<Robots> robot = equipos.getRobotsByIdEquipo().stream()
                    .filter(r -> r.getActivo().equals(Numeros.UNO.getNumero())).findFirst();

            if(robot.isPresent()) {
                Optional<Participantes> participantes = participantesRepository.findById(
                        participanteEquipoRepository.findByIdEquipo(equipos.getIdEquipo()).getIdParticipante());

                nombreParticipanteActivo = participantes.orElseThrow().getNombre() + Constantes.ESPACIO
                        + participantes.orElseThrow().getApellidos();
            }
        }

        return nombreParticipanteActivo;

    }

    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder();
    }

}
