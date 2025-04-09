package com.retonequi.franchise.infraestructure.entrypoints.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class FranquiciaDTO {
    private String id;
    private String nombre;
}
