package com.retonequi.franchise.infraestructure.output.jpa.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "franquicia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FranquiciaEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String nombre;
}
