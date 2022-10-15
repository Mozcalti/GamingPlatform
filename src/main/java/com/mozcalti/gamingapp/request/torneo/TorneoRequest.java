package com.mozcalti.gamingapp.request.torneo;

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
    private List<HoraHabilRequest> horasHabilesRequest;
    private Integer numEtapas;
    private List<EtapaRequest> etapasRequest;

}

