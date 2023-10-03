package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Batallas;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadoPorDia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BatallasRepository extends CrudRepository<Batallas, Integer> {

    Batallas findByViewToken(String token);

    @Query(value = "SELECT b.fecha, e.numero_etapa numeroEtapa, res.score, bp.nombre, rob.nombre robot " +
            "FROM " +
            "batallas b, resultados res, robots rob, " +
            "batalla_participantes bp, participante_equipo pe, " +
            "etapa_equipo ee, etapas e " +
            "WHERE b.id_batalla = res.id_batalla " +
            "AND b.estatus != 'CANCELADA'" +
            "AND res.rank = 1 " +
            "AND rob.class_name = res.teamleadername " +
            "AND bp.id_participante_equipo = rob.id_equipo " +
            "AND pe.id_equipo = rob.id_equipo " +
            "AND ee.id_equipo = rob.id_equipo " +
            "AND e.id_etapa = ee.id_etapa " +
            "ORDER BY e.numero_etapa, b.fecha, res.score DESC", nativeQuery = true)
    List<ResultadoPorDia> findAllResultadosPorDia();

}
