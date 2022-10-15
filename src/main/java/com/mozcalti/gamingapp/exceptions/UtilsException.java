package com.mozcalti.gamingapp.exceptions;

public class UtilsException extends Exception {

    public UtilsException() {
    }

    public UtilsException(String message) {
        super(message);
    }

    public UtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UtilsException(Throwable cause) {
        super(cause);
    }

    public UtilsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
