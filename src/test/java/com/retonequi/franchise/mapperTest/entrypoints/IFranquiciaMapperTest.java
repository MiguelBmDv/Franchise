package com.retonequi.franchise.mapperTest.entrypoints;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.infraestructure.entrypoints.dto.FranquiciaDTO;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.IFranquiciaMapper;

class IFranquiciaMapperTest {

    private IFranquiciaMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(IFranquiciaMapper.class);
    }

    @Test
    void shouldMapDtoToModel() {
        UUID id = UUID.randomUUID();
        FranquiciaDTO dto = FranquiciaDTO.builder()
            .id(id.toString())
            .nombre("Test Franquicia")
            .build();

        Franquicia model = mapper.toModel(dto);

        assertEquals(id, model.id());
        assertEquals(dto.getNombre(), model.nombre());
    }

    @Test
    void shouldMapModelToDto() {
        UUID id = UUID.randomUUID();
        Franquicia model = new Franquicia(id, "Franquicia Model");

        FranquiciaDTO dto = mapper.toDTO(model);

        assertEquals(id.toString(), dto.getId());
        assertEquals(model.nombre(), dto.getNombre());
    }

    @Test
    void shouldMapModelListToDtoList() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        List<Franquicia> modelos = List.of(
            new Franquicia(id1, "Franquicia Uno"),
            new Franquicia(id2, "Franquicia Dos")
        );

        List<FranquiciaDTO> dtos = mapper.toDTOList(modelos);

        assertEquals(2, dtos.size());
        assertEquals(id1.toString(), dtos.get(0).getId());
        assertEquals("Franquicia Uno", dtos.get(0).getNombre());
    }
}
