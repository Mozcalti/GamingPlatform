package com.mozcalti.gamingapp.exceptions;

public class SendMailException extends Exception {

    public SendMailException(String message) {
        super(message);
    }

    public SendMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendMailException(Throwable cause) {
        super(cause);
    }

}
