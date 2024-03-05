package br.com.controleestoque.exception;

import javax.naming.AuthenticationException;

public class InvalidJwtAuthenticationExceptionException extends AuthenticationException {
    public InvalidJwtAuthenticationExceptionException(String message) {
        super(message);
    }
}