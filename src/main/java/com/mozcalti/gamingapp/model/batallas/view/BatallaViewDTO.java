package com.mozcalti.gamingapp.model.batallas.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class BatallaViewDTO {

    List<String> battleParticipantes;
    String battleFecha;
    String battleXml;

}
