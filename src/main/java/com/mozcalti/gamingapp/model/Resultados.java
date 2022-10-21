package com.mozcalti.gamingapp.model;

import com.mozcalti.gamingapp.model.batallas.resultado.Result;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "resultados")
@Data
@NoArgsConstructor
public class Resultados {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_resultado")
    private int idResultado;
    @Basic
    @Column(name = "teamleadername")
    private String teamleadername;
    @Basic
    @Column(name = "rank")
    private Integer rank;
    @Basic
    @Column(name = "score")
    private Double score;
    @Basic
    @Column(name = "survival")
    private Double survival;
    @Basic
    @Column(name = "lastsurvivorbonus")
    private Double lastsurvivorbonus;
    @Basic
    @Column(name = "bulletdamage")
    private Double bulletdamage;
    @Basic
    @Column(name = "bulletdamagebonus")
    private Double bulletdamagebonus;
    @Basic
    @Column(name = "ramdamage")
    private Double ramdamage;
    @Basic
    @Column(name = "ramdamagebonus")
    private Double ramdamagebonus;
    @Basic
    @Column(name = "firsts")
    private Integer firsts;
    @Basic
    @Column(name = "seconds")
    private Integer seconds;
    @Basic
    @Column(name = "thirds")
    private Integer thirds;
    @Basic
    @Column(name = "ver")
    private Integer ver;
    @Basic
    @Column(name = "id_batalla")
    private Integer idBatalla;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_batalla", referencedColumnName = "id_batalla", insertable = false, updatable = false)
    private Batallas batallasByIdBatalla;

    public Resultados(Result result, Integer idBatalla) {
        this.teamleadername = result.getTeamLeaderName();
        this.rank = result.getRank();
        this.score = result.getScore();
        this.survival = result.getSurvival();
        this.lastsurvivorbonus = result.getLastSurvivorBonus();
        this.bulletdamage = result.getBulletDamage();
        this.bulletdamagebonus = result.getLastSurvivorBonus();
        this.ramdamage = result.getRamDamage();
        this.ramdamagebonus = result.getBulletDamageBonus();
        this.firsts = result.getFirsts();
        this.seconds = result.getSeconds();
        this.thirds = result.getThirds();
        this.ver = result.getVer();
        this.idBatalla = idBatalla;
    }
}
