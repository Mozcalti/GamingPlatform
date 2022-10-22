package com.mozcalti.gamingapp.controller;

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
    public RobotsDTO cargarArchivo(@RequestParam(value="idParticipante") int idParticipante, @RequestParam(value="tipo") String tipo, @RequestParam(value = "file") MultipartFile file)  throws IOException {
        return robotsService.cargarRobot(idParticipante, tipo, file);
    }

    @PostMapping(value = "/guardarRobot")
    public void guardarRobot(@RequestBody Robots robot) {
        robotsService.guardarRobot(robot);
    }

    @PostMapping(value = "/eliminarRobot")
    public void eliminarRobot(@RequestParam(value="nombreRobot") String nombreRobot, @RequestParam(value="idRobot") int idRobot) throws NoSuchFileException {
        robotsService.eliminarRobot(nombreRobot, idRobot);
    }

    @GetMapping(value ="/verRobots")
    public List<Robots> verRobots(@RequestParam(value="idParticipante") int idParticipante){
        return robotsService.obtenerRobots(idParticipante);
    }

    @PostMapping(value = "/seleccionarRobot")
    public void seleccionarRobot(@RequestParam(value="nombreRobot") String nombreRobot, @RequestParam(value="idRobot") int idRobot) {
        robotsService.seleccionarRobot(nombreRobot, idRobot);
    }
}
