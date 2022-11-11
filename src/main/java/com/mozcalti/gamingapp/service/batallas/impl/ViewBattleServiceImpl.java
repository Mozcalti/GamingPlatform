package com.mozcalti.gamingapp.service.batallas.impl;

import com.mozcalti.gamingapp.model.Batallas;
import com.mozcalti.gamingapp.model.batallas.view.BatallaViewDTO;
import com.mozcalti.gamingapp.repository.BatallasRepository;
import com.mozcalti.gamingapp.service.batallas.ViewBattleService;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ViewBattleServiceImpl implements ViewBattleService {

    @Autowired
    private BatallasRepository batallasRepository;

    @Value("${robocode.battles}")
    private String pathRobocodeBattles;

    @Override
    public BatallaViewDTO obtieneDatosViewBattle(String idBatalla) {

        log.info("Obteniendo Json Visualización de la batalla: {}", idBatalla);

        Batallas batallas = batallasRepository.findByViewToken(idBatalla);

        BatallaViewDTO batallaViewDTO = new BatallaViewDTO();

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

        File file = new File(pathRobocodeBattles + File.separator + idBatalla + ".xml");

        if (file.exists()) {
            DocumentBuilder documentBuilder;
            try {
                documentBuilder = getDocumentBuilder();
                batallaViewDTO.setBattleXml(documentBuilder.parse(file));
            } catch (ParserConfigurationException | SAXException | IOException e) {
                log.error(e.getMessage());
            }
        }

        return batallaViewDTO;
    }

    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder();
    }

}
