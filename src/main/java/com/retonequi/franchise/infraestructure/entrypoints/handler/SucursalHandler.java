package com.retonequi.franchise.infraestructure.entrypoints.handler;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.retonequi.franchise.domain.api.ISucursalServicePort;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Sucursal;
import com.retonequi.franchise.infraestructure.entrypoints.dto.SucursalDTO;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.ISucursalMapper;
import com.retonequi.franchise.infraestructure.entrypoints.util.ApiResponse;
import com.retonequi.franchise.infraestructure.entrypoints.util.ErrorDTO;
import com.retonequi.franchise.infraestructure.entrypoints.util.HelperFunctions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class SucursalHandler {

    private final ISucursalServicePort sucursalServicePort;
    private final ISucursalMapper sucursalMapper;

    public Mono<ServerResponse> createSucursal(ServerRequest request) {
        return request.bodyToMono(SucursalDTO.class)
                .flatMap(dto -> {
                        UUID franquiciaId = HelperFunctions.validateUUID(dto.getFranquiciaId());
                        Sucursal model = sucursalMapper.toModel(dto);
                        model = new Sucursal(model.id(), model.nombre(), franquiciaId.toString());
                        return sucursalServicePort.saveSucursal(model);
                })
                .flatMap(ignored -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(Messages.SUCURSAL_CREATED.getMessage()))
                .doOnError(ex -> log.error("Error creando sucursal", ex))
                .onErrorResume(DomainException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getTechnicalMessage(),
                        List.of(ErrorDTO.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .build())))
                .onErrorResume(ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        Messages.INTERNAL_ERROR,
                        List.of(ErrorDTO.builder()
                                .code(Messages.INTERNAL_ERROR.getCode())
                                .message(Messages.INTERNAL_ERROR.getMessage())
                                .build())));
    }
    
    @SuppressWarnings("java:S1172")
    public Mono<ServerResponse> getSucursales(ServerRequest request) {
        return sucursalServicePort.getAllSucursales()
                .map(sucursalMapper::toDTO)
                .collectList()
                .flatMap(dtos -> {
                    ApiResponse<List<SucursalDTO>> response = ApiResponse.<List<SucursalDTO>>builder()
                            .code(Messages.SUCURSAL_FOUND.getCode())
                            .message(Messages.SUCURSAL_FOUND.getMessage())
                            .date(Instant.now().toString())
                            .data(dtos)
                            .build();
                    return ServerResponse.ok().bodyValue(response);
                })
                .doOnError(ex -> log.error("Error obteniendo sucursales", ex))
                .onErrorResume(DomainException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getTechnicalMessage(),
                        List.of(ErrorDTO.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .build())))
                .onErrorResume(ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        Messages.INTERNAL_ERROR,
                        List.of(ErrorDTO.builder()
                                .code(Messages.INTERNAL_ERROR.getCode())
                                .message(Messages.INTERNAL_ERROR.getMessage())
                                .build())));
    }

    public Mono<ServerResponse> updateSucursal(ServerRequest request) {
        return request.bodyToMono(SucursalDTO.class)
                .flatMap(dto -> {
                        UUID id = HelperFunctions.validateUUID(dto.getId());
        
                        return sucursalServicePort.getSucursal(id)
                        .flatMap(existing -> {
                                Sucursal model = new Sucursal(
                                existing.id(),
                                dto.getNombre(),
                                existing.franquiciaId()
                                );
                                return sucursalServicePort.updateSucursal(model);
                        });
                })
                .flatMap(ignored -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(Messages.SUCURSAL_UPDATE.getMessage()))
                .doOnError(ex -> log.error("Error actualizando sucursal", ex))
                .onErrorResume(DomainException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getTechnicalMessage(),
                        List.of(ErrorDTO.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .build())))
                .onErrorResume(ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        Messages.INTERNAL_ERROR,
                        List.of(ErrorDTO.builder()
                                .code(Messages.INTERNAL_ERROR.getCode())
                                .message(Messages.INTERNAL_ERROR.getMessage())
                                .build())));
    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus, Messages error, List<ErrorDTO> errors) {
        ApiResponse<Object> apiErrorResponse = ApiResponse.builder()
                .code(error.getCode())
                .message(error.getMessage())
                .date(Instant.now().toString())
                .errors(errors)
                .build();

        return ServerResponse.status(httpStatus).bodyValue(apiErrorResponse);
    }

}
