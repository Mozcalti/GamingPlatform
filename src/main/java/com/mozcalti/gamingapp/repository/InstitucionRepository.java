package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Institucion;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InstitucionRepository extends PagingAndSortingRepository<Institucion, Integer>, JpaSpecificationExecutor<Institucion> {

    Optional<Institucion> findByNombre(String nombre);

}
