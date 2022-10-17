package com.mozcalti.gamingapp.model.dto;

import javax.validation.constraints.NotBlank;

public record CredencialesUsuarioDTO(@NotBlank String username, @NotBlank String password) {
}
