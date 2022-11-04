package com.mozcalti.gamingapp.model.catalogos;

import com.mozcalti.gamingapp.model.Institucion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class InstitucionDTO {
    private Integer idInstitucion;
    private String nombre;

    public InstitucionDTO(Institucion institucion) {
        this.idInstitucion = institucion.getId();
        this.nombre = institucion.getNombre();
    }

}
