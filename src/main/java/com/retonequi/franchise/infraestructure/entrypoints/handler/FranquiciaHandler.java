package com.retonequi.franchise.infraestructure.entrypoints.handler;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.retonequi.franchise.domain.api.IFranquiciaServicePort;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.infraestructure.entrypoints.dto.FranquiciaDTO;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.IFranquiciaMapper;
import com.retonequi.franchise.infraestructure.entrypoints.util.ApiResponse;
import com.retonequi.franchise.infraestructure.entrypoints.util.ErrorDTO;
import com.retonequi.franchise.infraestructure.entrypoints.util.HelperFunctions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class FranquiciaHandler {

    private final IFranquiciaServicePort franquiciaServicePort;
    private final IFranquiciaMapper franquiciaMapper;

    public Mono<ServerResponse> createFranquicia(ServerRequest request) {
        return request.bodyToMono(FranquiciaDTO.class)
                .flatMap(dto -> {
                    Franquicia model = franquiciaMapper.toModel(dto);
                    return franquiciaServicePort.saveFranquicia(model)
                            .then(ServerResponse.status(HttpStatus.CREATED)
                                    .bodyValue(Messages.FRANQUICIA_CREATED.getMessage()));
                })
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
    public Mono<ServerResponse> getFranquicias(ServerRequest request) {
        return franquiciaServicePort.getAllFranquicias()
                .map(franquiciaMapper::toDTO)
                .collectList()
                .flatMap(dtos -> {
                    ApiResponse<List<FranquiciaDTO>> response = ApiResponse.<List<FranquiciaDTO>>builder()
                            .code(Messages.FRANQUICIA_FOUND.getCode())
                            .message(Messages.FRANQUICIA_FOUND.getMessage())
                            .date(Instant.now().toString())
                            .data(dtos)
                            .build();
                    return ServerResponse.ok().bodyValue(response);
                })
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

    public Mono<ServerResponse> updateFranquicia(ServerRequest request) {
        return request.bodyToMono(FranquiciaDTO.class)
                .flatMap(dto -> {
                    UUID id = HelperFunctions.validateUUID(dto.getId());
                    return franquiciaServicePort.getFranquicia(id)
                            .flatMap(oldFranquicia -> {
                                Franquicia incoming = franquiciaMapper.toModel(dto);
                                Franquicia updated = new Franquicia(
                                        oldFranquicia.id(),
                                        incoming.nombre()
                                );
                                return franquiciaServicePort.updateFranquicia(updated);
                            })
                            .then(ServerResponse.ok().bodyValue(Messages.FRANQUICIA_UPDATE.getMessage()));
                })
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

    private Mono<ServerResponse> buildErrorResponse(HttpStatus status, Messages message, List<ErrorDTO> errors) {
        ApiResponse<Object> errorResponse = ApiResponse.builder()
                .code(message.getCode())
                .message(message.getMessage())
                .date(Instant.now().toString())
                .errors(errors)
                .build();
        return ServerResponse.status(status).bodyValue(errorResponse);
    }
}
