package com.retonequi.franchise.infraestructure.entrypoints.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.infraestructure.entrypoints.dto.ProductoDTO;

@Mapper(componentModel = "spring")
public interface IProductoMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "stock", target = "stock")
    @Mapping(source = "sucursalId", target = "sucursalId")
    @Mapping(target = "sucursalNombre", ignore = true)
    Producto toModel(ProductoDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "stock", target = "stock")
    @Mapping(source = "sucursalId", target = "sucursalId")
    ProductoDTO toDTO(Producto model);
    List<ProductoDTO> toDTOList(List<Producto> productos);
}
