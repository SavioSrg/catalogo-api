package com.savio.catalogo_api.jdbc.connection;

public final class Config {

    private Config() {}

    public static String url() {
        return "jdbc:postgresql://localhost:5432/" + exigir("POSTGRES_DB");
    }

    public static String usuario() {
        return exigir("POSTGRES_USER");
    }

    public static String senha() {
        return exigir("POSTGRES_PASSWORD");
    }

    private static String exigir(String nome) {
        String valor = System.getenv(nome);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Variável de ambiente ausente: " + nome);
        }
        return valor;
    }
}
