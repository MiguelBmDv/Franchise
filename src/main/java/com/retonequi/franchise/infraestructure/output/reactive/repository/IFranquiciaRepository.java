package com.retonequi.franchise.infraestructure.output.reactive.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.retonequi.franchise.infraestructure.output.reactive.entity.FranquiciaEntity;

import reactor.core.publisher.Mono;

public interface IFranquiciaRepository extends ReactiveCrudRepository<FranquiciaEntity, UUID> {
    Mono<FranquiciaEntity> findByNombre(String nombre);
}

