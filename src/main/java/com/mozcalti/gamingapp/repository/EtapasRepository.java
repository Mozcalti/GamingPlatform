package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Etapas;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EtapasRepository extends CrudRepository<Etapas, Integer> {

    List<Etapas> findAllByIdTorneo(Integer idTorneo);

}
