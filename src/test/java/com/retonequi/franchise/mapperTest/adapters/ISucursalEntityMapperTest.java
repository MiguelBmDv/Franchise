package com.retonequi.franchise.mapperTest.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.infraestructure.output.reactive.entity.SucursalEntity;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.ISucursalEntityMapper;

class ISucursalEntityMapperTest {
    
    private final ISucursalEntityMapper mapper = Mappers.getMapper(ISucursalEntityMapper.class);

    @Test
    void shouldMapEntityToModel() {
        UUID id = UUID.randomUUID();
        SucursalEntity entity = new SucursalEntity(id, "Sucursal 1", "franq-123");

        Sucursal model = mapper.toModel(entity);

        assertEquals(id, model.id());
        assertEquals("Sucursal 1", model.nombre());
        assertEquals("franq-123", model.franquiciaId());
    }

    @Test
    void shouldMapModelToEntity() {
        UUID id = UUID.randomUUID();
        Sucursal model = new Sucursal(id, "Sucursal 2", "franq-456");

        SucursalEntity entity = mapper.toEntity(model);

        assertEquals(id, entity.getId());
        assertEquals("Sucursal 2", entity.getNombre());
        assertEquals("franq-456", entity.getFranquiciaId());
    }

}   
