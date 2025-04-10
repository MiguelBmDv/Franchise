package com.retonequi.franchise.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Messages {

    //GENERAL ERRORS
    INTERNAL_ERROR("500", "Algo salió mal, por favor intenta de nuevo"),
    INTERNAL_ERROR_IN_ADAPTERS("PRC501", "Ocurrió un error en los adaptadores, por favor intenta de nuevo"),
    INVALID_REQUEST("400", "Solicitud incorrecta, por favor verifica los datos"),
    INVALID_PARAMETERS(INVALID_REQUEST.getCode(), "Parámetros incorrectos, por favor verifica los datos"),
    INVALID_NAME("403", "Nombre inválido o vacio, por favor verifica"),
    UNSUPPORTED_OPERATION("501", "Método no soportado, por favor intenta de nuevo"),
    INVALID_ID("400", "El id proporcionado no tiene el formato correcto"),
    ID_EMPTY("400", "El id de referencia no puede ser vacio ni ausente"),
    //FRANQUICIA
    FRANQUICIA_CREATED("201", "Franquicia creada exitosamente"),
    FRANQUICIA_FOUND("201", "Franquicia(s) encontrada(s) exitosamente"),
    FRANQUICIA_UPDATE("201", "Franquicia actualizada exitosamente"),
    FRANQUICIA_ALREADY_EXISTS("400", "Esta franquicia ya se encuentra registrada"),
    FRANQUICIA_NOT_FOUND("404-0", "La Franquicia no existe, por favor verifica"),
    FORBIDDEN_UPDATE_FRANQUICIA_ID("403", "La sucursal no puede cambiar de franquicia"),
    //SUCURSAL
    SUCURSAL_CREATED("201", "Sucursal creada exitosamente"),
    SUCURSAL_NOT_FOUND("404-0", "La sucursal no existe, por favor verifica"),
    SUCURSAL_FOUND("201", "Sucursal(es) encontrada(s) exitosamente"),
    SUCURSAL_UPDATE("201", "Sucursal actulizada exitosamente"),
    SUCURSAL_ALREADY_EXISTS("400", "Esta sucursal ya se encuentra registrada con este nombre y franquicia"),
    FORBIDDEN_UPDATE_SUCURSAL_ID("403", "El producto no puede cambiar de sucursal"),
    //PRODUCTOS
    PRODUCTO_CREATED("201", "Producto creado exitosamente"),
    PRODUCTO_UPDATE("201", "Producto actulizado exitosamente"),
    PRODUCTOTOP_FOUND("201", "Producto(s) TOP por sucursal de una franquicia encontrado(s) exitosamente"),
    PRODUCTO_FOUND("201", "Producto(s) encontrado(s) exitosamente"),
    PRODUCTO_DELETE("201", "Producto eliminado exitosamente"),
    PRODUCTO_ALREADY_EXISTS("400", "Este producto ya se encuentra registrado con este nombre y sucursal"),
    PRODUCTO_NOT_FOUND("404", "El producto no existe, por favor verifica");

    private final String code;
    private final String message;
}
