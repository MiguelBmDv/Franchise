package com.retonequi.franchise.infraestructure.output.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retonequi.franchise.infraestructure.output.jpa.entity.FranquiciaEntity;

public interface IFranquiciaRepository extends JpaRepository <FranquiciaEntity, UUID>{
    Optional <FranquiciaEntity> findByNombre(String nombre);
    
}
