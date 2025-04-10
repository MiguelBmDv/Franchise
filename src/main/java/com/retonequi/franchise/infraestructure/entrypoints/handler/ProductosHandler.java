package com.retonequi.franchise.infraestructure.entrypoints.handler;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.retonequi.franchise.domain.api.IProductoServicePort;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.infraestructure.entrypoints.dto.ProductoDTO;
import com.retonequi.franchise.infraestructure.entrypoints.dto.ProductoTopDTO;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.IProductoMapper;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.IProductoTopMapper;
import com.retonequi.franchise.infraestructure.entrypoints.util.ApiResponse;
import com.retonequi.franchise.infraestructure.entrypoints.util.ErrorDTO;
import com.retonequi.franchise.infraestructure.entrypoints.util.HelperFunctions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductosHandler {

    private final IProductoServicePort productoServicePort;
    private final IProductoMapper productoMapper;
    private final IProductoTopMapper productoTopMapper;

    public Mono<ServerResponse> createProducto(ServerRequest request) {
        return request.bodyToMono(ProductoDTO.class)
                .flatMap(dto -> {
                        UUID sucursalId = HelperFunctions.validateUUID(dto.getSucursalId());
                        Producto model = productoMapper.toModel(dto);
                        model = new Producto(model.id(), model.nombre(), model.stock(),sucursalId.toString(), null);
                        return productoServicePort.saveProducto(model);
                })
                .flatMap(ignored -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(Messages.PRODUCTO_CREATED.getMessage()))
                .doOnError(ex -> log.error("Error creando producto", ex))
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
    public Mono<ServerResponse> getProductos(ServerRequest request) {
        return productoServicePort.getAllProductos()
                .map(productoMapper::toDTO)
                .collectList()
                .flatMap(dtos -> {
                    ApiResponse<List<ProductoDTO>> response = ApiResponse.<List<ProductoDTO>>builder()
                            .code(Messages.PRODUCTO_FOUND.getCode())
                            .message(Messages.PRODUCTO_FOUND.getMessage())
                            .date(Instant.now().toString())
                            .data(dtos)
                            .build();
                    return ServerResponse.ok().bodyValue(response);
                })
                .doOnError(ex -> log.error("Error obteniendo productoes", ex))
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

    public Mono<ServerResponse> updateProducto(ServerRequest request) {
        return request.bodyToMono(ProductoDTO.class)
                .flatMap(dto -> {
                        UUID id = HelperFunctions.validateUUID(dto.getId());
        
                        return productoServicePort.getProducto(id)
                        .flatMap(existing -> {
                                Producto model = new Producto(
                                existing.id(),
                                dto.getNombre(),
                                dto.getStock(),
                                existing.sucursalId(),
                                null
                                );
                                return productoServicePort.updateProducto(model);
                        });
                })
                .flatMap(ignored -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(Messages.PRODUCTO_UPDATE.getMessage()))
                .doOnError(ex -> log.error("Error actualizando producto", ex))
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

    public Mono<ServerResponse> deleteProducto(ServerRequest request) {
        try {
            UUID id = HelperFunctions.validateUUID(request.pathVariable("id"));
            return productoServicePort.deleteProducto(id)
                .then(ServerResponse.ok().bodyValue(Messages.PRODUCTO_DELETE.getMessage()))
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
        } catch (DomainException ex) {
            return buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ex.getTechnicalMessage(),
                    List.of(ErrorDTO.builder()
                            .code(ex.getTechnicalMessage().getCode())
                            .message(ex.getTechnicalMessage().getMessage())
                            .build()));
        }
    }

    public Mono<ServerResponse> getTopStockBySucursal(ServerRequest request) {
        try {
            UUID franquiciaId = HelperFunctions.validateUUID(request.pathVariable("franquiciaId"));
    
            return productoServicePort.getTopStockBySucursal(franquiciaId)
                .map(productoTopMapper ::toDTOTop)
                .collectList()
                .flatMap(productos -> {
                    ApiResponse<List<ProductoTopDTO>> response = ApiResponse.<List<ProductoTopDTO>>builder()
                        .code(Messages.PRODUCTOTOP_FOUND.getCode())
                        .message(Messages.PRODUCTOTOP_FOUND.getMessage())
                        .date(Instant.now().toString())
                        .data(productos)
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
        } catch (DomainException ex) {
            return buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ex.getTechnicalMessage(),
                    List.of(ErrorDTO.builder()
                            .code(ex.getTechnicalMessage().getCode())
                            .message(ex.getTechnicalMessage().getMessage())
                            .build()));
        }
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
