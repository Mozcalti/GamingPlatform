package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.model.dto.InstitucionDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.model.dto.TablaInstitucionDTO;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;

public interface InstitucionService {

    List<InstitucionDTO> cargarArchivo(MultipartFile file) ;
    List<Institucion> guardarInstituciones(List<Institucion> instituciones);
    TablaDTO<TablaInstitucionDTO> listaInstituciones(String cadena, Integer indice);
    TablaInstitucionDTO obtenerInstitucion(UUID id);


}
