package com.mozcalti.gamingapp.model;


import javax.persistence.*;
import java.util.UUID;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Data
@Table(name = "cat_institucion")
public class Institucion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nombre") private String nombre;

    @Column(name = "correo") private String correo;

    @Column(name = "fecha_creacion")
    @Value("#{T(LocalDate.now().format(DateTimeFormatter.ofPattern(\"dd/MM/yyyy\"))}")
    private String fechaCreacion;

}
