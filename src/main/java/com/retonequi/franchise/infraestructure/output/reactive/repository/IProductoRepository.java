package com.retonequi.franchise.infraestructure.output.reactive.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.retonequi.franchise.infraestructure.output.reactive.entity.ProductoEntity;

import reactor.core.publisher.Mono;

public interface IProductoRepository extends ReactiveCrudRepository<ProductoEntity, UUID> {
    Mono<Boolean> existsByNombreAndSucursalId(String nombre, String sucursalId);
    Mono<ProductoEntity> findTopBySucursalIdOrderByStockDesc(String sucursalId); 
}
