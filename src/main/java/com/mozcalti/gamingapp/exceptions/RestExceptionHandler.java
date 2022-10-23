package com.mozcalti.gamingapp.exceptions;

import com.mozcalti.gamingapp.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({NoSuchElementException.class, DuplicateKeyException.class, IllegalArgumentException.class})
    protected ResponseEntity<ErrorResponse> handleClientException(Exception exc){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(httpStatus, exc, httpStatus.name());
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleServerException(Exception exc){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return buildResponseEntity(httpStatus,
                new RuntimeException("Ocurrió un error al procesar su solicitud. El equipo técnico está enterado y está trabajando en solucionarlo",exc),
                httpStatus.name());
    }

    @ExceptionHandler({ AuthenticationException.class })
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(Exception exc){
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return buildResponseEntity(httpStatus, exc, httpStatus.name());
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus httpStatus, Exception exception, String err){
        log.error("Error al procesar la petición", exception);
        return ResponseEntity.status(httpStatus).body(new ErrorResponse(httpStatus.value(),err,"GPA-" + exception.getLocalizedMessage(), DateUtils.formatDate(DateUtils.now())));
    }
}
