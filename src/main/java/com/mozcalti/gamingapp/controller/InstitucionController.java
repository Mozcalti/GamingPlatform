package com.mozcalti.gamingapp.controller;


import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.model.dto.DetalleInstitucionDTO;
import com.mozcalti.gamingapp.model.dto.InstitucionDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.model.dto.TablaInstitucionDTO;
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

    @GetMapping("/todas")
    public TablaDTO<TablaInstitucionDTO> todasInstituciones(@RequestParam String texto, @RequestParam Integer indice){
        return institucionService.listaInstituciones(texto,indice);
    }

    @GetMapping("/{id}")
    public DetalleInstitucionDTO obtenerInstitucion(@PathVariable Integer id){
        return institucionService.obtenerInstitucion(id);
    }

    @PostMapping(value = "/guardarInstitucion")
    public void guardarInstitucion(@RequestBody InstitucionDTO institucionDTO) {
        institucionService.guardarInstitucion(institucionDTO);
    }

    @GetMapping("/")
    public Iterable<Institucion> catalogoInstituciones(){
        return institucionService.instituciones();
    }

}
