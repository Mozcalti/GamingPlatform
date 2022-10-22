package com.mozcalti.gamingapp.model.batallas.resultado;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

@XStreamAlias("result")
@Data
public class Result {

    @XStreamAsAttribute
    private String teamLeaderName;

    @XStreamAsAttribute
    private Integer rank;

    @XStreamAsAttribute
    private Double score;

    @XStreamAsAttribute
    private Double survival;

    @XStreamAsAttribute
    private Double lastSurvivorBonus;

    @XStreamAsAttribute
    private Double bulletDamage;

    @XStreamAsAttribute
    private Double bulletDamageBonus;

    @XStreamAsAttribute
    private Double ramDamage;

    @XStreamAsAttribute
    private Double ramDamageBonus;

    @XStreamAsAttribute
    private Integer firsts;

    @XStreamAsAttribute
    private Integer seconds;

    @XStreamAsAttribute
    private Integer thirds;

    @XStreamAsAttribute
    private Integer ver;

}
