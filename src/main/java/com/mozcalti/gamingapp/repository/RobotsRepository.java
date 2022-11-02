package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Robots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RobotsRepository extends JpaRepository<Robots, Integer> {

    Robots findByNombre(String nombre);
    Robots findByClassName(String className);
    List<Robots> findAllByIdEquipo(Integer idEquipo);
    List<Robots> findAllByNombre(String nombre);
    List<Robots> findAllByClassName(String className);

}
