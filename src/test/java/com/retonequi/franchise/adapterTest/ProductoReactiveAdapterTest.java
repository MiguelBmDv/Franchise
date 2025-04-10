package com.retonequi.franchise.adapterTest;

import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.infraestructure.output.reactive.adapter.ProductoReactiveAdapter;
import com.retonequi.franchise.infraestructure.output.reactive.entity.ProductoEntity;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.IProductoEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.repository.IProductoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductoReactiveAdapterTest {

    @Mock
    private IProductoRepository productoRepository;

    @Mock
    private IProductoEntityMapper productoEntityMapper;

    private ProductoReactiveAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProductoReactiveAdapter(productoRepository, productoEntityMapper);
    }

    @Test
    void saveProducto_ShouldReturnSavedProducto() {
        Producto producto = new Producto(UUID.randomUUID(), "Pan", 10, "suc-123", null);
        ProductoEntity entity = new ProductoEntity(producto.id(), producto.nombre(), producto.stock(), producto.sucursalId());

        when(productoEntityMapper.toEntity(producto)).thenReturn(entity);
        when(productoRepository.save(entity)).thenReturn(Mono.just(entity));
        when(productoEntityMapper.toModel(entity)).thenReturn(producto);

        StepVerifier.create(adapter.saveProducto(producto))
            .expectNext(producto)
            .verifyComplete();
    }

    @Test
    void getProducto_ShouldReturnProductoIfFound() {
        UUID id = UUID.randomUUID();
        ProductoEntity entity = new ProductoEntity(id, "Café", 5, "suc-456");
        Producto producto = new Producto(id, "Café", 5, "suc-456", null);

        when(productoRepository.findById(id)).thenReturn(Mono.just(entity));
        when(productoEntityMapper.toModel(entity)).thenReturn(producto);

        StepVerifier.create(adapter.getProducto(id))
            .expectNext(producto)
            .verifyComplete();
    }

    @Test
    void updateProducto_ShouldReturnEmptyMono() {
        Producto producto = new Producto(UUID.randomUUID(), "Refresco", 15, "suc-789", null);
        ProductoEntity entity = new ProductoEntity(producto.id(), producto.nombre(), producto.stock(), producto.sucursalId());

        when(productoEntityMapper.toEntity(producto)).thenReturn(entity);
        when(productoRepository.save(entity)).thenReturn(Mono.just(entity));

        StepVerifier.create(adapter.updateProducto(producto))
            .verifyComplete();
    }

    @Test
    void deleteProducto_ShouldReturnEmptyMono() {
        UUID id = UUID.randomUUID();
        when(productoRepository.deleteById(id)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteProducto(id))
            .verifyComplete();
    }

    @Test
    void existsByNombreAndSucursalId_ShouldReturnTrue() {
        when(productoRepository.existsByNombreAndSucursalId("Jugo", "suc-001")).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsByNombreAndSucursalId("Jugo", "suc-001"))
            .expectNext(true)
            .verifyComplete();
    }

    @Test
    void findTopBySucursalIdOrderByStockDesc_ShouldReturnTopProducto() {
        ProductoEntity entity = new ProductoEntity(UUID.randomUUID(), "Galletas", 30, "suc-002");
        Producto producto = new Producto(entity.getId(), "Galletas", 30, "suc-002", null);

        when(productoRepository.findTopBySucursalIdOrderByStockDesc("suc-002")).thenReturn(Mono.just(entity));
        when(productoEntityMapper.toModel(entity)).thenReturn(producto);

        StepVerifier.create(adapter.findTopBySucursalIdOrderByStockDesc("suc-002"))
            .expectNext(producto)
            .verifyComplete();
    }

    @Test
    void getAllProductos_ShouldReturnFluxOfProductos() {
        ProductoEntity entity = new ProductoEntity(UUID.randomUUID(), "Leche", 12, "suc-003");
        Producto producto = new Producto(entity.getId(), "Leche", 12, "suc-003", null);

        when(productoRepository.findAll()).thenReturn(Flux.just(entity));
        when(productoEntityMapper.toModel(entity)).thenReturn(producto);

        StepVerifier.create(adapter.getAllProductos())
            .expectNext(producto)
            .verifyComplete();
    }
}
