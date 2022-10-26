package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.model.Participantes;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InstitucionRepository extends PagingAndSortingRepository<Institucion, Integer>, JpaSpecificationExecutor<Institucion> {

    Optional<Institucion> findByNombre(String nombre);

    @Query("select p from Participantes p where p.institucion.id = :idInstitucion order by p.nombre asc ")
    List<Participantes> participantesEnInstitucion(Integer idInstitucion);

}
