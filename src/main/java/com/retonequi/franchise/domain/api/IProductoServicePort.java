package com.retonequi.franchise.domain.api;

import java.util.UUID;

import com.retonequi.franchise.domain.model.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductoServicePort {
    Mono<Producto> saveProducto(Producto producto);
    Mono<Void> updateProducto(Producto producto);
    Mono<Void> deleteProducto(UUID id);    
    Mono<Producto> getProducto(UUID id);
    Flux<Producto> getAllProductos();
    Flux<Producto> getTopStockBySucursal(UUID franquiciaId);
}
