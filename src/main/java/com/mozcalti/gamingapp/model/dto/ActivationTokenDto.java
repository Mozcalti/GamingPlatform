package com.mozcalti.gamingapp.model.dto;

import javax.validation.constraints.NotBlank;

public record ActivationTokenDto(@NotBlank String token, @NotBlank String password) {
}
