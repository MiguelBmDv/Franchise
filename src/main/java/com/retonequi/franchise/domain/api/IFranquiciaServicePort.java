package com.retonequi.franchise.domain.api;

import java.util.List;
import java.util.UUID;

import com.retonequi.franchise.domain.model.Franquicia;

public interface IFranquiciaServicePort {
    void saveFranquicia(Franquicia franquicia);
    Franquicia getFranquicia(UUID id);
    List<Franquicia> getAllFranquicias();
    void updateFranquicia(Franquicia franquicia);
}
