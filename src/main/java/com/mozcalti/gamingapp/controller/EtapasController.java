package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosInstitucionGpoDTO;
import com.mozcalti.gamingapp.service.torneo.EtapasService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/etapas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EtapasController {

    private EtapasService etapasService;

    @GetMapping("/getParticipantes/{idEtapa}")
    public List<ResultadosInstitucionGpoDTO> obtieneParticipantes(@PathVariable Integer idEtapa) {
        return etapasService.obtieneParticipantes(idEtapa);
    }

}
