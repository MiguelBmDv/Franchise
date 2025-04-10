package com.retonequi.franchise.adapterTest;

import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.infraestructure.output.reactive.adapter.FranquiciaReactiveAdapter;
import com.retonequi.franchise.infraestructure.output.reactive.entity.FranquiciaEntity;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.IFranquiciaEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.repository.IFranquiciaRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FranquiciaReactiveAdapterTest {

    @Mock
    private IFranquiciaRepository repository;

    @Mock
    private IFranquiciaEntityMapper mapper;

    private FranquiciaReactiveAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FranquiciaReactiveAdapter(repository, mapper);
    }

    @Test
    void saveFranquicia_ShouldReturnSavedFranquicia() {
        UUID id = UUID.randomUUID();
        Franquicia model = new Franquicia(id, "FranqTest");
        FranquiciaEntity entity = new FranquiciaEntity(id, "FranqTest");

        when(mapper.toEntity(model)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(adapter.saveFranquicia(model))
            .expectNext(model)
            .verifyComplete();
    }

    @Test
    void getAllFranquicias_ShouldReturnFluxOfFranquicias() {
        UUID id = UUID.randomUUID();
        FranquiciaEntity entity = new FranquiciaEntity(id, "FranqX");
        Franquicia model = new Franquicia(id, "FranqX");

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(adapter.getAllFranquicias())
            .expectNext(model)
            .verifyComplete();
    }

    @Test
    void updateFranquicia_ShouldReturnEmptyMono() {
        UUID id = UUID.randomUUID();
        Franquicia model = new Franquicia(id, "Updated");
        FranquiciaEntity entity = new FranquiciaEntity(id, "Updated");

        when(mapper.toEntity(model)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));

        StepVerifier.create(adapter.updateFranquicia(model))
            .verifyComplete();
    }

    @Test
    void existsByNombre_ShouldReturnTrueIfExists() {
        String nombre = "Franq1";
        FranquiciaEntity entity = new FranquiciaEntity(UUID.randomUUID(), nombre);

        when(repository.findByNombre(nombre)).thenReturn(Mono.just(entity));

        StepVerifier.create(adapter.existsByNombre(nombre))
            .expectNext(true)
            .verifyComplete();
    }

    @Test
    void existsByNombre_ShouldReturnFalseIfNotFound() {
        when(repository.findByNombre("NoExiste")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.existsByNombre("NoExiste"))
            .expectNext(false)
            .verifyComplete();
    }

    @Test
    void getFranquicia_ShouldReturnFranquiciaIfFound() {
        UUID id = UUID.randomUUID();
        FranquiciaEntity entity = new FranquiciaEntity(id, "FranqZ");
        Franquicia model = new Franquicia(id, "FranqZ");

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(adapter.getFranquicia(id))
            .expectNext(model)
            .verifyComplete();
    }

    @Test
    void existsById_ShouldReturnTrueIfExists() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsById(id))
            .expectNext(true)
            .verifyComplete();
    }

    @Test
    void existsById_ShouldReturnFalseIfNotExists() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(Mono.just(false));

        StepVerifier.create(adapter.existsById(id))
            .expectNext(false)
            .verifyComplete();
    }
}