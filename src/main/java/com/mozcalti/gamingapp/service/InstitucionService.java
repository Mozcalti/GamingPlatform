package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.model.dto.InstitucionDTO;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface InstitucionService {

    List<InstitucionDTO> cargarArchivo(MultipartFile file) ;
    List<Institucion> guardarInstituciones(List<Institucion> instituciones) ;


}
