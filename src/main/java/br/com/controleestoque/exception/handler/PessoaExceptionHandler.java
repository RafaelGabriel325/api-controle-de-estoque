package br.com.controleestoque.exception.handler;

import br.com.controleestoque.exception.PessoaException;
import br.com.controleestoque.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class PessoaExceptionHandler {
    @ExceptionHandler(PessoaException.class)
    public final ResponseEntity<ErrorResponse> handlePessoaException(Exception exception, WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
