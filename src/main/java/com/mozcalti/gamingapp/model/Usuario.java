package com.mozcalti.gamingapp.model;

import com.mozcalti.gamingapp.model.dto.UsuarioDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(	name = "usuarios")
@Getter
@Setter
@EqualsAndHashCode
public class Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(updatable=false)
    private Long id;

    @Column(updatable = false)
    @NotBlank
    private String email;


    @Column
    @NotBlank
    private String nombre;

    @Column
    @NotBlank
    private String apellidos;


    @Column(nullable=false)
    @NotBlank
    private String password;

    private boolean habilitado;

    @Column(nullable=false)
    private String rol;

    public Usuario(UsuarioDTO usuarioDTO) {
        this.email = usuarioDTO.email();
        this.nombre = usuarioDTO.nombre();
        this.apellidos = usuarioDTO.apellidos();
        this.rol = usuarioDTO.rol();
        this.password = "<EMPTY>";
    }
}
