package br.com.controleestoque.exception.handler;

import br.com.controleestoque.exception.ErrorResponse;
import br.com.controleestoque.exception.PermissionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class PermissionExceptionHandler {
    @ExceptionHandler(PermissionException.class)
    public final ResponseEntity<ErrorResponse> handlePermissionException(Exception exception, WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
