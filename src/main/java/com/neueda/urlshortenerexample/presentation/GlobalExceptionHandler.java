package com.neueda.urlshortenerexample.presentation;

import com.neueda.urlshortenerexample.presentation.responses.ErrorResponse;
import com.neueda.urlshortenerexample.infrastructure.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    private ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
                Constants.httpMessageNotReadableErrorCode,
                Constants.httpMessageNotReadableMessage,
                ex.getMessage());

        log.error(errorResponse.toString());

        return errorResponse;
    }

    @ExceptionHandler(ShortUrlNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    private ErrorResponse handleShortUrlNotFoundException(ShortUrlNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,
                Constants.shortUrlNotFoundErrorCode,
                Constants.shortUrlNotFoundMessage,
                ex.getMessage());

        log.error(errorResponse.toString());

        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    private ErrorResponse handleAnyException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
                Constants.anyExceptionErrorCode,
                Constants.anyExceptionMessage,
                ex.getMessage());

        log.error(errorResponse.toString());

        return errorResponse;
    }
}
