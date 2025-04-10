package com.retonequi.franchise.infraestructure.entrypoints.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.retonequi.franchise.infraestructure.entrypoints.handler.SucursalHandler;
import com.retonequi.franchise.infraestructure.entrypoints.util.Constants;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class SucursalRouter {

    private static final String BASE_PATH = Constants.Routes.SUCURSALES;

    @Bean
    public RouterFunction<ServerResponse> sucursalRoutes(SucursalHandler handler) {
        return RouterFunctions
                .route(POST(BASE_PATH), handler::createSucursal)
                .andRoute(GET(BASE_PATH), handler::getSucursales)
                .andRoute(PUT(BASE_PATH), handler::updateSucursal);
    }
}


