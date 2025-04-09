package com.retonequi.franchise.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Messages {

    INTERNAL_ERROR("500", "Algo salió mal, por favor intenta de nuevo", ""),
    INTERNAL_ERROR_IN_ADAPTERS("PRC501", "Ocurrió un error en los adaptadores, por favor intenta de nuevo", ""),
    INVALID_REQUEST("400", "Solicitud incorrecta, por favor verifica los datos", ""),
    INVALID_PARAMETERS(INVALID_REQUEST.getCode(), "Parámetros incorrectos, por favor verifica los datos", ""),
    INVALID_NAME("403", "Nombre inválido o vacio, por favor verifica", "nombre"),
    DESCRIPTION_TOO_LONG("403", "La descripción no puede tener más de 90 caracteres, por favor verifica", "descripcion"),
    UNSUPPORTED_OPERATION("501", "Método no soportado, por favor intenta de nuevo", ""),
    TECHNOLOGY_CREATED("201", "Tecnología creada exitosamente", ""),
    TECHNOLOGIES_EMPTY("200", "No hay tecnologías registradas", ""),
    TECHNOLOGIES_FOUND("200", "Lista de tecnologías obtenida con éxito", ""),
    CAPACIDADES_FOUND("200", "CapacidadesObrtenidas", ""),
    BOOTCAMPS_EMPTY("200", "No hay bootcamps registrados", ""),
    BOOTCAMP_CREATED("201", "Capacidad creada exitosamente", ""),
    FEW_TECHNOLOGIES("403", "Una capacidad debe tener minimo 3 tecnologias asociadas", ""),
    RANGE_CAPACITIES("403", "Un bootcamp debe tener minimo 1 capacidad y maximo 4 capacidades asociadas", ""),
    FRANQUICIA_ALREADY_EXISTS("400", "Esta franquicia ya se encuentra registrada", "nombre"),
    FRANQUICIA_NOT_FOUND("404-0", "La Franquicia no existe, por favor verifica", "id"),
    DUPLICATE_CAPACITIES("400", "Hay una o varias capacidades repetidas", ""),
    INVALID_ID("400", "El id no fue proporcionado en el cuerpo de la consulta", ""),
    INVALID_ORDER("400", "Orden invalido", ""),
    CAPACITIES_ALREADY_EXISTS("400", "Hay una o varias capacidades repetidas", "");

    private final String code;
    private final String message;
    private final String param;
}
