package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.PerfilesEntity;
import com.mozcalti.gamingapp.service.PerfilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/perfil")
public class PerfilesController {

    @Autowired
    private PerfilesService perfilesService;

    @GetMapping(value = "/all")
    public List<PerfilesEntity> getAll() {
        return perfilesService.getAll();
    }

    @PostMapping(value = "/save")
    public PerfilesEntity save(@RequestBody PerfilesEntity perfilesEntity) {
        return perfilesService.save(perfilesEntity);
    }

}
