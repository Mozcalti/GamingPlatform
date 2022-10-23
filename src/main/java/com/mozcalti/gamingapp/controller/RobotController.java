package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.Equipos;
import com.mozcalti.gamingapp.model.Robots;
import com.mozcalti.gamingapp.model.dto.*;
import com.mozcalti.gamingapp.service.RobotsService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

@RestController
@RequestMapping("/robot")
@AllArgsConstructor
public class RobotController {

    private RobotsService robotsService;


    @PostMapping(value = "/cargarRobot", produces = MediaType.APPLICATION_JSON_VALUE)
    public RobotsDTO cargarArchivo(@RequestParam(value="idEquipo") int idEquipo, @RequestParam(value="tipo") String tipo, @RequestParam(value = "file") MultipartFile file)  throws IOException {
        return robotsService.cargarRobot(idEquipo, tipo, file);
    }

    @PostMapping(value = "/guardarRobot")
    public void guardarRobot(@RequestBody RobotsDTO robotDTO) {
        Robots robot = new Robots();
        robot.setNombre(robotDTO.getNombre());
        robot.setActivo(robotDTO.getActivo());
        robot.setIdEquipo(1);
        robot.setEquiposByIdEquipo(new Equipos());
        robotsService.guardarRobot(robot);
    }

    @PostMapping(value = "/eliminarRobot")
    public void eliminarRobot(@RequestParam(value="idRobot") int idRobot) throws NoSuchFileException {
        robotsService.eliminarRobot(idRobot);
    }

    @GetMapping(value ="/verRobots")
    public List<Robots> verRobots(@RequestParam(value="idEquipo") int idEquipo){
        return robotsService.obtenerRobots(idEquipo);
    }

    @PostMapping(value = "/seleccionarRobot")
    public void seleccionarRobot(@RequestParam(value="nombreRobot") String nombreRobot, @RequestParam(value="idRobot") int idRobot) {
        robotsService.seleccionarRobot(nombreRobot, idRobot);
    }
}
