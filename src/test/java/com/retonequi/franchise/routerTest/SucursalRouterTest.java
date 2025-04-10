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

import com.retonequi.franchise.infraestructure.entrypoints.handler.SucursalHandler;
import com.retonequi.franchise.infraestructure.entrypoints.router.SucursalRouter;

@ExtendWith(MockitoExtension.class)
class SucursalRouterTest {

    @Mock
    private SucursalHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        RouterFunction<ServerResponse> route = new SucursalRouter().sucursalRoutes(handler);
        webTestClient = WebTestClient.bindToRouterFunction(route).build();
    }

    @Test
    void testCreateSucursal() {
        when(handler.createSucursal(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.post().uri("/sucursales").exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetSucursales() {
        when(handler.getSucursales(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.get().uri("/sucursales").exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUpdateSucursal() {
        when(handler.updateSucursal(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.put().uri("/sucursales").exchange()
                .expectStatus().isOk();
    }
}