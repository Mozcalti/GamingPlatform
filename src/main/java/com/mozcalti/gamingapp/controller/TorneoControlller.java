package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.EtapasEntity;
import com.mozcalti.gamingapp.model.TorneosEntity;
import com.mozcalti.gamingapp.request.EtapaRequest;
import com.mozcalti.gamingapp.request.TorneoRequest;
import com.mozcalti.gamingapp.service.EtapasService;
import com.mozcalti.gamingapp.service.TorneosService;
import com.mozcalti.gamingapp.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/torneo")
public class TorneoControlller {

    @Autowired
    private TorneosService torneosService;

    @Autowired
    private EtapasService etapasService;

    @GetMapping(value = "/all")
    public List<TorneosEntity> getAll() {
        return torneosService.getAll();
    }

    @PostMapping(value = "/save")
    public ResponseEntity save(@RequestBody TorneoRequest torneoRequest) {

        ResponseEntity responseEntity = null;
        HashMap responseObject = new HashMap();

        try {
            torneosService.saveTorneo(torneoRequest);

            responseObject.put("mensaje", "Torneo Guardado Correctamente");
            responseObject.put("codigo", "200");
            responseEntity = ResponseEntity.ok(responseObject);
        } catch (ValidacionException e) {
            responseObject.put("mensaje", e.getMessage());
            responseObject.put("codigo", "204");
            responseEntity = ResponseEntity.status(201).body(responseObject);
        } catch (Exception e) {
            responseObject.put("mensaje", "Ocurrio un error inesperado");
            responseObject.put("codigo", "204");
            responseEntity = ResponseEntity.status(201).body(responseObject);
            System.out.println("Ocurrio un error inesperado: " + e.getMessage());
        }

        return responseEntity;
    }
    /*public TorneosEntity save(@RequestBody TorneosEntity torneosEntity) {
        return torneosService.save(torneosEntity);
    }*/


}
