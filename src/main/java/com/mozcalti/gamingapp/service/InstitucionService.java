package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.model.Institucion;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface InstitucionService {

    List<Institucion> cargarArchivo(MultipartFile file) ;
    List<Institucion> guardarInstituciones(List<Institucion> instituciones) ;


}
