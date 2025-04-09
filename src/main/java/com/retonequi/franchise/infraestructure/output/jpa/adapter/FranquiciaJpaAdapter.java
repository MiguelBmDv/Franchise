package com.retonequi.franchise.infraestructure.output.jpa.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.domain.spi.IFranquiciaPersistencePort;
import com.retonequi.franchise.infraestructure.output.jpa.entity.FranquiciaEntity;
import com.retonequi.franchise.infraestructure.output.jpa.mapper.IFranquiciaEntityMapper;
import com.retonequi.franchise.infraestructure.output.jpa.repository.IFranquiciaRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class FranquiciaJpaAdapter implements IFranquiciaPersistencePort {

    private final IFranquiciaRepository franquiciaRepository;
    private final IFranquiciaEntityMapper franquiciaEntityMapper;

     @Override
    public void saveFranquicia(Franquicia franquicia) {
        FranquiciaEntity entity = franquiciaEntityMapper.toEntity(franquicia);
        franquiciaRepository.save(entity);
    }

    @Override
    public List<Franquicia> getAllFranquicias() {
        List<FranquiciaEntity> entities = franquiciaRepository.findAll();
        return entities.stream()
                .map(franquiciaEntityMapper::toModel)
                .toList();
    }

    @Override
    public void updateFranquicia(Franquicia franquicia) {
        franquiciaRepository.save(franquiciaEntityMapper.toEntity(franquicia));
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return franquiciaRepository.findByNombre(nombre).isPresent();
    }

    @Override
    public Optional<Franquicia> getFranquicia(UUID id) {
        return franquiciaRepository.findById(id)
                .map(franquiciaEntityMapper::toModel);
    }

}
