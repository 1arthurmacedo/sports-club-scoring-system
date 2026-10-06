package com.arthur.sportsclubscoringsystem.enums;

public enum Nivel {
    ESTADUAL(1.0),
    NACIONAL(1.3),
    REGIONAL(1.6),
    MUNDIAL(2.0);

    private final double multiplicador;

    Nivel(double multiplicador) {
        this.multiplicador = multiplicador;
    }

    public double getMultiplicador() {
        return multiplicador;
    }
}