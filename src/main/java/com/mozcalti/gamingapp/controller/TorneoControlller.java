package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import com.mozcalti.gamingapp.model.batallas.BatallasDTO;
import com.mozcalti.gamingapp.service.CalendarizarEtapasTorneoService;
import com.mozcalti.gamingapp.service.TorneosService;
import com.mozcalti.gamingapp.utils.Constantes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/torneo")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TorneoControlller {

    private CalendarizarEtapasTorneoService calendarizarEtapasTorneoService;

    private TorneosService torneosService;

    @PostMapping(value = "/save")
    public ResponseEntity<String> saveTorneo(@RequestBody TorneoDTO torneoDTO) {

        try {
            calendarizarEtapasTorneoService.saveTorneo(torneoDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Torneo Guardado Correctamente");
        } catch (ValidacionException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Constantes.OCURRIO_ERROR_INESPERADO);
        }

    }

    @GetMapping("/batallas/{idEtapa}")
    public BatallasDTO getBatallasByIdEtapa(@PathVariable Integer idEtapa) {
        return calendarizarEtapasTorneoService.generaBatallas(idEtapa);
    }

    @PostMapping("/batallas/save")
    public ResponseEntity<String> saveBatallas(@RequestBody BatallasDTO batallasDTO) {

        try {
            calendarizarEtapasTorneoService.saveBatallas(batallasDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Batallas Guardadas Correctamente");
        } catch (ValidacionException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Constantes.OCURRIO_ERROR_INESPERADO);
        }

    }

    @GetMapping("/consulta/{idTorneo}")
    public TorneoDTO getTorneo(@PathVariable Integer idTorneo) {
        return calendarizarEtapasTorneoService.getTorneo(idTorneo);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<String> updateTorneo(@RequestBody TorneoDTO torneoDTO) {

        try {
            calendarizarEtapasTorneoService.updateTorneo(torneoDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Torneo Actualizado Correctamente");
        } catch (ValidacionException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Constantes.OCURRIO_ERROR_INESPERADO);
        }

    }

    @DeleteMapping("/delete/{idTorneo}")
    public ResponseEntity<String> deleteTorneo(@PathVariable Integer idTorneo) {

        try {
            calendarizarEtapasTorneoService.deleteTorneo(idTorneo);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Se Elimino el Torneo Correctamente");
        } catch (ValidacionException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Constantes.OCURRIO_ERROR_INESPERADO);
        }

    }

    @GetMapping("/batallas/consulta/{idEtapa}")
    public BatallasDTO getBatallas(@PathVariable Integer idEtapa) {
        return calendarizarEtapasTorneoService.getBatallas(idEtapa);
    }

    @PutMapping(value = "/batallas/update")
    public ResponseEntity<String> updateBatallas(@RequestBody BatallasDTO batallasDTO) {

        try {
            calendarizarEtapasTorneoService.updateBatallas(batallasDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Se Actualizo la Batalla Correctamente");
        } catch (ValidacionException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Constantes.OCURRIO_ERROR_INESPERADO);
        }

    }

    @DeleteMapping("/batallas/delete/{idEtapa}")
    public ResponseEntity<String> deleteBatallas(@PathVariable Integer idEtapa) {

        try {
            calendarizarEtapasTorneoService.deleteBatallas(idEtapa);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Se Eliminaron las batallas Correctamente");
        } catch (ValidacionException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error(Constantes.OCURRIO_ERROR_INESPERADO + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Constantes.OCURRIO_ERROR_INESPERADO);
        }

    }

    @PostMapping("/guardar")
    public void guardaTorneo(@RequestBody TorneoDTO torneoDTO) {
        torneosService.guardaTorneo(torneoDTO);
    }

    @GetMapping("/consultar")
    public TorneoDTO obtieneTorneos() {
        return torneosService.obtieneTorneos();
    }

    @PutMapping("/cambiar")
    public void modificaTorneo(@RequestBody TorneoDTO torneoDTO) {
        torneosService.modificaTorneo(torneoDTO);
    }

    @DeleteMapping("/eliminar")
    public void eliminaTorneo() {
        torneosService.eliminaTorneo();
    }

    @PostMapping("/etapas/guardar")
    public void guardaEtapas(@RequestBody List<EtapaDTO> etapasDTOS) {
        torneosService.guardaEtapas(etapasDTOS);
    }

    @GetMapping("/etapas/consultar")
    public List<EtapaDTO> obtieneEtapas() {
        return torneosService.obtieneEtapas();
    }

    @DeleteMapping("/etapas/eliminar")
    public void eliminaEtapas() {
        torneosService.eliminarEtapas();
    }

    @PutMapping("/etapas/cambiar")
    public void modificaTorneo(@RequestBody List<EtapaDTO> etapasDTOS) {
        torneosService.modificaEtapas(etapasDTOS);
    }

}
