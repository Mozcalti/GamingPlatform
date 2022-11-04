package com.mozcalti.gamingapp.model.dto;

import com.mozcalti.gamingapp.model.Participantes;
import com.mozcalti.gamingapp.model.Usuario;
import com.mozcalti.gamingapp.utils.Constantes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UsuarioDTO {

    private String nombre;
    private String apellidos;
    private String email;
    private String rol;

    public UsuarioDTO(Usuario usuario) {
        this.nombre = usuario.getNombre();
        this.apellidos = usuario.getApellidos();
        this.email = usuario.getEmail();
        this.rol = usuario.getRol();
    }

    public UsuarioDTO(Participantes participante) {
        this.nombre = participante.getNombre();
        this.apellidos = participante.getApellidos();
        this.email = participante.getCorreo();
        this.rol = Constantes.ROL_PUBLIC;
    }

}
