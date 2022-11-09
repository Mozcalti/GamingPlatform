package com.mozcalti.gamingapp.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoBatalla {

    INDIVIDUAL("INDIVIDUAL"),
    EQUIPO("EQUIPO"),
    MIXTO("MIXTO");

    private final String trabajo;

}
