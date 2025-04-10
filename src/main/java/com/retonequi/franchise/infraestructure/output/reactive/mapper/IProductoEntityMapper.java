package com.retonequi.franchise.infraestructure.output.reactive.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.infraestructure.output.reactive.entity.ProductoEntity;

@Mapper(componentModel = "spring")
public interface IProductoEntityMapper {
    @Mapping(target = "sucursalNombre", ignore = true)
    Producto toModel(ProductoEntity entity);
    ProductoEntity toEntity(Producto model);

}
