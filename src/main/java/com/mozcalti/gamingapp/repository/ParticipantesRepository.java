package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Participantes;
import com.mozcalti.gamingapp.model.batallas.ParticipanteCorreo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantesRepository extends PagingAndSortingRepository<Participantes, Integer>, JpaSpecificationExecutor<Participantes> {

    Participantes findByCorreo(String correo);

    List<Participantes> findAllByInstitucionIdOrderByNombreAsc(Integer id);

    List<Participantes> findAllByInstitucionId(Integer idInstitucion);

    @Query(value = "SELECT p.nombre, p.apellidos, p.correo, pe.id_participante_equipo idParticipanteEquipo, bp.id_batalla idBatalla " +
            "FROM participantes p, participante_equipo pe, etapa_equipo ee, etapas e, batalla_participantes bp " +
            "WHERE p.id_participante = :idParticipante " +
            "AND p.id_participante = pe.id_participante " +
            "AND pe.id_equipo = ee.id_equipo " +
            "AND ee.id_etapa = e.id_etapa " +
            "AND e.numero_etapa = :numeroEtapa " +
            "AND bp.id_participante_equipo = pe.id_equipo", nativeQuery = true)
    ParticipanteCorreo findByCorreoReenvio(
            @Param("idParticipante") Integer idParticipante,
            @Param("numeroEtapa") Integer numeroEtapa);

}
