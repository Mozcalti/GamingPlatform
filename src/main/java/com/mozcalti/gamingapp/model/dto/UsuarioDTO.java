package com.mozcalti.gamingapp.model.dto;

import com.mozcalti.gamingapp.model.Usuario;

import javax.validation.constraints.NotBlank;

public record UsuarioDTO(@NotBlank String nombre,
                         @NotBlank String apellidos,
                         @NotBlank String email,
                         @NotBlank String rol) {

    public UsuarioDTO(Usuario usuario) {
        this(usuario.getNombre(), usuario.getApellidos(), usuario.getEmail(), usuario.getRol());
    }
}
