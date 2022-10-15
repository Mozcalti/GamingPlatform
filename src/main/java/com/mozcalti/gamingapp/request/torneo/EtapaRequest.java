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
public class EtapaRequest {

    private int numeroEtapa;
    private String fechaInicio;
    private String fechaFin;
    private ReglasRequest reglasRequest;
    private List<ParticipanteRequest> participantesRequest;
    private List<EquipoRequest> equiposRequest;

}
