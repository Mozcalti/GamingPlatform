package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.catalogos.EtapasDTO;
import com.mozcalti.gamingapp.model.catalogos.InstitucionDTO;
import com.mozcalti.gamingapp.service.batallas.BatallasService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalogo")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CatalogosController {

    private BatallasService batallasService;

    @GetMapping("/etapas")
    public List<EtapasDTO> getEtapas() {
        return batallasService.getEtapas();
    }

    @GetMapping("/instituciones")
    public List<InstitucionDTO> getInstituciones() {
        return batallasService.getInstituciones();
    }

}
