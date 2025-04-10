package com.retonequi.franchise.useCaseTest;

import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.domain.spi.IFranquiciaPersistencePort;
import com.retonequi.franchise.domain.spi.ISucursalPersistencePort;
import com.retonequi.franchise.domain.usecase.SucursalUseCase;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class SucursalUseCaseTest {

    @Mock
    ISucursalPersistencePort sucursalPersistencePort;

    @Mock
    IFranquiciaPersistencePort franquiciaPersistencePort;

    SucursalUseCase sucursalUseCase;

    @BeforeEach
    void setUp() {
        sucursalUseCase = new SucursalUseCase(sucursalPersistencePort, franquiciaPersistencePort);
    }

    Sucursal getSampleSucursal() {
        return new Sucursal(UUID.randomUUID(), "Sucursal 1", "123e4567-e89b-12d3-a456-556642440000");
    }

    @Test
    void saveSucursal_success() {
        Sucursal sucursal = getSampleSucursal();

        when(franquiciaPersistencePort.existsById(UUID.fromString(sucursal.franquiciaId()))).thenReturn(Mono.just(true));
        when(sucursalPersistencePort.existsByNombreAndFranquiciaId(sucursal.nombre(), sucursal.franquiciaId())).thenReturn(Mono.just(false));
        when(sucursalPersistencePort.saveSucursal(sucursal)).thenReturn(Mono.just(sucursal));

        StepVerifier.create(sucursalUseCase.saveSucursal(sucursal))
            .expectNext(sucursal)
            .verifyComplete();
    }

    @Test
    void saveSucursal_invalidFranquiciaId() {
        Sucursal sucursal = new Sucursal(UUID.randomUUID(), "Sucursal 1", null);

        StepVerifier.create(sucursalUseCase.saveSucursal(sucursal))
            .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(Messages.ID_EMPTY.getMessage()))
            .verify();
    }

    @Test
    void saveSucursal_franquiciaNotFound() {
        Sucursal sucursal = getSampleSucursal();

        when(franquiciaPersistencePort.existsById(UUID.fromString(sucursal.franquiciaId()))).thenReturn(Mono.just(false));

        StepVerifier.create(sucursalUseCase.saveSucursal(sucursal))
            .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(Messages.FRANQUICIA_NOT_FOUND.getMessage()))
            .verify();
    }

    @Test
    void saveSucursal_sucursalAlreadyExists() {
        Sucursal sucursal = getSampleSucursal();

        when(franquiciaPersistencePort.existsById(UUID.fromString(sucursal.franquiciaId()))).thenReturn(Mono.just(true));
        when(sucursalPersistencePort.existsByNombreAndFranquiciaId(sucursal.nombre(), sucursal.franquiciaId())).thenReturn(Mono.just(true));

        StepVerifier.create(sucursalUseCase.saveSucursal(sucursal))
            .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(Messages.SUCURSAL_ALREADY_EXISTS.getMessage()))
            .verify();
    }

    @Test
    void getSucursal_success() {
        Sucursal sucursal = getSampleSucursal();

        when(sucursalPersistencePort.getSucursal(sucursal.id())).thenReturn(Mono.just(sucursal));

        StepVerifier.create(sucursalUseCase.getSucursal(sucursal.id()))
            .expectNext(sucursal)
            .verifyComplete();
    }

    @Test
    void getSucursal_invalidId() {
        StepVerifier.create(sucursalUseCase.getSucursal(null))
            .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(Messages.INVALID_ID.getMessage()))
            .verify();
    }

    @Test
    void getSucursal_notFound() {
        UUID id = UUID.randomUUID();

        when(sucursalPersistencePort.getSucursal(id)).thenReturn(Mono.empty());

        StepVerifier.create(sucursalUseCase.getSucursal(id))
            .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(Messages.SUCURSAL_NOT_FOUND.getMessage()))
            .verify();
    }

    @Test
    void getAllSucursales_success() {
        Sucursal sucursal = getSampleSucursal();

        when(sucursalPersistencePort.getAllSucursales()).thenReturn(Flux.just(sucursal));

        StepVerifier.create(sucursalUseCase.getAllSucursales())
            .expectNext(sucursal)
            .verifyComplete();
    }

    @Test
    void updateSucursal_success() {
        Sucursal sucursal = getSampleSucursal();

        when(sucursalPersistencePort.getSucursal(sucursal.id())).thenReturn(Mono.just(sucursal));
        when(sucursalPersistencePort.existsByNombreAndFranquiciaId(sucursal.nombre(), sucursal.franquiciaId())).thenReturn(Mono.just(false));
        when(sucursalPersistencePort.updateSucursal(sucursal)).thenReturn(Mono.empty());

        StepVerifier.create(sucursalUseCase.updateSucursal(sucursal))
            .verifyComplete();
    }

    @Test
    void updateSucursal_notFound() {
        Sucursal sucursal = getSampleSucursal();

        when(sucursalPersistencePort.getSucursal(sucursal.id())).thenReturn(Mono.empty());

        StepVerifier.create(sucursalUseCase.updateSucursal(sucursal))
            .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(Messages.SUCURSAL_NOT_FOUND.getMessage()))
            .verify();
    }

    @Test
    void updateSucursal_wrongFranquiciaId() {
        Sucursal sucursal = getSampleSucursal();
        Sucursal existing = new Sucursal(sucursal.id(), sucursal.nombre(), "other-franquicia-id");

        when(sucursalPersistencePort.getSucursal(sucursal.id())).thenReturn(Mono.just(existing));

        StepVerifier.create(sucursalUseCase.updateSucursal(sucursal))
            .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(Messages.FORBIDDEN_UPDATE_FRANQUICIA_ID.getMessage()))
            .verify();
    }

    @Test
    void updateSucursal_duplicateName() {
        Sucursal sucursal = getSampleSucursal();

        when(sucursalPersistencePort.getSucursal(sucursal.id())).thenReturn(Mono.just(sucursal));
        when(sucursalPersistencePort.existsByNombreAndFranquiciaId(sucursal.nombre(), sucursal.franquiciaId())).thenReturn(Mono.just(true));

        StepVerifier.create(sucursalUseCase.updateSucursal(sucursal))
            .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(Messages.SUCURSAL_ALREADY_EXISTS.getMessage()))
            .verify();
    }
}

