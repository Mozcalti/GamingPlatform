package com.mozcalti.gamingapp.controller;


import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.model.dto.InstitucionDTO;
import com.mozcalti.gamingapp.service.InstitucionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

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


}
