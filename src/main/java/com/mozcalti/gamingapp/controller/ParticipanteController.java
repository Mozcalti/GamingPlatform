package com.mozcalti.gamingapp.controller;


import com.mozcalti.gamingapp.model.Participantes;
import com.mozcalti.gamingapp.model.dto.ParticipanteDTO;
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
    public void guardar(@RequestBody List<Participantes> participantes) {
        participantesService.guardarParticipantes(participantes);
    }
}