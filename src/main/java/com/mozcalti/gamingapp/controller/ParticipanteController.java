package com.mozcalti.gamingapp.controller;


import com.mozcalti.gamingapp.model.dto.ParticipanteDTO;
import com.mozcalti.gamingapp.model.dto.TablaParticipantesDTO;
import com.mozcalti.gamingapp.service.ParticipantesService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/participante")
@AllArgsConstructor
public class ParticipanteController {

    private ParticipantesService participantesService;


    @PostMapping(value = "/cargarArchivo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ParticipanteDTO> cargarArchivo(@RequestParam(value = "file") MultipartFile file) {
        return participantesService.cargarArchivo(file);
    }

    @PostMapping(value = "/guardar")
    public void guardar(@RequestBody List<ParticipanteDTO> participantesDTO) {
        participantesService.guardarParticipantes(participantesDTO);
    }

    @GetMapping("/todos")
    public List<TablaParticipantesDTO> todosParticipantes(@RequestParam String texto,@RequestParam String fecha){
        return participantesService.listaParticipantes(texto,fecha);
    }

    @GetMapping("/{id}")
    public TablaParticipantesDTO obtenerParticipante(@PathVariable Integer id){
        return participantesService.obtenerParticipante(id);
    }

    @PostMapping(value = "/guardarParticipante")
    public void guardarParticipante(@RequestBody ParticipanteDTO participanteDTO) {
        participantesService.guardarParticipante(participanteDTO);
    }


    @PostMapping(value = "/actualizarParticipante")
    public void actualizarParticipante(@RequestBody TablaParticipantesDTO participanteDTO) {
        participantesService.actualizarParticipante(participanteDTO);
    }

    @GetMapping(value ="/correo")
    public Integer verRobots(@RequestParam String correo){
        return participantesService.obtieneIdParticipante(correo);
    }

}
