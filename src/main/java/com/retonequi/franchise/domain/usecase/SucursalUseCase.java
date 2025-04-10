package com.retonequi.franchise.domain.usecase;

import java.util.UUID;

import com.retonequi.franchise.domain.api.ISucursalServicePort;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.domain.spi.IFranquiciaPersistencePort;
import com.retonequi.franchise.domain.spi.ISucursalPersistencePort;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SucursalUseCase implements ISucursalServicePort {

    private final ISucursalPersistencePort sucursalPersistencePort;
    private final IFranquiciaPersistencePort franquiciaPersistencePort;

    public SucursalUseCase(ISucursalPersistencePort sucursalPersistencePort, IFranquiciaPersistencePort franquiciaPersistencePort) {
        this.sucursalPersistencePort = sucursalPersistencePort;
        this.franquiciaPersistencePort = franquiciaPersistencePort;
    }

    @Override
    public Mono<Sucursal> saveSucursal(Sucursal sucursal) {
        if (sucursal.franquiciaId() == null || sucursal.franquiciaId().isBlank()) {
            return Mono.error(new DomainException(Messages.ID_EMPTY));
        }

        return franquiciaPersistencePort.existsById(UUID.fromString(sucursal.franquiciaId()))
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new DomainException(Messages.FRANQUICIA_NOT_FOUND));
                }
                return sucursalPersistencePort.existsByNombreAndFranquiciaId(
                    sucursal.nombre(), sucursal.franquiciaId()
                );
            })
            .flatMap(exists -> {
                if (Boolean.TRUE.equals(exists)) {
                    return Mono.error(new DomainException(Messages.SUCURSAL_ALREADY_EXISTS));
                }
                return sucursalPersistencePort.saveSucursal(sucursal);
            });
    }
    
    @Override
    public Mono<Sucursal> getSucursal(UUID id) {
        if (id == null) {
            return Mono.error(new DomainException(Messages.INVALID_ID));
        }

        return sucursalPersistencePort.getSucursal(id)
            .switchIfEmpty(Mono.error(new DomainException(Messages.SUCURSAL_NOT_FOUND)));
    }

    @Override
    public Flux<Sucursal> getAllSucursales() {
        return sucursalPersistencePort.getAllSucursales();
    }

    @Override
    public Mono<Void> updateSucursal(Sucursal sucursal) {
        return sucursalPersistencePort.getSucursal(sucursal.id())
            .switchIfEmpty(Mono.error(new DomainException(Messages.SUCURSAL_NOT_FOUND)))
            .flatMap(existing -> {
                if (!existing.franquiciaId().equals(sucursal.franquiciaId())) {
                    return Mono.error(new DomainException(Messages.FORBIDDEN_UPDATE_FRANQUICIA_ID));
                }
                return sucursalPersistencePort.existsByNombreAndFranquiciaId(
                    sucursal.nombre(), sucursal.franquiciaId()
                ).flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DomainException(Messages.SUCURSAL_ALREADY_EXISTS));
                    }
                    return sucursalPersistencePort.updateSucursal(sucursal);
                });
            });
    }


}
