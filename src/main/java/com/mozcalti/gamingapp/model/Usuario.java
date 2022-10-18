package com.mozcalti.gamingapp.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(	name = "usuarios")
@Getter
@Setter
@EqualsAndHashCode
public class Usuario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable=false, updatable=false)
    private Long id;

    @Column(nullable=false, updatable = false, unique=true)
    @NotBlank
    private String email;

    @Column(nullable=false)
    @NotBlank
    private String password;

    private boolean habilitado;

    @Column(nullable=false)
    private String rol;
}
