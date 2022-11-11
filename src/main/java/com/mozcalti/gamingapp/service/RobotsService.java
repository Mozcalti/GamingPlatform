package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Robots;
import com.mozcalti.gamingapp.model.dto.RobotsDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RobotsService extends GenericServiceAPI<Robots, Integer> {

    RobotsDTO cargarRobot(int idParticipante ,String tipo, MultipartFile file) throws IOException;
    Robots guardarRobot(Robots robot);
    void eliminarRobot(int idRobot, int idParticipante) throws IOException;
    @PostMapping(value = "/seleccionarRobot")
    void seleccionarRobot(String nombre, int idParticipante);
    List<RobotsDTO> obtenerRobots(Integer idParticipante);
    void ValidaHoraPermitida(int idParticipante) throws ValidacionException;

}
