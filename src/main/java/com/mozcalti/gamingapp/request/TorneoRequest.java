package com.mozcalti.gamingapp.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TorneoRequest {

    private String fechaInicio;
    private String fechaFin;
    private Integer numEtapas;
    private List<EtapaRequest> etapasRequest;

}

