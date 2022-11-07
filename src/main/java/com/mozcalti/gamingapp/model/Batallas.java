package com.mozcalti.gamingapp.model;

import com.mozcalti.gamingapp.model.batallas.BatallaDTO;
import com.mozcalti.gamingapp.utils.EstadosBatalla;
import com.mozcalti.gamingapp.utils.Numeros;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "batallas")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Basic
    @Column(name = "bnd_envio_correo")
    private Integer bndEnvioCorreo;

    @Basic
    @Column(name = "estatus")
    private String estatus;

    @Basic
    @Column(name = "view_url")
    private String viewUrl;

    @Basic
    @Column(name = "view_token")
    private String viewToken;

    @OneToMany(mappedBy = "batallasByIdBatalla", fetch = FetchType.EAGER)
    private Collection<BatallaParticipantes> batallaParticipantesByIdBatalla;

    @OneToMany(mappedBy = "batallasByIdBatalla")
    private Collection<EtapaBatalla> etapaBatallasByIdBatalla;

    @OneToMany(mappedBy = "batallasByIdBatalla")
    private Collection<Resultados> resultadosByIdBatalla;

    public Batallas(BatallaDTO batallaDTO) {
        this.fecha = batallaDTO.getFecha();
        this.horaInicio = batallaDTO.getHoraInicio();
        this.horaFin = batallaDTO.getHoraFin();
        this.rondas = batallaDTO.getRondas();
        this.bndEnvioCorreo = Numeros.CERO.getNumero();
        this.estatus = EstadosBatalla.PENDIENTE.getEstado();
    }

}
