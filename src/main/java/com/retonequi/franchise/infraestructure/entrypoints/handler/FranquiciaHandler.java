package com.retonequi.franchise.infraestructure.entrypoints.handler;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.retonequi.franchise.domain.api.IFranquiciaServicePort;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Franquicia;
import com.retonequi.franchise.infraestructure.entrypoints.dto.FranquiciaDTO;
import com.retonequi.franchise.infraestructure.entrypoints.mapper.IFranquiciaMapper;
import com.retonequi.franchise.infraestructure.entrypoints.util.ApiResponse;
import com.retonequi.franchise.infraestructure.entrypoints.util.ErrorDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class FranquiciaHandler {
    private final IFranquiciaServicePort franquiciaServicePort;
    private final IFranquiciaMapper franquiciaMapper;

     public ResponseEntity<ApiResponse<Void>> createFranquicia(FranquiciaDTO dto) {
        try {
            Franquicia model = franquiciaMapper.toModel(dto);
            franquiciaServicePort.saveFranquicia(model);

            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .code("200")
                    .message("Franquicia creada")
                    .date(Instant.now().toString())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DomainException ex) {
            ApiResponse<Void> error = ApiResponse.<Void>builder()
                    .code(ex.getTechnicalMessage().getCode())
                    .message(ex.getTechnicalMessage().getMessage())
                    .date(Instant.now().toString())
                    .errors(List.of(ErrorDTO.builder()
                            .code(ex.getTechnicalMessage().getCode())
                            .message(ex.getTechnicalMessage().getMessage())
                            .build()))
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }


    public ResponseEntity<ApiResponse<List<FranquiciaDTO>>> getFranquicias() {
        List<Franquicia> franquicias = franquiciaServicePort.getAllFranquicias();
        List<FranquiciaDTO> dtos = franquiciaMapper.toDTOList(franquicias);

        ApiResponse<List<FranquiciaDTO>> response = ApiResponse.<List<FranquiciaDTO>>builder()
                .code("200")
                .message("Franquicias obtenidas")
                .date(Instant.now().toString())
                .data(dtos)
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Void>> updateFranquicia(FranquiciaDTO dto) {
        try {
            UUID id = validateUUID(dto.getId());
            Franquicia oldFranquicia = franquiciaServicePort.getFranquicia(id);
            Franquicia incoming = franquiciaMapper.toModel(dto);
            Franquicia updated = new Franquicia(
                oldFranquicia.id(),         
                incoming.nombre()
            );

            franquiciaServicePort.updateFranquicia(updated);

            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .code("200")
                    .message("Franquicia actualizada")
                    .date(Instant.now().toString())
                    .build();

            return ResponseEntity.ok(response);

        } catch (DomainException ex) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getTechnicalMessage(), List.of(
                    ErrorDTO.builder()
                            .code(ex.getTechnicalMessage().getCode())
                            .message(ex.getTechnicalMessage().getMessage())
                            .build()));
        }
    }

    public <T> ResponseEntity<ApiResponse<T>> buildErrorResponse(
        HttpStatus httpStatus,
        Messages error,
        List<ErrorDTO> errors
    ) {
        ApiResponse<T> apiErrorResponse = ApiResponse.<T>builder()
                .code(error.getCode())
                .message(error.getMessage())
                .date(Instant.now().toString())
                .errors(errors)
                .build();

        return ResponseEntity.status(httpStatus).body(apiErrorResponse);
    }

    private UUID validateUUID(String idStr) {
        try {
            return UUID.fromString(idStr);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new DomainException(Messages.INVALID_ID);
        }
    }
    

}
