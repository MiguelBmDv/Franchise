package com.retonequi.franchise.domain.usecase;

import java.util.UUID;

import com.retonequi.franchise.domain.api.IFranquiciaServicePort;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.domain.spi.IFranquiciaPersistencePort;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FranquiciaUseCase implements IFranquiciaServicePort {

    private final IFranquiciaPersistencePort franquiciaPersistencePort;

    public FranquiciaUseCase(IFranquiciaPersistencePort franquiciaPersistencePort) {
        this.franquiciaPersistencePort = franquiciaPersistencePort;
    }

    @Override
    public Mono <Franquicia> saveFranquicia(Franquicia franquicia) {
        return franquiciaPersistencePort.existsByNombre(franquicia.nombre())
            .flatMap(exists -> {
                if (Boolean.TRUE.equals(exists)) {
                    return Mono.error(new DomainException(Messages.FRANQUICIA_ALREADY_EXISTS));
                }
                return franquiciaPersistencePort.saveFranquicia(franquicia);
            });
    }

    @Override
    public Flux<Franquicia> getAllFranquicias() {
        return franquiciaPersistencePort.getAllFranquicias();
    }

    @Override
    public Mono<Void> updateFranquicia(Franquicia franquicia) {
        if (franquicia.nombre() == null || franquicia.nombre().isBlank()) {
            return Mono.error(new DomainException(Messages.INVALID_NAME));
        }

        return franquiciaPersistencePort.updateFranquicia(franquicia);
    }

    @Override
    public Mono<Franquicia> getFranquicia(UUID id) {
        if (id == null) {
            return Mono.error(new DomainException(Messages.INVALID_ID));
        }

        return franquiciaPersistencePort.getFranquicia(id)
            .switchIfEmpty(Mono.error(new DomainException(Messages.FRANQUICIA_NOT_FOUND)));
    }
}
