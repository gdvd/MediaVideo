package fr.gdvd.media_manager.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

//@Log4j2
//@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
/*
    @ExceptionHandler({ MyException2.class })
    protected ResponseEntity<Object> handleNotFound(
            Exception ex, WebRequest request) {
        log.error("==========>Error");
        return handleExceptionInternal(ex, "==========>Error",
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ MyException.class,
            ConstraintViolationException.class,
            DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(
            Exception ex, WebRequest request) {
        log.error("==========>Error2");
        return handleExceptionInternal(ex, "==========>Error2"*//*ex.getLocalizedMessage()*//*,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }*/
}

/*class MyException extends RuntimeException {

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
}
class MyException2 extends RuntimeException {

    public MyException2(String message, Throwable cause) {
        super(message, cause);
    }
}*/
