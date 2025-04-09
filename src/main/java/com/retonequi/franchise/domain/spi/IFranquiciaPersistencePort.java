package com.retonequi.franchise.domain.spi;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.retonequi.franchise.domain.model.Franquicia;

public interface IFranquiciaPersistencePort {
    void saveFranquicia(Franquicia franquicia);
    List<Franquicia> getAllFranquicias();
    void updateFranquicia(Franquicia franquicia);
    boolean existsByNombre(String nombre);
    Optional<Franquicia> getFranquicia(UUID id);

}
