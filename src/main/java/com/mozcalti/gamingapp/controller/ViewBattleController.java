package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.batallas.view.BatallaViewDTO;
import com.mozcalti.gamingapp.service.batallas.ViewBattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/visualizar")
public class ViewBattleController {

    @Autowired
    private ViewBattleService viewBattleService;

    @GetMapping("/datos/{token}")
    public BatallaViewDTO obtieneDatos(@PathVariable String token) {
        return viewBattleService.obtieneDatosViewBattle(token);
    }



}
