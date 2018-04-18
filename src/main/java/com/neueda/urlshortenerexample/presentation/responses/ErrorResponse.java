package com.neueda.urlshortenerexample.presentation.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ErrorResponse {
    private HttpStatus status;
    private String error_code;
    private String message;
    private String detail;
}
