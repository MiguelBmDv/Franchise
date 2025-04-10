package com.retonequi.franchise.domain.api;

import com.retonequi.franchise.domain.model.Sucursal;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.UUID;

public interface ISucursalServicePort {
    Mono<Sucursal> saveSucursal(Sucursal sucursal);
    Mono<Sucursal> getSucursal(UUID id);
    Flux<Sucursal> getAllSucursales();
    Mono<Void> updateSucursal(Sucursal sucursal);
}
