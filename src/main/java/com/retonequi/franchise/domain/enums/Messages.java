package com.retonequi.franchise.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Messages {

    INTERNAL_ERROR("500", "Algo salió mal, por favor intenta de nuevo"),
    INTERNAL_ERROR_IN_ADAPTERS("PRC501", "Ocurrió un error en los adaptadores, por favor intenta de nuevo"),
    INVALID_REQUEST("400", "Solicitud incorrecta, por favor verifica los datos"),
    INVALID_PARAMETERS(INVALID_REQUEST.getCode(), "Parámetros incorrectos, por favor verifica los datos"),
    INVALID_NAME("403", "Nombre inválido o vacio, por favor verifica"),
    UNSUPPORTED_OPERATION("501", "Método no soportado, por favor intenta de nuevo"),
    FRANQUICIA_CREATED("201", "Franquicia creada exitosamente"),
    SUCURSAL_CREATED("201", "Sucursal creada exitosamente"),
    FRANQUICIA_FOUND("201", "Franquicia traida exitosamente"),
    SUCURSAL_FOUND("201", "Sucursal traida exitosamente"),
    FRANQUICIA_UPDATE("201", "Franquicia actualizada exitosamente"),
    SUCURSAL_UPDATE("201", "Sucursal actulizada exitosamente"),
    FRANQUICIA_ALREADY_EXISTS("400", "Esta franquicia ya se encuentra registrada"),
    FRANQUICIA_NOT_FOUND("404-0", "La Franquicia no existe, por favor verifica"),
    SUCURSAL_ALREADY_EXISTS("400", "Esta sucursal ya se encuentra registrada con este nombre y franquicia"),
    SUCURSAL_NOT_FOUND("404-0", "La sucursal no existe, por favor verifica"),
    INVALID_ID("400", "El id proporcionado no tiene el formato correcto"),
    ID_EMPTY("400", "El id de referencia *franquiciaId* no puede ser vacio ni ausente"),
    FORBIDDEN_UPDATE_FRANQUICIA_ID("403", "La sucursal no puede cambiar de franquicia");

    private final String code;
    private final String message;
}
