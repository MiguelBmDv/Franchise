package com.retonequi.franchise.mapperTest.entrypoints;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.infraestructure.entrypoints.dto.SucursalDTO;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.ISucursalMapper;

class ISucursalMapperTest {
    
    private ISucursalMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ISucursalMapper.class);
    }

    @Test
    void shouldMapDtoToModel() {
        UUID id = UUID.randomUUID();
        String franquiciaId = UUID.randomUUID().toString();

        SucursalDTO dto = SucursalDTO.builder()
            .id(id.toString())
            .nombre("Sucursal Test")
            .franquiciaId(franquiciaId)
            .build();

        Sucursal model = mapper.toModel(dto);

        assertEquals(id, model.id());
        assertEquals(dto.getNombre(), model.nombre());
        assertEquals(dto.getFranquiciaId(), model.franquiciaId());
    }

    @Test
    void shouldMapModelToDto() {
        UUID id = UUID.randomUUID();
        Sucursal model = new Sucursal(id, "Sucursal Model", "franq-id");

        SucursalDTO dto = mapper.toDTO(model);

        assertEquals(id.toString(), dto.getId());
        assertEquals(model.nombre(), dto.getNombre());
        assertEquals(model.franquiciaId(), dto.getFranquiciaId());
    }

    @Test
    void shouldMapSucursalListToDtoList() {
        List<Sucursal> modelos = List.of(
            new Sucursal(UUID.randomUUID(), "Sucursal A", "franq-1"),
            new Sucursal(UUID.randomUUID(), "Sucursal B", "franq-2")
        );

        List<SucursalDTO> dtos = mapper.toDTOList(modelos);

        assertEquals(2, dtos.size());
        assertEquals("Sucursal A", dtos.get(0).getNombre());
    }
}
