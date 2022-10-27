package com.mozcalti.gamingapp.model;

import com.mozcalti.gamingapp.model.torneos.EquipoDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "equipos")
@Data
@NoArgsConstructor
public class Equipos {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_equipo")
    private int idEquipo;

    @Basic
    @Column(name = "nombre")
    private String nombre;

    @Basic
    @Column(name = "activo")
    private boolean activo;

    @OneToMany(mappedBy = "equiposByIdEquipo")
    private Collection<EtapaEquipo> etapaEquiposByIdEquipo;

    @OneToMany(mappedBy = "equiposByIdEquipo", fetch = FetchType.EAGER)
    private Collection<ParticipanteEquipo> participanteEquiposByIdEquipo;

    @OneToMany(mappedBy = "equiposByIdEquipo")
    private Collection<Robots> robotsByIdEquipo;

    public Equipos(EquipoDTO equipoDTO) {
        this.nombre = equipoDTO.getNombreEquipo();
    }
}
