package com.mozcalti.gamingapp.exceptions;

import lombok.*;

import java.util.Date;

@Data
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private Date timestamp;
}
