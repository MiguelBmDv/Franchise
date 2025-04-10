package com.retonequi.franchise.domain.spi;

import java.util.UUID;

import com.retonequi.franchise.domain.model.Franquicia;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFranquiciaPersistencePort {
    Mono<Franquicia> saveFranquicia(Franquicia franquicia);
    Flux<Franquicia> getAllFranquicias();
    Mono<Void> updateFranquicia(Franquicia franquicia);
    Mono<Boolean> existsByNombre(String nombre);
    Mono<Boolean> existsById(UUID id);
    Mono<Franquicia> getFranquicia(UUID id);
}
