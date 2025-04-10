package com.retonequi.franchise.infraestructure.output.reactive.adapter;

import java.util.UUID;

import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.domain.spi.IProductoPersistencePort;
import com.retonequi.franchise.infraestructure.output.reactive.entity.ProductoEntity;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.IProductoEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.repository.IProductoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class ProductoReactiveAdapter implements IProductoPersistencePort {

    private final IProductoRepository productoRepository;
    private final IProductoEntityMapper productoEntityMapper;

    @Override
    public Mono<Producto> saveProducto(Producto producto) {
        ProductoEntity entity = productoEntityMapper.toEntity(producto);
        return productoRepository.save(entity)
                .map(productoEntityMapper::toModel);
    }

    @Override
    public Mono<Producto> getProducto(UUID id) {
        return productoRepository.findById(id)
                .map(productoEntityMapper::toModel);
    }

    @Override
    public Mono<Void> updateProducto(Producto producto) {
        ProductoEntity entity = productoEntityMapper.toEntity(producto);
        return productoRepository.save(entity).then();
    }

    @Override
    public Mono<Void> deleteProducto(UUID id) {
        return productoRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsByNombreAndSucursalId(String nombre, String sucursalId) {
        return productoRepository.existsByNombreAndSucursalId(nombre, sucursalId);
    }

    @Override
    public Mono<Producto> findTopBySucursalIdOrderByStockDesc(String sucursalId) {
        return productoRepository
                .findTopBySucursalIdOrderByStockDesc(sucursalId)
                .map(productoEntityMapper::toModel);
    }

    @Override
    public Flux<Producto> getAllProductos() {
        return productoRepository.findAll()
                .map(productoEntityMapper::toModel);
    }


}