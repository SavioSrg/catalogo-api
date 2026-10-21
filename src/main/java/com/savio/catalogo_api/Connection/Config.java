package com.savio.catalogo_api.app;

public final class Config {

    private Config() {}

    public static String url() {
        return "jdbc:postgresql://localhost:5432/" + exigir("catalogo");
    }

    public static String usuario() {
        return exigir("app");
    }

    public static String senha() {
        return exigir("senha");
    }

    private static String exigir(String nome) {
        String valor = System.getenv(nome);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Variável de ambiente ausente: " + nome);
        }
        return valor;
    }
}
