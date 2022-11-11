package com.mozcalti.gamingapp.model.batallas.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Document;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BatallaViewDTO {

    List<String> battleParticipantes;
    String battleFecha;
    Document battleXml;
    String estatus;

}
