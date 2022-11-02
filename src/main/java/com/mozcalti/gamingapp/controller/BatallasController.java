package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.batallas.BatallasDTO;
import com.mozcalti.gamingapp.service.batallas.CalendarizarEtapasTorneoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/batallas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BatallasController {

    private CalendarizarEtapasTorneoService calendarizarEtapasTorneoService;

    @GetMapping("/genera/{idEtapa}")
    public BatallasDTO getBatallasByIdEtapa(@PathVariable Integer idEtapa) {
        return calendarizarEtapasTorneoService.generaBatallas(idEtapa);
    }

    @PostMapping("/save")
    public void saveBatallas(@RequestBody BatallasDTO batallasDTO) {
        calendarizarEtapasTorneoService.saveBatallas(batallasDTO);
    }

    @GetMapping("/consulta/{idEtapa}")
    public BatallasDTO getBatallas(@PathVariable Integer idEtapa) {
        return calendarizarEtapasTorneoService.getBatallas(idEtapa);
    }

    @PutMapping(value = "/update")
    public void updateBatallas(@RequestBody BatallasDTO batallasDTO) {
        calendarizarEtapasTorneoService.updateBatallas(batallasDTO);
    }

    @DeleteMapping("/delete/{idEtapa}")
    public void deleteBatallas(@PathVariable Integer idEtapa) {
        calendarizarEtapasTorneoService.deleteBatallas(idEtapa);
    }

}
