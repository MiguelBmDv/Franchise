package com.retonequi.franchise.infraestructure.output.reactive.adapter;

import java.util.UUID;

import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.domain.spi.IFranquiciaPersistencePort;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.IFranquiciaEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.repository.IFranquiciaRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class FranquiciaReactiveAdapter implements IFranquiciaPersistencePort {

    private final IFranquiciaRepository franquiciaRepository;
    private final IFranquiciaEntityMapper franquiciaEntityMapper;

    @Override
    public Mono<Franquicia> saveFranquicia(Franquicia franquicia) {
        return franquiciaRepository.save(franquiciaEntityMapper.toEntity(franquicia))
            .map(franquiciaEntityMapper::toModel);
    }

    @Override
    public Flux<Franquicia> getAllFranquicias() {
        return franquiciaRepository.findAll()
                .map(franquiciaEntityMapper::toModel);
    }

    @Override
    public Mono<Void> updateFranquicia(Franquicia franquicia) {
        return franquiciaRepository.save(franquiciaEntityMapper.toEntity(franquicia)).then();
    }

    @Override
    public Mono<Boolean> existsByNombre(String nombre) {
        return franquiciaRepository.findByNombre(nombre)
                .map(f -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Franquicia> getFranquicia(UUID id) {
        return franquiciaRepository.findById(id)
                .map(franquiciaEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return franquiciaRepository.existsById(id);
    }

}
