package com.retonequi.franchise.infraestructure.entrypoints.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ProductoDTO {
    private String id;
    private String nombre;
    private Integer stock;
    private String sucursalId;
}
