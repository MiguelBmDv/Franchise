package com.retonequi.franchise.domain.spi;

import java.util.UUID;

import com.retonequi.franchise.domain.model.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductoPersistencePort {
    Mono<Producto> saveProducto(Producto producto);
    Mono<Void> deleteProducto(UUID id);
    Mono<Void> updateProducto(Producto producto);
    Mono<Boolean> existsByNombreAndSucursalId(String nombre, String sucursalId);
    Mono<Producto> getProducto(UUID id);
    Flux<Producto> getAllProductos();
    Mono<Producto> findTopBySucursalIdOrderByStockDesc(String sucursalId);
}
