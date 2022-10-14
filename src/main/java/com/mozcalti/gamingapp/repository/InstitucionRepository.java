package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Institucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InstitucionRepository extends JpaRepository<Institucion, UUID> {
    Institucion findByNombre(String nombre);

}
