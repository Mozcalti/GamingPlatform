package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Robots;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RobotsRepository extends CrudRepository<Robots, Integer> {

    List<Robots> findAllByIdEquipo(Integer idEquipo);

    List<Robots> findAllByNombre(String nombre);

}
