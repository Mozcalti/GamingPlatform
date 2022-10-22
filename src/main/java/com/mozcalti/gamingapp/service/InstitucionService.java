package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.model.dto.InstitucionDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.model.dto.TablaInstitucionDTO;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


public interface InstitucionService {

    List<InstitucionDTO> cargarArchivo(MultipartFile file) ;
    List<Institucion> guardarInstituciones(List<Institucion> instituciones);
    TablaDTO<TablaInstitucionDTO> listaInstituciones(String cadena, Integer indice);
    TablaInstitucionDTO obtenerInstitucion(Integer id);

    Institucion guardarInstitucion(InstitucionDTO institucionDTO);

    Iterable<Institucion> instituciones();



}
