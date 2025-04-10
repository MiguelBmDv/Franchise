package com.retonequi.franchise.infraestructure.entrypoints.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.infraestructure.entrypoints.dto.ProductoTopDTO;

@Mapper(componentModel = "spring")
public interface IProductoTopMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "stock", target = "stock")
    @Mapping(source = "sucursalId", target = "sucursalId") 
    @Mapping(source = "sucursalNombre", target = "sucursalNombre") 
    ProductoTopDTO toDTOTop(Producto model);
    List<ProductoTopDTO> toDTOList(List<Producto> productos);
}
