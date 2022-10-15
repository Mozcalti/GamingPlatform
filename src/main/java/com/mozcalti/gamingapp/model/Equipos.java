package com.mozcalti.gamingapp.model;

import com.mozcalti.gamingapp.request.torneo.EquipoRequest;
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

    @OneToMany(mappedBy = "equiposByIdEquipo")
    private Collection<EtapaEquipo> etapaEquiposByIdEquipo;

    @OneToMany(mappedBy = "equiposByIdEquipo", fetch = FetchType.EAGER)
    private Collection<ParticipanteEquipo> participanteEquiposByIdEquipo;

    public Equipos(EquipoRequest equipoRequest) {
        this.nombre = equipoRequest.getNombreEquipo();
    }
}
