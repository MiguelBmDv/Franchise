package com.retonequi.franchise.useCaseTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.domain.spi.IFranquiciaPersistencePort;
import com.retonequi.franchise.domain.usecase.FranquiciaUseCase;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FranquiciaUseCaseTest {

    @Mock
    private IFranquiciaPersistencePort franquiciaPersistencePort;

    @InjectMocks
    private FranquiciaUseCase useCase;

    private final UUID id = UUID.randomUUID();
    private final String nombre = "Franquicia Test";
    private final Franquicia franquicia = new Franquicia(id, nombre);

    @Test
    void saveFranquicia_deberiaGuardarSiNoExiste() {
        when(franquiciaPersistencePort.existsByNombre(nombre)).thenReturn(Mono.just(false));
        when(franquiciaPersistencePort.saveFranquicia(franquicia)).thenReturn(Mono.just(franquicia));

        StepVerifier.create(useCase.saveFranquicia(franquicia))
            .expectNext(franquicia)
            .verifyComplete();

        verify(franquiciaPersistencePort).existsByNombre(nombre);
        verify(franquiciaPersistencePort).saveFranquicia(franquicia);
    }

    @Test
    void saveFranquicia_deberiaFallarSiYaExiste() {
        when(franquiciaPersistencePort.existsByNombre(nombre)).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.saveFranquicia(franquicia))
            .expectErrorMatches(e -> e instanceof DomainException &&
                ((DomainException) e).getTechnicalMessage().equals(Messages.FRANQUICIA_ALREADY_EXISTS))
            .verify();

        verify(franquiciaPersistencePort).existsByNombre(nombre);
        verify(franquiciaPersistencePort, never()).saveFranquicia(any());
    }

    @Test
    void getAllFranquicias_deberiaRetornarFlux() {
        when(franquiciaPersistencePort.getAllFranquicias()).thenReturn(Flux.just(franquicia));

        StepVerifier.create(useCase.getAllFranquicias())
            .expectNext(franquicia)
            .verifyComplete();
    }

    @Test
    void updateFranquicia_deberiaActualizarSiNombreEsValido() {
        when(franquiciaPersistencePort.updateFranquicia(franquicia)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateFranquicia(franquicia))
            .verifyComplete();

        verify(franquiciaPersistencePort).updateFranquicia(franquicia);
    }

    @Test
    void updateFranquicia_deberiaFallarSiNombreEsInvalido() {
        Franquicia invalida = new Franquicia(id, " ");

        StepVerifier.create(useCase.updateFranquicia(invalida))
            .expectErrorMatches(e -> e instanceof DomainException &&
                ((DomainException) e).getTechnicalMessage().equals(Messages.INVALID_NAME))
            .verify();

        verify(franquiciaPersistencePort, never()).updateFranquicia(any());
    }

    @Test
    void getFranquicia_deberiaRetornarFranquiciaSiExiste() {
        when(franquiciaPersistencePort.getFranquicia(id)).thenReturn(Mono.just(franquicia));

        StepVerifier.create(useCase.getFranquicia(id))
            .expectNext(franquicia)
            .verifyComplete();
    }

    @Test
    void getFranquicia_deberiaFallarSiIdEsNull() {
        StepVerifier.create(useCase.getFranquicia(null))
            .expectErrorMatches(e -> e instanceof DomainException &&
                ((DomainException) e).getTechnicalMessage().equals(Messages.INVALID_ID))
            .verify();
    }

    @Test
    void getFranquicia_deberiaFallarSiNoExiste() {
        when(franquiciaPersistencePort.getFranquicia(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getFranquicia(id))
            .expectErrorMatches(e -> e instanceof DomainException &&
                ((DomainException) e).getTechnicalMessage().equals(Messages.FRANQUICIA_NOT_FOUND))
            .verify();
    }
}
