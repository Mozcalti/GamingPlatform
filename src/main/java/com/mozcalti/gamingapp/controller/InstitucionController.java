package com.mozcalti.gamingapp.controller;


import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.model.dto.InstitucionDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.model.dto.TablaInstitucionDTO;
import com.mozcalti.gamingapp.service.InstitucionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/institucion")
@AllArgsConstructor
public class InstitucionController {
    private InstitucionService institucionService;

    @PostMapping(value = "/cargarArchivo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<InstitucionDTO> cargarArchivo(@RequestParam(value = "file") MultipartFile file) {
        return institucionService.cargarArchivo(file);
    }

    @PostMapping(value = "/guardar")
    public void guardar(@RequestBody List<Institucion> instituciones) {
        institucionService.guardarInstituciones(instituciones);
    }


    @GetMapping("/todas")
    public TablaDTO<TablaInstitucionDTO> todasInstituciones(@RequestParam String texto, @RequestParam Integer indice){
        return institucionService.listaInstituciones(texto,indice);
    }

    @GetMapping("/{id}")
    public TablaInstitucionDTO obtenerInstitucion(@PathVariable UUID id){
        return institucionService.obtenerInstitucion(id);
    }
}
