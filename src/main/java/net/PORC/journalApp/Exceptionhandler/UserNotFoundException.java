package net.PORC.journalApp.Exceptionhandler;


import org.springframework.web.bind.annotation.ExceptionHandler;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
