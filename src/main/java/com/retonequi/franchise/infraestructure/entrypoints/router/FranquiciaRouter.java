package com.retonequi.franchise.infraestructure.entrypoints.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.retonequi.franchise.infraestructure.entrypoints.handler.FranquiciaHandler;
import com.retonequi.franchise.infraestructure.entrypoints.util.Constants;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class FranquiciaRouter {

    private static final String BASE_PATH = Constants.Routes.FRANQUICIAS;

    @Bean
    public RouterFunction<ServerResponse> franquiciaRoutes(FranquiciaHandler handler) {
        return RouterFunctions
                .route(POST(BASE_PATH), handler::createFranquicia)
                .andRoute(GET(BASE_PATH), handler::getFranquicias)
                .andRoute(PUT(BASE_PATH), handler::updateFranquicia);
    }
} 


