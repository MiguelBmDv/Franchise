package com.retonequi.franchise.infraestructure.entrypoints.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Constants {
    public static final class Routes {
        private Routes() {}

        public static final String FRANQUICIAS = "/franquicias";
        public static final String SUCURSALES = "/sucursales";
        public static final String PRODUCTOS = "/productos";
        public static final String ID = "/{id}";
        public static final String FRANQUICIAID = "/top-stock/{franquiciaId}";

    }

}
