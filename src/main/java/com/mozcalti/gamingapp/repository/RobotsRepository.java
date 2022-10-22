package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Robots;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RobotsRepository extends PagingAndSortingRepository<Robots, Integer>, JpaSpecificationExecutor<Robots> {

    Robots findByNombre(String nombre);
    void deleteById(int idRobot);

    List<Robots> findAllByIdParticipante(int idParticipante);

    @Modifying
    @Query("UPDATE Robots robot SET robot.activo = ?1 where robot.idRobot = ?2")
    int updateActivo(Integer activo, int idRobot);

}
