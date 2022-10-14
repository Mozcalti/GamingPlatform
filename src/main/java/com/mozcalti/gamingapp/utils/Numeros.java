package com.mozcalti.gamingapp.utils;

public enum Numeros {
    CERO(0),
    UNO(1) ,
    DOS(2),
    TRES(3);
    private final int numero;
    Numeros(int numero) {
        this.numero = numero;
    }
    public int retornarNumero() {
        return numero;
    }
}
