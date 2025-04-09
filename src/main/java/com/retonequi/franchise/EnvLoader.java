package com.retonequi.franchise;

import io.github.cdimascio.dotenv.Dotenv;

public final class EnvLoader {

    private EnvLoader() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void load() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}