package com.retonequi.franchise.infraestructure.entrypoints.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ApiResponse<T> {
    private String code;
    private String message;
    private String identifier;
    private String date;
    private T data;
    private List<ErrorDTO> errors;
}
