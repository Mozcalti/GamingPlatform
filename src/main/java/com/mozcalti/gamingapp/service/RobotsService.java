package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.model.Robots;
import com.mozcalti.gamingapp.model.dto.RobotsDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

public interface RobotsService extends GenericServiceAPI<Robots, Integer> {

    RobotsDTO cargarRobot(int idParticipante ,String tipo, MultipartFile file) throws IOException;
    Robots guardarRobot(Robots robot);
    void eliminarRobot(String nombreRobot, int idRobot) throws NoSuchFileException;
    @PostMapping(value = "/seleccionarRobot")
    int seleccionarRobot(String nombreRobot, int idRobot);
    List<Robots> obtenerRobots(int idParticipante);
}
