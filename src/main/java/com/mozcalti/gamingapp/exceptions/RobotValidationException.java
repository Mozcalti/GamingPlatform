package com.mozcalti.gamingapp.exceptions;

public class RobotValidationException extends RuntimeException{

    public RobotValidationException(String message) {
        super(message);
    }

    public RobotValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RobotValidationException(Throwable cause) {
        super(cause);
    }



}
