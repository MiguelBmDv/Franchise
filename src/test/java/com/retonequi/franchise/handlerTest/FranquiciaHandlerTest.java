package com.retonequi.franchise.handlerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retonequi.franchise.domain.api.IFranquiciaServicePort;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.infraestructure.entrypoints.dto.FranquiciaDTO;
import com.retonequi.franchise.infraestructure.entrypoints.handler.FranquiciaHandler;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.IFranquiciaMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FranquiciaHandlerTest {

    private IFranquiciaServicePort franquiciaServicePort;
    private IFranquiciaMapper franquiciaMapper;
    private FranquiciaHandler franquiciaHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        franquiciaServicePort = mock(IFranquiciaServicePort.class);
        franquiciaMapper = mock(IFranquiciaMapper.class);
        franquiciaHandler = new FranquiciaHandler(franquiciaServicePort, franquiciaMapper);
    }

    @Test
    void createFranquicia_ReturnsCreatedStatus() throws Exception {
        FranquiciaDTO dto = FranquiciaDTO.builder().id(UUID.randomUUID().toString()).nombre("Franquicia 1").build();
        Franquicia model = new Franquicia(UUID.fromString(dto.getId()), dto.getNombre());

        when(franquiciaMapper.toModel(dto)).thenReturn(model);
        when(franquiciaServicePort.saveFranquicia(model)).thenReturn(Mono.empty());

        String jsonBody = objectMapper.writeValueAsString(dto);
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());

        MockServerHttpRequest httpRequest = MockServerHttpRequest
                .post("/api/franquicia")
                .header("Content-Type", "application/json")
                .body(Mono.just(dataBuffer));

        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<ServerResponse> response = franquiciaHandler.createFranquicia(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();
    }

    @Test
    void createFranquicia_WhenDomainException_ReturnsBadRequest() throws Exception {
        FranquiciaDTO dto = FranquiciaDTO.builder().id(UUID.randomUUID().toString()).nombre("Franquicia x").build();
        Franquicia model = new Franquicia(UUID.fromString(dto.getId()), dto.getNombre());

        when(franquiciaMapper.toModel(dto)).thenReturn(model);
        when(franquiciaServicePort.saveFranquicia(model))
                .thenReturn(Mono.error(new DomainException(Messages.FRANQUICIA_ALREADY_EXISTS)));

        String jsonBody = objectMapper.writeValueAsString(dto);
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());

        MockServerHttpRequest httpRequest = MockServerHttpRequest
                .post("/api/franquicia")
                .header("Content-Type", "application/json")
                .body(Mono.just(dataBuffer));

        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<ServerResponse> response = franquiciaHandler.createFranquicia(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void createFranquicia_WhenGenericException_ReturnsInternalServerError() throws Exception {
        FranquiciaDTO dto = FranquiciaDTO.builder().id(UUID.randomUUID().toString()).nombre("Franquicia error").build();
        Franquicia model = new Franquicia(UUID.fromString(dto.getId()), dto.getNombre());

        when(franquiciaMapper.toModel(dto)).thenReturn(model);
        when(franquiciaServicePort.saveFranquicia(model))
                .thenReturn(Mono.error(new RuntimeException("Fallo inesperado")));

        String jsonBody = objectMapper.writeValueAsString(dto);
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());

        MockServerHttpRequest httpRequest = MockServerHttpRequest
                .post("/api/franquicia")
                .header("Content-Type", "application/json")
                .body(Mono.just(dataBuffer));

        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<ServerResponse> response = franquiciaHandler.createFranquicia(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }

    @Test
    void getFranquicias_ReturnsOk() {
        Franquicia franquicia = new Franquicia(UUID.randomUUID(), "Franquicia");
        FranquiciaDTO dto = FranquiciaDTO.builder().id(franquicia.id().toString()).nombre(franquicia.nombre()).build();

        when(franquiciaServicePort.getAllFranquicias()).thenReturn(Flux.just(franquicia));
        when(franquiciaMapper.toDTO(franquicia)).thenReturn(dto);

        ServerRequest request = MockServerRequest.builder().build();

        Mono<ServerResponse> response = franquiciaHandler.getFranquicias(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void getFranquicias_WhenDomainException_ReturnsBadRequest() {
        when(franquiciaServicePort.getAllFranquicias())
                .thenReturn(Flux.error(new DomainException(Messages.INTERNAL_ERROR)));

        ServerRequest request = MockServerRequest.builder().build();

        Mono<ServerResponse> response = franquiciaHandler.getFranquicias(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void getFranquicias_WhenGenericException_ReturnsInternalServerError() {
        when(franquiciaServicePort.getAllFranquicias())
                .thenReturn(Flux.error(new RuntimeException("Error inesperado")));

        ServerRequest request = MockServerRequest.builder().build();

        Mono<ServerResponse> response = franquiciaHandler.getFranquicias(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }

    @Test
    void updateFranquicia_ReturnsOkStatus() throws Exception {
        UUID id = UUID.randomUUID();
        FranquiciaDTO dto = FranquiciaDTO.builder().id(id.toString()).nombre("Actualizado").build();
        Franquicia oldFranquicia = new Franquicia(id, "Viejo nombre");
        Franquicia incoming = new Franquicia(id, "Actualizado");
        Franquicia updated = new Franquicia(oldFranquicia.id(), incoming.nombre());

        when(franquiciaMapper.toModel(dto)).thenReturn(incoming);
        when(franquiciaServicePort.getFranquicia(id)).thenReturn(Mono.just(oldFranquicia));
        when(franquiciaServicePort.updateFranquicia(updated)).thenReturn(Mono.empty());

        String jsonBody = objectMapper.writeValueAsString(dto);
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());

        MockServerHttpRequest httpRequest = MockServerHttpRequest
                .put("/api/franquicia")
                .header("Content-Type", "application/json")
                .body(Mono.just(dataBuffer));

        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<ServerResponse> response = franquiciaHandler.updateFranquicia(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.OK)
                )
                .verifyComplete();
    }

    @Test
    void updateFranquicia_WhenDomainExceptionThrownDuringGet_ReturnsBadRequest() throws Exception {
        UUID id = UUID.randomUUID();
        FranquiciaDTO dto = FranquiciaDTO.builder().id(id.toString()).nombre("Nombre").build();

        when(franquiciaMapper.toModel(dto)).thenReturn(new Franquicia(id, "Nombre"));
        when(franquiciaServicePort.getFranquicia(id))
                .thenReturn(Mono.error(new DomainException(Messages.FRANQUICIA_NOT_FOUND)));

        String jsonBody = objectMapper.writeValueAsString(dto);
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());

        MockServerHttpRequest httpRequest = MockServerHttpRequest
                .put("/api/franquicia")
                .header("Content-Type", "application/json")
                .body(Mono.just(dataBuffer));

        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<ServerResponse> response = franquiciaHandler.updateFranquicia(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST)
                )
                .verifyComplete();
    }

    @Test
    void updateFranquicia_WhenDomainExceptionThrownDuringUpdate_ReturnsBadRequest() throws Exception {
        UUID id = UUID.randomUUID();
        FranquiciaDTO dto = FranquiciaDTO.builder().id(id.toString()).nombre("Nombre").build();
        Franquicia oldFranquicia = new Franquicia(id, "Viejo");
        Franquicia incoming = new Franquicia(id, "Nombre");

        when(franquiciaMapper.toModel(dto)).thenReturn(incoming);
        when(franquiciaServicePort.getFranquicia(id)).thenReturn(Mono.just(oldFranquicia));
        when(franquiciaServicePort.updateFranquicia(any()))
                .thenReturn(Mono.error(new DomainException(Messages.FRANQUICIA_ALREADY_EXISTS)));

        String jsonBody = objectMapper.writeValueAsString(dto);
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());

        MockServerHttpRequest httpRequest = MockServerHttpRequest
                .put("/api/franquicia")
                .header("Content-Type", "application/json")
                .body(Mono.just(dataBuffer));

        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<ServerResponse> response = franquiciaHandler.updateFranquicia(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.BAD_REQUEST)
                )
                .verifyComplete();
    }

    @Test
    void updateFranquicia_WhenGenericExceptionThrownDuringUpdate_ReturnsInternalServerError() throws Exception {
        UUID id = UUID.randomUUID();
        FranquiciaDTO dto = FranquiciaDTO.builder().id(id.toString()).nombre("Nombre").build();
        Franquicia oldFranquicia = new Franquicia(id, "Viejo");
        Franquicia incoming = new Franquicia(id, "Nombre");

        when(franquiciaMapper.toModel(dto)).thenReturn(incoming);
        when(franquiciaServicePort.getFranquicia(id)).thenReturn(Mono.just(oldFranquicia));
        when(franquiciaServicePort.updateFranquicia(any()))
                .thenReturn(Mono.error(new RuntimeException("error")));

        String jsonBody = objectMapper.writeValueAsString(dto);
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());

        MockServerHttpRequest httpRequest = MockServerHttpRequest
                .put("/api/franquicia")
                .header("Content-Type", "application/json")
                .body(Mono.just(dataBuffer));

        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<ServerResponse> response = franquiciaHandler.updateFranquicia(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)
                )
                .verifyComplete();
    }

}
