package com.retonequi.franchise.infraestructure.entrypoints.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.retonequi.franchise.infraestructure.entrypoints.handler.ProductosHandler;
import com.retonequi.franchise.infraestructure.entrypoints.util.Constants;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ProductoRouter {

    private static final String BASE_PATH = Constants.Routes.PRODUCTOS;
    private static final String PARAM_ID_PATH = Constants.Routes.ID;
    private static final String PARAM_FRANQUICIA_PATH = Constants.Routes.FRANQUICIAID;
    @Bean
    public RouterFunction<ServerResponse> productoRoutes(ProductosHandler handler) {
        return RouterFunctions
            .route(POST(BASE_PATH), handler::createProducto)
            .andRoute(GET(BASE_PATH), handler::getProductos)
            .andRoute(PUT(BASE_PATH), handler::updateProducto)
            .andRoute(DELETE(BASE_PATH + PARAM_ID_PATH), handler::deleteProducto)
            .andRoute(GET(BASE_PATH + PARAM_FRANQUICIA_PATH), handler::getTopStockBySucursal);
    }
}

