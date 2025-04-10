package com.retonequi.franchise.mapperTest.entrypoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.infraestructure.entrypoints.dto.ProductoDTO;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.IProductoMapper;

class IProductoMapperTest {
    
    private IProductoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(IProductoMapper.class);
    }

    @Test
    void shouldMapDtoToModel_IgnoringSucursalNombre() {
        UUID id = UUID.randomUUID();

        ProductoDTO dto = ProductoDTO.builder()
            .id(id.toString())
            .nombre("Coca-Cola")
            .stock(100)
            .sucursalId("sucursal-123")
            .build();

        Producto model = mapper.toModel(dto);

        assertEquals(id, model.id());
        assertEquals(dto.getNombre(), model.nombre());
        assertEquals(dto.getStock(), model.stock());
        assertEquals(dto.getSucursalId(), model.sucursalId());
        assertNull(model.sucursalNombre());
    }

    @Test
    void shouldMapModelToDto() {
        UUID id = UUID.randomUUID();
        Producto model = new Producto(id, "Pepsi", 50, "sucursal-456", "Sucursal Norte");

        ProductoDTO dto = mapper.toDTO(model);

        assertEquals(id.toString(), dto.getId());
        assertEquals(model.nombre(), dto.getNombre());
        assertEquals(model.stock(), dto.getStock());
        assertEquals(model.sucursalId(), dto.getSucursalId());
    }

    @Test
    void shouldMapProductoListToDtoList() {
        List<Producto> productos = List.of(
            new Producto(UUID.randomUUID(), "Producto A", 10, "suc-1", null),
            new Producto(UUID.randomUUID(), "Producto B", 20, "suc-2", null)
        );

        List<ProductoDTO> dtos = mapper.toDTOList(productos);

        assertEquals(2, dtos.size());
        assertEquals("Producto A", dtos.get(0).getNombre());
        assertEquals("suc-1", dtos.get(0).getSucursalId());
    }

}
