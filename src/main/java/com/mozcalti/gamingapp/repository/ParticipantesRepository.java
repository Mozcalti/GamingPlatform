package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Participantes;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantesRepository extends PagingAndSortingRepository<Participantes, Integer>, JpaSpecificationExecutor<Participantes> {

    Participantes findByCorreo(String correo);
}
