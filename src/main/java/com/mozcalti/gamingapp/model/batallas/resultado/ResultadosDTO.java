package com.mozcalti.gamingapp.model.batallas.resultado;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResultadosDTO {

    private Integer idInstitucion;
    private String teamLeaderName;
    private Integer rank;
    private Double score;
    private Double survival;
    private Double lastSurvivorBonus;
    private Double bulletDamage;
    private Double bulletDamageBonus;
    private Double ramDamage;
    private Double ramDamageBonus;
    private Integer firsts;
    private Integer seconds;
    private Integer thirds;
    private Integer ver;

}
