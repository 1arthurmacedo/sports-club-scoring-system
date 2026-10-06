package com.arthur.sportsclubscoringsystem.enums;

public enum Posicao {
    PRIMEIRO(5),
    SEGUNDO(3),
    TERCEIRO(1);

    private final int pontosBase;

    Posicao(int pontosBase) {
        this.pontosBase = pontosBase;
    }

    public int getPontosBase() {
        return pontosBase;
    }
}