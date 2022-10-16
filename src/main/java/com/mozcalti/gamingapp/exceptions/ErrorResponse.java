package com.mozcalti.gamingapp.exceptions;

public record ErrorResponse(int status, String error, String message, String timestamp) {
}
