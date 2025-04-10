package com.retonequi.franchise.infraestructure.output.reactive.adapter;

import java.util.UUID;

import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.domain.spi.ISucursalPersistencePort;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.ISucursalEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.repository.ISucursalRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@AllArgsConstructor
public class SucursalReactiveAdapter implements ISucursalPersistencePort {

    private final ISucursalRepository sucursalRepository;
    private final ISucursalEntityMapper sucursalEntityMapper;

    @Override
    public Mono<Sucursal> saveSucursal(Sucursal sucursal) {
        return sucursalRepository.save(sucursalEntityMapper.toEntity(sucursal))
            .map(sucursalEntityMapper::toModel);
    }

    @Override
    public Flux<Sucursal> getAllSucursales() {
        return sucursalRepository.findAll()
                .map(sucursalEntityMapper::toModel);
    }

    @Override
    public Mono<Void> updateSucursal(Sucursal sucursal) {
        return sucursalRepository.save(sucursalEntityMapper.toEntity(sucursal)).then();
    }

    @Override
    public Mono<Sucursal> getSucursal(UUID id) {
        return sucursalRepository.findById(id)
                .map(sucursalEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByNombreAndFranquiciaId(String nombre, String franquiciaId) {
        return sucursalRepository.findByNombreAndFranquiciaId(nombre, franquiciaId)
                .map(s -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return sucursalRepository.existsById(id);
    }

    @Override
    public Flux<Sucursal> findByFranquiciaId(String franquiciaId) {
        return sucursalRepository.findByFranquiciaId(franquiciaId)
                .map(sucursalEntityMapper::toModel);
    }

}

