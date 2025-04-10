package com.retonequi.franchise.infraestructure.output.reactive.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.retonequi.franchise.infraestructure.output.reactive.entity.SucursalEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISucursalRepository extends ReactiveCrudRepository<SucursalEntity, UUID> {
    Mono<SucursalEntity> findByNombreAndFranquiciaId(String nombre, String franquiciaId);
    Flux<SucursalEntity> findByFranquiciaId(String franquiciaId); 
    Mono<Boolean> existsById(UUID id);

}

