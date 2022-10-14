package com.mozcalti.gamingapp.exceptions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final String timestamp;


}
