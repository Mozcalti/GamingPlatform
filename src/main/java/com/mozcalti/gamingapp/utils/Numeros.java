package com.mozcalti.gamingapp.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Numeros {
    CERO(0),
    UNO(1),
    DOS(2),
    TRES(3),
    CUATRO(4),
    CINCO(5),
    SEIS(6),
    SIETE(7),
    OCHO(8),

    UNO_NEGATIVO(-1),
    DOS_NEGATIVO(-2);

    private final int numero;

}
