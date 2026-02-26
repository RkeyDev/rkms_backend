package com.rkey.rkms_backend.core.api;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse <T>(
    String code,
    T data

)
{
    public static <T> ResponseEntity<ApiResponse<T>> toResponseEntity(ResponseType responseType, T data){
        return ResponseEntity
            .status(responseType.getHttpStatus())
            .body(new ApiResponse<>(responseType.getCode(), data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> toResponseEntity(ResponseType responseType){
        return ResponseEntity
            .status(responseType.getHttpStatus())
            .body(new ApiResponse<>(responseType.getCode(), null));
    }

}
