package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mozcalti.gamingapp.request.torneo.HoraHabilRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "torneo_horas_habiles")
@Data
@NoArgsConstructor
public class TorneoHorasHabiles {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_hora_habil")
    private int idHoraHabil;

    @Basic
    @Column(name = "hora_ini_habil")
    private String horaIniHabil;

    @Basic
    @Column(name = "hora_fin_habil")
    private String horaFinHabil;

    @Basic
    @Column(name = "id_torneo")
    private Integer idTorneo;

    @ManyToOne
    @JoinColumn(name = "id_torneo", referencedColumnName = "id_torneo", insertable = false, updatable = false)
    @JsonIgnore
    private Torneos horasHabilesByIdTorneo;

    public TorneoHorasHabiles(HoraHabilRequest horaHabilRequest, Integer idTorneo) {
        this.horaIniHabil = horaHabilRequest.getHoraIniHabil();
        this.horaFinHabil = horaHabilRequest.getHoraFinHabil();
        this.idTorneo = idTorneo;
    }
}
