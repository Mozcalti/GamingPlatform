package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.BatallaParticipantes;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BatallaParticipantesRepository extends CrudRepository<BatallaParticipantes, Integer> {

    List<BatallaParticipantes> findAllByIdBatalla(Integer idBatalla);

}
