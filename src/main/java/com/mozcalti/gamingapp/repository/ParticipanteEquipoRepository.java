package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.ParticipanteEquipo;
import org.springframework.data.repository.CrudRepository;

public interface ParticipanteEquipoRepository extends CrudRepository<ParticipanteEquipo, Integer> {

    void deleteByIdEquipo(Integer idEquipo);

}
