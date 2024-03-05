package br.com.controleestoque.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}