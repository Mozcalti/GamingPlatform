package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.Robots;
import com.mozcalti.gamingapp.model.dto.*;
import com.mozcalti.gamingapp.service.RobotsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/robot")
@RequiredArgsConstructor
@Slf4j
public class RobotController {

    private final RobotsService robotsService;

    @PostMapping(value = "/cargarRobot", produces = MediaType.APPLICATION_JSON_VALUE)
    public RobotsDTO cargarArchivo(
            @RequestParam(value="idParticipante") int idParticipante,
            @RequestParam(value="tipo") String tipo,
            @RequestParam(value = "file") MultipartFile file) throws IOException {
        return robotsService.cargarRobot(idParticipante, tipo, file);
    }

    @PostMapping(value = "/guardarRobot")
    public void guardarRobot(@RequestBody RobotsDTO robotDTO) {
        Robots robot = new Robots();
        robot.setNombre(robotDTO.getNombre());
        robot.setActivo(robotDTO.getActivo());
        robot.setIdEquipo(robotDTO.getIdEquipo());
        robot.setClassName(robotDTO.getClassName());
        robot.setTipo(robotDTO.getTipo());
        robotsService.guardarRobot(robot);
    }

    @Transactional
    @DeleteMapping(value = "/eliminarRobot")
    public void eliminarRobot(@RequestParam(value="idRobot") int idRobot,
                              @RequestParam(value="idParticipante") int idParticipante) throws IOException {
        robotsService.eliminarRobot(idRobot, idParticipante);
    }

    @GetMapping(value ="/verRobots")
    public List<RobotsDTO> verRobots(@RequestParam Integer idParticipante){
        return robotsService.obtenerRobots(idParticipante);
    }

    @Transactional
    @PutMapping(value = "/seleccionarRobot")
    public void seleccionarRobot(@RequestParam(value="nombre") String nombre,
                                 @RequestParam(value="idParticipante") int idParticipante) {
        robotsService.seleccionarRobot(nombre, idParticipante);
    }
}