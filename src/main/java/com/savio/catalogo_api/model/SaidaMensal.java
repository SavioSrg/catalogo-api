package com.savio.catalogo_api.model;

import java.time.LocalDate;

public record SaidaMensal (
        LocalDate mes,
        long total) {}
