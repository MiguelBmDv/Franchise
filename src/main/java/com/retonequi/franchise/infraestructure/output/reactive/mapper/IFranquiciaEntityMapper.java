package com.retonequi.franchise.infraestructure.output.reactive.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.infraestructure.output.reactive.entity.FranquiciaEntity;

@Mapper(componentModel = "spring")
public interface IFranquiciaEntityMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    Franquicia toModel(FranquiciaEntity entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    FranquiciaEntity toEntity(Franquicia model);

}
