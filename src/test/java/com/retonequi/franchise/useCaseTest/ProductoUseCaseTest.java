package com.retonequi.franchise.useCaseTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.domain.spi.IProductoPersistencePort;
import com.retonequi.franchise.domain.spi.ISucursalPersistencePort;
import com.retonequi.franchise.domain.usecase.ProductoUseCase;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductoUseCaseTest {

    @Mock
    private IProductoPersistencePort productoPersistencePort;

    @Mock
    private ISucursalPersistencePort sucursalPersistencePort;

    private ProductoUseCase productoUseCase;

    @BeforeEach
    void setUp() {
        productoUseCase = new ProductoUseCase(productoPersistencePort, sucursalPersistencePort);
    }

    @Test
    void saveProducto_success() {
        String sucursalId = UUID.randomUUID().toString();
        Producto producto = new Producto(null, "Café", 10, sucursalId, null);

        when(sucursalPersistencePort.existsById(UUID.fromString(sucursalId))).thenReturn(Mono.just(true));
        when(productoPersistencePort.existsByNombreAndSucursalId("Café", sucursalId)).thenReturn(Mono.just(false));
        when(productoPersistencePort.saveProducto(producto)).thenReturn(Mono.just(producto));

        StepVerifier.create(productoUseCase.saveProducto(producto))
            .expectNext(producto)
            .verifyComplete();
    }

    @Test
    void saveProducto_invalidSucursalId() {
        Producto producto = new Producto(null, "Café", 10, null, null);

        StepVerifier.create(productoUseCase.saveProducto(producto))
            .expectErrorMatches(error -> error instanceof DomainException &&
                error.getMessage().equals(Messages.ID_EMPTY.getMessage()))
            .verify();
    }

    @Test
    void saveProducto_sucursalNotFound() {
        String sucursalId = UUID.randomUUID().toString();
        Producto producto = new Producto(null, "Café", 10, sucursalId, null);

        when(sucursalPersistencePort.existsById(UUID.fromString(sucursalId))).thenReturn(Mono.just(false));

        StepVerifier.create(productoUseCase.saveProducto(producto))
            .expectErrorMatches(error -> error instanceof DomainException &&
                error.getMessage().equals(Messages.SUCURSAL_NOT_FOUND.getMessage()))
            .verify();
    }

    @Test
    void saveProducto_productoAlreadyExists() {
        String sucursalId = UUID.randomUUID().toString();
        Producto producto = new Producto(null, "Café", 10, sucursalId, null);

        when(sucursalPersistencePort.existsById(UUID.fromString(sucursalId))).thenReturn(Mono.just(true));
        when(productoPersistencePort.existsByNombreAndSucursalId("Café", sucursalId)).thenReturn(Mono.just(true));

        StepVerifier.create(productoUseCase.saveProducto(producto))
            .expectErrorMatches(error -> error instanceof DomainException &&
                error.getMessage().equals(Messages.PRODUCTO_ALREADY_EXISTS.getMessage()))
            .verify();
    }

    @Test
    void updateProducto_forbiddenChangeSucursalId() {
        UUID id = UUID.randomUUID();
        Producto existing = new Producto(id, "Café", 10, "sucursal-1", null);
        Producto input = new Producto(id, "Café", 10, "sucursal-2", null);

        when(productoPersistencePort.getProducto(id)).thenReturn(Mono.just(existing));

        StepVerifier.create(productoUseCase.updateProducto(input))
            .expectErrorMatches(error -> error instanceof DomainException &&
                error.getMessage().equals(Messages.FORBIDDEN_UPDATE_SUCURSAL_ID.getMessage()))
            .verify();
    }

    @Test
    void updateProducto_success() {
        UUID id = UUID.randomUUID();
        Producto existing = new Producto(id, "Café", 10, "sucursalId", null);
        Producto input = new Producto(id, "Café Actualizado", 15, "sucursalId", null);

        when(productoPersistencePort.getProducto(id)).thenReturn(Mono.just(existing));
        when(productoPersistencePort.existsByNombreAndSucursalId("Café Actualizado", "sucursalId")).thenReturn(Mono.just(false));
        when(productoPersistencePort.updateProducto(any())).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.updateProducto(input))
            .verifyComplete();
    }

    @Test
    void updateProducto_nombreDuplicadoConOtro() {
        UUID id = UUID.randomUUID();
        Producto existing = new Producto(id, "Café", 10, "sucursalId", null);
        Producto input = new Producto(id, "Pan", 15, "sucursalId", null);

        when(productoPersistencePort.getProducto(id)).thenReturn(Mono.just(existing));
        when(productoPersistencePort.existsByNombreAndSucursalId("Pan", "sucursalId")).thenReturn(Mono.just(true));

        StepVerifier.create(productoUseCase.updateProducto(input))
            .expectErrorMatches(error -> error instanceof DomainException &&
                error.getMessage().equals(Messages.PRODUCTO_ALREADY_EXISTS.getMessage()))
            .verify();
    }


    @Test
    void deleteProducto_success() {
        UUID id = UUID.randomUUID();
        Producto producto = new Producto(id, "Café", 10, "sucursalId", null);

        when(productoPersistencePort.getProducto(id)).thenReturn(Mono.just(producto));
        when(productoPersistencePort.deleteProducto(id)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.deleteProducto(id))
            .verifyComplete();
    }

    @Test
    void deleteProducto_idNull() {
        StepVerifier.create(productoUseCase.deleteProducto(null))
            .expectErrorMatches(error -> error instanceof DomainException &&
                error.getMessage().equals(Messages.ID_EMPTY.getMessage()))
            .verify();
    }


    @Test
    void getProducto_notFound() {
        UUID id = UUID.randomUUID();
        when(productoPersistencePort.getProducto(id)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.getProducto(id))
            .expectErrorMatches(e -> e instanceof DomainException &&
                e.getMessage().equals(Messages.PRODUCTO_NOT_FOUND.getMessage()))
            .verify();
    }

    @Test
    void getProducto_idNull() {
        StepVerifier.create(productoUseCase.getProducto(null))
            .expectErrorMatches(error -> error instanceof DomainException &&
                error.getMessage().equals(Messages.ID_EMPTY.getMessage()))
            .verify();
    }

    @Test
    void getAllProductos_success() {
        Producto p1 = new Producto(UUID.randomUUID(), "Café", 10, "sucursalId", null);
        Producto p2 = new Producto(UUID.randomUUID(), "Pan", 20, "sucursalId", null);

        when(productoPersistencePort.getAllProductos()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(productoUseCase.getAllProductos())
            .expectNext(p1)
            .expectNext(p2)
            .verifyComplete();
    }

    @Test
    void getTopStockBySucursal_idNull() {
        StepVerifier.create(productoUseCase.getTopStockBySucursal(null))
            .expectErrorMatches(error -> error instanceof DomainException &&
                error.getMessage().equals(Messages.ID_EMPTY.getMessage()))
            .verify();
    }

    @Test
    void getTopStockBySucursal_success() {
        UUID franquiciaId = UUID.randomUUID();
        Sucursal sucursal = new Sucursal(UUID.randomUUID(), "Sucursal 1", franquiciaId.toString());
        Producto producto = new Producto(UUID.randomUUID(), "Café", 30, sucursal.id().toString(), null);

        when(sucursalPersistencePort.findByFranquiciaId(franquiciaId.toString()))
            .thenReturn(Flux.just(sucursal));
        when(productoPersistencePort.findTopBySucursalIdOrderByStockDesc(sucursal.id().toString()))
            .thenReturn(Mono.just(producto));

        StepVerifier.create(productoUseCase.getTopStockBySucursal(franquiciaId))
            .expectNextMatches(p ->
                p.nombre().equals("Café") &&
                p.stock().equals(30) &&
                p.sucursalId().equals(sucursal.id().toString()) &&
                p.sucursalNombre().equals("Sucursal 1")
            )
            .verifyComplete();
    }
}
