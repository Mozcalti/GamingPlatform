package com.mozcalti.gamingapp.model.batallas.resultado;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

@XStreamAlias("rules")
@Data
public class Rules {

    @XStreamAsAttribute
    private Integer battlefieldWidth;

    @XStreamAsAttribute
    private Integer battlefieldHeight;

    @XStreamAsAttribute
    private Integer numRounds;

    @XStreamAsAttribute
    private Double gunCoolingRate;

    @XStreamAsAttribute
    private Integer inactivityTime;

    @XStreamAsAttribute
    private Integer ver;

}
