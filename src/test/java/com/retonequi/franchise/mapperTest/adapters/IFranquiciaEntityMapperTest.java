package com.retonequi.franchise.mapperTest.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.infraestructure.output.reactive.entity.FranquiciaEntity;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.IFranquiciaEntityMapper;

class IFranquiciaEntityMapperTest {
    private final IFranquiciaEntityMapper mapper = Mappers.getMapper(IFranquiciaEntityMapper.class);

    @Test
    void shouldMapEntityToModel() {
        UUID id = UUID.randomUUID();
        FranquiciaEntity entity = new FranquiciaEntity(id, "Franquicia X");

        Franquicia model = mapper.toModel(entity);

        assertEquals(id, model.id());
        assertEquals("Franquicia X", model.nombre());
    }

    @Test
    void shouldMapModelToEntity() {
        UUID id = UUID.randomUUID();
        Franquicia model = new Franquicia(id, "Franquicia Y");

        FranquiciaEntity entity = mapper.toEntity(model);

        assertEquals(id, entity.getId());
        assertEquals("Franquicia Y", entity.getNombre());
    }
}
