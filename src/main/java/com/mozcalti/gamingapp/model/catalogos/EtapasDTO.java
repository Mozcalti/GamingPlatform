package com.mozcalti.gamingapp.model.catalogos;

import com.mozcalti.gamingapp.model.Etapas;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EtapasDTO {

    private Integer idEtapa;
    private Integer numeroEtapa;

    public EtapasDTO(Etapas etapas) {
        this.idEtapa = etapas.getIdEtapa();
        this.numeroEtapa = etapas.getNumeroEtapa();
    }
}
