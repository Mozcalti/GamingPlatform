package com.mozcalti.gamingapp.model.batallas.resultado;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

import java.util.List;

@XStreamAlias("recordInfo")
@Data
public class RecordInfo {

    @XStreamAsAttribute
    private Integer robotCount;

    @XStreamAsAttribute
    private Integer roundsCount;

    @XStreamAsAttribute
    private Integer ver;

    @XStreamAlias("rules")
    private Rules rules;

    @XStreamAlias("rounds")
    private List<Turns> rounds;

    @XStreamAlias("results")
    private List<Result> results;
}
