package com.mozcalti.gamingapp.model;

import com.mozcalti.gamingapp.response.batalla.BatallaResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "batallas")
@Data
@NoArgsConstructor
public class Batallas {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_batalla")
    private int idBatalla;

    @Basic
    @Column(name = "fecha")
    private String fecha;

    @Basic
    @Column(name = "hora_inicio")
    private String horaInicio;

    @Basic
    @Column(name = "hora_fin")
    private String horaFin;

    @Basic
    @Column(name = "rondas")
    private Integer rondas;

    @OneToMany(mappedBy = "batallasByIdBatalla", fetch = FetchType.EAGER)
    private Collection<BatallaParticipantes> batallaParticipantesByIdBatalla;

    @OneToMany(mappedBy = "batallasByIdBatalla")
    private Collection<EtapaBatalla> etapaBatallasByIdBatalla;

    public Batallas(BatallaResponse batallaResponse) {
        this.fecha = batallaResponse.getFecha();
        this.horaInicio = batallaResponse.getHoraInicio();
        this.horaFin = batallaResponse.getHoraFin();
        this.rondas = batallaResponse.getRondas();
    }
}
