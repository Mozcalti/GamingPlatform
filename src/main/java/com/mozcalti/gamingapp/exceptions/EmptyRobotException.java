package com.mozcalti.gamingapp.exceptions;

public class EmptyRobotException extends RuntimeException {


    public EmptyRobotException(String message) {
        super(message);
    }

    public EmptyRobotException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyRobotException(Throwable cause) {
        super(cause);
    }
}
