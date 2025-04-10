package com.retonequi.franchise.routerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.retonequi.franchise.infraestructure.entrypoints.handler.ProductosHandler;
import com.retonequi.franchise.infraestructure.entrypoints.router.ProductoRouter;

@ExtendWith(MockitoExtension.class)
class ProductoRouterTest {

    @Mock
    private ProductosHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        RouterFunction<ServerResponse> route = new ProductoRouter().productoRoutes(handler);
        webTestClient = WebTestClient.bindToRouterFunction(route).build();
    }

    @Test
    void testCreateProducto() {
        when(handler.createProducto(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.post().uri("/productos").exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetProductos() {
        when(handler.getProductos(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.get().uri("/productos").exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUpdateProducto() {
        when(handler.updateProducto(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.put().uri("/productos").exchange()
                .expectStatus().isOk();
    }

    @Test
    void testDeleteProducto() {
        when(handler.deleteProducto(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.delete().uri("/productos/123").exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetTopStockBySucursal() {
        when(handler.getTopStockBySucursal(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.get().uri("/productos/top-stock/456").exchange()
                .expectStatus().isOk();
    }
}