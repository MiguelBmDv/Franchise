package com.retonequi.franchise.adapterTest;

import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.infraestructure.output.reactive.adapter.SucursalReactiveAdapter;
import com.retonequi.franchise.infraestructure.output.reactive.entity.SucursalEntity;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.ISucursalEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.repository.ISucursalRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class SucursalReactiveAdapterTest {

    @Mock
    private ISucursalRepository sucursalRepository;

    @Mock
    private ISucursalEntityMapper sucursalEntityMapper;

    private SucursalReactiveAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SucursalReactiveAdapter(sucursalRepository, sucursalEntityMapper);
    }

    @Test
    void saveSucursal_ShouldReturnSavedSucursal() {
        Sucursal sucursal = new Sucursal(UUID.randomUUID(), "Sucursal A", "f-001");
        SucursalEntity entity = new SucursalEntity(sucursal.id(), sucursal.nombre(), sucursal.franquiciaId());

        when(sucursalEntityMapper.toEntity(sucursal)).thenReturn(entity);
        when(sucursalRepository.save(entity)).thenReturn(Mono.just(entity));
        when(sucursalEntityMapper.toModel(entity)).thenReturn(sucursal);

        StepVerifier.create(adapter.saveSucursal(sucursal))
            .expectNext(sucursal)
            .verifyComplete();
    }

    @Test
    void getAllSucursales_ShouldReturnFluxOfSucursales() {
        SucursalEntity entity = new SucursalEntity(UUID.randomUUID(), "Sucursal B", "f-002");
        Sucursal sucursal = new Sucursal(entity.getId(), entity.getNombre(), entity.getFranquiciaId());

        when(sucursalRepository.findAll()).thenReturn(Flux.just(entity));
        when(sucursalEntityMapper.toModel(entity)).thenReturn(sucursal);

        StepVerifier.create(adapter.getAllSucursales())
            .expectNext(sucursal)
            .verifyComplete();
    }

    @Test
    void updateSucursal_ShouldReturnEmptyMono() {
        Sucursal sucursal = new Sucursal(UUID.randomUUID(), "Sucursal C", "f-003");
        SucursalEntity entity = new SucursalEntity(sucursal.id(), sucursal.nombre(), sucursal.franquiciaId());

        when(sucursalEntityMapper.toEntity(sucursal)).thenReturn(entity);
        when(sucursalRepository.save(entity)).thenReturn(Mono.just(entity));

        StepVerifier.create(adapter.updateSucursal(sucursal))
            .verifyComplete();
    }

    @Test
    void getSucursal_ShouldReturnSucursalIfExists() {
        UUID id = UUID.randomUUID();
        SucursalEntity entity = new SucursalEntity(id, "Sucursal D", "f-004");
        Sucursal sucursal = new Sucursal(id, "Sucursal D", "f-004");

        when(sucursalRepository.findById(id)).thenReturn(Mono.just(entity));
        when(sucursalEntityMapper.toModel(entity)).thenReturn(sucursal);

        StepVerifier.create(adapter.getSucursal(id))
            .expectNext(sucursal)
            .verifyComplete();
    }

    @Test
    void existsByNombreAndFranquiciaId_ShouldReturnTrue() {
        String nombre = "Sucursal E";
        String franquiciaId = "f-005";
        SucursalEntity entity = new SucursalEntity(UUID.randomUUID(), nombre, franquiciaId);

        when(sucursalRepository.findByNombreAndFranquiciaId(nombre, franquiciaId))
            .thenReturn(Mono.just(entity));

        StepVerifier.create(adapter.existsByNombreAndFranquiciaId(nombre, franquiciaId))
            .expectNext(true)
            .verifyComplete();
    }

    @Test
    void existsByNombreAndFranquiciaId_ShouldReturnFalse() {
        when(sucursalRepository.findByNombreAndFranquiciaId("Sucursal X", "f-000"))
            .thenReturn(Mono.empty());

        StepVerifier.create(adapter.existsByNombreAndFranquiciaId("Sucursal X", "f-000"))
            .expectNext(false)
            .verifyComplete();
    }

    @Test
    void existsById_ShouldReturnTrue() {
        UUID id = UUID.randomUUID();
        when(sucursalRepository.existsById(id)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsById(id))
            .expectNext(true)
            .verifyComplete();
    }

    @Test
    void findByFranquiciaId_ShouldReturnSucursales() {
        String franquiciaId = "f-007";
        SucursalEntity entity = new SucursalEntity(UUID.randomUUID(), "Sucursal G", franquiciaId);
        Sucursal sucursal = new Sucursal(entity.getId(), entity.getNombre(), entity.getFranquiciaId());

        when(sucursalRepository.findByFranquiciaId(franquiciaId)).thenReturn(Flux.just(entity));
        when(sucursalEntityMapper.toModel(entity)).thenReturn(sucursal);

        StepVerifier.create(adapter.findByFranquiciaId(franquiciaId))
            .expectNext(sucursal)
            .verifyComplete();
    }
}
