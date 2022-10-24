package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Participante;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantesRepository extends PagingAndSortingRepository<Participante, Integer>, JpaSpecificationExecutor<Participante> {

    Participante findByCorreo(String correo);
}
