package com.mozcalti.gamingapp.model.batallas.resultado;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

@XStreamAlias("turns")
@Data
public class Turns {

    @XStreamAsAttribute
    private Integer value;

}
