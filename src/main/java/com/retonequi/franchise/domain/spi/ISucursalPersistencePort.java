package com.retonequi.franchise.domain.spi;

import java.util.UUID;

import com.retonequi.franchise.domain.model.Sucursal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISucursalPersistencePort {
    Mono<Sucursal> saveSucursal(Sucursal sucursal);
    Flux<Sucursal> getAllSucursales();
    Mono<Void> updateSucursal(Sucursal sucursal);
    Mono<Boolean> existsById(UUID id);
    Mono<Boolean> existsByNombreAndFranquiciaId(String nombre, String franquiciaId);
    Mono<Sucursal> getSucursal(UUID id);
    Flux<Sucursal> findByFranquiciaId(String franquiciaId);

}
