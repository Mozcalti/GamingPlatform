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
    public RobotsDTO cargarArchivo(@RequestParam(value="idEquipo") int idEquipo, @RequestParam(value="tipo") String tipo, @RequestParam(value = "file") MultipartFile file) throws IOException {
        return robotsService.cargarRobot(idEquipo, tipo, file);
    }

    @PostMapping(value = "/guardarRobot")
    public void guardarRobot(@RequestBody RobotsDTO robotDTO) {
        Robots robot = new Robots();
        robot.setIdRobot(robotDTO.getIdRobot());
        robot.setNombre(robotDTO.getNombre());
        robot.setActivo(robotDTO.getActivo());
        robot.setIdEquipo(robotDTO.getIdEquipo());
        robot.setClassName(robotDTO.getClassName());
        robot.setTipo(robotDTO.getTipo());
        robotsService.guardarRobot(robot);
    }

    @Transactional
    @DeleteMapping(value = "/eliminarRobot")
    public void eliminarRobot(@RequestParam(value="idRobot") int idRobot) throws IOException {
        robotsService.eliminarRobot(idRobot);
    }

    @GetMapping(value ="/verRobots")
    public List<RobotsDTO> verRobots(@RequestParam Integer idEquipo){
        return robotsService.obtenerRobots(idEquipo);
    }

    @Transactional
    @PutMapping(value = "/seleccionarRobot")
    public void seleccionarRobot(@RequestParam(value="nombre") String nombre, @RequestParam(value="idEquipo") int idEquipo) {
        robotsService.seleccionarRobot(nombre, idEquipo);
    }
}