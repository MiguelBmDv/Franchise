package com.retonequi.franchise.domain.api;
import java.util.UUID;

import com.retonequi.franchise.domain.model.Franquicia;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFranquiciaServicePort {
    Mono<Franquicia> saveFranquicia(Franquicia franquicia);
    Mono<Franquicia> getFranquicia(UUID id);
    Flux<Franquicia> getAllFranquicias();
    Mono<Void> updateFranquicia(Franquicia franquicia);
}
