package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.request.torneo.TorneoRequest;
import com.mozcalti.gamingapp.response.batalla.BatallasResponse;
import com.mozcalti.gamingapp.service.CalendarizarEtapasTorneoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/torneo")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TorneoControlller {

    private CalendarizarEtapasTorneoService calendarizarEtapasTorneoService;

    /*@GetMapping(value = "/all")
    public List<TorneosEntity> getAll() {
        return torneosService.getAll();
    }*/

    @PostMapping(value = "/save")
    public ResponseEntity save(@RequestBody TorneoRequest torneoRequest) {

        ResponseEntity responseEntity = null;
        HashMap responseObject = new HashMap();

        try {
            calendarizarEtapasTorneoService.saveTorneo(torneoRequest);
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

    @GetMapping("/batallas/{idEtapa}")
    public ResponseEntity getBatallasByIdEtapa(@PathVariable Integer idEtapa) {

        ResponseEntity responseEntity = null;
        HashMap responseObject = new HashMap();
        BatallasResponse batallasResponse;

        try {
            batallasResponse = calendarizarEtapasTorneoService.generaBatallas(idEtapa);
            responseObject.put("mensaje", "Generación de Batallas Correcta");
            responseObject.put("codigo", "200");
            responseEntity = ResponseEntity.ok(batallasResponse);
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

    @PostMapping("/batallas/save")
    public ResponseEntity saveBatallas(@RequestBody BatallasResponse batallasRequest) {

        ResponseEntity responseEntity = null;
        HashMap responseObject = new HashMap();
        BatallasResponse batallasResponse;

        try {
            batallasResponse = calendarizarEtapasTorneoService.saveBatallas(batallasRequest);
            responseObject.put("mensaje", "Batallas Guardadas Correctamente");
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


}
