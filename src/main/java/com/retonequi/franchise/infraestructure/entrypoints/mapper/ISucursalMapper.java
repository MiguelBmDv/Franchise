package com.retonequi.franchise.infraestructure.entrypoints.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.infraestructure.entrypoints.dto.SucursalDTO;

@Mapper(componentModel = "spring")
public interface ISucursalMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "franquiciaId", target = "franquiciaId")
    Sucursal toModel(SucursalDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "franquiciaId", target = "franquiciaId")
    SucursalDTO toDTO(Sucursal model);
    List<SucursalDTO> toDTOList(List<Sucursal> sucursales);
}
