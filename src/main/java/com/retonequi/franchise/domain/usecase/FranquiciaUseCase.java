package com.retonequi.franchise.domain.usecase;

import java.util.List;
import java.util.UUID;

import com.retonequi.franchise.domain.api.IFranquiciaServicePort;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.domain.spi.IFranquiciaPersistencePort;

public class FranquiciaUseCase implements IFranquiciaServicePort {

    private final IFranquiciaPersistencePort franquiciaPersistencePort;

    public FranquiciaUseCase(IFranquiciaPersistencePort franquiciaPersistencePort) {
        this.franquiciaPersistencePort = franquiciaPersistencePort;
    }

    @Override
    public void saveFranquicia(Franquicia franquicia) {
        if (franquiciaPersistencePort.existsByNombre(franquicia.nombre())) {
            throw new DomainException(Messages.FRANQUICIA_ALREADY_EXISTS);
        }
        franquiciaPersistencePort.saveFranquicia(franquicia);
    }

    @Override
    public List<Franquicia> getAllFranquicias() {
        return franquiciaPersistencePort.getAllFranquicias();
    }

    @Override
    public void updateFranquicia(Franquicia franquicia) {
        if (franquicia.nombre() == null || franquicia.nombre().isBlank()) {
            throw new DomainException(Messages.INVALID_NAME);
        }

        franquiciaPersistencePort.updateFranquicia(franquicia);
    }

    @Override
    public Franquicia getFranquicia(UUID id) {
        if (id == null) {
            throw new DomainException(Messages.INVALID_ID);
        }
        return franquiciaPersistencePort.getFranquicia(id)
                .orElseThrow(() -> new DomainException(Messages.FRANQUICIA_NOT_FOUND));
    }




}
