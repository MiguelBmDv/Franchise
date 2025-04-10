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

import com.retonequi.franchise.infraestructure.entrypoints.handler.FranquiciaHandler;
import com.retonequi.franchise.infraestructure.entrypoints.router.FranquiciaRouter;

@ExtendWith(MockitoExtension.class)
class FranquiciaRouterTest {

    @Mock
    private FranquiciaHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        RouterFunction<ServerResponse> route = new FranquiciaRouter().franquiciaRoutes(handler);
        webTestClient = WebTestClient.bindToRouterFunction(route).build();
    }

    @Test
    void testCreateFranquicia() {
        when(handler.createFranquicia(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.post()
                .uri("/franquicias")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetFranquicias() {
        when(handler.getFranquicias(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.get()
                .uri("/franquicias")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUpdateFranquicia() {
        when(handler.updateFranquicia(any())).thenReturn(ServerResponse.ok().build());

        webTestClient.put()
                .uri("/franquicias")
                .exchange()
                .expectStatus().isOk();
    }
}