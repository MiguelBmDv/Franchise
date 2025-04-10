package com.retonequi.franchise.mapperTest.entrypoints;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.infraestructure.entrypoints.dto.ProductoTopDTO;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.IProductoTopMapper;

class IProductoTopMapperTest {
    
    private IProductoTopMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(IProductoTopMapper.class);
    }

    @Test
    void shouldMapModelToTopDto() {
        UUID id = UUID.randomUUID();
        Producto model = new Producto(id, "Fanta", 30, "suc-789", "Sucursal VIP");

        ProductoTopDTO dto = mapper.toDTOTop(model);

        assertEquals(id.toString(), dto.getId());
        assertEquals(model.nombre(), dto.getNombre());
        assertEquals(model.stock(), dto.getStock());
        assertEquals(model.sucursalId(), dto.getSucursalId());
        assertEquals(model.sucursalNombre(), dto.getSucursalNombre());
    }

    @Test
    void shouldMapProductoListToTopDtoList() {
        List<Producto> productos = List.of(
            new Producto(UUID.randomUUID(), "Top 1", 99, "suc-top-1", "Sucursal Pro"),
            new Producto(UUID.randomUUID(), "Top 2", 80, "suc-top-2", "Sucursal Max")
        );

        List<ProductoTopDTO> dtos = mapper.toDTOList(productos);

        assertEquals(2, dtos.size());
        assertEquals("Top 1", dtos.get(0).getNombre());
        assertEquals("Sucursal Pro", dtos.get(0).getSucursalNombre());
    }
}
