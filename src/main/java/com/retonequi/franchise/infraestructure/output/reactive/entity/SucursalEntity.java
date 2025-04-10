package com.retonequi.franchise.infraestructure.output.reactive.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "sucursal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SucursalEntity {
    @Id
    private UUID id;
    private String nombre;
    private String franquiciaId;
}
