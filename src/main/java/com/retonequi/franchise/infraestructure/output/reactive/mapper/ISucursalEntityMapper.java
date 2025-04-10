package com.retonequi.franchise.infraestructure.output.reactive.mapper;

import org.mapstruct.Mapper;

import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.infraestructure.output.reactive.entity.SucursalEntity;

@Mapper(componentModel = "spring")
public interface ISucursalEntityMapper {

    Sucursal toModel(SucursalEntity entity);
    SucursalEntity toEntity(Sucursal model);
}
