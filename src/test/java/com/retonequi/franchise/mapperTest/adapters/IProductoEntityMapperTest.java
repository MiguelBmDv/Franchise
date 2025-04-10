package com.retonequi.franchise.mapperTest.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.infraestructure.output.reactive.entity.ProductoEntity;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.IProductoEntityMapper;

class IProductoEntityMapperTest {
    
    private final IProductoEntityMapper mapper = Mappers.getMapper(IProductoEntityMapper.class);

    @Test
    void shouldMapEntityToModel() {
        UUID id = UUID.randomUUID();
        ProductoEntity entity = new ProductoEntity(id, "Producto A", 100, "suc-001");

        Producto model = mapper.toModel(entity);

        assertEquals(id, model.id());
        assertEquals("Producto A", model.nombre());
        assertEquals(100, model.stock());
        assertEquals("suc-001", model.sucursalId());
        assertNull(model.sucursalNombre()); // campo ignorado
    }

    @Test
    void shouldMapModelToEntity() {
        UUID id = UUID.randomUUID();
        Producto model = new Producto(id, "Producto B", 30, "suc-002", "Sucursal Z");

        ProductoEntity entity = mapper.toEntity(model);

        assertEquals(id, entity.getId());
        assertEquals("Producto B", entity.getNombre());
        assertEquals(30, entity.getStock());
        assertEquals("suc-002", entity.getSucursalId());
    }
}
