package com.mozcalti.gamingapp.model.batallas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantesDTO {
    private Integer numeroEtapa;
    private List<Integer> idParticipantes;
}
