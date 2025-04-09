package com.retonequi.franchise.infraestructure.entrypoints.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.infraestructure.entrypoints.dto.FranquiciaDTO;

@Mapper(componentModel = "spring")
public interface IFranquiciaMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    Franquicia toModel(FranquiciaDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    FranquiciaDTO toDTO(Franquicia model);

    List<FranquiciaDTO> toDTOList(List<Franquicia> franquicias);
}
