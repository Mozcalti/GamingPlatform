package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import com.mozcalti.gamingapp.model.batallas.BatallasDTO;
import com.mozcalti.gamingapp.service.CalendarizarEtapasTorneoService;
import com.mozcalti.gamingapp.utils.Constantes;
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

    @PostMapping(value = "/save")
    public ResponseEntity saveTorneo(@RequestBody TorneoDTO torneoDTO) {

        ResponseEntity responseEntity;
        HashMap responseObject = new HashMap();

        try {
            calendarizarEtapasTorneoService.saveTorneo(torneoDTO);
            responseObject.put(Constantes.MENSAJE, "Torneo Guardado Correctamente");
            responseObject.put(Constantes.CODIGO, Constantes.OK);
            responseEntity = ResponseEntity.ok(responseObject);
        } catch (ValidacionException e) {
            responseObject.put(Constantes.MENSAJE, e.getMessage());
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
        } catch (Exception e) {
            responseObject.put(Constantes.MENSAJE, Constantes.OCURRIO_ERROR_INESPERADO);
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
            System.out.println(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
        }

        return responseEntity;
    }

    @GetMapping("/batallas/{idEtapa}")
    public ResponseEntity getBatallasByIdEtapa(@PathVariable Integer idEtapa) {

        ResponseEntity responseEntity;
        HashMap responseObject = new HashMap();
        BatallasDTO batallasDTO;

        try {
            batallasDTO = calendarizarEtapasTorneoService.generaBatallas(idEtapa);
            responseObject.put(Constantes.MENSAJE, "Generación de Batallas Correcta");
            responseObject.put(Constantes.CODIGO, Constantes.OK);
            responseEntity = ResponseEntity.ok(batallasDTO);
        } catch (ValidacionException e) {
            responseObject.put(Constantes.MENSAJE, e.getMessage());
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
        } catch (Exception e) {
            responseObject.put(Constantes.MENSAJE, Constantes.OCURRIO_ERROR_INESPERADO);
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
            System.out.println(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
        }

        return responseEntity;

    }

    @PostMapping("/batallas/save")
    public ResponseEntity saveBatallas(@RequestBody BatallasDTO batallasDTO) {

        ResponseEntity responseEntity;
        HashMap responseObject = new HashMap();

        try {
            calendarizarEtapasTorneoService.saveBatallas(batallasDTO);
            responseObject.put(Constantes.MENSAJE, "Batallas Guardadas Correctamente");
            responseObject.put(Constantes.CODIGO, Constantes.OK);
            responseEntity = ResponseEntity.ok(responseObject);
        } catch (ValidacionException e) {
            responseObject.put(Constantes.MENSAJE, e.getMessage());
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
        } catch (Exception e) {
            responseObject.put(Constantes.MENSAJE, Constantes.OCURRIO_ERROR_INESPERADO);
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
            System.out.println(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
        }

        return responseEntity;
    }

    @GetMapping("/consulta/{idTorneo}")
    public ResponseEntity getTorneo(@PathVariable Integer idTorneo) {
        ResponseEntity responseEntity;
        HashMap responseObject = new HashMap();
        TorneoDTO torneoDTO;

        try {
            torneoDTO = calendarizarEtapasTorneoService.getTorneo(idTorneo);
            responseEntity = ResponseEntity.ok(torneoDTO);
        } catch (ValidacionException e) {
            responseObject.put(Constantes.MENSAJE, e.getMessage());
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
        } catch (Exception e) {
            responseObject.put(Constantes.MENSAJE, Constantes.OCURRIO_ERROR_INESPERADO);
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
            System.out.println(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
        }

        return responseEntity;
    }

    @PutMapping(value = "/update")
    public ResponseEntity updateTorneo(@RequestBody TorneoDTO torneoDTO) {

        ResponseEntity responseEntity;
        HashMap responseObject = new HashMap();

        try {
            calendarizarEtapasTorneoService.updateTorneo(torneoDTO);
            responseObject.put(Constantes.MENSAJE, "Torneo Actualizado Correctamente");
            responseObject.put(Constantes.MENSAJE, Constantes.OK);
            responseEntity = ResponseEntity.ok(responseObject);
        } catch (ValidacionException e) {
            responseObject.put(Constantes.MENSAJE, e.getMessage());
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
        } catch (Exception e) {
            responseObject.put(Constantes.MENSAJE, Constantes.OCURRIO_ERROR_INESPERADO);
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
            System.out.println(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
        }

        return responseEntity;
    }

    @DeleteMapping("/delete/{idTorneo}")
    public ResponseEntity deleteTorneo(@PathVariable Integer idTorneo) {
        ResponseEntity responseEntity;
        HashMap responseObject = new HashMap();

        try {
            calendarizarEtapasTorneoService.deleteTorneo(idTorneo);
            responseObject.put(Constantes.MENSAJE, "Se Elimino el Torneo Correctamente");
            responseObject.put(Constantes.CODIGO, Constantes.OK);
            responseEntity = ResponseEntity.ok(responseObject);
        } catch (ValidacionException e) {
            responseObject.put(Constantes.MENSAJE, e.getMessage());
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
        } catch (Exception e) {
            responseObject.put(Constantes.MENSAJE, Constantes.OCURRIO_ERROR_INESPERADO);
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
            System.out.println(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
        }

        return responseEntity;
    }

    @GetMapping("/batallas/consulta/{idEtapa}")
    public ResponseEntity getBatallas(@PathVariable Integer idEtapa) {
        ResponseEntity responseEntity;
        HashMap responseObject = new HashMap();
        BatallasDTO batallasDTO;

        try {
            batallasDTO = calendarizarEtapasTorneoService.getBatallas(idEtapa);
            responseEntity = ResponseEntity.ok(batallasDTO);
        } catch (ValidacionException e) {
            responseObject.put(Constantes.MENSAJE, e.getMessage());
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
        } catch (Exception e) {
            responseObject.put(Constantes.MENSAJE, Constantes.OCURRIO_ERROR_INESPERADO);
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
            System.out.println(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
        }

        return responseEntity;
    }

    @PutMapping(value = "/batallas/update")
    public ResponseEntity updateBatallas(@RequestBody BatallasDTO batallasDTO) {

        ResponseEntity responseEntity;
        HashMap responseObject = new HashMap();

        try {
            calendarizarEtapasTorneoService.updateBatallas(batallasDTO);
            responseObject.put(Constantes.MENSAJE, "Batallas Actualizadas Correctamente");
            responseObject.put(Constantes.CODIGO, Constantes.OK);
            responseEntity = ResponseEntity.ok(responseObject);
        } catch (ValidacionException e) {
            responseObject.put(Constantes.MENSAJE, e.getMessage());
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
        } catch (Exception e) {
            responseObject.put(Constantes.MENSAJE, Constantes.OCURRIO_ERROR_INESPERADO);
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
            System.out.println(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
        }

        return responseEntity;
    }

    @DeleteMapping("/batallas/delete/{idEtapa}")
    public ResponseEntity deleteBatallas(@PathVariable Integer idEtapa) {
        ResponseEntity responseEntity;
        HashMap responseObject = new HashMap();

        try {
            calendarizarEtapasTorneoService.deleteBatallas(idEtapa);
            responseObject.put(Constantes.MENSAJE, "Se Eliminarons las batallas Correctamente");
            responseObject.put(Constantes.CODIGO, Constantes.OK);
            responseEntity = ResponseEntity.ok(responseObject);
        } catch (ValidacionException e) {
            responseObject.put(Constantes.MENSAJE, e.getMessage());
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
        } catch (Exception e) {
            responseObject.put(Constantes.MENSAJE, Constantes.OCURRIO_ERROR_INESPERADO);
            responseObject.put(Constantes.CODIGO, Constantes.ERROR);
            responseEntity = ResponseEntity.status(201).body(responseObject);
            System.out.println(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
        }

        return responseEntity;
    }

}
