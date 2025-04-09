package com.retonequi.franchise.infraestructure.entrypoints.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retonequi.franchise.infraestructure.entrypoints.dto.FranquiciaDTO;
import com.retonequi.franchise.infraestructure.entrypoints.handler.FranquiciaHandler;
import com.retonequi.franchise.infraestructure.entrypoints.util.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/franquicias")
@RequiredArgsConstructor
public class FranquiciaController {

    private final FranquiciaHandler handler;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody FranquiciaDTO dto) {
        return handler.createFranquicia(dto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FranquiciaDTO>>> getAll() {
        return handler.getFranquicias();
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> update(@RequestBody FranquiciaDTO dto) {
        return handler.updateFranquicia(dto);
    }
}
