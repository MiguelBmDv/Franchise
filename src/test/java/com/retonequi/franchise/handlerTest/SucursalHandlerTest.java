package com.retonequi.franchise.handlerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.retonequi.franchise.domain.api.ISucursalServicePort;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.infraestructure.entrypoints.dto.SucursalDTO;
import com.retonequi.franchise.infraestructure.entrypoints.handler.SucursalHandler;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.ISucursalMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class SucursalHandlerTest {
    
    @Mock
    ISucursalServicePort sucursalServicePort;

    @Mock
    ISucursalMapper sucursalMapper;

    SucursalHandler sucursalHandler;

    @BeforeEach
    void setUp() {
        sucursalHandler = new SucursalHandler(sucursalServicePort, sucursalMapper);
    }

    @Test
    void createSucursal_WhenValidRequest_ReturnsCreated() {
        SucursalDTO dto = SucursalDTO.builder()
            .id(UUID.randomUUID().toString())
            .nombre("Sucursal Centro")
            .franquiciaId(UUID.randomUUID().toString())
            .build();
    
        Sucursal modelMapped = new Sucursal(null, dto.getNombre(), dto.getFranquiciaId());
    
        ServerRequest request = mock(ServerRequest.class);
    
        when(request.bodyToMono(SucursalDTO.class)).thenReturn(Mono.just(dto));
        when(sucursalMapper.toModel(dto)).thenReturn(modelMapped);
        when(sucursalServicePort.saveSucursal(any())).thenReturn(Mono.empty());
    
        StepVerifier.create(sucursalHandler.createSucursal(request))
            .expectNextMatches(response -> {
                assert response.statusCode().equals(HttpStatus.CREATED);
                return true;
            })
            .verifyComplete();
    }    

    @Test
    void createSucursal_WhenDomainException_ReturnsBadRequest() {
        SucursalDTO dto = SucursalDTO.builder()
            .id(UUID.randomUUID().toString())
            .nombre("Sucursal Centro")
            .franquiciaId(UUID.randomUUID().toString())
            .build();
    
        DomainException ex = new DomainException(Messages.SUCURSAL_NOT_FOUND);
    
        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(SucursalDTO.class)).thenReturn(Mono.just(dto));
        when(sucursalMapper.toModel(dto)).thenReturn(
            new Sucursal(null, dto.getNombre(), dto.getFranquiciaId())
        );
        when(sucursalServicePort.saveSucursal(any())).thenReturn(Mono.error(ex));
    
        StepVerifier.create(sucursalHandler.createSucursal(request))
            .expectNextMatches(res -> res.statusCode().equals(HttpStatus.BAD_REQUEST))
            .verifyComplete();
    }
    

    @Test
    void createSucursal_WhenUnexpectedException_ReturnsInternalServerError() {
        SucursalDTO dto = SucursalDTO.builder()
            .id(UUID.randomUUID().toString())
            .nombre("Sucursal Centro")
            .franquiciaId(UUID.randomUUID().toString())
            .build();
    
        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(SucursalDTO.class)).thenReturn(Mono.just(dto));
        when(sucursalMapper.toModel(dto)).thenReturn(
            new Sucursal(null, dto.getNombre(), dto.getFranquiciaId())
        );
        when(sucursalServicePort.saveSucursal(any())).thenReturn(Mono.error(new RuntimeException("boom")));
    
        StepVerifier.create(sucursalHandler.createSucursal(request))
            .expectNextMatches(res -> res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
            .verifyComplete();
    }
    

    @Test
    void getSucursales_WhenSucursalesExist_ReturnsList() {
        Sucursal model = new Sucursal(UUID.randomUUID(), "Sucursal Norte", "123");
        SucursalDTO dto = SucursalDTO.builder()
            .id(UUID.randomUUID().toString())
            .nombre("Sucursal Centro")
            .franquiciaId(UUID.randomUUID().toString())
            .build();

        when(sucursalServicePort.getAllSucursales()).thenReturn(Flux.just(model));
        when(sucursalMapper.toDTO(model)).thenReturn(dto);

        ServerRequest request = MockServerRequest.builder().build();

        StepVerifier.create(sucursalHandler.getSucursales(request))
            .expectNextMatches(res -> res.statusCode().equals(HttpStatus.OK))
            .verifyComplete();
    }

    @Test
    void getSucursales_WhenDomainException_ReturnsBadRequest() {
        DomainException ex = new DomainException(Messages.SUCURSAL_NOT_FOUND);

        when(sucursalServicePort.getAllSucursales()).thenReturn(Flux.error(ex));

        ServerRequest request = MockServerRequest.builder().build();

        StepVerifier.create(sucursalHandler.getSucursales(request))
            .expectNextMatches(res -> res.statusCode().equals(HttpStatus.BAD_REQUEST))
            .verifyComplete();
    }

    @Test
    void getSucursales_WhenUnexpectedException_ReturnsInternalServerError() {
        when(sucursalServicePort.getAllSucursales()).thenReturn(Flux.error(new RuntimeException("fail")));

        ServerRequest request = MockServerRequest.builder().build();

        StepVerifier.create(sucursalHandler.getSucursales(request))
            .expectNextMatches(res -> res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
            .verifyComplete();
    }

    @Test
    void updateSucursal_WhenValidRequest_ReturnsCreated() {
        UUID id = UUID.randomUUID();
    
        SucursalDTO dto = SucursalDTO.builder()
            .id(id.toString())
            .nombre("Sucursal Sur")
            .franquiciaId("franquicia123")
            .build();
    
        Sucursal existing = new Sucursal(id, "Old Name", "franquicia123");
        Sucursal updated = new Sucursal(id, dto.getNombre(), existing.franquiciaId());
    
        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(SucursalDTO.class)).thenReturn(Mono.just(dto));
        when(sucursalServicePort.getSucursal(id)).thenReturn(Mono.just(existing));
        when(sucursalServicePort.updateSucursal(updated)).thenReturn(Mono.empty());
    
        StepVerifier.create(sucursalHandler.updateSucursal(request))
            .expectNextMatches(response -> {
                assert response.statusCode().equals(HttpStatus.CREATED);
                return true;
            })
            .verifyComplete();
    }

    @Test
    void updateSucursal_WhenDomainException_ReturnsBadRequest() {
        UUID id = UUID.randomUUID();
    
        SucursalDTO dto = SucursalDTO.builder()
                .id(id.toString())
                .nombre("Sucursal Sur")
                .franquiciaId("franquicia123")
                .build();
    
        DomainException ex = new DomainException(Messages.SUCURSAL_NOT_FOUND);
    
        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(SucursalDTO.class)).thenReturn(Mono.just(dto));
        when(sucursalServicePort.getSucursal(id)).thenReturn(Mono.error(ex));
    
        StepVerifier.create(sucursalHandler.updateSucursal(request))
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }
    

    @Test
    void updateSucursal_WhenUnexpectedException_ReturnsInternalServerError() {
        UUID id = UUID.randomUUID();
    
        SucursalDTO dto = SucursalDTO.builder()
                .id(id.toString())
                .nombre("Sucursal Sur")
                .franquiciaId("franquicia123")
                .build();
    
        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(SucursalDTO.class)).thenReturn(Mono.just(dto));
        when(sucursalServicePort.getSucursal(id)).thenReturn(Mono.error(new RuntimeException("boom")));
    
        StepVerifier.create(sucursalHandler.updateSucursal(request))
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }

}
