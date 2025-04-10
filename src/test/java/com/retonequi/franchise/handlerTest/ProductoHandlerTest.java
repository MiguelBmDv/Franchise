package com.retonequi.franchise.handlerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retonequi.franchise.domain.api.IProductoServicePort;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.infraestructure.entrypoints.dto.ProductoDTO;
import com.retonequi.franchise.infraestructure.entrypoints.dto.ProductoTopDTO;
import com.retonequi.franchise.infraestructure.entrypoints.handler.ProductosHandler;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.IProductoMapper;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.IProductoTopMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductosHandlerTest {

    private IProductoServicePort productoServicePort;
    private IProductoMapper productoMapper;
    private IProductoTopMapper productoTopMapper;
    private ProductosHandler productosHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        productoServicePort = mock(IProductoServicePort.class);
        productoMapper = mock(IProductoMapper.class);
        productoTopMapper = mock(IProductoTopMapper.class);
        productosHandler = new ProductosHandler(productoServicePort, productoMapper, productoTopMapper);
    }

    @Test
    void createProducto_ReturnsCreated() throws Exception {
        ProductoDTO dto = ProductoDTO.builder()
            .id(null)
            .nombre("Producto X")
            .stock(10)
            .sucursalId(UUID.randomUUID().toString())
            .build();
        Producto model = new Producto(null, dto.getNombre(), dto.getStock(), dto.getSucursalId(), null);

        when(productoMapper.toModel(dto)).thenReturn(model);
        when(productoServicePort.saveProducto(any())).thenReturn(Mono.empty()); // Esto ahora va a funcionar

        String jsonBody = objectMapper.writeValueAsString(dto);
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());

        MockServerHttpRequest httpRequest = MockServerHttpRequest
            .post("/api/producto")
            .header("Content-Type", "application/json")
            .body(Mono.just(dataBuffer));

        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        Mono<ServerResponse> response = productosHandler.createProducto(request);

        StepVerifier.create(response)
            .expectNextMatches(res -> res.statusCode().equals(HttpStatus.CREATED))
            .verifyComplete();
    }


    @Test
    void createProducto_WhenDomainException_ReturnsBadRequest() throws Exception {
        ProductoDTO dto = ProductoDTO.builder()
            .id(UUID.randomUUID().toString())
            .nombre("Nombre")
            .stock(5)
            .sucursalId(UUID.randomUUID().toString())
            .build();
        Producto model = new Producto(null, dto.getNombre(), dto.getStock(), dto.getSucursalId(), null);

        when(productoMapper.toModel(dto)).thenReturn(model);
        when(productoServicePort.saveProducto(any()))
            .thenReturn(Mono.error(new DomainException(Messages.PRODUCTO_ALREADY_EXISTS)));

        String jsonBody = objectMapper.writeValueAsString(dto);
        ServerRequest request = buildRequestWithBody("POST", "/producto", jsonBody);

        StepVerifier.create(productosHandler.createProducto(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
            .verifyComplete();
    }

    @Test
    void createProducto_WhenException_ReturnsInternalError() throws Exception {
        ProductoDTO dto = ProductoDTO.builder()
            .id(UUID.randomUUID().toString())
            .nombre("Nombre")
            .stock(5)
            .sucursalId(UUID.randomUUID().toString())
            .build();
        Producto model = new Producto(null, dto.getNombre(), dto.getStock(), dto.getSucursalId(), null);

        when(productoMapper.toModel(dto)).thenReturn(model);
        when(productoServicePort.saveProducto(any())).thenReturn(Mono.error(new RuntimeException("Fallo")));

        String jsonBody = objectMapper.writeValueAsString(dto);
        ServerRequest request = buildRequestWithBody("POST", "/api/producto", jsonBody);

        StepVerifier.create(productosHandler.createProducto(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
            .verifyComplete();
    }

    @Test
    void getProductos_ReturnsOk() {
        Producto model = new Producto(UUID.randomUUID(), "Producto A", 5, UUID.randomUUID().toString(), null);
        ProductoDTO dto = ProductoDTO.builder()
            .id(model.id().toString())
            .nombre(model.nombre())
            .stock(model.stock())
            .sucursalId(model.sucursalId())
            .build();

        when(productoServicePort.getAllProductos()).thenReturn(Flux.just(model));
        when(productoMapper.toDTO(model)).thenReturn(dto);

        ServerRequest request = MockServerRequest.builder().build();

        StepVerifier.create(productosHandler.getProductos(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
            .verifyComplete();
    }

    @Test
    void getProductos_WhenDomainException_ReturnsBadRequest() {
        when(productoServicePort.getAllProductos())
            .thenReturn(Flux.error(new DomainException(Messages.PRODUCTO_NOT_FOUND)));

        ServerRequest request = MockServerRequest.builder().build();

        StepVerifier.create(productosHandler.getProductos(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
            .verifyComplete();
    }

    @Test
    void getProductos_WhenException_ReturnsInternalError() {
        when(productoServicePort.getAllProductos())
            .thenReturn(Flux.error(new RuntimeException("Ups")));

        ServerRequest request = MockServerRequest.builder().build();

        StepVerifier.create(productosHandler.getProductos(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
            .verifyComplete();
    }

    @Test
    void updateProducto_ReturnsCreated() throws Exception {
        UUID id = UUID.randomUUID();
        ProductoDTO dto = ProductoDTO.builder()
            .id(id.toString())
            .nombre("Nuevo nombre")
            .stock(5)
            .sucursalId(UUID.randomUUID().toString())
            .build();
        
        Producto existing = new Producto(id, "Viejo nombre", 10, dto.getSucursalId(), null);
        Producto updated = new Producto(id, dto.getNombre(), dto.getStock(), existing.sucursalId(), null);
    
        when(productoServicePort.getProducto(id)).thenReturn(Mono.just(existing));
        when(productoServicePort.updateProducto(updated)).thenReturn(Mono.empty());
    
        String jsonBody = objectMapper.writeValueAsString(dto);
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());
    
        MockServerHttpRequest httpRequest = MockServerHttpRequest
            .put("/api/producto")
            .header("Content-Type", "application/json")
            .body(Mono.just(dataBuffer));
    
        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
    
        Mono<ServerResponse> response = productosHandler.updateProducto(request);
    
        StepVerifier.create(response)
            .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.CREATED))
            .verifyComplete();
    }

    @Test
    void updateProducto_WhenDomainException_ReturnsBadRequest() throws Exception {
        UUID id = UUID.randomUUID();
        ProductoDTO dto = ProductoDTO.builder()
            .id(id.toString())
            .nombre("Nombre")
            .stock(5)
            .sucursalId(UUID.randomUUID().toString())
            .build();

        when(productoServicePort.getProducto(id)).thenReturn(Mono.error(new DomainException(Messages.PRODUCTO_NOT_FOUND)));

        String jsonBody = objectMapper.writeValueAsString(dto);
        ServerRequest request = buildRequestWithBody("PUT", "/api/producto", jsonBody);

        StepVerifier.create(productosHandler.updateProducto(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
            .verifyComplete();
    }

    @Test
    void updateProducto_WhenUnexpectedException_ReturnsInternalServerError() throws Exception {
        UUID id = UUID.randomUUID();
        ProductoDTO dto = ProductoDTO.builder()
            .id(id.toString())
            .nombre("Producto raro")
            .stock(5)
            .sucursalId(UUID.randomUUID().toString())
            .build();

        when(productoServicePort.getProducto(id)).thenThrow(new RuntimeException("Fallo inesperado"));

        String jsonBody = objectMapper.writeValueAsString(dto);
        ServerRequest request = buildRequestWithBody("PUT", "/api/producto", jsonBody);

        StepVerifier.create(productosHandler.updateProducto(request))
            .expectNextMatches(res -> res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
            .verifyComplete();
    }


    @Test
    void deleteProducto_ReturnsOk() {
        UUID id = UUID.randomUUID();
        ServerRequest request = MockServerRequest.builder()
            .pathVariable("id", id.toString()).build();

        when(productoServicePort.deleteProducto(id)).thenReturn(Mono.empty());

        StepVerifier.create(productosHandler.deleteProducto(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
            .verifyComplete();
    }
    @Test
    void deleteProducto_WhenUnexpectedException_ReturnsInternalServerError() {
        UUID id = UUID.randomUUID();

        when(productoServicePort.deleteProducto(id)).thenReturn(Mono.error(new RuntimeException("Fallo feo")));

        ServerRequest request = MockServerRequest.builder()
            .pathVariable("id", id.toString())
            .build();

        StepVerifier.create(productosHandler.deleteProducto(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
            .verifyComplete();
    }

    @Test
    void deleteProducto_WhenUUIDInvalid_ReturnsBadRequest() {
        String invalidUUID = "not-a-uuid";
        ServerRequest request = MockServerRequest.builder()
            .pathVariable("id", invalidUUID)
            .build();

        StepVerifier.create(productosHandler.deleteProducto(request))
            .expectNextMatches(res -> res.statusCode().equals(HttpStatus.BAD_REQUEST))
            .verifyComplete();
    }

    @Test
    void deleteProducto_WhenDomainException_ReturnsBadRequest() {
        UUID id = UUID.randomUUID();
        ServerRequest request = MockServerRequest.builder()
            .pathVariable("id", id.toString()).build();

        when(productoServicePort.deleteProducto(id))
            .thenReturn(Mono.error(new DomainException(Messages.PRODUCTO_NOT_FOUND)));

        StepVerifier.create(productosHandler.deleteProducto(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
            .verifyComplete();
    }

    @Test
    void getTopStockBySucursal_ReturnsOk() {
        UUID franquiciaId = UUID.randomUUID();
        ProductoTopDTO dto = ProductoTopDTO.builder()
            .id(UUID.randomUUID().toString())
            .nombre("Nuevo nombre")
            .stock(5)
            .sucursalId(UUID.randomUUID().toString())
            .sucursalNombre(null)
            .build();
        Producto model = new Producto(UUID.randomUUID(), "Producto", 100, "sucursalId", null);

        when(productoServicePort.getTopStockBySucursal(franquiciaId)).thenReturn(Flux.just(model));
        when(productoTopMapper.toDTOTop(model)).thenReturn(dto);

        ServerRequest request = MockServerRequest.builder()
            .pathVariable("franquiciaId", franquiciaId.toString()).build();

        StepVerifier.create(productosHandler.getTopStockBySucursal(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
            .verifyComplete();
    }

    @Test
    void getTopStockBySucursal_WhenDomainException_ReturnsBadRequest() {
        UUID franquiciaId = UUID.randomUUID();
        ServerRequest request = MockServerRequest.builder()
            .pathVariable("franquiciaId", franquiciaId.toString()).build();

        when(productoServicePort.getTopStockBySucursal(franquiciaId))
            .thenReturn(Flux.error(new DomainException(Messages.PRODUCTO_NOT_FOUND)));

        StepVerifier.create(productosHandler.getTopStockBySucursal(request))
            .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
            .verifyComplete();
    }

    @Test
    void getTopStockBySucursal_WhenUnexpectedException_ReturnsInternalServerError() {
        UUID franquiciaId = UUID.randomUUID();

        when(productoServicePort.getTopStockBySucursal(franquiciaId))
            .thenReturn(Flux.error(new RuntimeException("Fallo raro")));

        ServerRequest request = MockServerRequest.builder()
            .pathVariable("franquiciaId", franquiciaId.toString())
            .build();

        StepVerifier.create(productosHandler.getTopStockBySucursal(request))
            .expectNextMatches(res -> res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
            .verifyComplete();
    }

    @Test
    void getTopStockBySucursal_WhenUUIDInvalid_ReturnsBadRequest() {
        ServerRequest request = MockServerRequest.builder()
            .pathVariable("franquiciaId", "invalid-uuid")
            .build();

        StepVerifier.create(productosHandler.getTopStockBySucursal(request))
            .expectNextMatches(res -> res.statusCode().equals(HttpStatus.BAD_REQUEST))
            .verifyComplete();
    }

    private ServerRequest buildRequestWithBody(String method, String uri, String jsonBody) {
        DataBuffer buffer = new DefaultDataBufferFactory().wrap(jsonBody.getBytes());
        MockServerHttpRequest httpRequest = MockServerHttpRequest.method(HttpMethod.valueOf(method), uri)
            .header("Content-Type", "application/json")
            .body(Mono.just(buffer));

        return ServerRequest.create(MockServerWebExchange.from(httpRequest), HandlerStrategies.withDefaults().messageReaders());
    }
}