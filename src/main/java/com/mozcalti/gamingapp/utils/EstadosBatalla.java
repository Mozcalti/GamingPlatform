package com.mozcalti.gamingapp.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstadosBatalla {
    PENDIENTE("PENDIENTE"),
    EN_PROCESO("EN PROCESO"),
    TERMINADA("TERMINADA"),
    CANCELADA("CANCELADA"),
    RESULTADOS("RESULTADOS");
    private final String estado;
}
